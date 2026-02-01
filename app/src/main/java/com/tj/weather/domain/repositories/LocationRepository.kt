package com.tj.weather.domain.repositories

import com.tj.weather.domain.models.Location

interface LocationRepository {
    suspend fun getCachedLocation(): Location?
    suspend fun cacheLocation(location: Location)
    suspend fun clearCache()
}
