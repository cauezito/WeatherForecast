package br.com.cauezito.simpleweatherforecast.repository

import br.com.cauezito.simpleweatherforecast.api.*
import okhttp3.ResponseBody
import retrofit2.Response

class ForecastRepository {

    fun loadCurrentForecast(zipcode: String, callback: OperationCallback<ApiWeather>) {
        val call = createOpenWeatherMapService().getCurrentWeatherByZipcode(zipcode, "metric")

        call.enqueue(object : DefaultCallback<ApiWeather>() {
            override fun onSuccess(response: Response<ApiWeather>, code: Int) {
                callback.onSuccess(response.body())
            }

            override fun onError(body: ResponseBody?, code: Int, t: Throwable?) {
                callback.onError("Erro interno código ${code}")
            }
        })
    }
}