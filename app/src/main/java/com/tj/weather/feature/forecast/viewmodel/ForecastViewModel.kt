package com.tj.weather.feature.forecast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tj.weather.domain.usecases.DetermineWeatherTypeUseCase
import com.tj.weather.domain.usecases.GetCurrentLocationUseCase
import com.tj.weather.domain.usecases.GetWeatherForecastUseCase
import com.tj.weather.feature.forecast.state.ForecastUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val determineWeatherTypeUseCase: DetermineWeatherTypeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    init {
        loadWeatherForecast()
    }

    fun loadWeatherForecast() {
        viewModelScope.launch {
            _uiState.value = ForecastUiState.Loading

            getCurrentLocationUseCase().fold(
                onSuccess = { location ->
                    getWeatherForecastUseCase(location).fold(
                        onSuccess = { forecast ->
                            val currentWeatherType = if (forecast.dailyForecasts.isNotEmpty()) {
                                determineWeatherTypeUseCase(forecast.dailyForecasts.first().weatherCondition.iconCode)
                            } else {
                                com.tj.weather.domain.models.WeatherType.CLOUDY
                            }

                            _uiState.value = ForecastUiState.Success(
                                forecast = forecast,
                                weatherType = currentWeatherType
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = ForecastUiState.Error(
                                message = exception.message ?: "Failed to load weather forecast",
                                retryable = true
                            )
                        }
                    )
                },
                onFailure = { exception ->
                    val errorMessage = exception.message ?: "Failed to get location"
                    if (errorMessage.contains("permission", ignoreCase = true)) {
                        _uiState.value = ForecastUiState.PermissionDenied
                    } else {
                        _uiState.value = ForecastUiState.Error(
                            message = errorMessage,
                            retryable = true
                        )
                    }
                }
            )
        }
    }

    fun retryLoadWeather() {
        loadWeatherForecast()
    }

    fun onPermissionResult(granted: Boolean) {
        if (granted) {
            loadWeatherForecast()
        } else {
            _uiState.value = ForecastUiState.PermissionDenied
        }
    }
}
