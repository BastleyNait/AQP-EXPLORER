package com.example.aqpexplorer

import android.app.Application
import com.example.aqpexplorer.data.local.database.AppDatabase
import com.example.aqpexplorer.data.remote.FirestoreService
import com.example.aqpexplorer.data.repository.ReservationRepository
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import com.google.firebase.firestore.FirebaseFirestore

class ExplorerApplication : Application() {

    // Base de datos (Room)
    val database by lazy { AppDatabase.getDatabase(this) }

    // Instancia de Firestore (Nube)
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    // Repositorio de Lugares
    val repository by lazy {
        TouristPlaceRepository(
            dao = database.touristPlaceDao(),
            remote = FirestoreService(firestore)
        )
    }

    // Repositorio de Reservas (AQUÍ LO INYECTAMOS)
    val reservationRepository by lazy {
        ReservationRepository(
            dao = database.reservationDao(),
            firestore = firestore
        )
    }
}