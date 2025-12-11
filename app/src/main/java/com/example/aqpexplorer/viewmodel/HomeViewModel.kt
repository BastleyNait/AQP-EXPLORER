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

class HomeViewModel : ViewModel() {

    private val _allPlaces = MutableStateFlow<List<TouristPlace>>(emptyList())

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _uiState = MutableStateFlow<List<TouristPlace>>(emptyList())
    val uiState: StateFlow<List<TouristPlace>> = _uiState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchPlaces()
        observeData()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = Firebase.firestore.collection("sitios_turisticos").get().await()
                val list = result.documents.mapNotNull { it.toObject(TouristPlace::class.java) }
                _allPlaces.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun observeData() {
        combine(
            _allPlaces,
            _selectedCategory,
            _searchText,
            FavoritesRepository.favoriteIds
        ) { places, category, search, favIds ->

            places.map { place ->
                place.copy(isFavorite = favIds.contains(place.id.toString()))
            }.filter { place ->
                val matchCategory = if (category == "Todos") true else place.categoria == category
                val matchSearch = place.name.contains(search, ignoreCase = true)
                matchCategory && matchSearch
            }

        }.onEach { filteredList ->
            _uiState.value = filteredList
        }.launchIn(viewModelScope)
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    // CAMBIO: Ahora lanza en corrutina porque toggleFavorite es suspend
    fun toggleFavorite(placeId: Int) {
        viewModelScope.launch {
            FavoritesRepository.toggleFavorite(placeId)
        }
    }
}