package com.tj.weather.data.datasources.remote

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tj.weather.domain.models.Location
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

class LocationDataSource(
    private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val timeoutMillis = 10_000L // 10 seconds timeout

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Result<Location> {
        return try {
            if (!isLocationEnabled()) {
                return Result.failure(Exception("Location services are disabled. Please enable location in settings."))
            }

            val cancellationTokenSource = CancellationTokenSource()

            val locationResult = withTimeoutOrNull(timeoutMillis) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).await()
            }

            if (locationResult == null) {
                // Try last known location as fallback
                val lastLocation = fusedLocationClient.lastLocation.await()
                if (lastLocation != null) {
                    Result.success(
                        Location(
                            latitude = lastLocation.latitude,
                            longitude = lastLocation.longitude
                        )
                    )
                } else {
                    Result.failure(Exception("Unable to get location. Please ensure location services are enabled."))
                }
            } else {
                Result.success(
                    Location(
                        latitude = locationResult.latitude,
                        longitude = locationResult.longitude
                    )
                )
            }
        } catch (e: SecurityException) {
            Result.failure(Exception("Location permission not granted", e))
        } catch (e: Exception) {
            Result.failure(Exception("Failed to get location: ${e.message}", e))
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? {
        return try {
            val lastLocation = fusedLocationClient.lastLocation.await()
            lastLocation?.let {
                Location(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
