package com.example.aqpexplorer.presentation.screen.home

// --- IMPORTS DE COMPOSE UI & FOUNDATION ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager // <--- NATIVO (NO ACCOMPANIST)
import androidx.compose.foundation.pager.rememberPagerState // <--- NATIVO
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// --- IMPORTS DE MATERIAL DESIGN 3 ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// --- IMPORTS DE UTILIDADES GRÁFICAS ---
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- IMPORTS DE TERCEROS Y PROYECTO ---
import coil.compose.AsyncImage
import com.example.aqpexplorer.R
import com.example.aqpexplorer.data.TouristPlace

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Observamos los estados del ViewModel
    val places by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // FONDO: Usa el color definido en tu Theme.kt (AqpDarkBackground)
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading && places.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(modifier = Modifier.verticalScroll(scrollState)) {

                // --- 1. CABECERA (Logo + Botón Settings) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo AQP Explorer",
                        modifier = Modifier
                            .height(60.dp)
                            .widthIn(max = 200.dp),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.CenterStart
                    )

                    IconButton(onClick = { onNavigateToSettings() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Configuración",
                            modifier = Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // --- 2. CARRUSEL DE IMÁGENES (PAGER) ---
                if (places.isNotEmpty()) {
                    ImageCarousel(places.take(5))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- 3. FILTROS DE CATEGORÍA ---
                TimeFilters(selectedCategory) { category ->
                    viewModel.onCategorySelected(category)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- 4. LISTA DE RECOMENDACIONES ---
                RecommendationsSection(places, onNavigateToDetail)

                // Espacio final para que el BottomBar no tape el último item
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ImageCarousel(places: List<TouristPlace>) {
    val pagerState = rememberPagerState(pageCount = { places.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            pageSpacing = 16.dp
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
                        .background(Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        ))
                )

                Text(
                    text = place.name,
                    color = Color.White,
                    fontSize = 20.sp,
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
fun TimeFilters(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Todos", "Histórico", "Aventura", "Naturaleza", "Cultural", "Urbano")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { filter ->
            val isSelected = selectedCategory == filter

            // Colores dinámicos del Tema
            val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)

            FilterChip(
                onClick = { onCategorySelected(filter) },
                label = {
                    Text(
                        text = filter,
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isSelected,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null,
                shape = RoundedCornerShape(50)
            )
        }
    }
}

@Composable
fun RecommendationsSection(places: List<TouristPlace>, onNavigateToDetail: (Int) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Destinos Populares",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(places) { place ->
                RecommendationCard(
                    place = place,
                    onClick = { onNavigateToDetail(place.id) }
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(place: TouristPlace, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
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
                .background(Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                ))
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
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "S/ ${place.precio}",
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Indicador visual de Favorito (Corazón)
        if (place.isFavorite) {
            Text(
                text = "❤️",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                fontSize = 16.sp
            )
        }
    }
}