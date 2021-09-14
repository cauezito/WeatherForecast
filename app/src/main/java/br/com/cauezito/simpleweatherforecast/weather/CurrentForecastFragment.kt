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
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.repository.ForecastRepository
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

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
        val binding: FragmentCurrentForecastBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_current_forecast, container, false)
        var zipcode : String? = ""

        arguments?.let {
            zipcode = it.getString(KEY_ZIPCODE) as String
        }

        val fabLocationEntryButton = binding.fabLocationEntryButton

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
        val weeklyForecastObsever = Observer<List<DailyForecast>> { forecastItems ->
            //update our list adapter
            dailyForestAdapter.submitList(forecastItems)
        }
        forecastRepository.weeklyForecast.observe(requireActivity(), weeklyForecastObsever)

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.Zipcode -> forecastRepository.loadForecast(savedLocation.zipcode)
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)

        return binding.root
    }

    fun showLocationEntry(){
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    fun showForecastDetails(forecast : DailyForecast){
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment(forecast.temp, forecast.description)
        findNavController().navigate(action)
    }

    companion object {
        const val KEY_ZIPCODE = "KEY_ZIP_CODE"
        @JvmStatic // if your Kotlin code is called from Java, it makes sense to add the annotation
        fun newInstance(zipcode : String) =
            CurrentForecastFragment().apply {
                arguments = Bundle().apply {
                    arguments?.putString(KEY_ZIPCODE, zipcode)
                }
            }
    }
}