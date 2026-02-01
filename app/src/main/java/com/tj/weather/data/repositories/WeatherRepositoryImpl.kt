package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.remote.WeatherApiService
import com.tj.weather.data.mappers.WeatherApiMapper
import com.tj.weather.domain.models.WeatherForecast
import com.tj.weather.domain.repositories.WeatherRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
                    Exception("Weather service error. Please try again later.")
                )
            }

            // Map API response to domain model
            val weatherForecast = WeatherApiMapper.toDomain(response)

            Result.success(weatherForecast)
        } catch (e: UnknownHostException) {
            Result.failure(
                Exception("No internet connection. Please check your network and try again.")
            )
        } catch (e: SocketTimeoutException) {
            Result.failure(
                Exception("Request timed out. Please check your connection and try again.")
            )
        } catch (e: IOException) {
            Result.failure(
                Exception("Network error. Please check your internet connection.")
            )
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Invalid API key. Please contact support."
                404 -> "Weather data not found for this location."
                429 -> "Too many requests. Please try again in a few moments."
                500, 502, 503 -> "Weather service is temporarily unavailable. Please try again later."
                else -> "Unable to fetch weather data. Please try again."
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(
                Exception("An unexpected error occurred: ${e.message ?: "Unknown error"}")
            )
        }
    }
}
