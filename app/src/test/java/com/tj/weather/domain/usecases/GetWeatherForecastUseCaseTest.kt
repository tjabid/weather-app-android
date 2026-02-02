package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.DailyForecast
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.models.WeatherCondition
import com.tj.weather.domain.models.WeatherForecast
import com.tj.weather.domain.models.WeatherType
import com.tj.weather.domain.repositories.WeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetWeatherForecastUseCaseTest {

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var useCase: GetWeatherForecastUseCase

    private val testLocation = Location(latitude = 25.2048, longitude = 55.2708)

    private val testForecast = WeatherForecast(
        location = testLocation,
        dailyForecasts = listOf(
            DailyForecast(
                date = 1738368000L,
                dateFormated = "Sat, 31 Jan",
                weatherCondition = WeatherCondition(
                    temperature = 28.0,
                    description = "Clear sky",
                    iconCode = "01d"
                ),
                weatherType = WeatherType.SUNNY,
                minTemperature = 22.0,
                maxTemperature = 28.0,
                humidity = 45,
                windSpeed = 12.0,
                precipitationProbability = 10
            )
        )
    )

    @Before
    fun setup() {
        weatherRepository = mock()
        useCase = GetWeatherForecastUseCase(weatherRepository)
    }

    @Test
    fun `invoke returns success when repository returns valid forecast`() = runTest {
        // Given
        whenever(
            weatherRepository.getFiveDayForecast(
                latitude = testLocation.latitude,
                longitude = testLocation.longitude
            )
        ).thenReturn(Result.success(testForecast))

        // When
        val result = useCase(testLocation)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(testForecast, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when forecast is empty`() = runTest {
        // Given
        val emptyForecast = testForecast.copy(dailyForecasts = emptyList())
        whenever(
            weatherRepository.getFiveDayForecast(
                latitude = testLocation.latitude,
                longitude = testLocation.longitude
            )
        ).thenReturn(Result.success(emptyForecast))

        // When
        val result = useCase(testLocation)

        // Then
        assertTrue(result.isFailure)
        assertEquals("No forecast data available", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        // Given
        val exception = Exception("Network error")
        whenever(
            weatherRepository.getFiveDayForecast(
                latitude = testLocation.latitude,
                longitude = testLocation.longitude
            )
        ).thenReturn(Result.failure(exception))

        // When
        val result = useCase(testLocation)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `invoke handles exception gracefully`() = runTest {
        // Given
        whenever(
            weatherRepository.getFiveDayForecast(
                latitude = testLocation.latitude,
                longitude = testLocation.longitude
            )
        ).thenThrow(RuntimeException("Unexpected error"))

        // When
        val result = useCase(testLocation)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RuntimeException)
    }

    @Test
    fun `invoke passes correct coordinates to repository`() = runTest {
        // Given
        whenever(
            weatherRepository.getFiveDayForecast(
                latitude = testLocation.latitude,
                longitude = testLocation.longitude
            )
        ).thenReturn(Result.success(testForecast))

        // When
        useCase(testLocation)

        // Then
        org.mockito.kotlin.verify(weatherRepository).getFiveDayForecast(
            latitude = 25.2048,
            longitude = 55.2708
        )
    }
}
