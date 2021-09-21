package br.com.cauezito.simpleweatherforecast.weather.details

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.FragmentForecastDetailsBinding
import br.com.cauezito.simpleweatherforecast.util.ForecastUtil
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil
import coil.load

class ForecastDetailsFragment : Fragment() {

    private val args: ForecastDetailsFragmentArgs by navArgs()
    private val viewModel : ForecastDetailsViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )
    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private lateinit var binding: FragmentForecastDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_details, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewStateObserver = Observer<ForecastDetailsViewState> {
      //      binding.tvTemperature.text =  ForecastUtil.formatForecastForShow(it.temp, SharedPreferencesUtil(requireContext()).getTemperatureDisplay())
            binding.tvDetail.text = it.description
            binding.ivIcon.load("http://openweathermap.org/img/wn/${args.icon}@2x.png")
        }

        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }
}