package br.com.cauezito.simpleweatherforecast.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.cauezito.simpleweatherforecast.BuildConfig
import br.com.cauezito.simpleweatherforecast.api.CurrentWeather
import br.com.cauezito.simpleweatherforecast.api.WeeklyForecast
import br.com.cauezito.simpleweatherforecast.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository {
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather : LiveData<CurrentWeather> = _currentWeather

    //is private because only the repository can change it
    private val _weeklyForecast = MutableLiveData<WeeklyForecast>()
    val weeklyForecast: LiveData<WeeklyForecast> = _weeklyForecast;

    fun loadWeeklyForecast(zipcode : String){
        val call = createOpenWeatherMapService().getCurrentWeatherByZipcode(zipcode, "metric")
        call.enqueue(object : Callback<CurrentWeather>{
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null){
                    // Loading 7 day forecast
                    val forecastCall = createOpenWeatherMapService().getSevenDayForecast(
                        lat = weatherResponse.coordinates.lat,
                        long = weatherResponse.coordinates.lon,
                        exclude = "current,minutely,hourly",
                        units = "metric"
                    )

                    forecastCall.enqueue(object: Callback<WeeklyForecast>{
                        override fun onResponse(call: Call<WeeklyForecast>, response: Response<WeeklyForecast>) {
                            val weeklyForecastResponse = response.body()
                            if (weeklyForecastResponse != null) _weeklyForecast.value = weeklyForecastResponse
                        }

                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.d("weekly", "error get weekly forecast 2")
                        }

                    })
                }
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("api", "Error get weekly forecast")
            }
        })
    }

    fun loadCurrentForecast(zipcode : String){
        val call = createOpenWeatherMapService().getCurrentWeatherByZipcode(zipcode, "metric")
        call.enqueue(object : Callback<CurrentWeather>{
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()
                if (weatherResponse != null) _currentWeather.value = weatherResponse
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e("api", "Error get current forecast")
            }
        })
    }
}