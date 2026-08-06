package com.pairlix.dating.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8B5DF6),
    background = DarkBackground,
    surface = DarkCard,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = Color(0xFFFFFFFF),              // Border color for dark mode
    outlineVariant = Color(0xFFFFFFFF),       // Alternative border color
    surfaceVariant = Color.White   ,
    onTertiary = Color(0xFFCCCCCC),
    surfaceTint = Color.Black,
    tertiaryContainer = Color(0xFF000000),
    onTertiaryFixed=Color(0xFFFFFFFF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF590988),
    background = LightBackground,
    surface = LightCard,
    onBackground = LightText,
    onSurface = LightText,
    outline = Color(0xFF33000000),              // Border color for light mode (gray)
    outlineVariant = Color(0xFF1A000000),       // Alternative border color
    onSurfaceVariant = Color(0xFF757575) ,     // Gray text for light mode
            surfaceVariant = Color(0x33000000) ,
    onTertiary = Color(0xFF999999),
    surfaceTint = Color(0xFFF0EFFD), // Gray text for light mode
    tertiaryContainer = Color(0xFFF7F3F9),
    onTertiaryFixed = Color(0xFF000000)
)

@Composable
fun PairlixTheme(
    themeMode: Int,
    content: @Composable () -> Unit
) {

    val systemDark = isSystemInDarkTheme()

    val darkTheme = when (themeMode) {
        0 -> false          // Light
        1 -> true           // Dark
        else -> systemDark  // As per OS
    }

    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}