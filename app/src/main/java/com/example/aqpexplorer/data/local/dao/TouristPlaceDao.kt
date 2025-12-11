package com.example.aqpexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aqpexplorer.data.local.entity.TouristPlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TouristPlaceDao {

    // Devuelve un Flow para que la UI se actualice automáticamente
    @Query("SELECT * FROM tourist_places")
    fun getAllPlaces(): Flow<List<TouristPlaceEntity>>

    // Obtener un solo lugar por ID
    @Query("SELECT * FROM tourist_places WHERE id = :id")
    suspend fun getPlaceById(id: Int): TouristPlaceEntity?

    // Insertar lista (útil para cuando bajas datos de Firebase)
    // REPLACE: Si el ID ya existe, sobreescribe los datos nuevos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<TouristPlaceEntity>)

    // Actualizar solo el favorito
    @Query("UPDATE tourist_places SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    // Limpiar base de datos
    @Query("DELETE FROM tourist_places")
    suspend fun clearAll()
}