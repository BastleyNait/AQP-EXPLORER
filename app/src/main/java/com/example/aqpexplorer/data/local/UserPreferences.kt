package com.example.aqpexplorer.data.local

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    // Nombre del archivo interno donde se guardan los datos
    private val prefs: SharedPreferences = context.getSharedPreferences("aqp_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LANGUAGE = "language"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS = "notifications"
    }

    // --- IDIOMA ---
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "Español") ?: "Español"
    }

    fun saveLanguage(language: String) {
        prefs.edit().putString(KEY_LANGUAGE, language).apply()
    }

    // --- MODO OSCURO ---
    fun isDarkMode(): Boolean {
        // Por defecto true (tu diseño es oscuro)
        return prefs.getBoolean(KEY_DARK_MODE, true)
    }

    fun saveDarkMode(isEnabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_MODE, isEnabled).apply()
    }

    // --- NOTIFICACIONES ---
    fun areNotificationsEnabled(): Boolean {
        return prefs.getBoolean(KEY_NOTIFICATIONS, false)
    }

    fun saveNotifications(isEnabled: Boolean) {
        prefs.edit().putBoolean(KEY_NOTIFICATIONS, isEnabled).apply()
    }
}