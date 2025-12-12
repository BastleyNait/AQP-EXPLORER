package com.example.aqpexplorer.core

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.aqpexplorer.MainActivity
import com.example.aqpexplorer.R

object NotificationHelper {
    private const val CHANNEL_ID = "aqp_reservations"
    private const val CHANNEL_NAME = "Recordatorios de Reservas"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notificaciones sobre tus próximas reservas"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showReservationReminder(
        context: Context,
        reservationId: String,
        placeName: String,
        daysUntil: Int
    ) {
        // VERIFICAR PERMISO ANTES DE MOSTRAR NOTIFICACIÓN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            reservationId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val message = when (daysUntil) {
            0 -> "¡Tu visita a $placeName es HOY!"
            1 -> "Mañana tienes tu visita a $placeName"
            7 -> "En una semana visitarás $placeName"
            else -> "En $daysUntil días visitarás $placeName"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Recordatorio de Reserva")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(reservationId.hashCode(), notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}