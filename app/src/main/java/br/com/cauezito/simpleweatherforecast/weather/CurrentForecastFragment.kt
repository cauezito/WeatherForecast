package br.com.cauezito.simpleweatherforecast.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.cauezito.simpleweatherforecast.BaseActivity
import br.com.cauezito.simpleweatherforecast.ModalTaskListener
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.api.ApiWeather
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.TemperatureDisplaySetting

class CurrentForecastFragment : BaseActivity() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentForecastBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_current_forecast, container, false
        )
        var zipcode: String? = ""

        arguments?.let {
            zipcode = it.getString(KEY_ZIPCODE) as String
        }

        val llWeatherInfo = binding.llWeatherInfo
        val progressbar = binding.pbLoading
        val temperature = binding.tvTemperature
        val location = binding.tvLocation
        val weatherDescription = binding.tvWeatherDescription
        val windSpeed = binding.tvWindSpeed
        val humidity = binding.tvHumidity
        val precipitation = binding.tvPrecipitation
        val feelsLike = binding.tvFeelsLike

        val currentWeatherObserver = Observer<ApiWeather> { weather ->
            llWeatherInfo.visibility = View.VISIBLE
            progressbar.visibility = View.GONE

            temperature.text = ForecastUtil.formatForecastForShow(
                weather.current.temperature, TemperatureDisplaySetting.Celsius
            )
            location.text = weather.location.name
            weatherDescription.text = weather.current.weatherDescriptions[0]
            windSpeed.text = weather.current.windSpeed.toString()
            humidity.text = weather.current.humidity.toString()
            precipitation.text = weather.current.precipitation.toString()
            feelsLike.text = weather.current.feelslike.toString()
        }

        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    progressbar.visibility = View.VISIBLE
                    llWeatherInfo.visibility = View.GONE

                    forecastRepository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }

        locationRepository = LocationRepository(requireContext())
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        location.setOnClickListener(View.OnClickListener {
            //Abrir dialog para inserção da localização
            //todo entender object:
            openLocationModal();
        })

        return binding.root
    }

    private fun openLocationModal() {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_location_card, null, true)

        val displayMetrics = this.resources.displayMetrics
        var screenDp = (displayMetrics.heightPixels / 2) - 360

        animateModalUpWithMargin(object : ModalTaskListener(view) {
            override fun onModalCancelled() {

            }

        }, screenDp)
    }

    companion object {
        const val KEY_ZIPCODE = "KEY_ZIP_CODE"

        @JvmStatic // if your Kotlin code is called from Java, it makes sense to add the annotation
        fun newInstance(zipcode: String) =
            CurrentForecastFragment().apply {
                arguments = Bundle().apply {
                    arguments?.putString(KEY_ZIPCODE, zipcode)
                }
            }
    }

    interface OnInsertLocation {
        fun setLocation(location: String)
    }
}