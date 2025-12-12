package com.example.aqpexplorer.presentation.screen.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aqpexplorer.core.NotificationHelper

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Observamos estados
    val dailyNotifications by viewModel.dailyNotifications.collectAsState()
    val travelAlerts by viewModel.travelAlerts.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    // Solo un diálogo: Créditos
    var showCreditsDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // --- CABECERA ---
        Row(
            modifier = Modifier.padding(top = 16.dp, start = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Configuración",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- APARIENCIA ---
        SettingsSectionTitle("Apariencia")
        SettingItemWithSwitch(
            icon = Icons.Default.Star,
            title = "Modo Oscuro",
            subtitle = if(isDarkMode) "Activado" else "Desactivado",
            isChecked = isDarkMode,
            onCheckedChange = { viewModel.toggleDarkMode(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- NOTIFICACIONES ---
        SettingsSectionTitle("Notificaciones")
        SettingItemWithSwitch(
            icon = Icons.Default.Notifications,
            title = "Novedades Diarias",
            subtitle = "Promociones y nuevos lugares",
            isChecked = dailyNotifications,
            onCheckedChange = { viewModel.toggleDailyNotifications(it) }
        )
        SettingItemWithSwitch(
            icon = Icons.Default.DateRange,
            title = "Alertas de Viaje",
            subtitle = "Recordatorios de tus reservas",
            isChecked = travelAlerts,
            onCheckedChange = { viewModel.toggleTravelAlerts(it) }
        )
        SettingItem(
            icon = Icons.Default.Settings,
            title = "Permisos del sistema",
            subtitle = "Gestionar en Android",
            onClick = { openAndroidSettings(context) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- GENERAL (Sin Idioma) ---
        SettingsSectionTitle("General")

        SettingItem(
            icon = Icons.Default.ThumbUp,
            title = "Calificar AQP Explorer",
            subtitle = "¡Danos 5 estrellas!",
            onClick = { /* Link PlayStore */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- INFORMACIÓN ---
        SettingsSectionTitle("Información")
        SettingItem(
            icon = Icons.Default.Person,
            title = "Créditos",
            subtitle = "Ver desarrolladores",
            onClick = { showCreditsDialog = true }
        )
        SettingItem(
            icon = Icons.Default.Build,
            title = "Versión",
            subtitle = "1.0.0",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(100.dp))

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current

        OutlinedButton(
            onClick = {
                NotificationHelper.showReservationReminder(
                    context,
                    reservationId = "demo_id",
                    placeName = "Monasterio (Demo)",
                    daysUntil = 1 // Simula que es mañana
                )
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red) // Rojo para ubicarlo rápido
        ) {
            Text("Probar Notificación (Debug)")
        }
    }

    // --- DIÁLOGO DE CRÉDITOS ---
    if (showCreditsDialog) {
        AlertDialog(
            onDismissRequest = { showCreditsDialog = false },
            title = { Text("AQP-EXPLORER", fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Desarrolladores:", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))

                    // LISTA DE INTEGRANTES
                    val teamMembers = listOf(
                        "Chirinos Negron, Sebastián Arley",
                        "Cuadros Amanqui, Joe Jhonny",
                        "Marrón Carcausto, Daniel Enrique",
                        "Marrón Lope, Misael Josias",
                        "Viza Cuti, Rodrigo Estefano"
                    )

                    teamMembers.forEach { name ->
                        Text(
                            text = name,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Gracias por descargar nuestra app :)",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showCreditsDialog = false }) { Text("Cerrar") }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

// Funciones de utilidad y componentes (Igual que antes)
fun openAndroidSettings(context: Context) {
    try {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingItem(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            if(subtitle.isNotEmpty()) Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f))
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha=0.3f))
    }
}

@Composable
fun SettingItemWithSwitch(icon: ImageVector, title: String, subtitle: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            if(subtitle.isNotEmpty()) Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f))
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
        )
    }
}