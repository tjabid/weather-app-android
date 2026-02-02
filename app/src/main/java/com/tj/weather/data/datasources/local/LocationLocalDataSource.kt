package com.tj.weather.data.datasources.local

import android.content.Context
import com.tj.weather.domain.models.Location

class LocationLocalDataSource(
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCachedLocation(): Location? {
        val latitude = sharedPreferences.getFloat(KEY_LATITUDE, Float.NaN)
        val longitude = sharedPreferences.getFloat(KEY_LONGITUDE, Float.NaN)

        return if (!latitude.isNaN() && !longitude.isNaN()) {
            Location(latitude.toDouble(), longitude.toDouble())
        } else {
            null
        }
    }

    fun cacheLocation(location: Location) {
        sharedPreferences.edit().apply {
            putFloat(KEY_LATITUDE, location.latitude.toFloat())
            putFloat(KEY_LONGITUDE, location.longitude.toFloat())
            putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            apply()
        }
    }

    fun clearCache() {
        sharedPreferences.edit().apply {
            remove(KEY_LATITUDE)
            remove(KEY_LONGITUDE)
            remove(KEY_TIMESTAMP)
            apply()
        }
    }

    fun getCacheTimestamp(): Long? {
        val timestamp = sharedPreferences.getLong(KEY_TIMESTAMP, -1L)
        return if (timestamp != -1L) timestamp else null
    }

    companion object {
        private const val PREFS_NAME = "weather_location_prefs"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_TIMESTAMP = "timestamp"
    }

}