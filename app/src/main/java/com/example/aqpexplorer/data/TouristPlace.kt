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
            imageUrl = "https://freewalkingtoursperu.com/wp-content/uploads/2024/02/como-llegar-canon-colca_3.webp",
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
            imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0f/2e/29/6a/vista-del-misti-en-todo.jpg?w=700&h=-1&s=1",
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
            imageUrl = "https://www.peru.travel/Contenido/Destino/Imagen/es/3/1.2/Principal/Plaza%20de%20Armas%20Arequipa.home%20_2_.jpg",
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
            imageUrl = "https://www.peru.travel/Contenido/Atractivo/Imagen/es/44/1.1/Principal/monasterio-santa-catalina.jpg",
            rating = 4.9f,
            reviewCount = 1500
        )
    )
    
    val categories = listOf("Todos", "Cultura", "Naturaleza", "Comida", "Comida")
    
    val timeOfDayRecommendations = listOf(
        TimeRecommendation(1, "Noche", "https://andinoperu.b-cdn.net/wp-content/uploads/2024/03/vista-volcan-misti-desde-arequipa.webp"),
        TimeRecommendation(2, "Tarde", "https://andinoperu.b-cdn.net/wp-content/uploads/2024/01/atardecer-arequipa-guia-informativa-completa-arequipa-peru.webp"),
        TimeRecommendation(3, "Día", "https://freewalkingtoursperu.com/wp-content/uploads/2024/02/como-llegar-canon-colca_3.webp")
    )
    
    val carouselImages = listOf(
        "https://freewalkingtoursperu.com/wp-content/uploads/2024/02/como-llegar-canon-colca_3.webp",
        "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0f/2e/29/6a/vista-del-misti-en-todo.jpg?w=700&h=-1&s=1",
        "https://www.peru.travel/Contenido/Destino/Imagen/es/3/1.2/Principal/Plaza%20de%20Armas%20Arequipa.home%20_2_.jpg",
        "https://www.peru.travel/Contenido/Atractivo/Imagen/es/44/1.1/Principal/monasterio-santa-catalina.jpg"
    )
}

data class TimeRecommendation(
    val id: Int,
    val timeOfDay: String,
    val imageUrl: String
)