package com.example.aqpexplorer.model

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

data class TimeRecommendation(
    val id: Int,
    val timeOfDay: String,
    val imageUrl: String
)

data class Category(
    val id: Int,
    val name: String,
    val isSelected: Boolean = false
)