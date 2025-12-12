package com.example.aqpexplorer.presentation.screen.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {

    private val _dailyNotifications = MutableStateFlow(false)
    val dailyNotifications: StateFlow<Boolean> = _dailyNotifications.asStateFlow()

    private val _travelAlerts = MutableStateFlow(true)
    val travelAlerts: StateFlow<Boolean> = _travelAlerts.asStateFlow()

    fun toggleDailyNotifications(enabled: Boolean) {
        _dailyNotifications.value = enabled
    }

    fun toggleTravelAlerts(enabled: Boolean) {
        _travelAlerts.value = enabled
    }
}