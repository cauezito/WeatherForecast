package br.com.cauezito.simpleweatherforecast.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "http://api.openweathermap.org"

fun createOpenWeatherMapService(): OpenWeatherMapService {
    return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    fun getCurrentWeatherByZipcode(
        @Query("zip") zipCode: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<CurrentWeather>


    @GET("/data/2.5/onecall")
    fun getSevenDayForecast(
        @Query("lat") lat: Float,
        @Query("lon") long: Float,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Call<WeeklyForecast>
}