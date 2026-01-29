package com.tj.weather.feature.forecast.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.tj.weather.feature.forecast.state.ForecastUiState
import com.tj.weather.feature.forecast.viewmodel.ForecastViewModel

@Composable
fun MainScreen(
    viewModel: ForecastViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onPermissionResult(isGranted)
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is ForecastUiState.Loading -> {
                    LoadingIndicator()
                }

                is ForecastUiState.Success -> {
                    WeatherBackground(weatherType = state.weatherType)
                    ForecastContent(forecast = state.forecast)
                }

                is ForecastUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.retryLoadWeather() }
                    )
                }

                is ForecastUiState.PermissionDenied -> {
                    PermissionRationale(
                        onRequestPermission = {
                            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    )
                }
            }
        }
    }
}
