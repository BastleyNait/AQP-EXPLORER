package com.example.aqpexplorer.presentation.screen.reservations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.Reservation
import com.example.aqpexplorer.data.local.entity.ReservationEntity
import com.example.aqpexplorer.data.repository.ReservationRepository
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class ReservationViewModel(
    private val repository: ReservationRepository
) : ViewModel() {

    // --- ESTADOS DE LA UI ---
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _reservationSuccess = MutableStateFlow<String?>(null)
    val reservationSuccess: StateFlow<String?> = _reservationSuccess

    // --- FLUJO DE DATOS PRINCIPAL ---
    // Escuchamos al Repositorio. Gracias a 'stateIn', este flujo se mantiene vivo
    // y convierte automáticamente las Entities de Room a objetos Reservation de la UI.
    val allReservations: StateFlow<List<Reservation>> = repository.allReservationsFlow
        .map { entities ->
            entities.map { it.toDomain() } // Convertimos cada item
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Mantiene datos 5s si giras la pantalla
            initialValue = emptyList()
        )

    init {
        // Al crearse el ViewModel, intentamos actualizar datos desde la nube
        refreshReservations()
    }

    private fun refreshReservations() {
        viewModelScope.launch {
            _isLoading.value = true
            // "user123" es hardcoded por ahora.
            // En el futuro aquí iría: FirebaseAuth.getInstance().currentUser?.uid
            repository.syncReservations("user123")
            _isLoading.value = false
        }
    }

    // --- ACCIÓN: CANCELAR RESERVA ---
    fun cancelReservation(reservationId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Llamamos al Repositorio (Nube + Local)
                repository.cancelReservation(reservationId)

                // 2. Éxito
                _reservationSuccess.value = "Reserva cancelada correctamente"

            } catch (e: Exception) {
                _reservationSuccess.value = "Error al cancelar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- MAPPER HELPER (Entity -> Domain) ---
    private fun ReservationEntity.toDomain(): Reservation {
        return Reservation(
            id = id,
            placeId = placeId,
            placeName = placeName,
            placeImage = placeImage,
            userId = userId,
            fecha = fecha,
            numPersonas = numPersonas,
            precioTotal = precioTotal,
            estado = estado
        )
    }
    fun createReservation(
        placeId: Int,
        placeName: String,
        placeImage: String,
        fecha: Date,
        numPersonas: Int,
        precioTotal: Double
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Generamos los datos
                val newReservation = ReservationEntity(
                    id = UUID.randomUUID().toString(),
                    placeId = placeId,
                    placeName = placeName,
                    placeImage = placeImage,
                    userId = "user123", // Hardcoded por ahora
                    fecha = Timestamp(fecha), // De java.util.Date a Firebase Timestamp
                    numPersonas = numPersonas,
                    precioTotal = precioTotal,
                    estado = "Confirmada",
                    createdAt = Timestamp.now()
                )

                // 2. Llamamos al repositorio (Él guarda en Nube y Local)
                repository.createReservation(newReservation)

                _reservationSuccess.value = "¡Reserva confirmada con éxito!"

            } catch (e: Exception) {
                e.printStackTrace()
                _reservationSuccess.value = "Error al reservar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _reservationSuccess.value = null
    }
}