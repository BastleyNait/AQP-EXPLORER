package com.example.aqpexplorer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsState
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.model.TimeRecommendation
import com.example.aqpexplorer.model.UiState
import com.example.aqpexplorer.viewmodel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.message,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
        is UiState.Success -> {
            HomeContent(navController, uiState.data)
        }
    }
}

@Composable
fun HomeContent(
    navController: NavHostController,
    uiState: com.example.aqpexplorer.model.HomeUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        // Header de búsqueda
        SearchHeader(navController)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Carrusel de imágenes
        ImageCarousel(uiState.carouselImages)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Filtros de tiempo
        TimeFilters(uiState.timeRecommendations)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Sección de recomendaciones
        RecommendationsSection(navController, uiState.places)
        
        Spacer(modifier = Modifier.height(100.dp)) // Espacio para bottom navigation
    }
}

@Composable
fun SearchHeader(navController: NavHostController) {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = { 
            Text(
                "Buscar lugares...", 
                color = Color.Gray,
                fontSize = 16.sp
            ) 
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { navController.navigate("search") },
        enabled = false, // Para que funcione como botón
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.White,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<String>) {
    val pagerState = rememberPagerState()
    
    if (images.isEmpty()) return
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
    ) {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = "Carousel image",
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
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
                
                // Text overlay
                Text(
                    text = "Descubre Arequipa",
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

@Composable
fun TimeFilters(timeRecommendations: List<TimeRecommendation>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(timeRecommendations) { recommendation ->
            FilterChip(
                onClick = { },
                label = { 
                    Text(
                        recommendation.timeOfDay,
                        color = Color.White,
                        fontSize = 14.sp
                    ) 
                },
                selected = false,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2A2A2A),
                    selectedContainerColor = Color(0xFF4A4A4A)
                )
            )
        }
    }
}

@Composable
fun RecommendationsSection(navController: NavHostController, places: List<com.example.aqpexplorer.model.TouristPlace>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Lugares Recomendados",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(places.take(5)) { place ->
                PlaceRecommendationCard(
                    place = place,
                    onClick = { navController.navigate("place_detail/${place.id}") }
                )
            }
        }
    }
}

@Composable
fun PlaceRecommendationCard(
    place: com.example.aqpexplorer.model.TouristPlace,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
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
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )
        
        // Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Text(
                text = place.name,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
            Text(
                text = "${place.currency}${place.price}",
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}