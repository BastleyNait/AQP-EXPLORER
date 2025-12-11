package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.FavoritesRepository
import com.example.aqpexplorer.data.TouristPlace
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PlaceDetailViewModel : ViewModel() {

    private val _place = MutableStateFlow<TouristPlace?>(null)
    val place: StateFlow<TouristPlace?> = _place.asStateFlow()

    // 1. NUEVO: Estado para comunicar mensajes a la vista
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val db = Firebase.firestore

    fun loadPlace(placeId: Int) {
        viewModelScope.launch {
            try {
                val doc = db.collection("sitios_turisticos")
                    .document(placeId.toString())
                    .get()
                    .await()

                val loadedPlace = doc.toObject(TouristPlace::class.java)
                if (loadedPlace != null) {
                    _place.value = loadedPlace.copy(
                        isFavorite = FavoritesRepository.isFavorite(placeId)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite() {
        val currentPlace = _place.value ?: return
        val newStatus = !currentPlace.isFavorite

        viewModelScope.launch {
            // Actualización optimista (UI instantánea)
            _place.value = currentPlace.copy(isFavorite = newStatus)

            // 2. Establecer el mensaje para que la Vista lo muestre
            if (newStatus) {
                _toastMessage.value = "Añadido a favoritos ❤️"
            } else {
                _toastMessage.value = "Eliminado de favoritos"
            }

            // Guardar en repositorio
            FavoritesRepository.toggleFavorite(currentPlace.id)
        }
    }

    // 3. Función para limpiar el mensaje después de mostrarlo
    fun clearToastMessage() {
        _toastMessage.value = null
    }
}