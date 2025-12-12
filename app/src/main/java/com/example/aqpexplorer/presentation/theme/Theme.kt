package com.example.aqpexplorer.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 1. Esquema OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = AqpPrimary,
    background = AqpDarkBackground,
    surface = AqpDarkSurface,
    onBackground = AqpDarkText,
    onSurface = AqpDarkText,
    tertiary = AqpTertiary,
    error = AqpError,
    secondary = AqpSecondary
)

// 2. Esquema CLARO
private val LightColorScheme = lightColorScheme(
    primary = AqpPrimary,
    background = AqpLightBackground,
    surface = AqpLightSurface,
    onBackground = AqpLightText,
    onSurface = AqpLightText,
    tertiary = AqpTertiary,
    error = AqpError,
    secondary = AqpSecondary // O un gris más claro si prefieres
)

@Composable
fun AQPEXPLORERTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Recibe el valor del Switch o Sistema
    content: @Composable () -> Unit
) {
    // 3. Selección de colores
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Barras transparentes (Edge-to-Edge)
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            // Control de iconos (Blancos en modo oscuro, Negros en modo claro)
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Se conecta con Type.kt
        content = content
    )
}