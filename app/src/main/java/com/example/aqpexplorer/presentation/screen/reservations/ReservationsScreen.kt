package com.example.aqpexplorer.presentation.screen.reservations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.Reservation
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReservationsScreen(
    viewModel: ReservationViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val allReservations by viewModel.allReservations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Filtrado local
    val activeReservations = remember(allReservations) {
        allReservations.filter { it.estado == "Confirmada" } // Usar constante si tienes
    }
    val historyReservations = remember(allReservations) {
        allReservations.filter { it.estado != "Confirmada" }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // USO DE TEMA
            .background(MaterialTheme.colorScheme.background)
    ) {
        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Reservas",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (isLoading && allReservations.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (allReservations.isEmpty()) {
            EmptyStateMessage()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // PRÓXIMOS
                if (activeReservations.isNotEmpty()) {
                    item {
                        SectionHeader("Próximos Viajes", Icons.Default.PlayArrow, MaterialTheme.colorScheme.tertiary)
                    }
                    items(activeReservations) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            onCancel = { viewModel.cancelReservation(reservation.id) },
                            onClick = { onNavigateToDetail(reservation.placeId) }
                        )
                    }
                } else {
                    item {
                        SectionHeader("Próximos Viajes", Icons.Default.PlayArrow, MaterialTheme.colorScheme.onSurface.copy(alpha=0.5f))
                        Text(
                            "No tienes viajes programados",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )
                    }
                }

                // HISTORIAL
                if (historyReservations.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader("Historial", Icons.Default.Info, MaterialTheme.colorScheme.onSurface.copy(alpha=0.5f))
                    }
                    items(historyReservations) { reservation ->
                        ReservationCard(
                            reservation = reservation,
                            onCancel = { viewModel.cancelReservation(reservation.id) },
                            onClick = { onNavigateToDetail(reservation.placeId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector, iconTint: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun EmptyStateMessage() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No tienes reservas aún",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ReservationCard(
    reservation: Reservation,
    onCancel: () -> Unit,
    onClick: () -> Unit
) {
    val isCancelled = reservation.estado == "Cancelada" // O usa constante
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reservation.fecha.toDate())

    // COLORES DINÁMICOS DEL TEMA
    val cardContainer = if (isCancelled) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
    val statusColor = if (isCancelled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSurface
    val subTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val opacity = if (isCancelled) 0.6f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardContainer),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // IMAGEN
            Box(modifier = Modifier.width(100.dp).height(130.dp)) {
                AsyncImage(
                    model = reservation.placeImage,
                    contentDescription = reservation.placeName,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                    contentScale = ContentScale.Crop,
                    alpha = opacity
                )
            }

            // CONTENIDO
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
                    .height(130.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = reservation.placeName,
                        color = textColor.copy(alpha = opacity),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, null, tint = subTextColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(formattedDate, color = subTextColor, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, tint = subTextColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${reservation.numPersonas} personas", color = subTextColor, fontSize = 12.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "S/ ${reservation.precioTotal}",
                        color = if (isCancelled) subTextColor else statusColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (!isCancelled) {
                        IconButton(onClick = onCancel, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, "Cancelar", tint = MaterialTheme.colorScheme.error)
                        }
                    } else {
                        Surface(
                            color = statusColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = reservation.estado.uppercase(),
                                color = statusColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}