package com.tj.weather.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherApiResponse(
    @SerializedName("cod")
    val code: String,
    @SerializedName("message")
    val message: Int,
    @SerializedName("cnt")
    val count: Int,
    @SerializedName("list")
    val forecastList: List<ForecastItem>,
    @SerializedName("city")
    val city: City
)

data class ForecastItem(
    @SerializedName("dt")
    val timestamp: Long,
    @SerializedName("main")
    val main: MainWeatherData,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("clouds")
    val clouds: Clouds,
    @SerializedName("wind")
    val wind: Wind,
    @SerializedName("visibility")
    val visibility: Int,
    @SerializedName("pop")
    val probabilityOfPrecipitation: Double,
    @SerializedName("dt_txt")
    val dateTimeText: String
)

data class MainWeatherData(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("sea_level")
    val seaLevel: Int? = null,
    @SerializedName("grnd_level")
    val groundLevel: Int? = null
)

data class Weather(
    @SerializedName("id")
    val id: Int,
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)

data class Clouds(
    @SerializedName("all")
    val all: Int
)

data class Wind(
    @SerializedName("speed")
    val speed: Double,
    @SerializedName("deg")
    val degrees: Int,
    @SerializedName("gust")
    val gust: Double? = null
)

data class City(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("coord")
    val coordinates: Coordinates,
    @SerializedName("country")
    val country: String,
    @SerializedName("population")
    val population: Int,
    @SerializedName("timezone")
    val timezone: Int,
    @SerializedName("sunrise")
    val sunrise: Long,
    @SerializedName("sunset")
    val sunset: Long
)

data class Coordinates(
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double
)