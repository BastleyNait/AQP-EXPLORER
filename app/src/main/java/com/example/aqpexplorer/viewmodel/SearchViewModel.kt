package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.model.SearchUiState
import com.example.aqpexplorer.repository.TouristPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TouristPlaceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    fun updateQuery(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }
        
        searchPlaces(query)
    }
    
    private fun searchPlaces(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val results = repository.searchPlaces(query)
                _uiState.value = _uiState.value.copy(
                    searchResults = results,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error en la búsqueda: ${e.message}"
                )
            }
        }
    }
    
    fun toggleFavorite(placeId: Int) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(placeId)
                // Actualizar los resultados de búsqueda
                val updatedResults = _uiState.value.searchResults.map { place ->
                    if (place.id == placeId) {
                        place.copy(isFavorite = !place.isFavorite)
                    } else {
                        place
                    }
                }
                _uiState.value = _uiState.value.copy(searchResults = updatedResults)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al actualizar favorito: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}