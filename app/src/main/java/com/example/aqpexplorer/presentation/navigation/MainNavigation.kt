package com.example.aqpexplorer.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import com.example.aqpexplorer.presentation.screen.home.HomeScreen
import com.example.aqpexplorer.presentation.screen.home.HomeViewModel
import com.example.aqpexplorer.screens.SearchScreen // ELIMINAR
import com.example.aqpexplorer.screens.FavoritesScreen // ELIMINAR
import com.example.aqpexplorer.screens.ReservationsScreen // ELIMINAR
import com.example.aqpexplorer.screens.SettingsScreen // ELIMINAR
import com.example.aqpexplorer.screens.PlaceDetailScreen // ELIMINAR

@Composable
fun MainNavigation(
    navController: NavHostController,
    repository: TouristPlaceRepository, // Recibimos el repo aunque aún no lo uses en las viejas
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(paddingValues)
        ) {
            composable("home") {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            HomeViewModel(repository)
                        }
                    }
                )
                HomeScreen(
                    viewModel = homeViewModel,

                    // Navegación a Detalle
                    onNavigateToDetail = { placeId ->
                        navController.navigate("place_detail/$placeId")
                    },

                    // Navegación a Configuración (NUEVO)
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
            }
            // --- RUTAS ANTIGUAS (LEGACY) ---
            //composable("home") { HomeScreen(navController) }
            composable("search") { SearchScreen(navController) }
            composable("favorites") { FavoritesScreen(navController) }
            composable("reservations") { ReservationsScreen(navController) }
            composable("settings") { SettingsScreen(navController) }

            composable(
                route = "place_detail/{placeId}",
                arguments = listOf(navArgument("placeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getInt("placeId") ?: 0
                PlaceDetailScreen(placeId, navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Inicio", Icons.Default.Home),
        BottomNavItem("search", "Buscar", Icons.Default.Search),
        BottomNavItem("reservations", "Reservas", Icons.Default.DateRange),
        BottomNavItem("favorites", "Favoritos", Icons.Default.Favorite),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Colores obtenidos del Tema
    val barColor = MaterialTheme.colorScheme.surface // 0xFF2A2A2A
    val selectedColor = MaterialTheme.colorScheme.primary // 0xFF007AFF
    val indicatorColor = MaterialTheme.colorScheme.secondary // 0xFF3A3A3A
    val unselectedColor = com.example.aqpexplorer.presentation.theme.AqpGray

    NavigationBar(
        containerColor = barColor,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute?.startsWith(item.route) == true,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = indicatorColor
                )
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)