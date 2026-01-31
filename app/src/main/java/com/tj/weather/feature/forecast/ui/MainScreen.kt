package com.tj.weather.feature.forecast.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tj.weather.feature.forecast.state.ForecastUiState
import com.tj.weather.feature.forecast.viewmodel.ForecastViewModel

@Composable
fun MainScreen(
    viewModel: ForecastViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { isGranted ->
        val locationEnabled = isLocationEnabled(context)
        viewModel.onPermissionResult(isGranted, locationEnabled)
    }

    // Check permission and location services on initial load
    LaunchedEffect(Unit) {
        val hasPermission = hasLocationPermission(context)
        val locationEnabled = isLocationEnabled(context)
        viewModel.onPermissionChecked(hasPermission, locationEnabled)
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
                        onRetry = {
                            val hasPermission = hasLocationPermission(context)
                            val locationEnabled = isLocationEnabled(context)
                            viewModel.retryLoadWeather(hasPermission, locationEnabled)
                        }
                    )
                }

                is ForecastUiState.LocationServicesDisabled -> {
                    LocationServicesPrompt(
                        onRetry = {
                            val hasPermission = hasLocationPermission(context)
                            val locationEnabled = isLocationEnabled(context)
                            viewModel.retryLoadWeather(hasPermission, locationEnabled)
                        }
                    )
                }

                is ForecastUiState.PermissionDenied -> {
                    PermissionRationale(
                        showUseCachedLocation = state.hasCachedLocation,
                        onRequestPermission = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    ACCESS_FINE_LOCATION,
                                    ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        onUseCachedLocation = {
                            viewModel.loadCachedWeather()
                        }
                    )
                }

                is ForecastUiState.LoadLocation -> {

                    LoadingIndicator()
                    if (checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && isLocationEnabled(context)) {
                        val fusedLocationClient =
                            LocationServices.getFusedLocationProviderClient(context)

                        // Try to get current location with high accuracy
                        val cancellationTokenSource = CancellationTokenSource()
                        fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY,
                            cancellationTokenSource.token
                        )
                            .addOnSuccessListener(viewModel::setLocation)
                            .addOnFailureListener { exception ->
                                fusedLocationClient.lastLocation
                                    .addOnSuccessListener(viewModel::setLocation)
                                    .addOnFailureListener(viewModel::setLocationError)
                            }
                    }
                }
            }
        }
    }
}
