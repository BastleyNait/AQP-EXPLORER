package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.model.FavoritesUiState
import com.example.aqpexplorer.repository.TouristPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: TouristPlaceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val favorites = repository.getFavoritePlaces()
                _uiState.value = FavoritesUiState(
                    favoritePlaces = favorites,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar favoritos: ${e.message}"
                )
            }
        }
    }
    
    fun removeFavorite(placeId: Int) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(placeId)
                // Recargar favoritos
                loadFavorites()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al remover favorito: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}