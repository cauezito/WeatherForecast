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

const val BASE_API_URL = "http://api.weatherstack.com/"

fun createOpenWeatherMapService(): OpenWeatherMapService {
    val httpClient = OkHttpClient.Builder()

    httpClient.addInterceptor { chain ->
        val url = chain.request().url().newBuilder()
            .addQueryParameter("access_key", BuildConfig.API_KEY).build()
        val request = chain.request().newBuilder().url(url).build()
        chain.proceed(request)
    }

    return Retrofit.Builder().baseUrl(BASE_API_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(httpClient.build())
        .build()
        .create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {

    @GET("current")
    fun getCurrentWeatherByZipcode(
        @Query("query") location: String, @Query("unit") unit: String = "m"
    ): Call<ApiWeather>
}