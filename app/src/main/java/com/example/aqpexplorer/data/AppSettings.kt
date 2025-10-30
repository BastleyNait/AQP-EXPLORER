package com.example.aqpexplorer.data

data class AppSettings(
    val dailyNotifications: Boolean = false,
    val travelAlerts: Boolean = true,
    val language: String = "Español",
    val activityHistory: Boolean = true
)

data class NotificationSettings(
    val reservationConfirmation: Boolean = true,
    val newNearbyEvent: Boolean = true
)

object SettingsData {
    val defaultSettings = AppSettings()
    val defaultNotifications = NotificationSettings()
}