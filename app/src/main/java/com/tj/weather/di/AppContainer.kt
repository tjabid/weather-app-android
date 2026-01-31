package com.tj.weather.di

import android.content.Context
import com.tj.weather.data.datasources.remote.LocationDataSource
import com.tj.weather.data.repositories.LocationRepositoryImpl
import com.tj.weather.domain.repositories.LocationRepository
import com.tj.weather.domain.repositories.WeatherRepository
import com.tj.weather.domain.usecases.DetermineWeatherTypeUseCase
import com.tj.weather.domain.usecases.GetCurrentLocationUseCase
import com.tj.weather.domain.usecases.GetWeatherForecastUseCase

object AppContainer {
    private lateinit var locationRepository: LocationRepository
    private lateinit var weatherRepository: WeatherRepository

    fun initialize(context: Context) {
        // Location
        val locationDataSource = LocationDataSource(context)
        locationRepository = LocationRepositoryImpl(locationDataSource)

        // Weather (mock for now)
        weatherRepository = MockWeatherRepository()
    }

    fun provideGetCurrentLocationUseCase(): GetCurrentLocationUseCase {
        return GetCurrentLocationUseCase(locationRepository)
    }

    fun provideGetWeatherForecastUseCase(): GetWeatherForecastUseCase {
        return GetWeatherForecastUseCase(weatherRepository)
    }

    fun provideDetermineWeatherTypeUseCase(): DetermineWeatherTypeUseCase {
        return DetermineWeatherTypeUseCase()
    }
}
