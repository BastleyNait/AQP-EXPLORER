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
    val location: String = "Arequipa",
    val schedule: String = "Lun-Dom: 8:00 AM - 5:00 PM",
    val events: List<String> = emptyList()
)

object SampleData {
    val categories = listOf("Todos", "Aventura", "Cultura", "Naturaleza", "Gastronomía")

    val touristPlaces = listOf(
        TouristPlace(
            id = 1,
            name = "Cañón del Colca",
            description = "Uno de los cañones más profundos del mundo, hogar del majestuoso cóndor andino. Ofrece vistas espectaculares, aguas termales y pueblos tradicionales.",
            shortDescription = "Uno de los cañones más profundos del mundo.",
            price = 70.0,
            categories = listOf("Aventura", "Naturaleza"),
            imageUrl = "https://www.peru.travel/Contenido/General/Imagen/es/184/1.1/canon-del-colca.jpg",
            rating = 4.8f,
            reviewCount = 1250,
            schedule = "Lun-Dom: 6:00 AM - 6:00 PM",
            events = listOf("Festival del Cóndor - 15 Agosto", "Aniversario del Colca - 20 Setiembre")
        ),
        TouristPlace(
            id = 2,
            name = "Monasterio de Santa Catalina",
            description = "Una ciudad dentro de una ciudad. Este convento histórico de colores vibrantes data del siglo XVI y es un laberinto de calles estrechas, claustros y plazas.",
            shortDescription = "Convento histórico del siglo XVI.",
            price = 40.0,
            categories = listOf("Cultura", "Historia"),
            imageUrl = "https://www.peru.travel/Contenido/General/Imagen/es/185/1.1/monasterio-santa-catalina.jpg",
            rating = 4.7f,
            reviewCount = 980,
            schedule = "Lun-Dom: 9:00 AM - 5:00 PM",
            events = listOf("Visitas Nocturnas - Jueves y Viernes")
        ),
        TouristPlace(
            id = 3,
            name = "Volcán Misti",
            description = "El guardián de Arequipa. Un volcán activo que ofrece rutas de trekking desafiantes y vistas panorámicas de la ciudad blanca.",
            shortDescription = "Volcán activo guardián de Arequipa.",
            price = 0.0,
            categories = listOf("Aventura", "Naturaleza"),
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Misti_volcano.jpg/1200px-Misti_volcano.jpg",
            rating = 4.6f,
            reviewCount = 540,
            schedule = "Abierto 24 horas",
            events = listOf("Ascenso al Misti - Semana Santa")
        ),
        TouristPlace(
            id = 4,
            name = "Plaza de Armas",
            description = "El corazón de la ciudad, rodeada de portales de sillar y la imponente Catedral. Un lugar perfecto para pasear y disfrutar de la arquitectura colonial.",
            shortDescription = "El corazón histórico de la ciudad.",
            price = 0.0,
            categories = listOf("Cultura", "Ciudad"),
            imageUrl = "https://www.peru.travel/Contenido/General/Imagen/es/183/1.1/plaza-armas-arequipa.jpg",
            rating = 4.9f,
            reviewCount = 2100,
            schedule = "Abierto 24 horas",
            events = listOf("Desfile Dominical - Domingos 9:00 AM", "Fiestas de Arequipa - 15 Agosto")
        ),
        TouristPlace(
            id = 5,
            name = "Mirador de Yanahuara",
            description = "Famoso por sus arcos de sillar con inscripciones de poetas arequipeños. Ofrece una vista clásica del Misti y la ciudad.",
            shortDescription = "Arcos de sillar con vista al Misti.",
            price = 0.0,
            categories = listOf("Cultura", "Mirador"),
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Yanahuara_Mirador.jpg/1200px-Yanahuara_Mirador.jpg",
            rating = 4.5f,
            reviewCount = 850,
            schedule = "Abierto 24 horas"
        )
    )

    val timeOfDayRecommendations = listOf(
        TimeRecommendation(1, "Noche", "https://andinoperu.b-cdn.net/wp-content/uploads/2024/03/vista-volcan-misti-desde-arequipa.webp"),
        TimeRecommendation(2, "Tarde", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTu6SSelUXTwFFagVcZnKFVaSrF7sJiM3vClg&s"),
        TimeRecommendation(3, "Día", "https://wp-content.miviaje.com/2019/12/plaza-armas-arequipa.jpg")
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