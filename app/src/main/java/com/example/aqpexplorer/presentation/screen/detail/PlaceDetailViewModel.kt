package com.example.aqpexplorer.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.data.mapper.toDomain
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaceDetailViewModel(
    private val repository: TouristPlaceRepository,
    private val placeId: Int // Recibimos el ID en el constructor
) : ViewModel() {

    // Buscamos el lugar específico dentro de la lista que ya tiene el Repositorio
    val place: StateFlow<TouristPlace?> = repository.allPlaces
        .map { entities ->
            val entity = entities.find { it.id == placeId }
            entity?.toDomain()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun toggleFavorite() {
        val currentPlace = place.value ?: return
        val newStatus = !currentPlace.isFavorite

        viewModelScope.launch {
            // El mensaje depende de la acción
            _toastMessage.value = if (newStatus) "Añadido a favoritos ❤️" else "Eliminado de favoritos"

            // Guardar en repositorio (Room + Firebase)
            repository.toggleFavorite(currentPlace.id, currentPlace.isFavorite)
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}