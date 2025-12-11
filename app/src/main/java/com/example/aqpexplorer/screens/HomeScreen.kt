package com.example.aqpexplorer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings

import com.example.aqpexplorer.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val places by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState() // AHORA SÍ SE USA

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // Un poco más de aire arriba/abajo
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo",
                        // CAMBIO CLAVE AQUÍ:
                        modifier = Modifier
                            .height(80.dp) // Altura fija de cabecera (entre 50 y 70dp es estándar)
                            .widthIn(max = 200.dp), // Opcional: limita el ancho máximo si es necesario
                        contentScale = ContentScale.Fit, // Mantiene la proporción sin deformar
                        alignment = Alignment.CenterStart // Asegura que se pegue a la izquierda
                    )

                    IconButton(
                        onClick = { navController.navigate("settings") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "configuraciones",
                            modifier = Modifier.size(28.dp), // Ligeramente ajustado para elegancia
                            tint = Color.White // Asegura que se vea sobre fondo oscuro
                        )
                    }
                }

                if (places.isNotEmpty()) {
                    ImageCarousel(places.take(5))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // FILTROS AHORA FUNCIONAN
                TimeFilters(selectedCategory) { category ->
                    viewModel.onCategorySelected(category)
                }

                Spacer(modifier = Modifier.height(24.dp))

                RecommendationsSection(places, navController)

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(places: List<TouristPlace>) {
    val pagerState = rememberPagerState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
    ) {
        HorizontalPager(
            count = places.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val place = places[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = place.imagen,
                    contentDescription = place.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))))
                )

                Text(
                    text = place.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
        }
    }
}

// AHORA RECIBE selectedCategory Y onCategorySelected
@Composable
fun TimeFilters(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(listOf("Todos", "Histórico", "Aventura", "Naturaleza", "Cultural", "Urbano")) { filter ->
            FilterChip(
                onClick = { onCategorySelected(filter) },
                label = {
                    Text(
                        filter,
                        color = if (selectedCategory == filter) Color.White else Color.Gray,
                        fontSize = 14.sp
                    )
                },
                selected = selectedCategory == filter,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2A2A2A),
                    selectedContainerColor = Color(0xFF4A4A4A)
                )
            )
        }
    }
}

@Composable
fun RecommendationsSection(places: List<TouristPlace>, navController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Destinos Populares",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(places) { place ->
                RecommendationCard(
                    place = place,
                    onClick = {
                        navController.navigate("place_detail/${place.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(place: TouristPlace, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = place.imagen,
            contentDescription = place.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = place.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            Text(
                text = "S/ ${place.precio}",
                color = Color(0xFF4CAF50),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}