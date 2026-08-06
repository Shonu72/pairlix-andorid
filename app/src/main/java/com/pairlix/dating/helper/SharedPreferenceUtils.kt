package com.pairlix.dating.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.pairlix.dating.R


//--------------------------------Define Preference:: DeveloperDaya--------------------------------//

class SharedPreference(context: Context) {

    val preference: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = preference.edit()

    companion object {
        const val IS_USER_EXIST = "isUserExist"

        var instance: SharedPreference? = null
        fun get(ctx: Context): SharedPreference {
            if (instance == null) {
                instance = SharedPreference(ctx)
            }
            return instance!!
        }
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is Int -> edit { it.putInt(key, value) }
            is String? -> edit { it.putString(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> Log.e("TAG", "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String, defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    var isLogin: Boolean
        get() = preference["isLogin"] ?: false
        set(value) = preference.set("isLogin", value)
    var mobileNumber: String
        get() = preference["mobileNumber", ""] ?: ""
        set(value) = preference.set("mobileNumber", value)

    var deviceToken: String
        get() = preference["device_token", ""] ?: ""
        set(value) = preference.set("device_token", value)

    var userID: String
        get() = preference["user_id", ""] ?: ""
        set(value) = preference.set("user_id", value)

    var city: String
        get() = preference["city", ""] ?: ""
        set(value) = preference.set("city", value)

    var language: String
        get() = preference["language", ""] ?: ""
        set(value) = preference.set("language", value)

    var firstName: String
        get() = preference["firstName", ""] ?: ""
        set(value) = preference.set("firstName", value)

    var email: String
        get() = preference["email", ""] ?: ""
        set(value) = preference.set("email", value)

    var gender: String
        get() = preference["gender", ""] ?: ""
        set(value) = preference.set("gender", value)

    var dob: String
        get() = preference["dob", ""] ?: ""
        set(value) = preference.set("dob", value)

    var anniversary: String
        get() = preference["anniversary", ""] ?: ""
        set(value) = preference.set("anniversary", value)

    var lastName: String
        get() = preference["lastName", ""] ?: ""
        set(value) = preference.set("lastName", value)

    var accessToken: String
        get() = preference["access_token", ""] ?: ""
        set(value) = preference.set("access_token", value)


    var profileCompletionPercentage: String
        get() = preference["profileCompletionPercentage", ""] ?: "0"
        set(value) = preference.set("profileCompletionPercentage", value)

    var id: String
        get() = preference["id", ""] ?: ""
        set(value) = preference.set("id", value)

    var guestId: String
        get() = preference["guestId", ""] ?: ""
        set(value) = preference.set("guestId", value)

    var isGuest: Boolean
        get() = preference["isGuest"] ?: false
        set(value) = preference.set("isGuest", value)

    var isFirstLaunch: Boolean
        get() = preference.getBoolean("is_first_launch", true) // true = show language on fresh/clear
        set(value) = preference.edit().putBoolean("is_first_launch", value).apply()

    var numberVerified: Boolean
        get() = preference["numberVerified"] ?: false
        set(value) = preference.set("numberVerified", value)

    var tempNumberVerified: Boolean
        get() = preference["tempNumberVerified"] ?: false
        set(value) = preference.set("tempNumberVerified", value)

    var emailVerified: Boolean
        get() = preference["emailVerified"] ?: false
        set(value) = preference.set("emailVerified", value)

    var tempEmailVerified: Boolean
        get() = preference["tempEmailVerified"] ?: false
        set(value) = preference.set("tempEmailVerified", value)

    var location: String
        get() = preference["location", ""] ?: ""
        set(value) = preference.set("location", value)

    var lat: String
        get() = preference["lat", ""] ?: ""
        set(value) = preference.set("lat", value)

    var long: String
        get() = preference["long", ""] ?: ""
        set(value) = preference.set("long", value)


    var countryCode: String
        get() = preference["countryCode", ""] ?: ""
        set(value) = preference.set("countryCode", value)

    var countryISO: String
        get() = preference["countryISO", ""] ?: ""
        set(value) = preference.set("countryISO", value)


    var OtpType: Int
        get() = preference["OtpType", 1] ?: 1
        set(value) = preference.set("OtpType", value)

    var profileImage: String
        get() = preference["profile_image", ""] ?: ""
        set(value) = preference.set("profile_image", value)


    var boostEndTime: String?
        get() = preference.getString("BOOST_END_TIME", null)
        set(value) = preference.edit().putString("BOOST_END_TIME", value).apply()


    var frontDocs: String
        get() = preference["frontDocs", ""] ?: ""
        set(value) = preference.set("frontDocs", value)

    var backDocs: String
        get() = preference["backDocs", ""] ?: ""
        set(value) = preference.set("backDocs", value)


    var frontPan: String
        get() = preference["frontPan", ""] ?: ""
        set(value) = preference.set("frontPan", value)

    var backPan: String
        get() = preference["backPan", ""] ?: ""
        set(value) = preference.set("backPan", value)

    var themeMode: Int
        get() = preference.getInt("theme_mode", 0) // default = light
        set(value) = preference.edit().putInt("theme_mode", value).apply()

    var uploadedImageUrls: MutableList<String>
        get() {
            val saved = preference.getString("uploaded_image_urls", "") ?: ""
            return if (saved.isNotEmpty()) {
                saved.split(",").map { it.trim() }.toMutableList()
            } else {
                MutableList(9) { "" }  // Default to 9 empty strings if nothing saved
            }
        }
        set(value) = preference.edit().putString("uploaded_image_urls", value.joinToString(",")).apply()
}
