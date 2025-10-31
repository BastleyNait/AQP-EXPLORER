package com.example.aqpexplorer.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aqpexplorer.screens.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Search : Screen("search", "Búsqueda", Icons.Default.Search)
    object Favorites : Screen("favorites", "Favoritos", Icons.Default.Favorite)
    object Settings : Screen("settings", "Config", Icons.Default.Settings)
    object PlaceDetail : Screen("place_detail/{placeId}", "Detalle", Icons.Default.Place)
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }
            composable(Screen.PlaceDetail.route) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getString("placeId")?.toIntOrNull() ?: 1
                PlaceDetailScreen(placeId = placeId, navController = navController)
            }
        }
        
        // Solo mostrar bottom navigation si no estamos en la pantalla de detalle
        if (currentRoute != null && !currentRoute.startsWith("place_detail")) {
            BottomNavigationBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Favorites,
        Screen.Settings
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            ,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),

        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { screen ->
                val isSelected = currentRoute == screen.route
                
                BottomNavigationItem(
                    icon = screen.icon,
                    label = screen.title,
                    isSelected = isSelected,
                    onClick = {
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) Color(0xFF007AFF).copy(alpha = 0.2f)
                else Color.Transparent
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (!isSelected) Modifier else Modifier
            )
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF007AFF) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Text(
            text = label,
            color = if (isSelected) Color(0xFF007AFF) else Color.Gray,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}