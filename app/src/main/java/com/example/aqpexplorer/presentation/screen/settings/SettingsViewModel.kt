package com.example.aqpexplorer.presentation.screen.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.aqpexplorer.data.local.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Singleton para comunicar el cambio de tema al MainActivity instantáneamente
object ThemeManager {
    var isDarkMode = mutableStateOf(true)
}

class SettingsViewModel(
    private val prefs: UserPreferences
) : ViewModel() {

    // --- ESTADOS (Cargados desde Disco) ---
    private val _dailyNotifications = MutableStateFlow(prefs.areNotificationsEnabled())
    val dailyNotifications: StateFlow<Boolean> = _dailyNotifications.asStateFlow()

    private val _isDarkMode = MutableStateFlow(prefs.isDarkMode())
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _currentLanguage = MutableStateFlow(prefs.getLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    // Este es visual por ahora
    private val _travelAlerts = MutableStateFlow(true)
    val travelAlerts: StateFlow<Boolean> = _travelAlerts.asStateFlow()

    // --- FUNCIONES ---

    fun toggleDailyNotifications(enabled: Boolean) {
        _dailyNotifications.value = enabled
        prefs.saveNotifications(enabled) // Guardar en SharedPreferences
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        prefs.saveDarkMode(enabled) // Guardar en SharedPreferences
        ThemeManager.isDarkMode.value = enabled // Actualizar UI Global
    }

    fun changeLanguage(language: String) {
        _currentLanguage.value = language
        prefs.saveLanguage(language) // Guardar en SharedPreferences
    }

    fun toggleTravelAlerts(enabled: Boolean) {
        _travelAlerts.value = enabled
    }
}