package br.com.cauezito.simpleweatherforecast.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.ActivityForecastDetailsBinding
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

class ForecastDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast_details)

        val temperature = intent.getFloatExtra("temperature", 0f)
        val detail = intent.getStringExtra("detail")

        var sharedPreferencesUtil = SharedPreferencesUtil(this)
        binding.tvTemperature.text = ForecastUtil.formatForecastForShow(temperature, sharedPreferencesUtil.getTemperatureDisplay() )
        binding.tvDetail.text = detail.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuUnits -> {
                ForecastUtil.showDialog(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}