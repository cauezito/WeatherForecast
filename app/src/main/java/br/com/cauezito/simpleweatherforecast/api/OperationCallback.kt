package br.com.cauezito.simpleweatherforecast.api

interface OperationCallback<T> {
    fun onSuccess(data: ApiWeather?)
    fun onError(message: String)
}