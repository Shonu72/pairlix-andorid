package com.pairlix.dating.LanguageManager

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.pairlix.dating.helper.SharedPreference
import java.util.Locale

object AppLanguageManager {

    private const val DEFAULT_LANGUAGE = "en"

    // Observable state for Compose - triggers recomposition
    var currentLanguage by mutableStateOf(DEFAULT_LANGUAGE)
        private set

    var currentLocale by mutableStateOf(Locale(DEFAULT_LANGUAGE))
        private set

    fun initialize(context: Context) {
        val saved = SharedPreference.get(context).language
        val lang = if (saved.isEmpty()) DEFAULT_LANGUAGE else saved
        currentLanguage = lang
        currentLocale = Locale(lang)
    }

    fun setLanguage(context: Context, languageCode: String) {
        // ✅ Save to SharedPrefs FIRST (persists on app close)
        SharedPreference.get(context).language = languageCode

        // ✅ Update reactive state (triggers recomposition)
        currentLanguage = languageCode
        currentLocale = Locale(languageCode)

        // ✅ Update system locale
        Locale.setDefault(currentLocale)
    }

    @Suppress("DEPRECATION")
    fun forceLocaleForPicker(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        // ✅ Modify existing resources object directly
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // ✅ Also patch application resources (PictureSelector reads from here)
        context.applicationContext.resources.updateConfiguration(
            config,
            context.applicationContext.resources.displayMetrics
        )
    }
    /**
     * Get saved language - reads from SharedPreference
     */
    fun getLanguage(context: Context): String {
        val saved = SharedPreference.get(context).language
        val lang = if (saved.isEmpty()) DEFAULT_LANGUAGE else saved
        currentLanguage = lang
        currentLocale = Locale(lang)
        return currentLanguage
    }

    // ✅ Enhanced RTL check
    fun isRTL(): Boolean {
        return currentLanguage == "ar"
    }



    /**
     * Apply language to context - MUST be called in attachBaseContext
     */
    fun applyLanguage(context: Context, languageCode: String = getLanguage(context)): Context {
        // Skip if language is empty or invalid
        if (languageCode.isEmpty()) {
            return context
        }

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
        }

        // Set layout direction for RTL
        config.setLayoutDirection(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }


    fun resetLanguage(context: Context) {
        setLanguage(context, DEFAULT_LANGUAGE)
    }
}