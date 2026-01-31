package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.Location
import com.tj.weather.domain.models.WeatherForecast
import com.tj.weather.domain.repositories.WeatherRepository

class GetWeatherForecastUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(location: Location): Result<WeatherForecast> {
        return try {
            val result = weatherRepository.getFiveDayForecast(
                latitude = location.latitude,
                longitude = location.longitude
            )

            result.fold(
                onSuccess = { forecast ->
                    if (forecast.dailyForecasts.isEmpty()) {
                        Result.failure(Exception("No forecast data available"))
                    } else {
                        Result.success(forecast)
                    }
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
