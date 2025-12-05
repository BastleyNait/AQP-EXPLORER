
package com.example.aqpexplorer.screens

import android.widget.Toast
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.MainViewModel
import com.example.aqpexplorer.data.SampleData

@Composable
fun PlaceDetailScreen(placeId: Int, navController: NavHostController, viewModel: MainViewModel) {
    val place = SampleData.touristPlaces.find { it.id == placeId } 
        ?: SampleData.touristPlaces.first()
    
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isFavorite = favoriteIds.contains(place.id)
    
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        // Imagen principal con overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            
            // Back button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            // Favorite button
            IconButton(
                onClick = { viewModel.toggleFavorite(place.id) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
        
        // Contenido principal
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título y rating
            Text(
                text = place.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (index < place.rating.toInt()) Color.Yellow else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "${place.rating} (${place.reviewCount} reseñas)",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Categorías
            Text(
                text = "Categorías: ${place.categories.joinToString(", ")}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Acerca del lugar
            Text(
                text = "Acerca del lugar",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = place.description,
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Detalles Clave
            Text(
                text = "Detalles Clave",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Horarios
            Text(
                text = "Horarios de Atención:",
                color = Color(0xFF007AFF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = place.schedule,
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Eventos
            if (place.events.isNotEmpty()) {
                Text(
                    text = "Próximos Eventos:",
                    color = Color(0xFF007AFF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                place.events.forEach { event ->
                    Text(
                        text = "• $event",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Precio y botón de reserva
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${place.currency} ${place.price}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "por persona",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                
                Button(
                    onClick = { 
                        navController.navigate("reservation_form/${place.id}")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF)
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(120.dp)
                ) {
                    Text(
                        text = "Reservar",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(100.dp)) // Espacio para bottom navigation
        }
    }
}