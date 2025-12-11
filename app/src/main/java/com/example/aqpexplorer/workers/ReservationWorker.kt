package com.example.aqpexplorer.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aqpexplorer.data.Reservation
import com.example.aqpexplorer.utils.NotificationHelper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class ReservationReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            checkUpcomingReservations()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private suspend fun checkUpcomingReservations() {
        val db = Firebase.firestore
        val now = Timestamp.now()

        // Obtener todas las reservas confirmadas del usuario (aquí usarías el userId real)
        val reservations = db.collection("reservas")
            .whereEqualTo("estado", "Confirmada")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(Reservation::class.java) }

        reservations.forEach { reservation ->
            val reservationDate = reservation.fecha.toDate()
            val nowDate = now.toDate()

            val diffInMillis = reservationDate.time - nowDate.time
            val daysUntil = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

            // Notificar 7 días antes, 1 día antes, y el mismo día
            when (daysUntil) {
                7, 1, 0 -> {
                    NotificationHelper.showReservationReminder(
                        applicationContext,
                        reservation.id,
                        reservation.placeName,
                        daysUntil
                    )
                }
            }
        }
    }
}