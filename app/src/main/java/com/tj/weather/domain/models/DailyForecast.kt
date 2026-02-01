package com.tj.weather.domain.models

data class DailyForecast(
    val date: Long,
    val dateFormated: String,
    val weatherCondition: WeatherCondition,
    val weatherType: WeatherType,
    val minTemperature: Double,
    val maxTemperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int
)
