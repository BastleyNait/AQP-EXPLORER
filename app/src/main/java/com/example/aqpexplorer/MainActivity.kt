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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.aqpexplorer.presentation.navigation.MainNavigation
import com.example.aqpexplorer.core.NotificationHelper
import com.example.aqpexplorer.core.ReservationReminderWorker
import com.example.aqpexplorer.presentation.theme.AQPEXPLORERTheme
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) setupNotifications()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = application as ExplorerApplication
        val repository = appContainer.repository
        // Configuración
        NotificationHelper.createNotificationChannel(this)
        checkNotificationPermission()
        scheduleReservationReminders()

        enableEdgeToEdge()
        setContent {
            AQPEXPLORERTheme {
                AQPExplorerApp(repository)
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> setupNotifications()
                else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else setupNotifications()
    }
    private fun setupNotifications() {}
    private fun scheduleReservationReminders() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicRequest = PeriodicWorkRequestBuilder<ReservationReminderWorker>(24, TimeUnit.HOURS).setConstraints(constraints).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("reservation_reminders_periodic", ExistingPeriodicWorkPolicy.UPDATE, periodicRequest)
    }
}

@Composable
fun AQPExplorerApp(repository: com.example.aqpexplorer.data.repository.TouristPlaceRepository) { // Recibimos el Repo
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF1A1A1A)
    ) { innerPadding ->
        MainNavigation(
            navController = navController,
            repository = repository,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding(), top = 0.dp)
        )
    }
}