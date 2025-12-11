package com.example.aqpexplorer.data.mapper

import com.example.aqpexplorer.data.local.entity.TouristPlaceEntity
import com.example.aqpexplorer.data.TouristPlace

// Convierte de la Tabla SQL (Entity) al Objeto Visual (Domain)
fun TouristPlaceEntity.toDomain(): TouristPlace {
    return TouristPlace(
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