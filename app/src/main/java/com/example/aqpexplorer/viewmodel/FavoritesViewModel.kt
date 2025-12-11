package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.FavoritesRepository
import com.example.aqpexplorer.data.TouristPlace
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FavoritesViewModel : ViewModel() {
    private val _allPlaces = MutableStateFlow<List<TouristPlace>>(emptyList())

    private val _favorites = MutableStateFlow<List<TouristPlace>>(emptyList())
    val favorites: StateFlow<List<TouristPlace>> = _favorites

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchAllPlaces()
        observeFavorites()
    }

    // Cargar TODOS los lugares
    private fun fetchAllPlaces() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = Firebase.firestore.collection("sitios_turisticos").get().await()
                _allPlaces.value = result.documents.mapNotNull { it.toObject(TouristPlace::class.java) }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // NUEVO: Filtrar según los IDs del repositorio
    private fun observeFavorites() {
        combine(_allPlaces, FavoritesRepository.favoriteIds) { places, favIds ->
            places.filter { favIds.contains(it.id.toString()) }
                .map { it.copy(isFavorite = true) }
        }.onEach {
            _favorites.value = it
        }.launchIn(viewModelScope)
    }

    // Método para remover favorito desde la UI
    fun removeFavorite(placeId: Int) {
        viewModelScope.launch {
            FavoritesRepository.toggleFavorite(placeId)
        }
    }
}