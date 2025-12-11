package com.example.aqpexplorer.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.data.mapper.toDomain
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: TouristPlaceRepository
) : ViewModel() {

    // --- ESTADOS ---
    val searchText = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Todos")

    // --- LÓGICA DE FILTRADO ---
    // Combinamos la base de datos (Room) con los inputs de búsqueda en tiempo real
    val filteredPlaces: StateFlow<List<TouristPlace>> = combine(
        repository.allPlaces, // Fuente de verdad (Room)
        searchText,
        selectedCategory
    ) { entities, query, category ->

        // 1. Convertir Entity -> Domain
        val allPlaces = entities.map { it.toDomain() }

        // 2. Filtrar
        allPlaces.filter { place ->
            val matchSearch = place.name.contains(query, ignoreCase = true) ||
                    place.description.contains(query, ignoreCase = true)

            val matchCategory = if (category == "Todos") true else place.categoria == category

            matchSearch && matchCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- ACCIONES ---

    fun onSearchTextChange(text: String) {
        searchText.value = text
    }

    fun onCategorySelected(category: String) {
        selectedCategory.value = category
    }

    // Funcionalidad extra: Permitir marcar favorito desde la búsqueda
    fun toggleFavorite(place: TouristPlace) {
        viewModelScope.launch {
            repository.toggleFavorite(place.id, place.isFavorite)
        }
    }
}