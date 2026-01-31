package com.tj.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tj.weather.di.AppContainer
import com.tj.weather.feature.forecast.ui.MainScreen
import com.tj.weather.feature.forecast.viewmodel.ForecastViewModel
import com.tj.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create ViewModel with manual DI
        val viewModel = ForecastViewModel(
            getCurrentLocationUseCase = AppContainer.provideGetCurrentLocationUseCase(),
            getWeatherForecastUseCase = AppContainer.provideGetWeatherForecastUseCase(),
            getCachedLocationUseCase = AppContainer.provideGetCachedLocationUseCase()
        )

        setContent {
            WeatherTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

