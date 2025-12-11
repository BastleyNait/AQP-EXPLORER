package com.example.aqpexplorer.data.repository

import com.example.aqpexplorer.data.local.dao.TouristPlaceDao
import com.example.aqpexplorer.data.remote.FirestoreService
import com.example.aqpexplorer.data.remote.dto.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TouristPlaceRepository @Inject constructor(
    private val dao: TouristPlaceDao,        // Fuente Local
    private val remote: FirestoreService     // Fuente Remota
) {

    // 1. Fuente de Verdad: La UI observa ESTE flujo.
    // Viene directamente de Room. Si Room cambia, la UI cambia.
    val allPlaces = dao.getAllPlaces()

    // 2. Sincronización: Descarga de Firebase -> Guarda en Room
    suspend fun syncPlaces() {
        // Ejecutamos en IO para no bloquear
        try {
            // A. Traer datos frescos de la nube
            val remotePlaces = remote.getAllPlaces()

            if (remotePlaces.isNotEmpty()) {
                // B. Convertir DTOs a Entities
                val entities = remotePlaces.map { it.toEntity() }

                // C. Guardar en Room (Esto disparará el Flow 'allPlaces' automáticamente)
                dao.insertPlaces(entities)
            }
        } catch (e: Exception) {
            // Si falla internet, no hacemos nada.
            // La app seguirá mostrando lo que tenga guardado en Room (Cache).
        }
    }

    // 3. Manejo de Favoritos (Optimistic UI Update)
    suspend fun toggleFavorite(id: Int, currentStatus: Boolean) {
        val newStatus = !currentStatus

        // A. Actualizar Room INMEDIATAMENTE (La UI responde rápido)
        dao.updateFavoriteStatus(id, newStatus)

        // B. Intentar actualizar en la nube en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            try {
                remote.updateFavorite(id, newStatus)
            } catch (e: Exception) {
                // Opcional: Si falla la nube, podrías revertir el cambio local
                // dao.updateFavoriteStatus(id, currentStatus)
            }
        }
    }
}