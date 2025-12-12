package com.example.aqpexplorer.presentation.screen.settings

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Observamos el estado del ViewModel
    val dailyNotifications by viewModel.dailyNotifications.collectAsState()
    val travelAlerts by viewModel.travelAlerts.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // USO DE TEMA
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // --- BOTÓN ATRÁS ---
        IconButton(
            onClick = { onNavigateBack() },
            modifier = Modifier.padding(top = 16.dp, start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
        }

        // Header
        SettingsHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN NOTIFICACIONES ---
        SettingsSectionTitle("Notificaciones")

        SettingItemWithSwitch(
            icon = Icons.Default.Notifications,
            title = "Notificaciones diarias",
            subtitle = "Recibir novedades al día",
            isChecked = dailyNotifications,
            onCheckedChange = { viewModel.toggleDailyNotifications(it) }
        )

        SettingItemWithSwitch(
            icon = Icons.Default.DateRange, // Icono alternativo
            title = "Alertas de viaje",
            subtitle = "Avisos sobre tus reservas",
            isChecked = travelAlerts,
            onCheckedChange = { viewModel.toggleTravelAlerts(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN GENERAL ---
        SettingsSectionTitle("General")

        SettingItem(
            icon = Icons.Default.Settings,
            title = "Idiomas",
            subtitle = "Español (predeterminado)",
            onClick = { }
        )

        SettingItem(
            icon = Icons.Default.Info,
            title = "Historial de actividades",
            subtitle = "Ver actividades recientes",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN ACERCA DE ---
        SettingsSectionTitle("Acerca de")

        SettingItem(
            icon = Icons.Default.Person,
            title = "Créditos",
            subtitle = "Equipo de desarrollo AQP Explorer",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- PENDIENTES (Ejemplo visual) ---
        SettingsSectionTitle("Notificaciones pendientes")

        SettingItem(
            icon = Icons.Default.CheckCircle,
            title = "Confirmación de reserva",
            subtitle = "Tour Misti confirmado",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SettingsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Configuración",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingItemWithSwitch(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}