package com.tj.weather.domain.repositories

import com.tj.weather.domain.models.WeatherForecast

fun interface WeatherRepository {
    suspend fun getFiveDayForecast(latitude: Double, longitude: Double): Result<WeatherForecast>
}
