package com.tj.weather.feature.forecast.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tj.weather.domain.models.WeatherForecast

@Composable
fun ForecastContent(
    forecast: WeatherForecast,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "5-Day Forecast",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(forecast.dailyForecasts) { dailyForecast ->
            ForecastCard(dailyForecast = dailyForecast)
        }
    }
}
