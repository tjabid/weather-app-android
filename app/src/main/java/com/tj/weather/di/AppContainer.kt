package com.tj.weather.di

import android.content.Context
import com.tj.weather.BuildConfig
import com.tj.weather.data.datasources.local.LocationLocalDataSource
import com.tj.weather.data.network.NetworkProvider
import com.tj.weather.data.repositories.LocationRepositoryImpl
import com.tj.weather.data.repositories.WeatherRepositoryImpl
import com.tj.weather.domain.repositories.LocationRepository
import com.tj.weather.domain.repositories.WeatherRepository
import com.tj.weather.domain.usecases.CacheLocationUseCase
import com.tj.weather.domain.usecases.GetCachedLocationUseCase
import com.tj.weather.domain.usecases.GetWeatherForecastUseCase

object AppContainer {
    private lateinit var locationRepository: LocationRepository
    private lateinit var weatherRepository: WeatherRepository

    fun initialize(context: Context) {
        // Location
        val locationLocalDataSource = LocationLocalDataSource(context)
        locationRepository = LocationRepositoryImpl(locationLocalDataSource)

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

    fun provideGetCachedLocationUseCase(): GetCachedLocationUseCase {
        return GetCachedLocationUseCase(locationRepository)
    }

    fun provideCacheLocationUseCase(): CacheLocationUseCase {
        return CacheLocationUseCase(locationRepository)
    }

    fun provideGetWeatherForecastUseCase(): GetWeatherForecastUseCase {
        return GetWeatherForecastUseCase(weatherRepository)
    }
}
