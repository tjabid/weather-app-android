package com.tj.weather.data.mappers

import com.tj.weather.domain.models.WeatherType
import com.tj.weather.test.TestDataFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class WeatherApiMapperTest {

    @Test
    fun `toDomain maps API response to WeatherForecast correctly`() {
        // Given
        val apiResponse = TestDataFactory.createTestWeatherApiResponse()

        // When
        val result = WeatherApiMapper.toDomain(apiResponse)

        // Then
        assertEquals(25.2048, result.location.latitude, 0.0001)
        assertEquals(55.2708, result.location.longitude, 0.0001)
        assertTrue(result.dailyForecasts.isNotEmpty())
    }

    @Test
    fun `toDomain creates daily forecasts from API response`() {
        // Given
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 1, 12, 0, 0)
        val day1Timestamp = calendar.timeInMillis / 1000

        val forecastItems = listOf(
            TestDataFactory.createTestForecastItem(
                timestamp = day1Timestamp,
                dateTimeText = "2026-02-01 12:00:00"
            )
        )

        val apiResponse = TestDataFactory.createTestWeatherApiResponse(
            forecastList = forecastItems
        )

        // When
        val result = WeatherApiMapper.toDomain(apiResponse)

        // Then
        assertTrue(result.dailyForecasts.isNotEmpty())
        assertEquals(25.2048, result.location.latitude, 0.0001)
    }

    @Test
    fun `toDomain maps weather types correctly`() {
        // Given - Sunny weather
        val sunnyItem = TestDataFactory.createTestForecastItem(
            weather = listOf(TestDataFactory.createTestWeather(icon = "01d"))
        )
        val apiResponse = TestDataFactory.createTestWeatherApiResponse(
            forecastList = listOf(sunnyItem)
        )

        // When
        val result = WeatherApiMapper.toDomain(apiResponse)

        // Then
        assertTrue(result.dailyForecasts.isNotEmpty())
        assertEquals(WeatherType.SUNNY, result.dailyForecasts[0].weatherType)
    }

    @Test
    fun `toDomain limits to 5 days`() {
        // Given - Create 7 days of data
        val calendar = Calendar.getInstance()
        val forecastItems = mutableListOf<com.tj.weather.data.dto.ForecastItem>()

        for (day in 0..6) {
            calendar.set(2026, Calendar.FEBRUARY, 1 + day, 12, 0, 0)
            val timestamp = calendar.timeInMillis / 1000

            forecastItems.add(
                TestDataFactory.createTestForecastItem(
                    timestamp = timestamp,
                    dateTimeText = "2026-02-${String.format("%02d", 1 + day)} 12:00:00"
                )
            )
        }

        val apiResponse = TestDataFactory.createTestWeatherApiResponse(
            forecastList = forecastItems,
            count = forecastItems.size
        )

        // When
        val result = WeatherApiMapper.toDomain(apiResponse)

        // Then
        assertTrue("Should limit to 5 days", result.dailyForecasts.size <= 5)
    }

    @Test
    fun `toDomain sorts daily forecasts by date`() {
        // Given
        val apiResponse = TestDataFactory.createTestWeatherApiResponse()

        // When
        val result = WeatherApiMapper.toDomain(apiResponse)

        // Then
        if (result.dailyForecasts.size > 1) {
            for (i in 0 until result.dailyForecasts.size - 1) {
                assertTrue(
                    "Forecasts should be sorted by date",
                    result.dailyForecasts[i].date <= result.dailyForecasts[i + 1].date
                )
            }
        }
    }
}
