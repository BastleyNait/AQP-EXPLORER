package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.Reservation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ReservationViewModel : ViewModel() {
    private val db = Firebase.firestore

    // ID de usuario simulado (en el futuro usarás FirebaseAuth)
    private val currentUserId = "user123"

    // CAMBIO: Renombrado a 'allReservations' porque trae TODO (Confirmadas, Canceladas, Pasadas)
    // La UI se encarga de separarlas en "Próximas" e "Historial".
    private val _allReservations = MutableStateFlow<List<Reservation>>(emptyList())
    val allReservations: StateFlow<List<Reservation>> = _allReservations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _reservationSuccess = MutableStateFlow<String?>(null)
    val reservationSuccess: StateFlow<String?> = _reservationSuccess

    init {
        fetchReservations()
    }

    // Esta función recibe el 'Date' directo del DatePicker
    fun createReservation(
        placeId: Int,
        placeName: String,
        placeImage: String,
        fecha: Date, // <--- Aquí llega la fecha seleccionada en el calendario
        numPersonas: Int,
        precioTotal: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val reservationId = UUID.randomUUID().toString()

                val reservation = Reservation(
                    id = reservationId,
                    placeId = placeId,
                    placeName = placeName,
                    placeImage = placeImage,
                    userId = currentUserId,

                    // Convertimos el Date de Java a Timestamp de Firebase
                    fecha = Timestamp(fecha),

                    numPersonas = numPersonas,
                    precioTotal = precioTotal,
                    estado = Reservation.STATUS_CONFIRMED,
                    createdAt = Timestamp.now()
                )

                db.collection("reservas")
                    .document(reservationId)
                    .set(reservation)
                    .await()

                _reservationSuccess.value = "¡Reserva confirmada!"
                fetchReservations() // Recargamos para que aparezca en la lista
            } catch (e: Exception) {
                e.printStackTrace()
                _reservationSuccess.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchReservations() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // ⚠️ OJO: Esto requiere crear el ÍNDICE en Firebase Console
                // Si la lista sale vacía, revisa el Logcat y dale click al link que aparece.
                val result = db.collection("reservas")
                    .whereEqualTo("userId", currentUserId)
                    .orderBy("fecha", Query.Direction.ASCENDING) // Ordenar por fecha más próxima
                    .get()
                    .await()

                val list = result.documents.mapNotNull {
                    it.toObject(Reservation::class.java)
                }
                _allReservations.value = list

            } catch (e: Exception) {
                e.printStackTrace()
                // Tip: Si sale error "FAILED_PRECONDITION", es que falta el índice.
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelReservation(reservationId: String) {
        viewModelScope.launch {
            try {
                // 1. Actualizar en Firebase
                db.collection("reservas")
                    .document(reservationId)
                    .update("estado", Reservation.STATUS_CANCELLED)
                    .await()

                // 2. Actualizar localmente (Optimistic UI) para que sea instantáneo
                _allReservations.value = _allReservations.value.map {
                    if (it.id == reservationId) it.copy(estado = Reservation.STATUS_CANCELLED) else it
                }

                _reservationSuccess.value = "Reserva cancelada"
            } catch (e: Exception) {
                e.printStackTrace()
                _reservationSuccess.value = "No se pudo cancelar"
            }
        }
    }

    fun clearSuccessMessage() {
        _reservationSuccess.value = null
    }
}