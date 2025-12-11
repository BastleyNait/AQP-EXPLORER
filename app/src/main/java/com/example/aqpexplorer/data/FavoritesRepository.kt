package com.example.aqpexplorer.data

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

// REPOSITORIO MEJORADO: Sincroniza local + Firebase
object FavoritesRepository {
    private const val PREF_NAME = "aqp_favorites"
    private lateinit var prefs: SharedPreferences
    private val db = Firebase.firestore

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("ids", emptySet()) ?: emptySet()
        _favoriteIds.value = saved
    }

    // CAMBIO PRINCIPAL: Ahora es suspend y sincroniza con Firebase
    suspend fun toggleFavorite(placeId: Int) {
        val idString = placeId.toString()
        val currentSet = _favoriteIds.value.toMutableSet()
        val newStatus: Boolean

        if (currentSet.contains(idString)) {
            currentSet.remove(idString)
            newStatus = false
        } else {
            currentSet.add(idString)
            newStatus = true
        }

        // 1. Guardar local
        prefs.edit().putStringSet("ids", currentSet).apply()
        _favoriteIds.value = currentSet

        // 2. Actualizar Firebase
        try {
            db.collection("sitios_turisticos")
                .document(idString)
                .update("isFavorite", newStatus)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            // Si falla Firebase, revertir local
            if (newStatus) currentSet.remove(idString) else currentSet.add(idString)
            prefs.edit().putStringSet("ids", currentSet).apply()
            _favoriteIds.value = currentSet
        }
    }

    fun isFavorite(placeId: Int): Boolean {
        return _favoriteIds.value.contains(placeId.toString())
    }

    // Método para cargar favoritos desde Firebase al iniciar
    suspend fun syncWithFirebase() {
        try {
            val result = db.collection("sitios_turisticos")
                .whereEqualTo("isFavorite", true)
                .get()
                .await()

            val firebaseFavs = result.documents.mapNotNull { it.id }.toSet()
            prefs.edit().putStringSet("ids", firebaseFavs).apply()
            _favoriteIds.value = firebaseFavs
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}