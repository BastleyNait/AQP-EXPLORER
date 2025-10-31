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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.aqpexplorer.data.SampleData
import com.example.aqpexplorer.data.TouristPlace

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("Arequipa") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Header con búsqueda
        SearchHeader(searchText) { searchText = it }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filtros de categoría
        CategoryFilters(selectedCategory) { selectedCategory = it }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Lista de lugares
        PlacesList(navController)
    }
}

@Composable
fun SearchHeader(searchText: String, onSearchTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = { 
            Text(
                "Arequipa", 
                color = Color.Gray,
                fontSize = 16.sp
            ) 
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.White
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

@Composable
fun CategoryFilters(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(SampleData.categories) { category ->
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
fun PlacesList(navController: NavHostController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(SampleData.touristPlaces) { place ->
            PlaceCard(
                place = place,
                onClick = { navController.navigate("place_detail/${place.id}") }
            )
        }
        
        // Espacio para bottom navigation
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun PlaceCard(place: TouristPlace, onClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(place.isFavorite) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
            ) {
                AsyncImage(
                    model = place.imageUrl,
                    contentDescription = place.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Contenido
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
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = place.shortDescription,
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
                        Text(
                            text = "Precio: ${place.currency} ${place.price}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Categorías: ${place.categories.joinToString(", ")}",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    }
                    
                    Row {
                        IconButton(
                            onClick = { /* Share */ },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        
                        IconButton(
                            onClick = { isFavorite = !isFavorite },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) Color.Red else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}