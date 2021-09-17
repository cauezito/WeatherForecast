package br.com.cauezito.simpleweatherforecast.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "http://api.openweathermap.org"

fun createOpenWeatherMapService() : OpenWeatherMapService {
    return Retrofit.Builder().baseUrl(BASE_URL).build()
        .create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("units") units : String,
        @Query("appid") apiKey: String) : Call<CurrentWeather>

}