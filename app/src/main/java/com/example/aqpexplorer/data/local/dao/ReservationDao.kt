package com.example.aqpexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aqpexplorer.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations ORDER BY fecha ASC")
    fun getAllReservationsFlow(): Flow<List<ReservationEntity>>
    @Query("UPDATE reservations SET estado = :newStatus WHERE id = :reservationId")
    suspend fun updateStatus(reservationId: String, newStatus: String)
    // Para el Worker: Obtener solo las confirmadas
    @Query("SELECT * FROM reservations WHERE estado = 'Confirmada'")
    suspend fun getConfirmedReservations(): List<ReservationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservations(reservations: List<ReservationEntity>)

    // Para borrar datos viejos si hiciera falta
    @Query("DELETE FROM reservations")
    suspend fun clearAll()
}