package com.example.aqpexplorer.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aqpexplorer.ExplorerApplication
import com.example.aqpexplorer.utils.NotificationHelper
import java.util.Calendar
import java.util.concurrent.TimeUnit


class ReservationReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {

            val app = applicationContext as ExplorerApplication
            val repo = app.reservationRepository

            // --- VALIDACIÓN DE INTERNET ---
            try {
                // Este método suspende la ejecución hasta que Firebase responda (o falle)
                repo.syncReservations("user123")
            } catch (e: Exception) {
                // Si entra aquí, es probable que no haya internet o Firebase falló
                Log.e("SYNC_TEST", "3. [Worker]  FALLÓ la sincronización: ${e.message}")
            }

            // --- LECTURA LOCAL ---
            val reservations = repo.getConfirmedReservations()
            checkUpcomingReservations(reservations)
            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun checkUpcomingReservations(reservations: List<com.example.aqpexplorer.data.local.entity.ReservationEntity>) {

        // 1. Obtenemos la fecha de HOY a medianoche (00:00:00)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        reservations.forEach { reservation ->
            // 2. Obtenemos la fecha de la RESERVA a medianoche (00:00:00)
            val resCalendar = Calendar.getInstance().apply {
                time = reservation.fecha.toDate() // Convertimos timestamp a Date
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // 3. Calculamos la diferencia entre las dos "medianoches"
            val diffInMillis = resCalendar.timeInMillis - today.timeInMillis
            val daysUntil = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

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