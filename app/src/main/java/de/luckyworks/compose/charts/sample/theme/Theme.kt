package de.luckyworks.compose.charts.sample.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.tehras.charts.theme.*

private val LightThemeColors = lightColors(
        primary = Red700,
        primaryVariant = Red900,
        onPrimary = Color.White,
        secondary = Red700,
        secondaryVariant = Red900,
        onSecondary = Color.White,
        error = Red800
)

private val DarkThemeColors = darkColors(
        primary = Red300,
        primaryVariant = Red700,
        secondary = Red300,
        error = Red200
)

@Composable
fun ChartsTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
) {
    MaterialTheme(
            colors = if (darkTheme) DarkThemeColors else LightThemeColors,
            shapes = shapes,
            content = content
    )
}
