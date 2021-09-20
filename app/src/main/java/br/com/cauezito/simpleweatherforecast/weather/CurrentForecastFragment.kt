package br.com.cauezito.simpleweatherforecast.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.api.CurrentWeather
import br.com.cauezito.simpleweatherforecast.api.DailyForecast
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil
import br.com.cauezito.simpleweatherforecast.util.TemperatureDisplaySetting

class CurrentForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

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

        val fabLocationEntryButton = binding.fabLocationEntryButton

        fabLocationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        val locationName = binding.tvLocation
        val temperature = binding.tvTemperature
        val emptyText = binding.tvEmpty

        val currentWeatherObserver = Observer<CurrentWeather> { weather ->
            emptyText.visibility = View.GONE
            locationName.visibility = View.VISIBLE
            temperature.visibility = View.VISIBLE

            locationName.text = weather.name
            temperature.text = ForecastUtil.formatForecastForShow(
                weather.forecast.temp,
                TemperatureDisplaySetting.Celsius
            )
        }

        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> forecastRepository.loadCurrentForecast(savedLocation.zipcode)
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