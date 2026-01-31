package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository

class GetCurrentLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Result<Location> {
        return try {
            val cachedLocation = locationRepository.getCachedLocation()

            if (cachedLocation != null) {
                return Result.success(cachedLocation)
            }

            val result = locationRepository.getCurrentLocation()

            result.fold(
                onSuccess = { location ->
                    locationRepository.cacheLocation(location)
                    Result.success(location)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
