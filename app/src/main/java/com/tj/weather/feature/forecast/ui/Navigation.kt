package com.tj.weather.feature.forecast.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tj.weather.feature.forecast.viewmodel.ForecastViewModel

object WeatherNavigation {
    const val MAIN_ROUTE = "main"
}

@Composable
fun WeatherNavHost(
    viewModel: ForecastViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = WeatherNavigation.MAIN_ROUTE,
        modifier = modifier
    ) {
        composable(WeatherNavigation.MAIN_ROUTE) {
            MainScreen(viewModel = viewModel)
        }
    }
}
