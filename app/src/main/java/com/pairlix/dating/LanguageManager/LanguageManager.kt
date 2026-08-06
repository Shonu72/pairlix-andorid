package com.pairlix.dating.LanguageManager

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

class LanguageManager(initialLanguage: String = "en") {
    var currentLanguage by mutableStateOf(initialLanguage)
    var locale by mutableStateOf(Locale(initialLanguage))

    fun setLanguage(language: String) {
        currentLanguage = language
        locale = Locale(language)
    }
}

val LocalLanguageManager = compositionLocalOf<LanguageManager> {
    error("No LanguageManager provided")
}