package com.pairlix.dating.utils



import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getCountry(): String {
        return try {
            // First try SIM card country
            val simCountry = getSimCountry()
            if (simCountry.isNotBlank()) return simCountry

            // Fallback to device locale
            Locale.getDefault().displayCountry.ifBlank { "Unknown" }
        } catch (e: Exception) {
            Locale.getDefault().displayCountry.ifBlank { "Unknown" }
        }
    }

    private fun getSimCountry(): String {
        return try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
            val countryCode = tm.simCountryIso  // e.g. "in", "us"
            if (countryCode.isNotBlank()) {
                Locale("", countryCode).displayCountry  // e.g. "India", "United States"
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
}