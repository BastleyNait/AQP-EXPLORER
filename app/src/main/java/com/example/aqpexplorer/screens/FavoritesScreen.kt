package com.example.aqpexplorer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel = viewModel()
) {
    var selectedFilter by remember { mutableStateOf("Por Proximidad") }
    val favoritePlaces by viewModel.favorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        FavoritesHeader()

        Spacer(modifier = Modifier.height(16.dp))

        FavoriteFilters(selectedFilter) { selectedFilter = it }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else if (favoritePlaces.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes favoritos aún", color = Color.Gray)
            }
        } else {
            FavoritesList(navController, favoritePlaces, viewModel)
        }
    }
}

@Composable
fun FavoritesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Favoritos",
            color = Color.White,
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
        items(listOf("Por Proximidad", "Por Fecha")) { filter ->
            FilterChip(
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        filter,
                        color = if (selectedFilter == filter) Color.White else Color.Gray,
                        fontSize = 14.sp
                    )
                },
                selected = selectedFilter == filter,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2A2A2A),
                    selectedContainerColor = Color(0xFF4A4A4A)
                )
            )
        }
    }
}

@Composable
fun FavoritesList(
    navController: NavHostController,
    places: List<TouristPlace>,
    viewModel: FavoritesViewModel // NUEVO parámetro
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(places) { place ->
            FavoriteCard(
                place = place,
                onClick = { navController.navigate("place_detail/${place.id}") },
                onRemove = { viewModel.removeFavorite(place.id) } // CONECTADO
            )
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun FavoriteCard(
    place: TouristPlace,
    onClick: () -> Unit,
    onRemove: () -> Unit // NUEVO callback
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.width(120.dp).fillMaxHeight()) {
                AsyncImage(
                    model = place.imagen,
                    contentDescription = place.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(place.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = place.description.take(60) + "...",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Precio: S/ ${place.precio}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Categoría: ${place.categoria}", color = Color.Gray, fontSize = 10.sp)
                    }

                    Row {
                        IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Share, "Share", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                        // AHORA FUNCIONA EL DELETE
                        IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, "Remove", tint = Color.Red, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}