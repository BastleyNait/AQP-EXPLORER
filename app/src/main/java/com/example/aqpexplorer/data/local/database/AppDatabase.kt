package com.example.aqpexplorer.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aqpexplorer.data.local.converters.Converters
import com.example.aqpexplorer.data.local.dao.ReservationDao
import com.example.aqpexplorer.data.local.dao.TouristPlaceDao
import com.example.aqpexplorer.data.local.entity.ReservationEntity
import com.example.aqpexplorer.data.local.entity.TouristPlaceEntity

@Database(entities = [TouristPlaceEntity::class, ReservationEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun touristPlaceDao(): TouristPlaceDao
    abstract fun reservationDao(): ReservationDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aqp_explorer_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}