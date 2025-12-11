package com.example.aqpexplorer.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.data.mapper.toDomain
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TouristPlaceRepository
) : ViewModel() {

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Filtros
    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    // La lista final que ve la UI (Combinación de Room + Filtros)
    val uiState: StateFlow<List<TouristPlace>> = combine(
        repository.allPlaces, // Flujo directo de la base de datos local
        _selectedCategory,
        _searchText
    ) { entities, category, search ->

        // 1. Convertimos Entity -> Domain
        val domainList = entities.map { it.toDomain() }

        // 2. Aplicamos filtros
        domainList.filter { place ->
            val matchCategory = if (category == "Todos") true else place.categoria == category
            val matchSearch = place.name.contains(search, ignoreCase = true)
            matchCategory && matchSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        refreshData()
    }

    // Descarga de Firebase -> Guarda en Room -> Room avisa al uiState
    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.syncPlaces()
            _isLoading.value = false
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(place: TouristPlace) {
        viewModelScope.launch {
            // El repositorio se encarga de guardar en local y nube
            repository.toggleFavorite(place.id, place.isFavorite)
        }
    }
}