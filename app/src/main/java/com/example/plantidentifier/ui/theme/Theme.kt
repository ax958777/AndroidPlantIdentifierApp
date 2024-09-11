package com.example.plantidentifier.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,
    secondary = Color(0xFF81C784),
    onSecondary = Color.Black,
    background = Color(0xFFF1F8E9),
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF81C784),
    onPrimary = Color.Black,
    secondary = Color(0xFF4CAF50),
    onSecondary = Color.White,
    background = Color(0xFF1B1B1B),
    surface = Color(0xFF2B2B2B),
    onSurface = Color.White
)

@Composable
fun PlantIdentifierTheme(
    darkTheme: Boolean = false, // You can use isSystemInDarkTheme() here
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}