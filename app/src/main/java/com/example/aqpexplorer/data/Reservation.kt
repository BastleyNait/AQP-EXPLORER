package com.example.aqpexplorer.data

import com.google.firebase.Timestamp

data class Reservation(
    val id: String = "",
    val placeId: Int = 0,
    val placeName: String = "",
    val placeImage: String = "",
    val userId: String = "",
    val fecha: Timestamp = Timestamp.now(),
    val numPersonas: Int = 1,
    val precioTotal: Double = 0.0,

    // Por defecto es PENDIENTE o CONFIRMADA
    val estado: String = STATUS_CONFIRMED,
    val createdAt: Timestamp = Timestamp.now()
) {
    // Constantes para usar en toda la app y evitar errores de texto
    companion object {
        const val STATUS_CONFIRMED = "Confirmada"
        const val STATUS_CANCELLED = "Cancelada"
        const val STATUS_COMPLETED = "Completada"
    }
}