package com.example.aqpexplorer.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.TouristPlace
import com.example.aqpexplorer.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = viewModel()
) {
    // Observamos estados del ViewModel
    val searchText by viewModel.searchText.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredPlaces by viewModel.filteredPlaces.collectAsState()

    // Lista estática de categorías (podrías sacarla de firebase también si quisieras)
    val categories = listOf("Todos", "Histórico", "Naturaleza", "Aventura", "Cultural", "Urbano")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        SearchHeader(searchText) { viewModel.onSearchTextChange(it) }
        Spacer(modifier = Modifier.height(16.dp))
        CategoryFilters(categories, selectedCategory) { viewModel.onCategorySelected(it) }
        Spacer(modifier = Modifier.height(16.dp))
        PlacesList(navController, filteredPlaces)
    }
}

@Composable
fun SearchHeader(searchText: String, onSearchTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { Text("Buscar en Arequipa...", color = Color.Gray, fontSize = 16.sp) },
        leadingIcon = { Icon(Icons.Default.Search, "Search", tint = Color.White) },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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

@Composable
fun CategoryFilters(categories: List<String>, selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        category,
                        color = if (selectedCategory == category) Color.White else Color.Gray,
                        fontSize = 14.sp
                    )
                },
                selected = selectedCategory == category,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF2A2A2A),
                    selectedContainerColor = Color(0xFF4A4A4A)
                )
            )
        }
    }
}

@Composable
fun PlacesList(navController: NavHostController, places: List<TouristPlace>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onClick = { navController.navigate("place_detail/${place.id}") }
            )
        }
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun PlaceCard(place: TouristPlace, onClick: () -> Unit) {
    // Nota: El estado de favorito aquí es solo visual local,
    // para persistirlo necesitarías pasarle un evento al ViewModel
    var isFavoriteLocal by remember { mutableStateOf(place.isFavorite) }

    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp).clickable { onClick() },
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
                        text = place.description.take(50) + "...", // Descripción corta simulada
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
                        Text("Cat: ${place.categoria}", color = Color.Gray, fontSize = 10.sp)
                    }

                    Row {
                        IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Share, "Share", tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { isFavoriteLocal = !isFavoriteLocal }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                if (isFavoriteLocal) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                "Favorite",
                                tint = if (isFavoriteLocal) Color.Red else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}