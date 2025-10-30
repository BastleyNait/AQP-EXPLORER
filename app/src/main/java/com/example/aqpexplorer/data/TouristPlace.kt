package com.example.aqpexplorer.data

data class TouristPlace(
    val id: Int,
    val name: String,
    val description: String,
    val shortDescription: String,
    val price: Double,
    val currency: String = "S/",
    val categories: List<String>,
    val imageUrl: String,
    val rating: Float,
    val reviewCount: Int,
    val isFavorite: Boolean = false,
    val location: String = "Arequipa"
)

// Datos de ejemplo para la aplicación
object SampleData {
    val touristPlaces = listOf(
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
            categories = listOf("Aventura", "Cultura"),
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
            categories = listOf("Aventura", "Cultura"),
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
            categories = listOf("Aventura", "Cultura"),
            imageUrl = "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=800",
            rating = 4.9f,
            reviewCount = 1500
        )
    )
    
    val categories = listOf("Todos", "Cultura", "Naturaleza", "Comida", "Comida")
    
    val timeOfDayRecommendations = listOf(
        TimeRecommendation(1, "Noche", "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400"),
        TimeRecommendation(2, "Tarde", "https://images.unsplash.com/photo-1464822759844-d150ad6d1c71?w=400"),
        TimeRecommendation(3, "Día", "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=400")
    )
    
    val carouselImages = listOf(
        "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
        "https://images.unsplash.com/photo-1464822759844-d150ad6d1c71?w=800",
        "https://images.unsplash.com/photo-1555400082-8c5b7b8b4b8b?w=800"
    )
}

data class TimeRecommendation(
    val id: Int,
    val timeOfDay: String,
    val imageUrl: String
)