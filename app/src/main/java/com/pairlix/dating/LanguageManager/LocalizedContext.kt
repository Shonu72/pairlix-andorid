package com.pairlix.dating.LanguageManager

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.Locale

class LocalizedContext(base: Context) : ContextWrapper(base) {

    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            var newContext = context
            val config = Configuration(context.resources.configuration)
            val locale = Locale(language)

            Locale.setDefault(locale)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                config.setLocale(locale)
                config.setLocales(LocaleList(locale))
            } else {
                @Suppress("DEPRECATION")
                config.locale = locale
            }

            config.setLayoutDirection(locale)

            newContext = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context.createConfigurationContext(config)
            } else {
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                context
            }

            return ContextWrapper(newContext)
        }
    }
}