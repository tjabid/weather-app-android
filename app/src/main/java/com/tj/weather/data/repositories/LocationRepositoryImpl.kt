package com.tj.weather.data.repositories

import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class LocationRepositoryImpl : LocationRepository {
    private var cachedLocation: Location? = null

    override suspend fun getCurrentLocation(): Result<Location> {
        return try {
            // TODO: Implement FusedLocationProviderClient integration
            // For now, return cached location or error
            cachedLocation?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Location not available. Please cache location first."))
        } catch (e: Exception) {
            Result.failure(e)
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
