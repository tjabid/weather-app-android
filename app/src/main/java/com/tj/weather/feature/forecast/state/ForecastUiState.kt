package com.tj.weather.feature.forecast.state

import com.tj.weather.domain.models.WeatherForecast
import com.tj.weather.domain.models.WeatherType

sealed class ForecastUiState {
    data object Loading : ForecastUiState()

    data class Success(
        val forecast: WeatherForecast,
        val weatherType: WeatherType
    ) : ForecastUiState()

    data class Error(
        val message: String,
        val retryable: Boolean
    ) : ForecastUiState()

    data object PermissionDenied : ForecastUiState()
}
