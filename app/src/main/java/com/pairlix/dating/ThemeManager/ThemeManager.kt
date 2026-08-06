package com.pairlix.dating.ThemeManager


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