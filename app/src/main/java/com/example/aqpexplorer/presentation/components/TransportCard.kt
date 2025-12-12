package com.example.aqpexplorer.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TransportCard(
    transportInfo: String,
    lat: Double,
    lng: Double,
    placeName: String
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openGoogleMaps(context, lat, lng, placeName) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Abrir Mapa",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Cómo llegar",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transportInfo.ifEmpty { "Ver ubicación en Google Maps" },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun openGoogleMaps(context: Context, lat: Double, lng: Double, label: String) {
    val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($label)")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")

    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(browserIntent)
    }
}