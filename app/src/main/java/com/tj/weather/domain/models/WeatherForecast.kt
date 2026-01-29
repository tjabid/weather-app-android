package com.tj.weather.domain.models

data class WeatherForecast(
    val location: Location,
    val dailyForecasts: List<DailyForecast>
)
