package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.model.HomeUiState
import com.example.aqpexplorer.model.TouristPlace
import com.example.aqpexplorer.repository.TouristPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TouristPlaceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val places = repository.getAllPlaces()
                val categories = repository.categories.value
                val carouselImages = repository.carouselImages.value
                val timeRecommendations = repository.timeRecommendations.value
                
                _uiState.value = HomeUiState(
                    places = places,
                    categories = categories,
                    carouselImages = carouselImages,
                    timeRecommendations = timeRecommendations,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar los datos: ${e.message}"
                )
            }
        }
    }
    
    fun selectCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedCategory = category,
                isLoading = true
            )
            
            try {
                val filteredPlaces = repository.getPlacesByCategory(category)
                _uiState.value = _uiState.value.copy(
                    places = filteredPlaces,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al filtrar lugares: ${e.message}"
                )
            }
        }
    }
    
    fun toggleFavorite(placeId: Int) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(placeId)
                // Recargar los lugares para reflejar el cambio
                val updatedPlaces = if (_uiState.value.selectedCategory == "Todos") {
                    repository.getAllPlaces()
                } else {
                    repository.getPlacesByCategory(_uiState.value.selectedCategory)
                }
                _uiState.value = _uiState.value.copy(places = updatedPlaces)
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