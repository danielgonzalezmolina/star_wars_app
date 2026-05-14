package com.example.star_wars.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SW_Yellow,
    onPrimary = Color.Black,
    surface = SW_DarkGray,
    onSurface = Color.White,
    background = SW_Black,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = JediBlue,
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = SW_Black,
    background = SW_LightGray,
    onBackground = SW_Black
)

object StarWarsTheme {
    val customColors: StarWarsColors
        @Composable
        @ReadOnlyComposable
        get() = LocalStarWarsColors.current
}

@Composable
fun StarWarsTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalStarWarsColors provides DarkStarWarsPalette) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}