package br.com.cauezito.simpleweatherforecast.weather.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ForecastDetailsViewModelFactory(private val args: ForecastDetailsFragmentArgs) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastDetailsViewModel::class.java)) {
            return ForecastDetailsViewModel(args) as T
        }

        throw IllegalArgumentException("Unknow ViewModel Class")
    }
}

class ForecastDetailsViewModel(args: ForecastDetailsFragmentArgs): ViewModel() {

    private val _viewState : MutableLiveData<ForecastDetailsViewState> = MutableLiveData()
    val viewState : LiveData<ForecastDetailsViewState> = _viewState

    init {
        _viewState.value = ForecastDetailsViewState(
            temp = args.temperature,
            description = args.detail,
            iconUrl = "http://openweathermap.org/img/wn/${args.icon}@2x.png"
        )
    }
}