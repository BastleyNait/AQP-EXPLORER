package com.example.aqpexplorer.repository

import com.example.aqpexplorer.model.TouristPlace
import com.example.aqpexplorer.model.TimeRecommendation
import com.example.aqpexplorer.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TouristPlaceRepository @Inject constructor() {
    
    private val _places = MutableStateFlow(getSamplePlaces())
    val places: StateFlow<List<TouristPlace>> = _places.asStateFlow()
    
    private val _categories = MutableStateFlow(getSampleCategories())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    private val _timeRecommendations = MutableStateFlow(getSampleTimeRecommendations())
    val timeRecommendations: StateFlow<List<TimeRecommendation>> = _timeRecommendations.asStateFlow()
    
    private val _carouselImages = MutableStateFlow(getSampleCarouselImages())
    val carouselImages: StateFlow<List<String>> = _carouselImages.asStateFlow()
    
    suspend fun getAllPlaces(): List<TouristPlace> {
        // Simular carga de red
        delay(500)
        return _places.value
    }
    
    suspend fun getPlaceById(id: Int): TouristPlace? {
        delay(300)
        return _places.value.find { it.id == id }
    }
    
    suspend fun searchPlaces(query: String): List<TouristPlace> {
        delay(400)
        return if (query.isBlank()) {
            emptyList()
        } else {
            _places.value.filter { 
                it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.categories.any { category -> category.contains(query, ignoreCase = true) }
            }
        }
    }
    
    suspend fun getPlacesByCategory(category: String): List<TouristPlace> {
        delay(300)
        return if (category == "Todos") {
            _places.value
        } else {
            _places.value.filter { it.categories.contains(category) }
        }
    }
    
    suspend fun getFavoritePlaces(): List<TouristPlace> {
        delay(300)
        return _places.value.filter { it.isFavorite }
    }
    
    suspend fun toggleFavorite(placeId: Int): Boolean {
        val currentPlaces = _places.value.toMutableList()
        val index = currentPlaces.indexOfFirst { it.id == placeId }
        
        return if (index != -1) {
            val updatedPlace = currentPlaces[index].copy(isFavorite = !currentPlaces[index].isFavorite)
            currentPlaces[index] = updatedPlace
            _places.value = currentPlaces
            updatedPlace.isFavorite
        } else {
            false
        }
    }
    
    private fun getSamplePlaces() = listOf(
        TouristPlace(
            id = 1,
            name = "Cañón del Colca",
            description = "Aventura en el Cañón del Colca, uno de los cañones más profundos del mundo con avistamiento de cóndores y paisajes espectaculares.",
            shortDescription = "Uno de los cañones más profundos del mundo con avistamiento de cóndores y paisajes espectaculares",
            price = 145.00,
            categories = listOf("Aventura", "Cultura"),
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
            rating = 4.8f,
            reviewCount = 1200
        ),
        TouristPlace(
            id = 2,
            name = "Volcán Misti",
            description = "Volcán emblemático de Arequipa, ideal para trekking y aventura con vistas panorámicas",
            shortDescription = "Volcán emblemático de Arequipa, ideal para trekking y aventura con vistas panorámicas",
            price = 145.00,
            categories = listOf("Aventura", "Naturaleza"),
            imageUrl = "https://images.unsplash.com/photo-1464822759844-d150ad6d1c71?w=800",
            rating = 4.7f,
            reviewCount = 890
        ),
        TouristPlace(
            id = 3,
            name = "Plaza de Armas",
            description = "Corazón histórico de la ciudad con arquitectura colonial en sillar blanco y catedral",
            shortDescription = "Corazón histórico de la ciudad con arquitectura colonial en sillar blanco y catedral",
            price = 145.00,
            categories = listOf("Cultura", "Historia"),
            imageUrl = "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=800",
            rating = 4.6f,
            reviewCount = 2100
        ),
        TouristPlace(
            id = 4,
            name = "Monasterio de Santa Catalina",
            description = "Ciudadela religiosa del siglo XVI con arquitectura colonial única en sillar blanco",
            shortDescription = "Ciudadela religiosa del siglo XVI con arquitectura colonial única en sillar blanco",
            price = 145.00,
            categories = listOf("Cultura", "Historia"),
            imageUrl = "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=800",
            rating = 4.9f,
            reviewCount = 1500
        ),
        TouristPlace(
            id = 5,
            name = "Mercado San Camilo",
            description = "Mercado tradicional con productos locales, frutas exóticas y comida típica arequipeña",
            shortDescription = "Mercado tradicional con productos locales y comida típica",
            price = 25.00,
            categories = listOf("Comida", "Cultura"),
            imageUrl = "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=800",
            rating = 4.4f,
            reviewCount = 850
        ),
        TouristPlace(
            id = 6,
            name = "Laguna de Salinas",
            description = "Laguna salada con flamencos rosados en un paisaje altiplánico único",
            shortDescription = "Laguna salada con flamencos rosados en paisaje altiplánico",
            price = 85.00,
            categories = listOf("Naturaleza", "Aventura"),
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
            rating = 4.5f,
            reviewCount = 650
        )
    )
    
    private fun getSampleCategories() = listOf(
        Category(1, "Todos", true),
        Category(2, "Cultura"),
        Category(3, "Naturaleza"),
        Category(4, "Comida"),
        Category(5, "Aventura"),
        Category(6, "Historia")
    )
    
    private fun getSampleTimeRecommendations() = listOf(
        TimeRecommendation(1, "Noche", "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400"),
        TimeRecommendation(2, "Tarde", "https://images.unsplash.com/photo-1464822759844-d150ad6d1c71?w=400"),
        TimeRecommendation(3, "Día", "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=400")
    )
    
    private fun getSampleCarouselImages() = listOf(
        "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
        "https://images.unsplash.com/photo-1464822759844-d150ad6d1c71?w=800",
        "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=800"
    )
}