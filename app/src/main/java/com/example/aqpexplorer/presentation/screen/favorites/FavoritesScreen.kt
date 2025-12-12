package com.example.aqpexplorer.presentation.screen.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.TouristPlace

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    // Observamos la lista filtrada desde Room
    val favoritePlaces by viewModel.favoritePlaces.collectAsState()

    // Estado local para el filtro visual (opcional)
    var selectedFilter by remember { mutableStateOf("Todos") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            // USO DE TEMA
            .background(MaterialTheme.colorScheme.background)
    ) {
        FavoritesHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros visuales (puedes conectar lógica de ordenamiento aquí si quieres)
        FavoriteFilters(selectedFilter) { selectedFilter = it }

        Spacer(modifier = Modifier.height(16.dp))

        if (favoritePlaces.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "No tienes favoritos aún",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            FavoritesList(
                places = favoritePlaces,
                onNavigateToDetail = onNavigateToDetail,
                onRemove = { id -> viewModel.removeFavorite(id) }
            )
        }
    }
}

@Composable
fun FavoritesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Mis Favoritos",
            color = MaterialTheme.colorScheme.onBackground, // Tema
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FavoriteFilters(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(listOf("Todos", "Recientes", "Económicos")) { filter ->
            val isSelected = selectedFilter == filter

            FilterChip(
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        filter,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
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
fun FavoritesList(
    places: List<TouristPlace>,
    onNavigateToDetail: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(places, key = { it.id }) { place ->
            FavoriteCard(
                place = place,
                onClick = { onNavigateToDetail(place.id) },
                onRemove = { onRemove(place.id) }
            )
        }
        // Espacio para que no lo tape el BottomBar
        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun FavoriteCard(
    place: TouristPlace,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Tema
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
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = place.description,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )
                }

                // FOOTER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "S/ ${place.precio}",
                            color = MaterialTheme.colorScheme.tertiary, // Verde del tema
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row {
                        IconButton(onClick = { /* Compartir */ }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Default.Share,
                                "Share",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Default.Delete,
                                "Remove",
                                tint = Color(0xFFEF5350), // Rojo alerta para borrar
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}