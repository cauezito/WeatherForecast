package br.com.cauezito.simpleweatherforecast.api

import com.squareup.moshi.Json

data class ApiWeather(
    val current: CurrentWeather,
    val location: Location
)

data class CurrentWeather(
    val cloudcover: Int,
    val feelslike: Int,
    val humidity: Int,
    @field:Json(name = "precip")
    val precipitation: Double,
    val temperature: Double,
    val visibility: Double,
    @field:Json(name = "windDir")
    val windSpeed: Double,
    @field:Json(name = "weather_descriptions")
    val weatherDescriptions: List<String>
)

data class Location(
    val name: String
)
