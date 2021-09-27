package br.com.cauezito.simpleweatherforecast.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

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

        val llWeatherInfo1 = binding.llWeatherInfo1
        val llWeatherInfo2 = binding.llWeatherInfo2
        val progressbar = binding.pbLoading
        val temperature = binding.tvTemperature
        val location = binding.tvLocation
        val llCity = binding.llCity
        val locationError = binding.tvLocationError
        val weatherDescription = binding.tvWeatherDescription
        val windSpeed = binding.tvWindSpeed
        val humidity = binding.tvHumidity
        val precipitation = binding.tvPrecipitation
        val feelsLike = binding.tvFeelsLike
        val llLocation = binding.llLocation

        val currentWeatherObserver = Observer<ApiWeather> { apiWeather ->
            if (apiWeather.current != null) {
                llWeatherInfo1.visibility = View.VISIBLE
                llWeatherInfo2.visibility = View.VISIBLE
                locationError.visibility = View.GONE

                temperature.text = ForecastUtil.formatForecastForShow(
                    apiWeather?.current?.temperature,
                    TemperatureDisplaySetting.Celsius
                )
                location.text = apiWeather.location.name
                weatherDescription.text = apiWeather.current.weatherDescriptions.get(0)
                windSpeed.text = apiWeather.current.windSpeed.toString()
                humidity.text = apiWeather.current.humidity.toString()
                precipitation.text = apiWeather.current.precipitation.toString()
                feelsLike.text = apiWeather.current.feelslike.toString()
            } else {
                llWeatherInfo1.visibility = View.GONE
                llWeatherInfo2.visibility = View.GONE

                llLocation.visibility = View.VISIBLE
                locationError.visibility = View.VISIBLE

                location.text = "Inserir local"
                Toast.makeText(
                    activity,
                    "Não foi possível recuperar a previsão. Você digitou o local correto?",
                    Toast.LENGTH_LONG
                ).show()
            }

            progressbar.visibility = View.GONE
        }

        forecastRepository.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    progressbar.visibility = View.VISIBLE
                    llWeatherInfo1.visibility = View.GONE
                    llWeatherInfo2.visibility = View.GONE

                    forecastRepository.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }

        locationRepository = LocationRepository(requireContext())
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        llCity.setOnClickListener(View.OnClickListener {
            //todo entender object:
            openLocationModal(object : OnInsertLocation {
                override fun setLocation(location: String) {
                    locationRepository.saveLocation(Location.Zipcode(location))
                }
            })
        })

        return binding.root
    }

    private fun openLocationModal(onInsertLocation: OnInsertLocation) {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.custom_location_card, null, true)

        val displayMetrics = this.resources.displayMetrics
        var screenDp = (displayMetrics.heightPixels / 2) - 290

        val til = view.findViewById<TextInputLayout>(R.id.tilCity)
        val tiet = view.findViewById<TextInputEditText>(R.id.tietCity)
        val ivClose = view.findViewById<ImageView>(R.id.ivClose)

        ivClose.setOnClickListener {
            animateModalDown(view)
        }

        til.setEndIconOnClickListener {
            onInsertLocation.setLocation(tiet.text.toString())
            animateModalDown(view)
        }

        animateModalUpWithMargin(object : ModalTaskListener(view) {
            override fun onModalCancelled() {}
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