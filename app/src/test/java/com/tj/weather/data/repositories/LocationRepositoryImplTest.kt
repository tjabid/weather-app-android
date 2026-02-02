package com.tj.weather.data.repositories

import com.tj.weather.data.datasources.local.LocationLocalDataSource
import com.tj.weather.domain.models.Location
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class LocationRepositoryImplTest {

    private lateinit var locationDataSource: LocationLocalDataSource
    private lateinit var repository: LocationRepositoryImpl

    private val testLocation = Location(latitude = 25.2048, longitude = 55.2708)

    @Before
    fun setup() {
        locationDataSource = mock()
        repository = LocationRepositoryImpl(locationDataSource)
    }

    @Test
    fun `getCachedLocation returns null initially`() = runTest {
        // When
        val result = repository.getCachedLocation()

        // Then
        assertNull(result)
    }

    @Test
    fun `cacheLocation stores location correctly`() = runTest {
        // When
        repository.cacheLocation(testLocation)

        // Then
        assertEquals(testLocation, repository.getCachedLocation())
    }

    @Test
    fun `clearCache removes cached location`() = runTest {
        // Given
        repository.cacheLocation(testLocation)

        // When
        repository.clearCache()

        // Then
        assertNull(repository.getCachedLocation())
    }

    @Test
    fun `cacheLocation updates cached location`() = runTest {
        // Given
        val oldLocation = Location(latitude = 10.0, longitude = 20.0)
        repository.cacheLocation(oldLocation)

        // When
        repository.cacheLocation(testLocation)

        // Then
        assertEquals(testLocation, repository.getCachedLocation())
    }
}
