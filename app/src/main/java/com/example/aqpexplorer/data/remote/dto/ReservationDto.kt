package com.example.aqpexplorer.data.remote.dto

import com.example.aqpexplorer.data.local.entity.ReservationEntity
import com.google.firebase.Timestamp

// DTO para Reservas: Maneja nulos por seguridad
data class ReservationDto(
    // El ID no viene dentro del JSON de Firebase, viene en el nombre del documento
    val placeId: Int = 0,
    val placeName: String = "",
    val placeImage: String = "",
    val userId: String = "",
    val fecha: Timestamp? = null, // Puede ser null si falla la red al serializar
    val numPersonas: Int = 1,
    val precioTotal: Double = 0.0,
    val estado: String = "Pendiente",
    val createdAt: Timestamp? = null
)

// Mapper: Recibe el ID del documento (docId) y crea la Entidad
fun ReservationDto.toEntity(docId: String): ReservationEntity {
    return ReservationEntity(
        id = docId, // Aquí inyectamos el ID del documento
        placeId = placeId,
        placeName = placeName,
        placeImage = placeImage,
        userId = userId,
        // Si la fecha viene nula, ponemos la fecha actual para no romper la app
        fecha = fecha ?: Timestamp.now(),
        numPersonas = numPersonas,
        precioTotal = precioTotal,
        estado = estado,
        createdAt = createdAt ?: Timestamp.now()
    )
}