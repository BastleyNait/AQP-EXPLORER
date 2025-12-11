package com.example.aqpexplorer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info // Icono para historial
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow // Icono para próximos
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.Reservation
import com.example.aqpexplorer.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReservationsScreen(
    navController: NavHostController,
    viewModel: ReservationViewModel = viewModel()
) {
    // Obtenemos todas las reservas
    val allReservations by viewModel.allReservations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // LÓGICA DE FILTRADO: Separamos en dos listas
    val activeReservations = remember(allReservations) {
        allReservations.filter { it.estado == Reservation.STATUS_CONFIRMED }
    }

    val historyReservations = remember(allReservations) {
        allReservations.filter { it.estado != Reservation.STATUS_CONFIRMED }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Header Principal
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Reservas",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4CAF50)) // Verde corporativo
            }
        } else if (allReservations.isEmpty()) {
            // Estado vacío total
            EmptyStateMessage()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // SECCIÓN 1: PRÓXIMOS VIAJES
                if (activeReservations.isNotEmpty()) {
                    item {
                        SectionHeader("Próximos Viajes", Icons.Default.PlayArrow, Color(0xFF4CAF50))
                    }
                    items(activeReservations) { reservation ->
                        ReservationItemWrapper(reservation, viewModel, navController)
                    }
                } else {
                    // Si no hay activos pero sí historial, mostramos un mensajito sutil
                    item {
                        SectionHeader("Próximos Viajes", Icons.Default.PlayArrow, Color.Gray)
                        Text(
                            "No tienes viajes programados",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                        )
                    }
                }

                // SECCIÓN 2: HISTORIAL / CANCELADOS
                if (historyReservations.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionHeader("Historial", Icons.Default.Info, Color.Gray)
                    }
                    items(historyReservations) { reservation ->
                        ReservationItemWrapper(reservation, viewModel, navController)
                    }
                }
            }
        }
    }
}

// Wrapper simple para no repetir código en el LazyColumn
@Composable
fun ReservationItemWrapper(
    reservation: Reservation,
    viewModel: ReservationViewModel,
    navController: NavHostController
) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        ReservationCard(
            reservation = reservation,
            onCancel = { viewModel.cancelReservation(reservation.id) },
            onClick = { navController.navigate("place_detail/${reservation.placeId}") }
        )
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
            color = Color.White,
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
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("No tienes reservas aún", color = Color.Gray, fontSize = 16.sp)
        }
    }
}

@Composable
fun ReservationCard(
    reservation: Reservation,
    onCancel: () -> Unit,
    onClick: () -> Unit
) {
    // Lógica visual basada en estado
    val isCancelled = reservation.estado == Reservation.STATUS_CANCELLED
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reservation.fecha.toDate())

    // Colores dinámicos
    val cardBackground = if (isCancelled) Color(0xFF251818) else Color(0xFF2A2A2A) // Un rojo muy oscuro vs gris oscuro
    val statusColor = if (isCancelled) Color(0xFFFF5252) else Color(0xFF4CAF50)
    val opacity = if (isCancelled) 0.7f else 1f // Hacemos las canceladas un poco más transparentes

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(12.dp),
        // Sutil borde rojo si está cancelada
        border = if(isCancelled) null else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp) // Reset padding interno del row para que la imagen pegue al borde
        ) {
            // Imagen
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(130.dp)
            ) {
                AsyncImage(
                    model = reservation.placeImage,
                    contentDescription = reservation.placeName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)), // Clip solo lado izquierdo
                    contentScale = ContentScale.Crop,
                    alpha = opacity // Imagen un poco apagada si es historial
                )
            }

            // Contenido
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
                    .height(130.dp), // Forzamos altura para alinear verticalmente el contenido
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Parte superior: Título y Datos
                Column {
                    Text(
                        text = reservation.placeName,
                        color = Color.White.copy(alpha = opacity),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Fecha
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(formattedDate, color = Color.Gray, fontSize = 12.sp)
                    }

                    // Personas
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${reservation.numPersonas} personas", color = Color.Gray, fontSize = 12.sp)
                    }
                }

                // Parte inferior: Precio y Acción/Estado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "S/ ${reservation.precioTotal}",
                        color = if (isCancelled) Color.Gray else Color(0xFF4CAF50), // Precio gris si cancelado
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (!isCancelled) {
                        // Botón cancelar sutil
                        IconButton(
                            onClick = onCancel,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Delete, "Cancelar", tint = Color(0xFFEF5350))
                        }
                    } else {
                        // Badge de estado
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