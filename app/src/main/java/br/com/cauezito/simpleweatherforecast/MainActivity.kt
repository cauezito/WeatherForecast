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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.flContainer, LocationEntryFragment())
            .commit()
    }

    override fun navigateToCurrentForecast(zipCode: String) {
       supportFragmentManager
           .beginTransaction()
           .replace(R.id.flContainer, CurrentForecastFragment.newInstance(zipCode))
           .commit()
    }

    override fun navigateToLocationEntry() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flContainer, LocationEntryFragment())
            .commit()
    }
}