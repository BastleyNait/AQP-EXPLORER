
package com.example.aqpexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.aqpexplorer.navigation.MainNavigation
import com.example.aqpexplorer.ui.theme.AQPEXPLORERTheme
import com.example.aqpexplorer.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AQPEXPLORERTheme {
                AQPExplorerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AQPExplorerApp() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    val cartItems by viewModel.cartItems.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    
    AQPEXPLORERTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "AQP Explorer", 
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    actions = {
                        // User Icon
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White
                            )
                        }
                        
                        // Cart Icon with Badge
                        IconButton(onClick = { navController.navigate("cart") }) {
                            Box {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White
                                )
                                if (cartItems.isNotEmpty()) {
                                    Badge(
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    ) {
                                        Text(cartItems.size.toString())
                                    }
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1A1A1A)
                    )
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            MainNavigation(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}