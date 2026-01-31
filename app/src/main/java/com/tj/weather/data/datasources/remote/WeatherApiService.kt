package com.tj.weather.data.datasources.remote

import com.tj.weather.data.dto.WeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for OpenWeatherMap 5-day forecast API
 */
interface WeatherApiService {

    /**
     * Fetch 5-day weather forecast
     *
     * @param latitude Location latitude
     * @param longitude Location longitude
     * @param apiKey OpenWeatherMap API key
     * @param units Temperature units (default: metric for Celsius)
     * @param count Number of forecast entries (default: 40 = 5 days * 8 entries per day)
     *
     * Example: https://api.openweathermap.org/data/2.5/forecast?appid=YUOR_API_KEY&units=metric&cnt=40&lat=25.2048&lon=55.2708
     */
    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int = 40
    ): WeatherApiResponse

}
