package com.niklamix.composition.presentation.weather.model

data class WeatherItem(
    val city: String,
    val time: String,
    val condition: String,
    val imageUrl: String,
    val tempCurrent: String,
    val maxTemp: String,
    val minTemp: String,
)
