package com.example.aqpexplorer.data.remote.dto

import com.example.aqpexplorer.data.local.entity.TouristPlaceEntity
import com.google.firebase.firestore.PropertyName

// DTO: Espejo exacto de lo que hay en la nube.
data class TouristPlaceDto(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val precio: Double = 0.0,
    val categoria: String = "",
    val imagen: String = "",
    val rating: Double = 0.0,
    @get:PropertyName("isFavorite")
    @set:PropertyName("isFavorite")
    var isFavorite: Boolean = false,

    val location: Map<String, Double> = emptyMap(),
    val transportInfo: String = "",
    val localTips: List<String> = emptyList(),
    val services: Map<String, Boolean> = emptyMap()
)

// Mapper: Convierte de Nube -> Local
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