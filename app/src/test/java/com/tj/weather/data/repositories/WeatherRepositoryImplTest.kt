package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.remote.WeatherApiService
import com.tj.weather.test.TestDataFactory
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class WeatherRepositoryImplTest {

    private lateinit var apiService: WeatherApiService
    private lateinit var repository: WeatherRepositoryImpl

    private val testApiKey = "test_api_key"
    private val testLatitude = 25.2048
    private val testLongitude = 55.2708

    @Before
    fun setup() {
        apiService = mock()
        repository = WeatherRepositoryImpl(apiService, testApiKey)
    }

    @Test
    fun `getFiveDayForecast returns success with valid response`() = runTest {
        // Given - Create realistic mock data with multiple forecast items for a day
        val baseTimestamp = 1738368000L
        val forecastItems = listOf(
            TestDataFactory.createTestForecastItem(
                timestamp = baseTimestamp,
                dateTimeText = "2026-02-01 00:00:00"
            ),
            TestDataFactory.createTestForecastItem(
                timestamp = baseTimestamp + 10800, // +3 hours
                dateTimeText = "2026-02-01 03:00:00"
            ),
            TestDataFactory.createTestForecastItem(
                timestamp = baseTimestamp + 43200, // +12 hours (noon)
                dateTimeText = "2026-02-01 12:00:00"
            )
        )

        val mockResponse = TestDataFactory.createTestWeatherApiResponse(
            forecastList = forecastItems
        )

        whenever(
            apiService.getFiveDayForecast(
                latitude = testLatitude,
                longitude = testLongitude,
                apiKey = testApiKey
            )
        ).thenReturn(mockResponse)

        // When
        val result = repository.getFiveDayForecast(testLatitude, testLongitude)

        // Then
        assertTrue("Result should be success", result.isSuccess)
        val forecast = result.getOrNull()
        assertTrue("Forecast should not be null", forecast != null)
        assertEquals("Latitude should match", testLatitude, forecast?.location?.latitude ?: 0.0, 0.0001)
        assertEquals("Longitude should match", testLongitude, forecast?.location?.longitude ?: 0.0, 0.0001)
        assertTrue("Should have at least one daily forecast", forecast?.dailyForecasts?.isNotEmpty() == true)
    }

    @Test
    fun `getFiveDayForecast returns failure for non-200 API code`() = runTest {
        // Given
        val mockResponse = TestDataFactory.createTestWeatherApiResponse(code = "401")
        whenever(
            apiService.getFiveDayForecast(1.0, 1.0, "")
        ).thenReturn(mockResponse)

        // When
        val result = repository.getFiveDayForecast(testLatitude, testLongitude)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `getFiveDayForecast handles generic exception`() = runTest {
        // Given
        whenever(
            apiService.getFiveDayForecast(1.0, 1.0, "")
        ).thenThrow(RuntimeException("Unexpected error"))

        // When
        val result = repository.getFiveDayForecast(testLatitude, testLongitude)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("An unexpected error occurred") == true)
    }

    @Test
    fun `getFiveDayForecast passes correct parameters to API service`() = runTest {
        // Given
        val mockResponse = TestDataFactory.createTestWeatherApiResponse()
        whenever(
            apiService.getFiveDayForecast(
                latitude = testLatitude,
                longitude = testLongitude,
                apiKey = testApiKey
            )
        ).thenReturn(mockResponse)

        // When
        repository.getFiveDayForecast(testLatitude, testLongitude)

        // Then
        org.mockito.kotlin.verify(apiService).getFiveDayForecast(
            latitude = testLatitude,
            longitude = testLongitude,
            apiKey = testApiKey
        )
    }
}
