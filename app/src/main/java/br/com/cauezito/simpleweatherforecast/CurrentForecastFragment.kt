package br.com.cauezito.simpleweatherforecast

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.databinding.FragmentLocationEntryBinding
import br.com.cauezito.simpleweatherforecast.details.ForecastDetailsActivity
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

class CurrentForecastFragment : Fragment() {

    private val forecastRepository = ForecastRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCurrentForecastBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_forecast, container, false)

        val zipcode = requireArguments().getString(KEY_ZIPCODE) ?: ""

        val forecastList = binding.rvForecastList
        val dailyForestAdapter = DailyForestAdapter(SharedPreferencesUtil(requireContext())){ dailyForecast ->
            showForecastDetail(dailyForecast)
        }

        forecastList.layoutManager = LinearLayoutManager(requireContext())
        forecastList.adapter = dailyForestAdapter

        //observer will be updated anytime our live data changes in the repository
        val weeklyForecastObsever = Observer<List<DailyForecast>> {forecastItems ->
            //update our list adapter
            dailyForestAdapter.submitList(forecastItems)
        }

        forecastRepository.weeklyForecast.observe(requireActivity(), weeklyForecastObsever)
        forecastRepository.loadForecast(zipcode)

        return binding.root
    }

    fun showForecastDetail(forecast : DailyForecast){
        val forecastDetailsIntent = Intent(requireContext(), ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("temperature", forecast.temp)
        forecastDetailsIntent.putExtra("detail", forecast.description)
        startActivity(forecastDetailsIntent)
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