package com.example.aqpexplorer.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.data.mapper.toDomain
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: TouristPlaceRepository
) : ViewModel() {

    // Obtenemos los datos de Room y filtramos solo los favoritos
    val favoritePlaces: StateFlow<List<TouristPlace>> = repository.allPlaces
        .map { entities ->
            entities
                .map { it.toDomain() } // Convertir a Domain
                .filter { it.isFavorite } // Filtrar solo favoritos
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFavorite(placeId: Int) {
        viewModelScope.launch {
            // Al poner false, Room se actualiza -> favoritePlaces se actualiza -> La UI elimina el item sola
            repository.toggleFavorite(placeId, true)
        }
    }
}