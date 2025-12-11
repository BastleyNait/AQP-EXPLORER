package com.example.aqpexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tourist_places")
data class TouristPlaceEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val precio: Double,
    val categoria: String,
    val imagen: String,
    val rating: Double,
    val isFavorite: Boolean,

    // Estos campos complejos ahora se manejan gracias a los Converters
    val location: Map<String, Double>,
    val transportInfo: String,
    val localTips: List<String>,
    val services: Map<String, Boolean>
)