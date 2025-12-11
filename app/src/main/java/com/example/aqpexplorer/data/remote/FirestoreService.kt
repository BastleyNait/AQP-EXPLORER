package com.example.aqpexplorer.data.remote

import com.example.aqpexplorer.data.remote.dto.TouristPlaceDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Descarga toda la colección "sitios_turisticos"
    suspend fun getAllPlaces(): List<TouristPlaceDto> {
        return try {
            val snapshot = firestore.collection("sitios_turisticos").get().await()
            snapshot.documents.mapNotNull { doc ->
                // Convierte el documento JSON a tu clase DTO
                doc.toObject(TouristPlaceDto::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Actualizar solo el favorito en la nube
    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        try {
            // Buscamos el documento donde el campo 'id' sea igual al id numérico
            // OJO: Si tus documentos tienen el ID como nombre del documento, usa .document(id.toString())
            val query = firestore.collection("sitios_turisticos")
                .whereEqualTo("id", id)
                .get()
                .await()

            if (!query.isEmpty) {
                val docId = query.documents[0].id
                firestore.collection("sitios_turisticos")
                    .document(docId)
                    .update("isFavorite", isFavorite)
                    .await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}