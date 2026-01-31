package com.tj.weather.feature.forecast.ui

import androidx.annotation.DrawableRes
import com.tj.weather.R
import com.tj.weather.domain.models.WeatherType

@DrawableRes
fun WeatherType.toIconDrawable(): Int {
    return when (this) {
        WeatherType.SUNNY -> R.drawable.ic_sun
        WeatherType.CLOUDY -> R.drawable.ic_cloudy
        WeatherType.RAINY -> R.drawable.ic_rain
        WeatherType.THUNDERSTORM -> R.drawable.ic_thunderstorm
        WeatherType.SNOW -> R.drawable.ic_snow
    }
}
