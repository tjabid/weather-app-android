package com.tj.weather.feature.forecast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.models.UnableToFetchLocationException
import com.tj.weather.domain.usecases.GetCachedLocationUseCase
import com.tj.weather.domain.usecases.GetWeatherForecastUseCase
import com.tj.weather.feature.forecast.state.ForecastUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val getCachedLocationUseCase: GetCachedLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    /**
     * Step 1: Check if location permission is granted
     * Called from MainScreen after checking permission
     */
    fun onPermissionChecked(hasPermission: Boolean, isLocationEnabled: Boolean) {
        viewModelScope.launch {
            when {
                !hasPermission -> {
                    // Permission not granted
                    val hasCachedLocation = getCachedLocationUseCase() != null
                    _uiState.value = ForecastUiState.PermissionDenied(hasCachedLocation)
                }
                !isLocationEnabled -> {
                    // Permission granted but location services disabled
                    _uiState.value = ForecastUiState.LocationServicesDisabled
                }
                else -> {
                    // Permission granted and location enabled, fetch location
                    _uiState.value = ForecastUiState.LoadLocation
                }
            }
        }
    }

    /**
     * Called when user responds to permission request
     */
    fun onPermissionResult(granted: Map<String, @JvmSuppressWildcards Boolean>?, isLocationEnabled: Boolean) {
        if (granted?.values?.any { it } == true) {
            onPermissionChecked(true, isLocationEnabled)
        } else {
            viewModelScope.launch {
                val hasCachedLocation = getCachedLocationUseCase() != null
                _uiState.value = ForecastUiState.PermissionDenied(hasCachedLocation)
            }
        }
    }

    fun setLocation(location: android.location.Location?) {
        viewModelScope.launch {
            location?.let {
                _uiState.value = ForecastUiState.Loading
                fetchWeatherForecast(Location(it.latitude, it.longitude))
            } ?: setLocationError()
        }
    }

    fun setLocationError(exception: kotlin.Exception = UnableToFetchLocationException()) {
        _uiState.value = ForecastUiState.Error(
            message = exception.message ?: "Failed to load weather forecast",
            retryable = true
        )
    }

    private suspend fun fetchWeatherForecast(location: Location) {
        getWeatherForecastUseCase(location).fold(
            onSuccess = { forecast ->
                _uiState.value = ForecastUiState.Success(
                    forecast = forecast,
                    weatherType = forecast.dailyForecasts.first().weatherType
                )
            },
            onFailure = { exception ->
                _uiState.value = ForecastUiState.Error(
                    message = exception.message ?: "Failed to load weather forecast",
                    retryable = true
                )
            }
        )
    }

    fun retryLoadWeather(hasPermission: Boolean, isLocationEnabled: Boolean) {
        onPermissionChecked(hasPermission, isLocationEnabled)
    }

    fun loadCachedWeather() {
        viewModelScope.launch {
            _uiState.value = ForecastUiState.Loading

            val cachedLocation = getCachedLocationUseCase()

            if (cachedLocation != null) {
                fetchWeatherForecast(cachedLocation)
            } else {
                _uiState.value = ForecastUiState.Error(
                    message = "No cached location available. Please grant location permission.",
                    retryable = false
                )
            }
        }
    }
}
