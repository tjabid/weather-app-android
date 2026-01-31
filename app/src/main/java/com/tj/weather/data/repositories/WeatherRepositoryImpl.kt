package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.remote.WeatherApiService
import com.tj.weather.domain.models.WeatherForecast
import com.tj.weather.domain.repositories.WeatherRepository

class WeatherRepositoryImpl(
    private val apiService: WeatherApiService,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun getFiveDayForecast(
        latitude: Double,
        longitude: Double
    ): Result<WeatherForecast> {
        return try {
            val response = apiService.getFiveDayForecast(
                latitude = latitude,
                longitude = longitude,
                apiKey = apiKey
            )

            // Check if API returned success code
            if (response.code != "200") {
                return Result.failure(
                    Exception("API Error: ${response.code}")
                )
            }
            Result.success(weatherForecast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
