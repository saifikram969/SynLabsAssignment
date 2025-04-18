package com.example.synlabsassignment.models

data class City(
    val name: String,
    val places: List<Place>
)

data class Place(
    val id: String,
    val name: String,
    val description: String,
    val rating: Double,
    val imageUrl: String,
    val type: String
)

