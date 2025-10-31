package com.example.aqpexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.aqpexplorer.navigation.MainNavigation
import com.example.aqpexplorer.ui.theme.AQPEXPLORERTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

@Composable
fun AQPExplorerApp() {
    val navController = rememberNavController()
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        MainNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}