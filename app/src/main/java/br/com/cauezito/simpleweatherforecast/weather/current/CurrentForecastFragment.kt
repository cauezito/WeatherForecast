package br.com.cauezito.simpleweatherforecast.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import br.com.cauezito.simpleweatherforecast.BaseActivity
import br.com.cauezito.simpleweatherforecast.ModalTaskListener
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.api.ApiWeather
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CurrentForecastFragment : BaseActivity() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository
    private var zipcode: String = ""
    private val viewModelFactory = CurrentWeatherViewModelFactory(forecastRepository)
    private val viewModel: CurrentWeatherViewModel by viewModels(
        factoryProducer = { viewModelFactory })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentForecastBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_current_forecast, container, false
        )

        arguments?.let {
            zipcode = it.getString(KEY_ZIPCODE) as String
        }

        val llCity = binding.llCity
        val pbLoading = binding.pbLoading
        val llWeatherInfo1 = binding.llWeatherInfo1
        val llWeatherInfo2 = binding.llWeatherInfo2

        val currentWeatherObserver = Observer<ApiWeather> { apiWeather ->
            apiWeather?.let {
                binding.currentWeather = apiWeather.current
                binding.location = apiWeather.location

            } ?: run {
                binding.currentWeather = null
                binding.location = null

                Toast.makeText(
                    activity,
                    getString(R.string.error_message),
                    Toast.LENGTH_LONG
                ).show()
            }

            pbLoading.visibility = View.GONE
        }

        viewModel.currentWeather.observe(viewLifecycleOwner, currentWeatherObserver)

        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> {
                    pbLoading.visibility = View.VISIBLE
                    llWeatherInfo1.visibility = View.GONE
                    llWeatherInfo2.visibility = View.GONE

                    viewModel.loadCurrentForecast(savedLocation.zipcode)
                }
            }
        }

        locationRepository = LocationRepository(requireContext())
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        llCity.setOnClickListener(View.OnClickListener {
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

        @JvmStatic
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