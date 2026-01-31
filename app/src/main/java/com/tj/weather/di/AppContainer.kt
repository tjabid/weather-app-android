package com.tj.weather.di

import android.content.Context
import com.tj.weather.BuildConfig
import com.tj.weather.data.datasources.remote.LocationDataSource
import com.tj.weather.data.network.NetworkProvider
import com.tj.weather.data.repositories.LocationRepositoryImpl
import com.tj.weather.data.repositories.WeatherRepositoryImpl
import com.tj.weather.domain.repositories.LocationRepository
import com.tj.weather.domain.repositories.WeatherRepository
import com.tj.weather.domain.usecases.GetCurrentLocationUseCase
import com.tj.weather.domain.usecases.GetWeatherForecastUseCase

object AppContainer {
    private lateinit var locationRepository: LocationRepository
    private lateinit var weatherRepository: WeatherRepository

    fun initialize(context: Context) {
        // Location
        val locationDataSource = LocationDataSource(context)
        locationRepository = LocationRepositoryImpl(locationDataSource)

        // Network - Retrofit setup
        val okHttpClient = NetworkProvider.provideOkHttpClient()
        val retrofit = NetworkProvider.provideRetrofit(okHttpClient)
        val weatherApiService = NetworkProvider.provideWeatherApiService(retrofit)

        // Weather - Real API implementation
        weatherRepository = WeatherRepositoryImpl(
            apiService = weatherApiService,
            apiKey = BuildConfig.WEATHER_API_KEY
        )
    }

    fun provideGetCurrentLocationUseCase(): GetCurrentLocationUseCase {
        return GetCurrentLocationUseCase(locationRepository)
    }

    fun provideGetWeatherForecastUseCase(): GetWeatherForecastUseCase {
        return GetWeatherForecastUseCase(weatherRepository)
    }
}
