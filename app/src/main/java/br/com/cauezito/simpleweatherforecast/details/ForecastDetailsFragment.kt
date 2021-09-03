package br.com.cauezito.simpleweatherforecast.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.FragmentCurrentForecastBinding
import br.com.cauezito.simpleweatherforecast.databinding.FragmentForecastDetailsBinding
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

class ForecastDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentForecastDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_details, container, false)

        val sharedPreferencesUtil = SharedPreferencesUtil(requireContext())

       // val temperature = intent.getFloatExtra("temperature", 0f)
       // val detail = intent.getStringExtra("detail")

      //  binding.tvTemperature.text = ForecastUtil.formatForecastForShow(temperature, sharedPreferencesUtil.getTemperatureDisplay() )
        //binding.tvDetail.text = detail.toString()

       return binding.root
    }

}