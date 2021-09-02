package br.com.cauezito.simpleweatherforecast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.cauezito.simpleweatherforecast.databinding.ActivityMainBinding
import br.com.cauezito.simpleweatherforecast.details.ForecastDetailsActivity
import br.com.cauezito.simpleweatherforecast.location.LocationEntryFragment
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil
import java.util.*

class MainActivity : AppCompatActivity(), AppNavigatorInterface {

    private lateinit var binding : ActivityMainBinding
    private val forecastRepository = ForecastRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val forecastList = binding.rvForecastList
        val dailyForestAdapter = DailyForestAdapter(SharedPreferencesUtil(this)){ dailyForecast ->
            showForecastDetail(dailyForecast)
        }

        forecastList.layoutManager = LinearLayoutManager(this)
        forecastList.adapter = dailyForestAdapter



        //observer will be updated anytime our live data changes in the repository
        val weeklyForecastObsever = Observer<List<DailyForecast>> {forecastItems ->
            //update our list adapter
            dailyForestAdapter.submitList(forecastItems)
        }

        forecastRepository.weeklyForecast.observe(this, weeklyForecastObsever)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.flContainer, LocationEntryFragment())
            .commit()
    }

    fun showForecastDetail(forecast : DailyForecast){
        val forecastDetailsIntent = Intent(this, ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("temperature", forecast.temp)
        forecastDetailsIntent.putExtra("detail", forecast.description)
        startActivity(forecastDetailsIntent)
    }

    override fun navigateToCurrentForecast(zipCode: String) {
        forecastRepository.loadForecast(zipCode)
    }
}