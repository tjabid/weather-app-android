package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class CacheLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(location: Location) {
        locationRepository.cacheLocation(location)
    }
}
