package com.example.aqpexplorer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.aqpexplorer.data.FavoritesRepository
import com.example.aqpexplorer.navigation.MainNavigation
import com.example.aqpexplorer.ui.theme.AQPEXPLORERTheme
import com.example.aqpexplorer.utils.NotificationHelper
import com.example.aqpexplorer.workers.ReservationReminderWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    // Solicitud de permisos para notificaciones (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupNotifications()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar repositorio de favoritos
        FavoritesRepository.init(this)

        // Sincronizar favoritos con Firebase
        CoroutineScope(Dispatchers.IO).launch {
            FavoritesRepository.syncWithFirebase()
        }

        // Configurar notificaciones
        NotificationHelper.createNotificationChannel(this)
        checkNotificationPermission()

        // Programar worker para recordatorios diarios
        scheduleReservationReminders()

        enableEdgeToEdge()
        setContent {
            AQPEXPLORERTheme {
                AQPExplorerApp()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setupNotifications()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            setupNotifications()
        }
    }

    private fun setupNotifications() {
        // Notificaciones ya configuradas
    }

    private fun scheduleReservationReminders() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // 1. TAREA PERIÓDICA (Lo ideal, cada 24h)
        val periodicRequest = PeriodicWorkRequestBuilder<ReservationReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "reservation_reminders_periodic",
            ExistingPeriodicWorkPolicy.UPDATE, // UPDATE es mejor que KEEP si cambiaste lógica
            periodicRequest
        )

        // 2. TAREA INMEDIATA (EL TRUCO PARA LA DEMO)
        // Esto fuerza al Worker a correr AHORA MISMO (o en cuanto haya internet)
        // para buscar reservas próximas y lanzar la notificación.
        val oneTimeRequest = OneTimeWorkRequestBuilder<ReservationReminderWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "reservation_reminders_now",
            ExistingWorkPolicy.REPLACE, // Si ya había una, la reemplaza y corre de nuevo
            oneTimeRequest
        )
    }
}

@Composable
fun AQPExplorerApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Opcional: define el color de fondo aquí para evitar franjas blancas
        containerColor = androidx.compose.ui.graphics.Color(0xFF1A1A1A)
    ) { innerPadding ->

        // AQUÍ ESTÁ EL CAMBIO
        MainNavigation(
            navController = navController,
            modifier = Modifier
                .padding(
                    // Mantenemos el padding inferior para no chocar con la barra de gestos
                    bottom = innerPadding.calculateBottomPadding(),
                    // ELIMINAMOS el top padding automático (que es el que estorba)
                    top = 0.dp,
                )
        )
    }
}