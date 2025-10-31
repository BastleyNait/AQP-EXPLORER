package com.example.aqpexplorer.model

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// Estados específicos para cada pantalla
data class HomeUiState(
    val places: List<TouristPlace> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: String = "Todos",
    val carouselImages: List<String> = emptyList(),
    val timeRecommendations: List<TimeRecommendation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class SearchUiState(
    val query: String = "",
    val searchResults: List<TouristPlace> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class FavoritesUiState(
    val favoritePlaces: List<TouristPlace> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PlaceDetailUiState(
    val place: TouristPlace? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)