package com.example.aqpexplorer

import android.app.Application
import com.example.aqpexplorer.data.local.database.AppDatabase
import com.example.aqpexplorer.data.remote.FirestoreService
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import com.google.firebase.firestore.FirebaseFirestore

class ExplorerApplication : Application() {

    // 1. Base de datos Room (Lazy: se crea solo cuando se necesita)
    val database by lazy { AppDatabase.getDatabase(this) }

    // 2. Instancia de Firestore
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    // 3. El Repositorio Maestro (Conecta Room con Firestore)
    val repository by lazy {
        TouristPlaceRepository(
            dao = database.touristPlaceDao(),
            remote = FirestoreService(firestore)
        )
    }
}