package com.example.star_wars.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val SW_Yellow = Color(0xFFFFE81F)
val SW_Black = Color(0xFF000000)
val SW_DarkGray = Color(0xFF1C1C1C)
val SW_LightGray = Color(0xFFF5F5F5)

val JediBlue = Color(0xFF2E5BFF)
val SithRed = Color(0xFFE81E1E)

data class StarWarsColors(
    val accent: Color,
    val cardBackground: Color,
    val mainBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

val DarkStarWarsPalette = StarWarsColors(
    accent = SW_Yellow,
    cardBackground = SW_DarkGray,
    mainBackground = SW_Black,
    textPrimary = Color.White,
    textSecondary = Color.Gray
)

val LocalStarWarsColors = staticCompositionLocalOf<StarWarsColors> {
    error("No se proporcionó StarWarsColors")
}