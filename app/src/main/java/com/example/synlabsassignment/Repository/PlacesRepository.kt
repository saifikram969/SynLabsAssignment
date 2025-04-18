package com.example.synlabsassignment.Repository

import android.content.Context
import com.example.synlabsassignment.R
import com.example.synlabsassignment.models.City
import com.example.synlabsassignment.models.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream

class PlacesRepository(private val context: Context) {
    private val gson = Gson()
    private val type = object : TypeToken<Map<String, List<City>>>() {}.type

    private fun loadCities(): List<City> {
        return context.resources.openRawResource(R.raw.places).use { inputStream ->
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            gson.fromJson<Map<String, List<City>>>(jsonString, type)["cities"] ?: emptyList()
        }
    }

    fun getPlacesForCity(cityName: String): List<Place> {
        return loadCities().find { it.name.equals(cityName, ignoreCase = true) }?.places ?: emptyList()
    }

    fun getPlaceDetails(placeId: String): Place? {
        return loadCities().flatMap { it.places }.find { it.id == placeId }
    }

    fun getPlacesByType(type: String): List<Place> {
        return loadCities().flatMap { it.places }.filter { it.type.equals(type, ignoreCase = true) }
    }
}