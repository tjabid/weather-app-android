package com.tj.weather.feature.forecast.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tj.weather.domain.models.DailyForecast
import com.tj.weather.domain.models.WeatherCondition
import com.tj.weather.domain.models.WeatherType
import com.tj.weather.ui.theme.WeatherTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ForecastCard(
    dailyForecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date Header
            Text(
                text = formatDate(dailyForecast.date),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Weather Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weather Description & Icon Placeholder
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = dailyForecast.weatherCondition.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "☀️",
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                // Temperature Display
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${dailyForecast.weatherCondition.temperature.toInt()}°C",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "↑ ${dailyForecast.maxTemperature.toInt()}°",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "↓ ${dailyForecast.minTemperature.toInt()}°",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Additional Weather Details Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    icon = "💧",
                    value = "${dailyForecast.humidity}%",
                    label = "Humidity"
                )

                WeatherDetailItem(
                    icon = "💨",
                    value = "${dailyForecast.windSpeed.toInt()} km/h",
                    label = "Wind"
                )

                WeatherDetailItem(
                    icon = "🌧",
                    value = "${dailyForecast.precipitationProbability}%",
                    label = "Rain"
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = icon,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("EEEE, d MMM", Locale.getDefault())
    return format.format(date)
}

@Preview(showBackground = true, name = "Sunny Day")
@Composable
private fun ForecastCardSunnyPreview() {
    WeatherTheme {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis / 1000

        ForecastCard(
            dailyForecast = DailyForecast(
                date = today,
                weatherCondition = WeatherCondition(
                    temperature = 28.0,
                    description = "Clear sky",
                    iconCode = "01d"
                ),
                weatherType = WeatherType.SUNNY,
                minTemperature = 22.0,
                maxTemperature = 28.0,
                humidity = 45,
                windSpeed = 12.0,
                precipitationProbability = 10
            )
        )
    }
}

@Preview(showBackground = true, name = "Rainy Day")
@Composable
private fun ForecastCardRainyPreview() {
    WeatherTheme {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        val futureDate = calendar.timeInMillis / 1000

        ForecastCard(
            dailyForecast = DailyForecast(
                date = futureDate,
                weatherCondition = WeatherCondition(
                    temperature = 18.0,
                    description = "Heavy rain",
                    iconCode = "10d"
                ),
                weatherType = WeatherType.RAINY,
                minTemperature = 15.0,
                maxTemperature = 20.0,
                humidity = 85,
                windSpeed = 25.0,
                precipitationProbability = 90
            )
        )
    }
}

@Preview(showBackground = true, name = "Cloudy Day")
@Composable
private fun ForecastCardCloudyPreview() {
    WeatherTheme {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrow = calendar.timeInMillis / 1000

        ForecastCard(
            dailyForecast = DailyForecast(
                date = tomorrow,
                weatherCondition = WeatherCondition(
                    temperature = 22.0,
                    description = "Partly cloudy",
                    iconCode = "02d"
                ),
                weatherType = WeatherType.CLOUDY,
                minTemperature = 18.0,
                maxTemperature = 24.0,
                humidity = 60,
                windSpeed = 15.0,
                precipitationProbability = 30
            )
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme - Sunny")
@Composable
private fun ForecastCardDarkPreview() {
    WeatherTheme(darkTheme = true) {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis / 1000

        ForecastCard(
            dailyForecast = DailyForecast(
                date = today,
                weatherCondition = WeatherCondition(
                    temperature = 28.0,
                    description = "Clear sky",
                    iconCode = "01d"
                ),
                weatherType = WeatherType.SUNNY,
                minTemperature = 22.0,
                maxTemperature = 28.0,
                humidity = 45,
                windSpeed = 12.0,
                precipitationProbability = 10
            )
        )
    }
}


