package br.com.cauezito.simpleweatherforecast.weather.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cauezito.simpleweatherforecast.api.ApiWeather
import br.com.cauezito.simpleweatherforecast.api.OperationCallback
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository

class CurrentWeatherViewModelFactory(private val forecastRepository: ForecastRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(forecastRepository) as T
    }
}

class CurrentWeatherViewModel(private val forecastRepository: ForecastRepository) : ViewModel() {

    private val _currentWeather = MutableLiveData<ApiWeather>()
    val currentWeather: LiveData<ApiWeather> = _currentWeather

    fun loadCurrentForecast(zipcode: String) {
        forecastRepository.loadCurrentForecast(zipcode, object : OperationCallback<ApiWeather> {
            override fun onSuccess(data: ApiWeather?) {
                _currentWeather.value = data
            }

            override fun onError(message: String) {
                _currentWeather.value = null
            }
        })
    }

}