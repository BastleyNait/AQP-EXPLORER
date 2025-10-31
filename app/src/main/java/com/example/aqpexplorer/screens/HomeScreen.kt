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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.SampleData
import com.example.aqpexplorer.data.TimeRecommendation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .verticalScroll(scrollState)
    ) {
        // Header con búsqueda
        SearchHeader()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Carrusel de imágenes
        ImageCarousel()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Filtros de tiempo
        TimeFilters()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recomendaciones
        RecommendationsSection(navController)
        
        Spacer(modifier = Modifier.height(100.dp)) // Espacio para bottom navigation
    }
}

@Composable
fun SearchHeader() {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        placeholder = { 
            Text(
                "Hinted search text", 
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

            .padding(horizontal = 16.dp , vertical = 10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel() {
    val pagerState = rememberPagerState()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .width(300.dp)
            .height(205.dp)
            .padding(horizontal = 16.dp)
    ) {
        HorizontalPager(
            count = SampleData.carouselImages.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = SampleData.carouselImages[page],
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
                    text = "Carrusel de imágenes",
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
fun TimeFilters() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 18.dp)
    ) {
        items(listOf("Naturaleza", "Exterior", "Interior", "Noche", "Tarde")) { filter ->
            FilterChip(
                onClick = { },
                label = { 
                    Text(
                        filter,
                        color = Color.White,
                        textAlign = TextAlign.Center,

                        fontSize = 14.sp
                    ) 
                },
                selected = false,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2A2A2A),
                    labelColor = Color.White,
                    selectedContainerColor = Color(0xFF4A4A4A)
                )
            )
        }
    }
}

@Composable
fun RecommendationsSection(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Recomendaciones",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SampleData.timeOfDayRecommendations) { recommendation ->
                RecommendationCard(
                    recommendation = recommendation,
                    onClick = { navController.navigate("place_detail/${recommendation.id}") }
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(
    recommendation: TimeRecommendation,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(295.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = recommendation.imageUrl,
            contentDescription = recommendation.timeOfDay,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
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
        
        Text(
            text = recommendation.timeOfDay,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}