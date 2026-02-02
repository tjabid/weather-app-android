package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.local.LocationLocalDataSource
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class LocationRepositoryImpl(
    private val localDataSource: LocationLocalDataSource
) : LocationRepository {
    // In-memory cache for quick access
    private var cachedLocation: Location? = null

    override suspend fun getCachedLocation(): Location? {
        // Return in-memory cache if available
        if (cachedLocation != null) {
            return cachedLocation
        }

        // Otherwise, load from persistent storage
        cachedLocation = localDataSource.getCachedLocation()
        return cachedLocation
    }

    override suspend fun cacheLocation(location: Location) {
        // Update both in-memory and persistent cache
        cachedLocation = location
        localDataSource.cacheLocation(location)
    }

    override suspend fun clearCache() {
        // Clear both in-memory and persistent cache
        cachedLocation = null
        localDataSource.clearCache()
    }
}
