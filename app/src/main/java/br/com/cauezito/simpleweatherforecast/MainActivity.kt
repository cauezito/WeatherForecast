package br.com.cauezito.simpleweatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import br.com.cauezito.simpleweatherforecast.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val forecastRepository = ForecastRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btEnter.setOnClickListener{view ->
            val zipCode = binding.etZipcode.text.toString()

            if (zipCode.length <= 5) Toast.makeText(this, "Invalid!", Toast.LENGTH_SHORT).show()
            else forecastRepository.loadForecast(zipCode)
        }

        //observer will be updated anytime our live data changes in the repository
        val weeklyForecastObsever = Observer<List<DailyForecast>> {forecastItems ->
            //update our list adapter
            Toast.makeText(this, "Loaded Items", Toast.LENGTH_SHORT).show()
        }

        forecastRepository.weeklyForecast.observe(this, weeklyForecastObsever)
    }
}