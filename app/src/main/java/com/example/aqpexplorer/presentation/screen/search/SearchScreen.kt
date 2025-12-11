package com.example.aqpexplorer.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.TouristPlace

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    // Observamos estados del ViewModel
    val searchText by viewModel.searchText.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredPlaces by viewModel.filteredPlaces.collectAsState()

    val categories = listOf("Todos", "Histórico", "Naturaleza", "Aventura", "Cultural", "Urbano")

    Column(
        modifier = Modifier
            .fillMaxSize()
            // USO DE TEMA: Background dinámico
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- BARRA DE BÚSQUEDA ---
        Spacer(modifier = Modifier.height(16.dp))
        SearchHeader(searchText) { viewModel.onSearchTextChange(it) }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FILTROS ---
        CategoryFilters(categories, selectedCategory) { viewModel.onCategorySelected(it) }

        Spacer(modifier = Modifier.height(16.dp))

        // --- LISTA DE RESULTADOS ---
        PlacesList(
            places = filteredPlaces,
            onNavigateToDetail = onNavigateToDetail,
            onToggleFavorite = { place -> viewModel.toggleFavorite(place) }
        )
    }
}

@Composable
fun SearchHeader(searchText: String, onSearchTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = {
            Text(
                "Buscar en Arequipa...",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontSize = 16.sp
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(25.dp)
    )
}

@Composable
fun CategoryFilters(categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category

            // Colores dinámicos
            val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

            FilterChip(
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        category,
                        color = textColor,
                        fontSize = 14.sp
                    )
                },
                selected = isSelected,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                border = null
            )
        }
    }
}

@Composable
fun PlacesList(
    places: List<TouristPlace>,
    onNavigateToDetail: (Int) -> Unit,
    onToggleFavorite: (TouristPlace) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onClick = { onNavigateToDetail(place.id) },
                onFavoriteClick = { onToggleFavorite(place) }
            )
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun PlaceCard(place: TouristPlace, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // IMAGEN
            Box(modifier = Modifier.width(120.dp).fillMaxHeight()) {
                AsyncImage(
                    model = place.imagen,
                    contentDescription = place.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // INFO
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = place.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = place.description.take(50) + "...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }

                // FOOTER (Precio + Acciones)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "S/ ${place.precio}",
                            color = MaterialTheme.colorScheme.tertiary, // Verde (definido en Theme)
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = place.categoria,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontSize = 10.sp
                        )
                    }

                    Row {
                        IconButton(onClick = { /* Compartir */ }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                Icons.Default.Share,
                                "Share",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(onClick = { onFavoriteClick() }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (place.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}