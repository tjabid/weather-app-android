package com.tj.weather.feature.forecast.ui

import com.tj.weather.R
import com.tj.weather.domain.models.WeatherType
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for WeatherType to drawable resource mapping.
 * Validates that each WeatherType maps to the correct icon drawable.
 */
class WeatherTypeMapperTest {

    @Test
    fun `toIconDrawable maps SUNNY to sun icon`() {
        assertEquals(R.drawable.ic_sun, WeatherType.SUNNY.toIconDrawable())
    }

    @Test
    fun `toIconDrawable maps CLOUDY to cloudy icon`() {
        assertEquals(R.drawable.ic_cloudy, WeatherType.CLOUDY.toIconDrawable())
    }

    @Test
    fun `toIconDrawable maps RAINY to rain icon`() {
        assertEquals(R.drawable.ic_rain, WeatherType.RAINY.toIconDrawable())
    }

    @Test
    fun `toIconDrawable maps THUNDERSTORM to thunderstorm icon`() {
        assertEquals(R.drawable.ic_thunderstorm, WeatherType.THUNDERSTORM.toIconDrawable())
    }

    @Test
    fun `toIconDrawable maps SNOW to snow icon`() {
        assertEquals(R.drawable.ic_snow, WeatherType.SNOW.toIconDrawable())
    }

    @Test
    fun `all WeatherType values have drawable mappings`() {
        // Ensures we don't miss any new WeatherType values
        WeatherType.entries.forEach { weatherType ->
            val drawable = weatherType.toIconDrawable()
            assert(drawable != 0) { "WeatherType.$weatherType has no drawable mapping" }
        }
    }
}
