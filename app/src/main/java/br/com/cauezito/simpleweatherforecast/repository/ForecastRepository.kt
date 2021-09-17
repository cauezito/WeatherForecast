package br.com.cauezito.simpleweatherforecast.repository

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.cauezito.simpleweatherforecast.BuildConfig
import br.com.cauezito.simpleweatherforecast.api.CurrentWeather
import br.com.cauezito.simpleweatherforecast.api.createOpenWeatherMapService
import br.com.cauezito.simpleweatherforecast.weather.DailyForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.random.Random

class ForecastRepository {
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather : LiveData<CurrentWeather> = _currentWeather

    //is private because only the repository can change it
    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>()
    val weeklyForecast: LiveData<List<DailyForecast>> = _weeklyForecast;

    fun loadWeeklyForecast(zipcode : String?){
        val randomValues = List(50){ Random.nextFloat().rem(100) * 100 }
        val forecastItems = randomValues.map { temp ->
            DailyForecast(temp, getTemperatureDescription(temp))
        }

        _weeklyForecast.value = forecastItems
    }

    fun loadCurrentForecast(zipcode : String){
        val call = createOpenWeatherMapService().getCurrentWeatherByZipcode(zipcode, "metric", BuildConfig.OPEN_WEATHER_API_KEY)
        call.enqueue(object : Callback<CurrentWeather>{
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) _currentWeather.value = weatherResponse
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("api", "onFailure")
            }
        })
    }

    private fun getTemperatureDescription(temp : Float) : String {
        return when (temp) {
            in 32f.rangeTo(55f) -> "Colder than I would prefer"
            in 55f.rangeTo(65f) -> "Getting better"
            in 65f.rangeTo(80f) -> "That's the sweet spot!"
            in 80f.rangeTo(90f) -> "Getting a little warm"
            in 90f.rangeTo(100f) -> "Where's the A/C?"
            in 100f.rangeTo(Float.MAX_VALUE) -> "What is this, Arizona?"
            else -> "Does not compute"
        }
    }
}