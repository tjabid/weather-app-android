package com.tj.weather.domain.models

data class WeatherCondition(
    val temperature: Double,
    val description: String,
    val iconCode: String
)
