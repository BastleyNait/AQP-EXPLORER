package com.example.aqpexplorer.data

data class TouristPlace(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val precio: Double = 0.0,
    val categoria: String = "",
    val imagen: String = "",
    val rating: Double = 0.0,
    val isFavorite: Boolean = false,
    val location: Map<String, Double> = emptyMap(),

    // Texto detallado: "Tomar combi 'Azul' en Av. Ejército o taxi (aprox S/ 15)"
    val transportInfo: String = "Transporte no especificado",

    // Lista de tips: ["Llevar efectivo", "Mejor ir de mañana", "Cuidado con el sol"]
    val localTips: List<String> = emptyList(),

    // Mapa de servicios disponibles: {"baño": true, "comida": true, "guia": false}
    val services: Map<String, Boolean> = emptyMap()
)