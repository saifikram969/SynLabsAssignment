package com.example.synlabsassignment

import android.app.Application
import com.example.synlabsassignment.Repository.PlacesRepository

class TravelCompanionApplication : Application() {
    val placesRepository by lazy { PlacesRepository(this) }
}