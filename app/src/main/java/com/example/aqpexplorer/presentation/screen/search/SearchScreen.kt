package com.example.aqpexplorer.presentation.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aqpexplorer.data.TouristPlace
// Importamos tu componente reutilizable
import com.example.aqpexplorer.presentation.components.PlaceListCard

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val searchText by viewModel.searchText.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredPlaces by viewModel.filteredPlaces.collectAsState()

    val categories = listOf("Todos", "Histórico", "Naturaleza", "Aventura", "Cultural", "Urbano")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Búsqueda
        SearchHeader(searchText) { viewModel.onSearchTextChange(it) }

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        CategoryFilters(categories, selectedCategory) { viewModel.onCategorySelected(it) }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista Reutilizando Componente
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredPlaces, key = { it.id }) { place ->
                PlaceListCard(
                    place = place,
                    onClick = { onNavigateToDetail(place.id) },
                    actions = {
                        // BOTONES ESPECÍFICOS DE BÚSQUEDA

                        // Compartir
                        IconButton(onClick = { /* Share Logic */ }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Share, "Share", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                        }

                        // Favorito (Corazón)
                        IconButton(onClick = { viewModel.toggleFavorite(place) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = if (place.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (place.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SearchHeader(searchText: String, onSearchTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = {
            Text("Buscar...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurface) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
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
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
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