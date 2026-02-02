package com.tj.weather.test

import com.tj.weather.data.dto.*
import com.tj.weather.domain.models.*

/**
 * Test data factory for creating properly structured mock objects
 * All parameters match the actual DTO and domain model classes
 */
object TestDataFactory {

    // Domain Models
    fun createTestLocation(
        latitude: Double = 25.2048,
        longitude: Double = 55.2708
    ) = Location(latitude = latitude, longitude = longitude)

    fun createTestWeatherCondition(
        temperature: Double = 28.0,
        description: String = "Clear sky",
        iconCode: String = "01d"
    ) = WeatherCondition(
        temperature = temperature,
        description = description,
        iconCode = iconCode
    )

    fun createTestDailyForecast(
        date: Long = 1738368000L,
        dateFormated: String = "Sat, 1 Feb",
        weatherCondition: WeatherCondition = createTestWeatherCondition(),
        weatherType: WeatherType = WeatherType.SUNNY,
        minTemperature: Double = 22.0,
        maxTemperature: Double = 28.0,
        humidity: Int = 45,
        windSpeed: Double = 12.0,
        precipitationProbability: Int = 10
    ) = DailyForecast(
        date = date,
        dateFormated = dateFormated,
        weatherCondition = weatherCondition,
        weatherType = weatherType,
        minTemperature = minTemperature,
        maxTemperature = maxTemperature,
        humidity = humidity,
        windSpeed = windSpeed,
        precipitationProbability = precipitationProbability
    )

    fun createTestWeatherForecast(
        location: Location = createTestLocation(),
        dailyForecasts: List<DailyForecast> = listOf(createTestDailyForecast())
    ) = WeatherForecast(
        location = location,
        dailyForecasts = dailyForecasts
    )

    // DTOs
    fun createTestMainWeatherData(
        temperature: Double = 28.0,
        feelsLike: Double = 30.0,
        tempMin: Double = 22.0,
        tempMax: Double = 28.0,
        pressure: Int = 1013,
        humidity: Int = 45,
        seaLevel: Int? = 1013,
        groundLevel: Int? = 1010
    ) = MainWeatherData(
        temperature = temperature,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax,
        pressure = pressure,
        humidity = humidity,
        seaLevel = seaLevel,
        groundLevel = groundLevel
    )

    fun createTestWeather(
        id: Int = 800,
        main: String = "Clear",
        description: String = "clear sky",
        icon: String = "01d"
    ) = Weather(
        id = id,
        main = main,
        description = description,
        icon = icon
    )

    fun createTestClouds(
        all: Int = 0
    ) = Clouds(all = all)

    fun createTestWind(
        speed: Double = 12.0,
        degrees: Int = 180,
        gust: Double? = 15.0
    ) = Wind(
        speed = speed,
        degrees = degrees,
        gust = gust
    )

    fun createTestForecastItem(
        timestamp: Long = 1738368000L,
        main: MainWeatherData = createTestMainWeatherData(),
        weather: List<Weather> = listOf(createTestWeather()),
        clouds: Clouds = createTestClouds(),
        wind: Wind = createTestWind(),
        visibility: Int = 10000,
        probabilityOfPrecipitation: Double = 0.1,
        dateTimeText: String = "2026-02-01 12:00:00"
    ) = ForecastItem(
        timestamp = timestamp,
        main = main,
        weather = weather,
        clouds = clouds,
        wind = wind,
        visibility = visibility,
        probabilityOfPrecipitation = probabilityOfPrecipitation,
        dateTimeText = dateTimeText
    )

    fun createTestCoordinates(
        latitude: Double = 25.2048,
        longitude: Double = 55.2708
    ) = Coordinates(
        latitude = latitude,
        longitude = longitude
    )

    fun createTestCity(
        id: Int = 292223,
        name: String = "Dubai",
        coordinates: Coordinates = createTestCoordinates(),
        country: String = "AE",
        population: Int = 3411000,
        timezone: Int = 14400,
        sunrise: Long = 1738377600L,
        sunset: Long = 1738418400L
    ) = City(
        id = id,
        name = name,
        coordinates = coordinates,
        country = country,
        population = population,
        timezone = timezone,
        sunrise = sunrise,
        sunset = sunset
    )

    fun createTestWeatherApiResponse(
        code: String = "200",
        message: Int = 0,
        count: Int = 5,
        forecastList: List<ForecastItem> = listOf(createTestForecastItem()),
        city: City = createTestCity()
    ) = WeatherApiResponse(
        code = code,
        message = message,
        count = count,
        forecastList = forecastList,
        city = city
    )
}
