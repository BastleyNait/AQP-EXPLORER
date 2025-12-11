package com.example.aqpexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aqpexplorer.data.TouristPlace
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SearchViewModel : ViewModel() {
    // Lista completa original
    private val _allPlaces = MutableStateFlow<List<TouristPlace>>(emptyList())

    // Filtros
    val searchText = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Todos")

    // Resultado filtrado que verá la UI
    private val _filteredPlaces = MutableStateFlow<List<TouristPlace>>(emptyList())
    val filteredPlaces: StateFlow<List<TouristPlace>> = _filteredPlaces

    init {
        fetchPlaces()
        observeFilters()
    }

    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val result = Firebase.firestore.collection("sitios_turisticos").get().await()
                val list = result.documents.mapNotNull { it.toObject(TouristPlace::class.java) }
                _allPlaces.value = list
                // Inicialmente mostramos todo
                _filteredPlaces.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun observeFilters() {
        viewModelScope.launch {
            // Combina cambios en texto, categoría y la lista original
            combine(searchText, selectedCategory, _allPlaces) { text, category, list ->
                list.filter { place ->
                    val matchesSearch = place.name.contains(text, ignoreCase = true) ||
                            place.description.contains(text, ignoreCase = true)

                    val matchesCategory = if (category == "Todos") true else place.categoria == category

                    matchesSearch && matchesCategory
                }
            }.collect {
                _filteredPlaces.value = it
            }
        }
    }

    // Métodos para que la UI actualice los estados
    fun onSearchTextChange(text: String) { searchText.value = text }
    fun onCategorySelected(category: String) { selectedCategory.value = category }
}