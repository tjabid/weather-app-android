package com.tj.weather.data.mappers

import com.tj.weather.domain.models.WeatherType

/**
 * Maps OpenWeatherMap icon codes to app WeatherType enum and local icon resources
 *
 * OpenWeatherMap Icon Codes:
 * - 01d/01n: Clear sky
 * - 02d/02n: Few clouds
 * - 03d/03n: Scattered clouds
 * - 04d/04n: Broken clouds
 * - 09d/09n: Shower rain
 * - 10d/10n: Rain
 * - 11d/11n: Thunderstorm
 * - 13d/13n: Snow
 * - 50d/50n: Mist
 */
object WeatherTypeMapper {

    /**
     * OpenWeatherMap icon code to [WeatherType]
     * Reference for icons: https://openweathermap.org/weather-conditions?collection=other#Weather-Condition-Codes-2
     */
    fun fromApiCode(iconCode: String): WeatherType {
        return when (iconCode.take(2)) {
            "01" -> WeatherType.SUNNY
            "02", "03", "04" -> WeatherType.CLOUDY
            "09", "10" -> WeatherType.RAINY
            "11" -> WeatherType.THUNDERSTORM
            "13" -> WeatherType.SNOW
            "50" -> WeatherType.CLOUDY // Mist/fog consider as cloudy
            else -> WeatherType.CLOUDY
        }
    }

    /**
     * Map OpenWeatherMap weather condition ID to WeatherType
     * Alternative mapping based on condition ID ranges
     */
    fun fromWeatherConditionId(conditionId: Int): WeatherType {
        return when (conditionId) {
            in 200..232 -> WeatherType.THUNDERSTORM // Thunderstorm
            in 300..321 -> WeatherType.RAINY // Drizzle
            in 500..531 -> WeatherType.RAINY // Rain
            in 600..622 -> WeatherType.SNOW // Snow
            in 701..781 -> WeatherType.CLOUDY // Atmosphere (mist, fog, etc.)
            800 -> WeatherType.SUNNY // Clear
            in 801..804 -> WeatherType.CLOUDY // Clouds
            else -> WeatherType.CLOUDY
        }
    }
}
