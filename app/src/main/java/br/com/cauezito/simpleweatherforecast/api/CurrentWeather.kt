package br.com.cauezito.simpleweatherforecast.api

import com.squareup.moshi.Json

data class Forecast(val temp: Float)
data class Coordinates(val lat: Float, val lon: Float)
data class CurrentWeather(
    val name: String,
    val coordinates: Coordinates,
    @field:Json(name = "main")
    val forecast: Forecast
)