package com.pairlix.dating.ThemeManager


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class ThemeManager(initialTheme: Int) {
    var themeMode by mutableIntStateOf(initialTheme)
}

val LocalThemeManager = compositionLocalOf<ThemeManager> {
    error("No ThemeManager provided")
}

@Composable
fun isAppInDarkTheme(): Boolean {
    val themeManager = LocalThemeManager.current
    val systemDark = isSystemInDarkTheme()
    return when (themeManager.themeMode) {
        0 -> false          // Light Mode
        1 -> true           // Dark Mode
        else -> systemDark  // As per OS (2)
    }
}