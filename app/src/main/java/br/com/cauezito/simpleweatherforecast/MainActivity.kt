package br.com.cauezito.simpleweatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import br.com.cauezito.simpleweatherforecast.databinding.ActivityMainBinding
import br.com.cauezito.simpleweatherforecast.location.LocationEntryFragmentDirections
import java.util.*

class MainActivity : AppCompatActivity(), AppNavigatorInterface {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun navigateToCurrentForecast(zipCode: String) {
        val action = LocationEntryFragmentDirections.actionLocationEntryFragmentToCurrentForecastFragment()
        findNavController(R.id.navContainer).navigate(action)
    }

    override fun navigateToLocationEntry() {
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        findNavController(R.id.navContainer).navigate(action)
    }

    override fun navigateToForecastDetails(forecast: DailyForecast) {
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToForecastDetailsFragment()
        findNavController(R.id.navContainer).navigate(action)
    }

}