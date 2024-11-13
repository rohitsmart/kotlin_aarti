package com.rohit.aarti.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define colors with updated names to avoid conflicts
val Purple80Light = Color(0xFFD0BCFF)
val PurpleGrey80Light = Color(0xFFCCC2DC) // Renamed for light theme
val Pink80Light = Color(0xFFEFB8C8) // Renamed for light theme

val Purple40Dark = Color(0xFF6650a4) // Renamed for dark theme
val PurpleGrey40Dark = Color(0xFF625b71) // Renamed for dark theme
val Pink40Dark = Color(0xFF7D5260) // Renamed for dark theme

// Define Dark and Light Color Schemes
private val DarkColorScheme = darkColorScheme(
    primary = Purple40Dark,
    secondary = PurpleGrey40Dark,
    tertiary = Pink40Dark
)

private val LightColorScheme = lightColorScheme(
    primary = Purple80Light,
    secondary = PurpleGrey80Light,
    tertiary = Pink80Light
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Choose the color scheme based on the system's dark theme setting
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
