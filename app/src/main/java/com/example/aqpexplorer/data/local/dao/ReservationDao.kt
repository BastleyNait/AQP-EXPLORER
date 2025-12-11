package com.example.aqpexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aqpexplorer.data.local.entity.ReservationEntity

@Dao
interface ReservationDao {
    // Para el Worker: Obtener solo las confirmadas
    @Query("SELECT * FROM reservations WHERE estado = 'Confirmada'")
    suspend fun getConfirmedReservations(): List<ReservationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservations(reservations: List<ReservationEntity>)

    // Para borrar datos viejos si hiciera falta
    @Query("DELETE FROM reservations")
    suspend fun clearAll()
}