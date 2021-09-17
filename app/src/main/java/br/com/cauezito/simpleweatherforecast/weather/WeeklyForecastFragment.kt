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
import br.com.cauezito.simpleweatherforecast.api.DailyForecast
import br.com.cauezito.simpleweatherforecast.api.WeeklyForecast
import br.com.cauezito.simpleweatherforecast.databinding.FragmentWeeklyForecastBinding
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

class WeeklyForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentWeeklyForecastBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_weekly_forecast, container, false)

        val fabLocationEntryButton = binding.fabLocationEntryButton

        var zipcode : String? = ""

        arguments?.let {
            zipcode = it.getString(CurrentForecastFragment.KEY_ZIPCODE) as String
        }

        fabLocationEntryButton.setOnClickListener {
            showLocationEntry()
        }

        val forecastList = binding.rvForecastList
        val dailyForestAdapter = DailyForestAdapter(SharedPreferencesUtil(requireContext())){ dailyForecast ->
            showForecastDetails(dailyForecast)
        }

        forecastList.layoutManager = LinearLayoutManager(requireContext())
        forecastList.adapter = dailyForestAdapter

        //observer will be updated anytime our live data changes in the repository
        val weeklyForecastObsever = Observer<WeeklyForecast> { weeklyForecast ->
            //update our list adapter
            dailyForestAdapter.submitList(weeklyForecast.daily)
        }

        forecastRepository.weeklyForecast.observe(viewLifecycleOwner, weeklyForecastObsever)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> forecastRepository.loadWeeklyForecast(savedLocation.zipcode)
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return binding.root
    }

    fun showForecastDetails(forecast : DailyForecast){
        val temp = forecast.temp.max
        val description = forecast.weather[0].description
        val icon = forecast.weather[0].icon

        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment2(temp, description,icon)
        findNavController().navigate(action)
    }

    fun showLocationEntry(){
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment2()
        findNavController().navigate(action)
    }

    companion object {
        const val KEY_ZIPCODE = "KEY_ZIP_CODE"
        @JvmStatic // if your Kotlin code is called from Java, it makes sense to add the annotation
        fun newInstance(zipcode : String) =
            WeeklyForecastFragment().apply {
                arguments = Bundle().apply {
                    arguments?.putString(KEY_ZIPCODE, zipcode)
                }
            }
    }
}