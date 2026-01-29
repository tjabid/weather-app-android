package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.WeatherType

class DetermineWeatherTypeUseCase {
    operator fun invoke(iconCode: String): WeatherType {
        return when {
            iconCode.startsWith("01") -> WeatherType.SUNNY
            iconCode.startsWith("02") || iconCode.startsWith("03") || iconCode.startsWith("04") -> WeatherType.CLOUDY
            iconCode.startsWith("09") || iconCode.startsWith("10") -> WeatherType.RAINY
            iconCode.startsWith("11") -> WeatherType.THUNDERSTORM
            iconCode.startsWith("13") -> WeatherType.SNOW
            else -> WeatherType.CLOUDY
        }
    }
}
