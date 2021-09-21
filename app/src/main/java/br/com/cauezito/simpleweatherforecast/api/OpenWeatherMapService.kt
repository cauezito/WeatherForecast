package br.com.cauezito.simpleweatherforecast.api

import br.com.cauezito.simpleweatherforecast.BuildConfig
import okhttp3.Interceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


const val BASE_URL = "http://api.openweathermap.org"

fun createOpenWeatherMapService(): OpenWeatherMapService {
    val httpClient = OkHttpClient.Builder()

    httpClient.addInterceptor { chain ->
        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", BuildConfig.OPEN_WEATHER_API_KEY).build()
        val request = chain.request().newBuilder().url(url).build()
        chain.proceed(request)
    }

    return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create())
        .client(httpClient.build())
        .build()
        .create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    fun getCurrentWeatherByZipcode(
        @Query("zip") zipCode: String,
        @Query("units") units: String
    ): Call<CurrentWeather>


    @GET("/data/2.5/onecall")
    fun getSevenDayForecast(
        @Query("lat") lat: Float,
        @Query("lon") long: Float,
        @Query("exclude") exclude: String,
        @Query("units") units: String
    ): Call<WeeklyForecast>
}