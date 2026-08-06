package com.gravito.waiter_.Localization

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.gravito.waiter_.Localization.Const.lang
import com.pairlix.dating.LanguageManager.LocalLanguageManager
import java.util.Locale
import android.content.res.Configuration
/*
object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onCreate(context: Context) {
        val lang: String? = if (getLanguage(context)!!.isEmpty()) {
            getPersistedData(context, Locale.getDefault().language)
        } else {
            getLanguage(context)
        }
        setLocale(context, lang)
    }

    fun onCreate(context: Context, defaultLanguage: String) {
        val lang = getPersistedData(context, defaultLanguage)
        setLocale(context, lang)
    }

    private fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    // NEW: Add this method to get current locale
    fun getCurrentLocale(context: Context): Locale {
        val language = getPersistedData(context, "en") ?: "en"
        return Locale(language)
    }

    fun setLocale(context: Context, language: String?): Context {
        persist(context, language)
        lang = "" + language
        changeLang(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(SELECTED_LANGUAGE, language)
        editor.apply()
    }

    @TargetApi(Build.VERSION_CODES.P)
    fun updateResources(context: Context, language: String?): Context {
        val locale = Locale(language)
        Log.d("LocaleHelper", "language above 24: $language")
        Locale.setDefault(locale)
        val resources = context.resources
        val localeList = LocaleList(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(localeList)
        configuration.setLayoutDirection(locale)
        val newContext = context.createConfigurationContext(configuration)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return newContext
    }

    fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale = Locale(language)
        Log.d("LocaleHelper", "language below 24: $language")
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    fun changeLang(context: Context, lang_code: String?): ContextWrapper {
        var context = context
        val sysLocale: Locale
        val rs = context.resources
        val config = rs.configuration
        sysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            config.locales[0]
        } else {
            config.locale
        }
        if (lang_code != "" && sysLocale.language != lang_code) {
            val locale = Locale(lang_code)
            Locale.setDefault(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                config.setLocale(locale)
            } else {
                config.locale = locale
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config)
            } else {
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
        }
        return ContextWrapper(context)
    }
}*/

@Composable
fun localizedString(id: Int): String {
    val languageManager = LocalLanguageManager.current
    val context = LocalContext.current

    val config = Configuration(context.resources.configuration)
    config.setLocale(languageManager.locale)
    val localizedContext = context.createConfigurationContext(config)

    return localizedContext.resources.getString(id)
}