package com.example.aqpexplorer.presentation.screen.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.presentation.components.TransportCard
import com.example.aqpexplorer.presentation.screen.reservations.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PlaceDetailScreen(
    navController: NavHostController,
    viewModel: PlaceDetailViewModel,
    reservationViewModel: ReservationViewModel
) {
    val place by viewModel.place.collectAsState()
    val toastMsg by viewModel.toastMessage.collectAsState()
    val reservationSuccess by reservationViewModel.reservationSuccess.collectAsState()

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Estados para el diálogo
    var showReservationDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) }
    var numPersonas by remember { mutableIntStateOf(1) } // Optimizado para enteros

    // Manejo de Toast
    LaunchedEffect(toastMsg) {
        toastMsg?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage()
        }
    }

    // Manejo de éxito de reserva
    LaunchedEffect(reservationSuccess) {
        if (reservationSuccess != null) {
            showReservationDialog = false
            Toast.makeText(context, reservationSuccess, Toast.LENGTH_LONG).show()
            reservationViewModel.clearMessage() // Método que agregamos antes
        }
    }

    if (place == null) {
        Box(
            Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val currentPlace = place!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // --- IMAGEN PRINCIPAL ---
        Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
            AsyncImage(
                model = currentPlace.imagen,
                contentDescription = currentPlace.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradiente para el botón de atrás
            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.8f))
                    )
                )
            )
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp).padding(top = 24.dp)
            ) {
                Icon(Icons.Default.ArrowBack, "Atrás", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            // Título y Rating
            Text(currentPlace.name, color = MaterialTheme.colorScheme.onBackground, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if (index < currentPlace.rating.toInt()) Color(0xFFFFD700) else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("${currentPlace.rating}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información
            Text("Descripción", color = MaterialTheme.colorScheme.primary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                currentPlace.description,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            TransportCard(
                transportInfo = currentPlace.transportInfo,
                // Usamos lat/lng del objeto, con valores por defecto de seguridad (Plaza de Armas)
                lat = currentPlace.location["latitude"] ?: -16.3988,
                lng = currentPlace.location["longitude"] ?: -71.5369,
                placeName = currentPlace.name
            )

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(24.dp))

            // FOOTER: PRECIO Y BOTONES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "S/ ${currentPlace.precio}",
                        color = MaterialTheme.colorScheme.onBackground, // O color.tertiary
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("por persona", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Botón Favorito
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (currentPlace.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (currentPlace.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón Reservar (Arreglado)
                    Button(
                        onClick = { showReservationDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text("Reservar", fontWeight = FontWeight.Bold)
                    }
                }
            }
            // Espacio final
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // --- DIÁLOGO DE RESERVA ---
    if (showReservationDialog) {
        ReservationDialog(
            placeName = currentPlace.name,
            precio = currentPlace.precio,
            numPersonas = numPersonas,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            onNumPersonasChange = { numPersonas = it },
            onConfirm = {
                reservationViewModel.createReservation(
                    placeId = currentPlace.id,
                    placeName = currentPlace.name,
                    placeImage = currentPlace.imagen,
                    fecha = selectedDate,
                    numPersonas = numPersonas,
                    precioTotal = currentPlace.precio * numPersonas
                )
            },
            onDismiss = { showReservationDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDialog(
    placeName: String,
    precio: Double,
    numPersonas: Int,
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
    onNumPersonasChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // --- DATE PICKER DIALOG ---
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis() + 86400000 // Mañana por defecto
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Ajuste UTC simple para evitar el "día anterior"
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = millis
                        calendar.add(Calendar.HOUR_OF_DAY, 5) // +5h offset de seguridad
                        onDateSelected(calendar.time)
                        showDatePicker = false
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- DIÁLOGO PRINCIPAL ---
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text("Reservar Experiencia", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(placeName, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Fecha
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.DateRange, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(dateFormat.format(selectedDate))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contador de Personas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Personas:", color = MaterialTheme.colorScheme.onSurface)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledIconButton(
                            onClick = { if (numPersonas > 1) onNumPersonasChange(numPersonas - 1) },
                            modifier = Modifier.size(32.dp)
                        ) { Text("-", fontWeight = FontWeight.Bold) }

                        Text(
                            "$numPersonas",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.titleMedium
                        )

                        FilledIconButton(
                            onClick = { onNumPersonasChange(numPersonas + 1) },
                            modifier = Modifier.size(32.dp)
                        ) { Text("+", fontWeight = FontWeight.Bold) }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                // Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total:", fontWeight = FontWeight.Bold)
                    Text(
                        "S/ ${precio * numPersonas}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary // Verde dinero
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth()) {
                Text("Confirmar Reserva")
            }
        }
    )
}