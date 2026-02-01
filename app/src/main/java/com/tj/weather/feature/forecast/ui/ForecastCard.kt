package com.tj.weather.feature.forecast.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tj.weather.R
import com.tj.weather.domain.models.DailyForecast
import com.tj.weather.domain.models.WeatherCondition
import com.tj.weather.domain.models.WeatherType
import com.tj.weather.ui.theme.WeatherTheme
import java.util.Calendar

@Composable
fun ForecastCard(
    dailyForecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    val semanticDescription = "${dailyForecast.dateFormated}. " +
            "${dailyForecast.weatherCondition.description}. " +
            "Temperature ${dailyForecast.weatherCondition.temperature.toInt()} degrees celsius. " +
            "High ${dailyForecast.maxTemperature.toInt()}, Low ${dailyForecast.minTemperature.toInt()} degrees. " +
            "Humidity ${dailyForecast.humidity} percent. " +
            "Wind speed ${dailyForecast.windSpeed.toInt()} kilometers per hour. " +
            "Chance of rain ${dailyForecast.precipitationProbability} percent."

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = semanticDescription },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Main Weather Info Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column(
                    modifier = Modifier.wrapContentHeight().weight(1F),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Date Header
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        text = dailyForecast.dateFormated,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = dailyForecast.weatherCondition.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Image(
                    painter = painterResource(id = dailyForecast.weatherType.toIconDrawable()),
                    contentDescription = "${dailyForecast.weatherType.name} weather icon",
                    modifier = Modifier.size(80.dp).weight(0.5F),
                    contentScale = ContentScale.Fit
                )

                // Temperature Display
                Column(
                    modifier = Modifier.wrapContentWidth().weight(0.5F),
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
                    icon = R.drawable.ic_humidity,
                    value = "${dailyForecast.humidity}%",
                    label = "Humidity"
                )

                WeatherDetailItem(
                    icon = R.drawable.ic_wind,
                    value = "${dailyForecast.windSpeed.toInt()} km/h",
                    label = "Wind"
                )

                WeatherDetailItem(
                    icon = R.drawable.ic_rainy,
                    value = "${dailyForecast.precipitationProbability}%",
                    label = "Rain"
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailItem(
    @DrawableRes icon: Int = R.drawable.bg_snow,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "$label icon",
                modifier = Modifier.size(16.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
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
                dateFormated = "Sat, 31 Jan",
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
                dateFormated = "Sun, 1 Feb",
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
                dateFormated = "Sun, 1 Feb",
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
                dateFormated = "Sat, 31 Jan",
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


