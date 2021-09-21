package br.com.cauezito.simpleweatherforecast.api

import com.squareup.moshi.Json

data class ApiWeather(
    val current: CurrentWeather,
    val location: Location,
    val request: Request
)

data class Request(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)

data class CurrentWeather(
    val cloudcover: Int,
    val feelslike: Int,
    val humidity: Int,
    val observationTime: String,
    @field:Json(name = "precip")
    val precipitation: Double,
    val pressure: Double,
    val temperature: Double,
    val uvIndex: Int,
    val visibility: Double,
    val weatherCode: Int,
    val windDegree: Double,
    @field:Json(name = "windDir")
    val windDirection: String,
    val windSpeed: Double,
    @field:Json(name = "weather_descriptions")
    val weatherDescriptions: List<String>
)


data class Location(
    val country: String,
    val lat: String,
    val localtime: String,
    val localtimeEpoch: Int,
    val lon: String,
    val name: String,
    val region: String,
    val timezoneId: String,
    val utcOffset: String
)
