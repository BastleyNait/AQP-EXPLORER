package com.example.aqpexplorer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        // Header
        SettingsHeader()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Notificaciones
        NotificationSection()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // General
        GeneralSection()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Acerca de
        AboutSection()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Notificaciones pendientes
        PendingNotificationsSection()
        
        Spacer(modifier = Modifier.height(100.dp)) // Espacio para bottom navigation
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
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun NotificationSection() {
    var dailyNotifications by remember { mutableStateOf(false) }
    var travelAlerts by remember { mutableStateOf(true) }
    
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Notificaciones",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        SettingItemWithSwitch(
            icon = Icons.Default.Notifications,
            title = "Notificaciones diarias",
            subtitle = "Recibir notificaciones diarias",
            isChecked = dailyNotifications,
            onCheckedChange = { dailyNotifications = it }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SettingItemWithSwitch(
            icon = Icons.Default.Notifications,
            title = "Alertas de viaje",
            subtitle = "Recibe notificaciones sobre tus viajes",
            isChecked = travelAlerts,
            onCheckedChange = { travelAlerts = it }
        )
    }
}

@Composable
fun GeneralSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "General",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        SettingItem(
            icon = Icons.Default.Language,
            title = "Idiomas",
            subtitle = "Español (predeterminado)",
            onClick = { }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SettingItem(
            icon = Icons.Default.History,
            title = "Historial de actividades",
            subtitle = "Ver actividades recientes",
            onClick = { }
        )
    }
}

@Composable
fun AboutSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Acerca de",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        SettingItem(
            icon = Icons.Default.Person,
            title = "Créditos",
            subtitle = "Equipo de desarrollo",
            onClick = { }
        )
    }
}

@Composable
fun PendingNotificationsSection() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Notificaciones pendientes",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        SettingItem(
            icon = Icons.Default.Settings,
            title = "Confirmación de reserva",
            subtitle = "Tour de la ciudad confirmado para mañana",
            onClick = { }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        SettingItem(
            icon = Icons.Default.DateRange,
            title = "Nuevo evento cerca",
            subtitle = "Eventos próximos en tu área",
            onClick = { }
        )
    }
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        
        Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray,
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF34C759),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color(0xFF2A2A2A)
            )
        )
    }
}