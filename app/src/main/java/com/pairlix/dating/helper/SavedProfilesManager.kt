package com.pairlix.dating.helper

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pairlix.dating.response.GetMatchResponse

object SavedProfilesManager {
    private const val PREF_NAME = "saved_profiles_pref"
    private const val KEY_SAVED_PROFILES = "saved_profiles_list"
    private val gson = Gson()

    fun getSavedProfiles(context: Context): List<GetMatchResponse.Data> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SAVED_PROFILES, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<GetMatchResponse.Data>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveProfile(context: Context, profile: GetMatchResponse.Data): Boolean {
        val currentList = getSavedProfiles(context).toMutableList()
        val userId = profile.userId ?: return false
        if (currentList.none { it.userId == userId }) {
            currentList.add(0, profile)
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit { putString(KEY_SAVED_PROFILES, gson.toJson(currentList)) }
            return true
        }
        return false
    }

    fun removeProfile(context: Context, userId: String) {
        val currentList = getSavedProfiles(context).toMutableList()
        val updatedList = currentList.filterNot { it.userId == userId }
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_SAVED_PROFILES, gson.toJson(updatedList)) }
    }

    fun isProfileSaved(context: Context, userId: String): Boolean {
        if (userId.isEmpty()) return false
        return getSavedProfiles(context).any { it.userId == userId }
    }
}
