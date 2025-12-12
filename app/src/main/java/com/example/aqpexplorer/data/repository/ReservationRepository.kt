package com.example.aqpexplorer.data.repository

import android.util.Log
import com.example.aqpexplorer.data.local.dao.ReservationDao
import com.example.aqpexplorer.data.local.entity.ReservationEntity
import com.example.aqpexplorer.data.remote.dto.ReservationDto
import com.example.aqpexplorer.data.remote.dto.toEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ReservationRepository(
    private val dao: ReservationDao,
    private val firestore: FirebaseFirestore
) {

    // --- 1. PROPIEDAD FLOW (Lo que pide el ViewModel) ---
    // Esto conecta Room con el ViewModel. Si Room cambia, la UI se entera sola.
    val allReservationsFlow: Flow<List<ReservationEntity>> = dao.getAllReservationsFlow()

    // --- 2. SINCRONIZAR (Descargar de Nube) ---
    suspend fun syncReservations(userId: String) {
        try {
            val snapshot = firestore.collection("reservas")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val entities = snapshot.documents.mapNotNull { doc ->
                val dto = doc.toObject(ReservationDto::class.java)
                dto?.toEntity(doc.id)
            }

            if (entities.isNotEmpty()) {
                dao.insertReservations(entities)
            }
        } catch (e: Exception) {
            Log.e("REPO_RESERVA", "Error sincronizando: ${e.message}")
        }
    }

    // --- 3. LEER LOCAL (Para Worker) ---
    suspend fun getConfirmedReservations(): List<ReservationEntity> {
        return dao.getConfirmedReservations()
    }

    // --- 4. CANCELAR RESERVA (Nuevo) ---
    suspend fun cancelReservation(reservationId: String) {
        try {
            // A. Actualizar en Firebase (Nube)
            firestore.collection("reservas")
                .document(reservationId)
                .update("estado", "Cancelada") // O usa una constante si tienes
                .await()

            // B. Actualizar en Room (Local)
            // Al hacer esto, el 'allReservationsFlow' emitirá el cambio automáticamente
            dao.updateStatus(reservationId, "Cancelada")

        } catch (e: Exception) {
            Log.e("REPO_RESERVA", "Error al cancelar: ${e.message}")
            throw e // Re-lanzamos para que el ViewModel sepa que falló
        }
    }
    // --- 5. CREAR RESERVA (NUEVO) ---
    suspend fun createReservation(reservation: ReservationEntity) {
        try {
            // A. Guardar en Firebase
            firestore.collection("reservas")
                .document(reservation.id)
                .set(reservation)
                .await()

            // B. Guardar en Room (Para verlo offline inmediatamente)
            dao.insertReservations(listOf(reservation))

        } catch (e: Exception) {
            Log.e("REPO_RESERVA", "Error creando reserva: ${e.message}")
            throw e
        }
    }
}