package br.com.cauezito.simpleweatherforecast.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class DefaultCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            response.let {
                onSuccess(it, response.code())
            }
        } else {
            response.errorBody()?.let {
                onError(it, response.code(), null)
            }
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onError(null, -1, t)
    }

    abstract fun onSuccess(response: Response<T>, code: Int)
    abstract fun onError(body: ResponseBody?, code: Int, t: Throwable?)
}