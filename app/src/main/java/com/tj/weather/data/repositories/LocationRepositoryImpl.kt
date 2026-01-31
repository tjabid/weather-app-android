package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.local.LocationLocalDataSource
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class LocationRepositoryImpl(
    private val localDataSource: LocationLocalDataSource
) : LocationRepository {
    private var cachedLocation: Location? = null

    override suspend fun getCachedLocation(): Location? {
        return cachedLocation
    }

    override suspend fun cacheLocation(location: Location) {
        cachedLocation = location
    }

    override suspend fun clearCache() {
        cachedLocation = null
    }
}
