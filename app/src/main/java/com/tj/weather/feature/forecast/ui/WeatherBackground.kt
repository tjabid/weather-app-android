package com.tj.weather.feature.forecast.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tj.weather.domain.models.WeatherType

@Composable
fun WeatherBackground(
    weatherType: WeatherType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Crossfade(
            targetState = weatherType,
            label = "weather_background_transition"
        ) { type ->
            val backgroundResource = getBackgroundResource(type)

            if (backgroundResource != 0) {
                // Use drawable resource when available
                Image(
                    painter = painterResource(id = backgroundResource),
                    contentDescription = "Weather background for ${type.name}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback to gradient backgrounds
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(getGradientBackground(type))
                )
            }
        }

        // Content scrim for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )
    }
}

private fun getBackgroundResource(weatherType: WeatherType): Int {
    return when (weatherType) {
        WeatherType.SUNNY -> com.tj.weather.R.drawable.bg_sunny
        WeatherType.RAINY -> com.tj.weather.R.drawable.bg_rainy
        WeatherType.CLOUDY, WeatherType.THUNDERSTORM -> com.tj.weather.R.drawable.bg_cloudy
        WeatherType.SNOW -> com.tj.weather.R.drawable.bg_snow
    }
}

private fun getGradientBackground(weatherType: WeatherType): Brush {
    return when (weatherType) {
        WeatherType.SUNNY -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF87CEEB), // Sky blue
                Color(0xFFFDB813)  // Sunny yellow
            )
        )
        WeatherType.CLOUDY -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF757F9A), // Gray
                Color(0xFFD7DDE8)  // Light gray
            )
        )
        WeatherType.RAINY -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF536976), // Dark gray
                Color(0xFF292E49)  // Darker blue-gray
            )
        )
        WeatherType.THUNDERSTORM -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2C3E50), // Dark blue-gray
                Color(0xFF4B5563)  // Medium gray
            )
        )
        WeatherType.SNOW -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE0EAFC), // Light blue
                Color(0xFFCFDEF3)  // Pale blue
            )
        )
    }
}
