package com.example.aqpexplorer.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.aqpexplorer.ExplorerApplication
import com.example.aqpexplorer.data.repository.TouristPlaceRepository
import com.example.aqpexplorer.presentation.screen.detail.PlaceDetailScreen
import com.example.aqpexplorer.presentation.screen.detail.PlaceDetailViewModel
import com.example.aqpexplorer.presentation.screen.favorites.FavoritesViewModel
import com.example.aqpexplorer.presentation.screen.favorites.FavoritesScreen
import com.example.aqpexplorer.presentation.screen.home.HomeScreen
import com.example.aqpexplorer.presentation.screen.home.HomeViewModel
import com.example.aqpexplorer.presentation.screen.reservations.ReservationViewModel
import com.example.aqpexplorer.presentation.screen.reservations.ReservationsScreen
import com.example.aqpexplorer.presentation.screen.search.SearchScreen
import com.example.aqpexplorer.presentation.screen.search.SearchViewModel
import com.example.aqpexplorer.presentation.screen.settings.SettingsScreen
import com.example.aqpexplorer.presentation.screen.settings.SettingsViewModel
import com.example.aqpexplorer.data.local.UserPreferences


@Composable
fun MainNavigation(
    navController: NavHostController,
    repository: TouristPlaceRepository,
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
                    onNavigateToDetail = { placeId ->
                        navController.navigate("place_detail/$placeId")
                    },
                    onNavigateToSettings = {
                        navController.navigate("settings")
                    }
                )
            }
            composable("search") {
                val searchViewModel: SearchViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            SearchViewModel(repository)
                        }
                    }
                )
                SearchScreen(
                    viewModel = searchViewModel,
                    onNavigateToDetail = { placeId ->
                        navController.navigate("place_detail/$placeId")
                    }
                )
            }
            composable("favorites") {
                val favViewModel: FavoritesViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            FavoritesViewModel(repository)
                        }
                    }
                )
                FavoritesScreen(
                    viewModel = favViewModel,
                    onNavigateToDetail = { placeId ->
                        navController.navigate("place_detail/$placeId")
                    }
                )
            }
            composable("reservations") {
                val resViewModel: ReservationViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            val app = (this[androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as com.example.aqpexplorer.ExplorerApplication)
                            ReservationViewModel(app.reservationRepository)
                        }
                    }
                )
                ReservationsScreen (
                    viewModel = resViewModel,
                    onNavigateToDetail = { placeId ->
                        navController.navigate("place_detail/$placeId")
                    }
                )
            }
            composable(
                route = "place_detail/{placeId}",
                arguments = listOf(navArgument("placeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getInt("placeId") ?: 0
                val detailViewModel: PlaceDetailViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            PlaceDetailViewModel(repository, placeId)
                        }
                    }
                )
                val context = LocalContext.current
                val app = context.applicationContext as ExplorerApplication
                val reservationViewModel: ReservationViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            ReservationViewModel(app.reservationRepository)
                        }
                    }
                )
                PlaceDetailScreen(
                    navController = navController,
                    viewModel = detailViewModel,
                    reservationViewModel = reservationViewModel
                )
            }
            composable("settings") {
                val context = LocalContext.current

                val userPrefs = remember { UserPreferences(context) }

                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            SettingsViewModel(userPrefs)
                        }
                    }
                )

                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
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

    NavigationBar(
        // El color de la barra se adapta solo (Surface o SurfaceContainer)
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            val selected = currentRoute?.startsWith(item.route) == true

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    // --- COLORES INTELIGENTES (Material 3) ---
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
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