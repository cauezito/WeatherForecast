package br.com.cauezito.simpleweatherforecast.weather

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.FragmentForecastDetailsBinding
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil
import coil.load

class ForecastDetailsFragment : Fragment() {

    private val args: ForecastDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentForecastDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_details, container, false)

        val sharedPreferencesUtil = SharedPreferencesUtil(requireContext())

        binding.tvTemperature.text = ForecastUtil.formatForecastForShow(
            args.temperature,
            sharedPreferencesUtil.getTemperatureDisplay()
        )
        binding.tvDetail.text = args.detail
        binding.ivIcon.load("http://openweathermap.org/img/wn/${args.icon}@2x.png")

        return binding.root
    }

}