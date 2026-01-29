package com.tj.weather.feature.forecast.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.tj.weather.domain.models.WeatherType

@Composable
fun WeatherBackground(
    weatherType: WeatherType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        // TODO: Add actual background images for each weather type
        // For now, just a placeholder
        // Image(
        //     painter = painterResource(id = getBackgroundResource(weatherType)),
        //     contentDescription = "Weather background",
        //     modifier = Modifier.fillMaxSize(),
        //     contentScale = ContentScale.Crop
        // )
    }
}

private fun getBackgroundResource(weatherType: WeatherType): Int {
    // TODO: Map WeatherType to drawable resources
    // return when (weatherType) {
    //     WeatherType.SUNNY -> R.drawable.bg_sunny
    //     WeatherType.CLOUDY -> R.drawable.bg_cloudy
    //     WeatherType.RAINY -> R.drawable.bg_rainy
    //     WeatherType.THUNDERSTORM -> R.drawable.bg_thunderstorm
    //     WeatherType.SNOW -> R.drawable.bg_snow
    // }
    return 0
}
