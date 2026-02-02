package com.tj.weather.domain.usecases

import com.tj.weather.domain.models.Location
import com.tj.weather.domain.repositories.LocationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class CacheLocationUseCaseTest {

    private lateinit var locationRepository: LocationRepository
    private lateinit var cacheLocationUseCase: CacheLocationUseCase

    @Before
    fun setup() {
        locationRepository = mock()
        cacheLocationUseCase = CacheLocationUseCase(locationRepository)
    }

    @Test
    fun `invoke should cache location through repository`() = runTest {
        // Given
        val location = Location(latitude = 37.7749, longitude = -122.4194)

        // When
        cacheLocationUseCase(location)

        // Then
        verify(locationRepository).cacheLocation(location)
    }
}
