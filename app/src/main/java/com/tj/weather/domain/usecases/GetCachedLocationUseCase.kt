package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class GetCachedLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Location? {
        return locationRepository.getCachedLocation()
    }
}
