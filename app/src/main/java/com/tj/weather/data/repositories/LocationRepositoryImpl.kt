package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.remote.LocationDataSource
import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class LocationRepositoryImpl(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    private var cachedLocation: Location? = null

    override suspend fun getCurrentLocation(): Result<Location> {
        return try {
            val result = locationDataSource.getCurrentLocation()

            result.fold(
                onSuccess = { location ->
                    cacheLocation(location)
                    Result.success(location)
                },
                onFailure = { exception ->
                    // Try to return cached location if available
                    cachedLocation?.let {
                        Result.success(it)
                    } ?: Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            cachedLocation?.let {
                Result.success(it)
            } ?: Result.failure(e)
        }
    }

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
