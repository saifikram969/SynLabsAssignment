package com.example.synlabsassignment.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.synlabsassignment.Repository.PlacesRepository
import com.example.synlabsassignment.models.Place

class PlacesViewModel(private val repository: PlacesRepository) : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    private val _selectedPlace = MutableLiveData<Place?>()
    val selectedPlace: LiveData<Place?> = _selectedPlace

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getPlacesForCity(cityName: String) {
        _isLoading.value = true
        _error.value = null
        val result = repository.getPlacesForCity(cityName)
        if (result.isEmpty()) {
            _error.postValue("No places found for $cityName")
        } else {
            _places.postValue(result)
        }
        _isLoading.postValue(false)
    }

    fun getPlaceDetails(placeId: String) {
        _isLoading.value = true
        _error.value = null
        val result = repository.getPlaceDetails(placeId)
        if (result == null) {
            _error.postValue("Place details not found")
        } else {
            _selectedPlace.postValue(result)
        }
        _isLoading.postValue(false)
    }

    fun filterPlacesByType(type: String) {
        _isLoading.value = true
        _error.value = null
        val result = repository.getPlacesByType(type)
        if (result.isEmpty()) {
            _error.postValue("No $type places found")
        } else {
            _places.postValue(result)
        }
        _isLoading.postValue(false)
    }

    fun clearSelectedPlace() {
        _selectedPlace.postValue(null)
    }

    fun clearError() {
        _error.postValue(null)
    }
}