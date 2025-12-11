package com.example.aqpexplorer.data.repository

import android.util.Log
import com.example.aqpexplorer.data.local.dao.ReservationDao
import com.example.aqpexplorer.data.local.entity.ReservationEntity
import com.example.aqpexplorer.data.remote.dto.ReservationDto
import com.example.aqpexplorer.data.remote.dto.toEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReservationRepository(
    private val dao: ReservationDao,
    private val firestore: FirebaseFirestore
) {

    // 1. SINCRONIZAR: Nube -> Local
    suspend fun syncReservations(userId: String) {
        try {
            val snapshot = firestore.collection("reservas")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            // Usamos el DTO para convertir de forma segura
            val entities = snapshot.documents.mapNotNull { doc ->
                // A. Convertir JSON a DTO
                val dto = doc.toObject(ReservationDto::class.java)

                // B. Convertir DTO a Entity (Pasando el ID del documento)
                dto?.toEntity(doc.id)
            }

            if (entities.isNotEmpty()) {
                dao.insertReservations(entities)
            }
        } catch (e: Exception) {
            Log.e("REPO_RESERVA", "Error sincronizando: ${e.message}")
        }
    }

    // 2. LEER LOCAL (Para el Worker y la UI Offline)
    suspend fun getConfirmedReservations(): List<ReservationEntity> {
        return dao.getConfirmedReservations()
    }
}