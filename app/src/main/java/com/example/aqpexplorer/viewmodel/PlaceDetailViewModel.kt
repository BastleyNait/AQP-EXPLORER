package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.model.PlaceDetailUiState
import com.example.aqpexplorer.repository.TouristPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val repository: TouristPlaceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState: StateFlow<PlaceDetailUiState> = _uiState.asStateFlow()
    
    fun loadPlace(placeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val place = repository.getPlaceById(placeId)
                _uiState.value = PlaceDetailUiState(
                    place = place,
                    isLoading = false,
                    error = if (place == null) "Lugar no encontrado" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar el lugar: ${e.message}"
                )
            }
        }
    }
    
    fun toggleFavorite() {
        val currentPlace = _uiState.value.place ?: return
        
        viewModelScope.launch {
            try {
                val newFavoriteStatus = repository.toggleFavorite(currentPlace.id)
                _uiState.value = _uiState.value.copy(
                    place = currentPlace.copy(isFavorite = newFavoriteStatus)
                )
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