package com.example.aqpexplorer.data.remote.dto

import com.example.aqpexplorer.data.local.entity.TouristPlaceEntity

// DTO: Data Transfer Object (Espejo de Firebase)
data class TouristPlaceDto(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val precio: Double = 0.0,
    val categoria: String = "",
    val imagen: String = "",
    val rating: Double = 0.0,
    val isFavorite: Boolean = false,
    // Firestore soporta listas y mapas nativamente
    val location: Map<String, Double> = emptyMap(),
    val transportInfo: String = "",
    val localTips: List<String> = emptyList(),
    val services: Map<String, Boolean> = emptyMap()
)

// Función de extensión para convertir DTO -> Entity de Room
fun TouristPlaceDto.toEntity(): TouristPlaceEntity {
    return TouristPlaceEntity(
        id = id,
        name = name,
        description = description,
        precio = precio,
        categoria = categoria,
        imagen = imagen,
        rating = rating,
        isFavorite = isFavorite,
        location = location,
        transportInfo = transportInfo,
        localTips = localTips,
        services = services
    )
}