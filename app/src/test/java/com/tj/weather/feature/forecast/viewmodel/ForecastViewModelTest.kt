package com.tj.weather.feature.forecast.viewmodel

import com.tj.weather.domain.usecases.CacheLocationUseCase
import com.tj.weather.domain.usecases.GetCachedLocationUseCase
import com.tj.weather.domain.usecases.GetWeatherForecastUseCase
import com.tj.weather.feature.forecast.state.ForecastUiState
import com.tj.weather.test.TestDataFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    private lateinit var getWeatherForecastUseCase: GetWeatherForecastUseCase
    private lateinit var getCachedLocationUseCase: GetCachedLocationUseCase
    private lateinit var cacheLocationUseCase: CacheLocationUseCase
    private lateinit var viewModel: ForecastViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testLocation = TestDataFactory.createTestLocation()
    private val testForecast = TestDataFactory.createTestWeatherForecast()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getWeatherForecastUseCase = mock()
        getCachedLocationUseCase = mock()
        cacheLocationUseCase = mock()
        viewModel = ForecastViewModel(
            getWeatherForecastUseCase,
            getCachedLocationUseCase,
            cacheLocationUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.Loading)
    }

    @Test
    fun `onPermissionChecked with no permission shows PermissionDenied without cached location`() = runTest {
        // Given
        whenever(getCachedLocationUseCase()).thenReturn(null)

        // When
        viewModel.onPermissionChecked(hasPermission = false, isLocationEnabled = true)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.PermissionDenied)
        assertEquals(false, (state as ForecastUiState.PermissionDenied).hasCachedLocation)
    }

    @Test
    fun `onPermissionChecked with no permission shows PermissionDenied with cached location`() = runTest {
        // Given
        whenever(getCachedLocationUseCase()).thenReturn(testLocation)

        // When
        viewModel.onPermissionChecked(hasPermission = false, isLocationEnabled = true)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.PermissionDenied)
        assertEquals(true, (state as ForecastUiState.PermissionDenied).hasCachedLocation)
    }

    @Test
    fun `onPermissionChecked with permission but location disabled shows LocationServicesDisabled`() = runTest {
        // When
        viewModel.onPermissionChecked(hasPermission = true, isLocationEnabled = false)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.LocationServicesDisabled)
    }

    @Test
    fun `onPermissionChecked with permission and location enabled shows LoadLocation`() = runTest {
        // When
        viewModel.onPermissionChecked(hasPermission = true, isLocationEnabled = true)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.LoadLocation)
    }

    @Test
    fun `onPermissionResult with granted permission triggers permission check`() = runTest {
        // Given
        val grantedPermissions = mapOf("android.permission.ACCESS_FINE_LOCATION" to true)

        // When
        viewModel.onPermissionResult(grantedPermissions, isLocationEnabled = true)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.LoadLocation)
    }

    @Test
    fun `onPermissionResult with denied permission shows PermissionDenied`() = runTest {
        // Given
        val deniedPermissions = mapOf("android.permission.ACCESS_FINE_LOCATION" to false)
        whenever(getCachedLocationUseCase()).thenReturn(null)

        // When
        viewModel.onPermissionResult(deniedPermissions, isLocationEnabled = true)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.PermissionDenied)
    }

    @Test
    fun `setLocation with valid location fetches weather successfully`() = runTest {
        // Given
        val androidLocation = mock<android.location.Location>().apply {
            whenever(latitude).thenReturn(testLocation.latitude)
            whenever(longitude).thenReturn(testLocation.longitude)
        }
        whenever(getWeatherForecastUseCase(any())).thenReturn(Result.success(testForecast))

        // When
        viewModel.setLocation(androidLocation)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.Success)
        assertEquals(testForecast, (state as ForecastUiState.Success).forecast)
    }

    @Test
    fun `setLocation caches location when fetched successfully`() = runTest {
        // Given
        val androidLocation = mock<android.location.Location>().apply {
            whenever(latitude).thenReturn(testLocation.latitude)
            whenever(longitude).thenReturn(testLocation.longitude)
        }
        whenever(getWeatherForecastUseCase(any())).thenReturn(Result.success(testForecast))

        // When
        viewModel.setLocation(androidLocation)
        advanceUntilIdle()

        // Then
        org.mockito.kotlin.verify(cacheLocationUseCase).invoke(testLocation)
    }

    @Test
    fun `setLocation with null location shows error`() = runTest {
        // When
        viewModel.setLocation(null)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.Error)
    }

    @Test
    fun `setLocation with API failure shows error`() = runTest {
        // Given
        val androidLocation = mock<android.location.Location>().apply {
            whenever(latitude).thenReturn(testLocation.latitude)
            whenever(longitude).thenReturn(testLocation.longitude)
        }
        val exception = Exception("Network error")
        whenever(getWeatherForecastUseCase(any())).thenReturn(Result.failure(exception))

        // When
        viewModel.setLocation(androidLocation)
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.Error)
        assertEquals("Network error", (state as ForecastUiState.Error).message)
        assertTrue(state.retryable)
    }

    @Test
    fun `setLocationError shows error state`() = runTest {
        // When
        viewModel.setLocationError(Exception("Location error"))
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.Error)
        assertEquals("Location error", (state as ForecastUiState.Error).message)
    }

    @Test
    fun `retryLoadWeather re-checks permissions`() = runTest {
        // When
        viewModel.retryLoadWeather(hasPermission = true, isLocationEnabled = true)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is ForecastUiState.LoadLocation)
    }

    @Test
    fun `loadCachedWeather with cached location fetches weather successfully`() = runTest {
        // Given
        whenever(getCachedLocationUseCase()).thenReturn(testLocation)
        whenever(getWeatherForecastUseCase(testLocation)).thenReturn(Result.success(testForecast))

        // When
        viewModel.loadCachedWeather()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.Success)
        assertEquals(testForecast, (state as ForecastUiState.Success).forecast)
    }

    @Test
    fun `loadCachedWeather without cached location shows error`() = runTest {
        // Given
        whenever(getCachedLocationUseCase()).thenReturn(null)

        // When
        viewModel.loadCachedWeather()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.Error)
        assertEquals("No cached location available. Please grant location permission.", (state as ForecastUiState.Error).message)
        assertEquals(false, state.retryable)
    }

    @Test
    fun `loadCachedWeather with API failure shows error`() = runTest {
        // Given
        whenever(getCachedLocationUseCase()).thenReturn(testLocation)
        val exception = Exception("API error")
        whenever(getWeatherForecastUseCase(testLocation)).thenReturn(Result.failure(exception))

        // When
        viewModel.loadCachedWeather()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is ForecastUiState.Error)
        assertEquals("API error", (state as ForecastUiState.Error).message)
        assertTrue(state.retryable)
    }
}
