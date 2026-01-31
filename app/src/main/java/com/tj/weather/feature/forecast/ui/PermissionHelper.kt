package com.tj.weather.feature.forecast.ui

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? android.location.LocationManager
    return locationManager?.let {
        it.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                it.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
    } ?: false
}

