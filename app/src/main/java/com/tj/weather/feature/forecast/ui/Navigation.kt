package com.tj.weather.feature.forecast.ui

// TODO: Add navigation dependency to build.gradle.kts:
// implementation("androidx.navigation:navigation-compose:2.7.7")

// Navigation setup will be completed once dependency is added
// For now, MainScreen can be called directly from MainActivity

object WeatherNavigation {
    const val MAIN_ROUTE = "main"
}

// Uncomment when navigation dependency is added:
/*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tj.weather.feature.forecast.viewmodel.ForecastViewModel

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
*/
