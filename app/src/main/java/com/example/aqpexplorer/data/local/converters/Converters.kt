package com.example.aqpexplorer.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // --- Convertidor para List<String> (localTips) ---
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    // --- Convertidor para Map<String, Double> (location) ---
    @TypeConverter
    fun fromLocationMap(value: Map<String, Double>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLocationMap(value: String): Map<String, Double> {
        val mapType = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(value, mapType) ?: emptyMap()
    }

    // --- Convertidor para Map<String, Boolean> (services) ---
    @TypeConverter
    fun fromServicesMap(value: Map<String, Boolean>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toServicesMap(value: String): Map<String, Boolean> {
        val mapType = object : TypeToken<Map<String, Boolean>>() {}.type
        return gson.fromJson(value, mapType) ?: emptyMap()
    }
}