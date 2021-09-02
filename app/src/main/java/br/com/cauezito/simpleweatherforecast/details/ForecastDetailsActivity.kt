package br.com.cauezito.simpleweatherforecast.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.ActivityForecastDetailsBinding
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil

class ForecastDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forecast_details)

        val temperature = intent.getFloatExtra("temperature", 0f)
        val detail = intent.getStringExtra("detail")

        binding.tvTemperature.text = ForecastUtil.formatForecastForShow(temperature)
        binding.tvDetail.text = detail.toString()

    }
}