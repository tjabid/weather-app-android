package com.tj.weather.di

import com.tj.weather.domain.models.DailyForecast
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.models.WeatherCondition
import com.tj.weather.domain.models.WeatherForecast
import com.tj.weather.domain.models.WeatherType
import com.tj.weather.domain.repositories.WeatherRepository
import java.util.Calendar

class MockWeatherRepository : WeatherRepository {
    override suspend fun getFiveDayForecast(
        latitude: Double,
        longitude: Double
    ): Result<WeatherForecast> {
        // Mock 5-day forecast data
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis / 1000

        val dailyForecasts = listOf(
            DailyForecast(
                date = today,
                weatherCondition = WeatherCondition(
                    temperature = 28.0,
                    description = "Sunny",
                    iconCode = "01d"
                ),
                weatherType = WeatherType.SUNNY,
                minTemperature = 22.0,
                maxTemperature = 28.0,
                humidity = 45,
                windSpeed = 12.0,
                precipitationProbability = 10
            ),
            DailyForecast(
                date = today + 86400,
                weatherCondition = WeatherCondition(
                    temperature = 25.0,
                    description = "Partly Cloudy",
                    iconCode = "02d"
                ),
                weatherType = WeatherType.CLOUDY,
                minTemperature = 20.0,
                maxTemperature = 25.0,
                humidity = 60,
                windSpeed = 15.0,
                precipitationProbability = 30
            ),
            DailyForecast(
                date = today + (86400 * 2),
                weatherCondition = WeatherCondition(
                    temperature = 22.0,
                    description = "Rainy",
                    iconCode = "10d"
                ),
                weatherType = WeatherType.RAINY,
                minTemperature = 18.0,
                maxTemperature = 22.0,
                humidity = 85,
                windSpeed = 20.0,
                precipitationProbability = 80
            ),
            DailyForecast(
                date = today + (86400 * 3),
                weatherCondition = WeatherCondition(
                    temperature = 26.0,
                    description = "Cloudy",
                    iconCode = "03d"
                ),
                weatherType = WeatherType.CLOUDY,
                minTemperature = 21.0,
                maxTemperature = 26.0,
                humidity = 55,
                windSpeed = 10.0,
                precipitationProbability = 20
            ),
            DailyForecast(
                date = today + (86400 * 4),
                weatherCondition = WeatherCondition(
                    temperature = 29.0,
                    description = "Clear Sky",
                    iconCode = "01d"
                ),
                weatherType = WeatherType.SUNNY,
                minTemperature = 23.0,
                maxTemperature = 29.0,
                humidity = 40,
                windSpeed = 8.0,
                precipitationProbability = 5
            )
        )

        val forecast = WeatherForecast(
            location = Location(latitude, longitude),
            dailyForecasts = dailyForecasts
        )

        return Result.success(forecast)
    }
}
