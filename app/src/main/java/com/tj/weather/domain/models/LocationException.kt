package com.tj.weather.domain.models

class UnableToFetchLocationException : Exception("Unable to fetch location")
class LocationServicesDisabledException : Exception("Location services are disabled. Please enable location in settings.")
class LocationNotGrantedException : Exception("Location permission not granted")
