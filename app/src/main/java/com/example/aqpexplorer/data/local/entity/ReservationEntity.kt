package com.example.aqpexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: String, // Firebase usa Strings como ID
    val placeId: Int,
    val placeName: String,
    val placeImage: String,
    val userId: String,
    val fecha: Timestamp,
    val numPersonas: Int,
    val precioTotal: Double,
    val estado: String,
    val createdAt: Timestamp
)