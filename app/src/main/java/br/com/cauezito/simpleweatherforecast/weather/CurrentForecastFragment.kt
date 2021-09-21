package br.com.cauezito.simpleweatherforecast.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.api.ApiWeather
import br.com.cauezito.simpleweatherforecast.api.CurrentWeather
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.TemperatureDisplaySetting

class CurrentForecastFragment : Fragment() {

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

        location.setOnClickListener(View.OnClickListener {
            //Abrir dialog para inserção da localização
            Toast.makeText(context, "teste", Toast.LENGTH_LONG).show()
        })

        val currentWeatherObserver = Observer<ApiWeather> { weather ->
            llWeatherInfo.visibility = View.VISIBLE
            progressbar.visibility = View.GONE

            temperature.text = ForecastUtil.formatForecastForShow(
                weather.current.temperature,
                TemperatureDisplaySetting.Celsius
            )
            location.text = weather.location.name
            weatherDescription.text = weather.current.weatherDescriptions[0]
            windSpeed.text = weather.current.windSpeed.toString()
            humidity.text = weather.current.humidity.toString()
            precipitation.text = weather.current.precipitation.toString()
            feelsLike.text = weather.current.feelslike.toString()
        }

        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    progressbar.visibility = View.VISIBLE
                    llWeatherInfo.visibility = View.GONE

                    forecastRepository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return binding.root
    }

    fun showLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
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
}