package com.tj.weather.data.mappers

import com.tj.weather.data.dto.ForecastItem
import com.tj.weather.data.dto.WeatherApiResponse
import com.tj.weather.domain.models.DailyForecast
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.models.WeatherCondition
import com.tj.weather.domain.models.WeatherForecast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

object WeatherApiMapper {

    fun toDomain(apiResponse: WeatherApiResponse): WeatherForecast {
        val location = Location(
            latitude = apiResponse.city.coordinates.latitude,
            longitude = apiResponse.city.coordinates.longitude
        )

        // Group forecast items by date
        val dailyForecasts = apiResponse.forecastList
            .groupBy { forecastItem ->
                // Convert timestamp to date string (YYYY-MM-DD)
                val calendar = Calendar.getInstance()
                calendar.time = Date(forecastItem.timestamp * 1000)
                "${calendar[Calendar.YEAR]}-${calendar[Calendar.MONTH]}-${calendar[Calendar.DAY_OF_MONTH]}"
            }
            .map { (_, items) ->
                mapToDailyForecast(items)
            }
            .sortedBy { it.date }
            .take(5) // Ensure only 5 days

        return WeatherForecast(
            location = location,
            dailyForecasts = dailyForecasts
        )
    }

    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val format = SimpleDateFormat("EEE, d MMM", Locale.getDefault())
        return format.format(date)
    }

    private fun mapToDailyForecast(items: List<ForecastItem>): DailyForecast {
        // Use midday forecast (around 12:00) as primary weather condition
        // or the first item if midday not available
        val primaryItem = items.minByOrNull { item ->
            val calendar = Calendar.getInstance()
            calendar.time = Date(item.timestamp * 1000)
            val hour = calendar[Calendar.HOUR_OF_DAY]
            kotlin.math.abs(hour - 12) // Find closest to noon
        } ?: items.first()

        // Get the date (start of day timestamp)
        val calendar = Calendar.getInstance()
        calendar.time = Date(primaryItem.timestamp * 1000)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val dateTimestamp = calendar.timeInMillis / 1000

        // Calculate min/max temperatures for the day
        val minTemp = items.minOf { it.main.tempMin }
        val maxTemp = items.maxOf { it.main.tempMax }

        // Calculate average humidity
        val avgHumidity = items.map { it.main.humidity }.average().roundToInt()

        // Calculate average wind speed
        val avgWindSpeed = items.map { it.wind.speed }.average()

        // Get max precipitation probability for the day
        val maxPrecipProb = (items.maxOf { it.probabilityOfPrecipitation } * 100).roundToInt()

        // Extract weather condition from primary item
        val weather = primaryItem.weather.firstOrNull()
        val weatherCondition = WeatherCondition(
            temperature = primaryItem.main.temperature,
            description = weather?.description?.capitalize() ?: "Unknown",
            iconCode = weather?.icon ?: "01d"
        )

        // Determine weather type
        val weatherType = weather?.let {
            WeatherTypeMapper.fromApiCode(it.icon)
        } ?: WeatherTypeMapper.fromWeatherConditionId(weather?.id ?: 800)

        return DailyForecast(
            date = dateTimestamp,
            dateFormated = formatDate(dateTimestamp),
            weatherCondition = weatherCondition,
            weatherType = weatherType,
            minTemperature = minTemp,
            maxTemperature = maxTemp,
            humidity = avgHumidity,
            windSpeed = avgWindSpeed,
            precipitationProbability = maxPrecipProb
        )
    }

    private fun String.capitalize(): String {
        return this.split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            }
    }
}
