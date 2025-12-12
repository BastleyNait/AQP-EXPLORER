package com.example.aqpexplorer.presentation.screen.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Importamos el mismo componente
import com.example.aqpexplorer.presentation.components.PlaceListCard

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToDetail: (Int) -> Unit
) {
    val favoritePlaces by viewModel.favoritePlaces.collectAsState()
    var selectedFilter by remember { mutableStateOf("Todos") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Simple
        Row(modifier = Modifier.padding(16.dp)) {
            Text("Mis Favoritos", color = MaterialTheme.colorScheme.onBackground, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        // Filtros (Reutilizamos la lógica visual si quieres, o simple LazyRow)
        // ... (Tu código de filtros aquí si lo deseas)

        Spacer(modifier = Modifier.height(8.dp))

        if (favoritePlaces.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes favoritos aún", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoritePlaces, key = { it.id }) { place ->

                    // REUTILIZACIÓN MÁXIMA
                    PlaceListCard(
                        place = place,
                        onClick = { onNavigateToDetail(place.id) },
                        actions = {
                            // BOTÓN ESPECÍFICO DE FAVORITOS: ELIMINAR
                            IconButton(
                                onClick = { viewModel.removeFavorite(place.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    "Eliminar",
                                    tint = MaterialTheme.colorScheme.error, // Rojo tema
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}