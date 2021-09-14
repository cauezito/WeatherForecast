package br.com.cauezito.simpleweatherforecast.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.cauezito.simpleweatherforecast.weather.DailyForecast
import java.util.*
import kotlin.random.Random

class ForecastRepository {
    private val _currentForecast = MutableLiveData<DailyForecast>()
    val currentForecast : LiveData<DailyForecast> = _currentForecast

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

    fun loadCurrentForecast(zipcode : String?){
        val randomTemp = Random.nextFloat().rem(100) * 100
        val forecast = DailyForecast(randomTemp, getTemperatureDescription(randomTemp))
        _currentForecast.value = forecast
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