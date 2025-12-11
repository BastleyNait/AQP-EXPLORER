package com.example.aqpexplorer.screens

import ServiceIconsRow
import TransportCard
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.R
import com.example.aqpexplorer.viewmodel.PlaceDetailViewModel
import com.example.aqpexplorer.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PlaceDetailScreen(
    placeId: Int,
    navController: NavHostController,
    viewModel: PlaceDetailViewModel = viewModel(),
    reservationViewModel: ReservationViewModel = viewModel() // NUEVO
) {
    LaunchedEffect(placeId) {
        viewModel.loadPlace(placeId)
    }

    val place by viewModel.place.collectAsState()

    val toastMsg by viewModel.toastMessage.collectAsState()
    val context = LocalContext.current // Necesario para el Toast

    val scrollState = rememberScrollState()

    // Estados para el diálogo de reserva
    var showReservationDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Date()) }
    var numPersonas by remember { mutableStateOf(1) }

    // Observar éxito de reserva
    val reservationSuccess by reservationViewModel.reservationSuccess.collectAsState()

    LaunchedEffect(reservationSuccess) {
        if (reservationSuccess != null) {
            // Mostrar mensaje y cerrar diálogo
            showReservationDialog = false
        }
    }

    LaunchedEffect(toastMsg) {
        toastMsg?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearToastMessage() // Limpiamos para que no salga de nuevo al rotar pantalla
        }
    }

    if (place == null) {
        Box(Modifier.fillMaxSize().background(Color(0xFF1A1A1A)), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val currentPlace = place!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            AsyncImage(
                model = currentPlace.imagen,
                contentDescription = currentPlace.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.White, modifier = Modifier.size(48.dp))
            }


        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(currentPlace.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        "Star",
                        tint = if (index < currentPlace.rating.toInt()) Color.Yellow else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("${currentPlace.rating} (100+ reseñas)", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Categoría: ${currentPlace.categoria}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Acerca del lugar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Text(currentPlace.description, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)

            Spacer(modifier = Modifier.height(16.dp))

            ServiceIconsRow(currentPlace.services)

            // ... (Debajo de Text(currentPlace.description ...))

            Spacer(modifier = Modifier.height(16.dp))

            TransportCard(
                transportInfo = currentPlace.transportInfo, // Asegúrate de haber agregado este campo a tu Data Class
                lat = currentPlace.location["latitude"] ?: -16.409, // Fallback por si acaso
                lng = currentPlace.location["longitude"] ?: -71.537,
                placeName = currentPlace.name
            )

            Spacer(modifier = Modifier.height(16.dp))

            ServiceIconsRow(currentPlace.services)
// ...

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("S/ ${currentPlace.precio}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("por persona", color = Color.Gray, fontSize = 12.sp)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // BOTÓN DE RESERVA FUNCIONAL
                    Button(
                        onClick = { showReservationDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier.height(50.dp).width(120.dp)
                    ) {
                        Text("Reservar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = { viewModel.toggleFavorite() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            if (currentPlace.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            "Favorite",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // DIÁLOGO DE RESERVA
    if (showReservationDialog) {
        ReservationDialog(
            placeName = currentPlace.name,
            precio = currentPlace.precio,
            numPersonas = numPersonas,
            selectedDate = selectedDate,
            onDateSelected = { newDate -> selectedDate = newDate },
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

    // SNACKBAR DE CONFIRMACIÓN
    reservationSuccess?.let { message ->
        LaunchedEffect(message) {
            // Aquí podrías mostrar un Snackbar
            reservationViewModel.clearSuccessMessage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDialog(
    placeName: String,
    precio: Double,
    numPersonas: Int,
    selectedDate: Date,             // <--- Recibe la fecha
    onDateSelected: (Date) -> Unit, // <--- Callback para actualizarla
    onNumPersonasChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Estado para mostrar/ocultar el calendario
    var showDatePicker by remember { mutableStateOf(false) }

    // Formateador para mostrar la fecha bonita en texto (ej: "15/12/2025")
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    // Lógica del DatePicker de Material 3
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.time + 86400000, // +1 día sugerido o el actual
            selectableDates = object : SelectableDates {
                // Bloqueamos fechas pasadas para que no reserven ayer
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= System.currentTimeMillis()
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Ajuste simple de zona horaria añadiendo un offset si es necesario,
                        // o tomando los millis directos + 5 horas si ves desfase.
                        // Por ahora usamos Date(millis) estándar.
                        onDateSelected(Date(millis + 18000000)) // +5h aprox para compensar UTC si es necesario, o solo Date(millis)
                        showDatePicker = false
                    }
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // El Diálogo Principal
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Reserva", color = Color.White) },
        text = {
            Column {
                Text(placeName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // --- SELECCIONAR FECHA ---
                Text("Fecha de visita:", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.calendar), contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = dateFormat.format(selectedDate))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SELECCIONAR PERSONAS ---
                Text("Número de personas:", color = Color.Gray, fontSize = 14.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = { if (numPersonas > 1) onNumPersonasChange(numPersonas - 1) },
                        modifier = Modifier.background(Color(0xFF3A3A3A), shape = RoundedCornerShape(8.dp))
                    ) {
                        Text("-", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        "$numPersonas",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    IconButton(
                        onClick = { onNumPersonasChange(numPersonas + 1) },
                        modifier = Modifier.background(Color(0xFF3A3A3A), shape = RoundedCornerShape(8.dp))
                    ) {
                        Text("+", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- TOTAL ---
                Divider(color = Color.Gray.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total a pagar:", color = Color.Gray)
                    Text(
                        "S/ ${precio * numPersonas}",
                        color = Color(0xFF4CAF50),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
            }
        },
        containerColor = Color(0xFF2A2A2A),
        shape = RoundedCornerShape(16.dp)
    )
}