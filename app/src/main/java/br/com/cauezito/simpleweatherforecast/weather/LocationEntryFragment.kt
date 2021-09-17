package br.com.cauezito.simpleweatherforecast.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.FragmentLocationEntryBinding
import br.com.cauezito.simpleweatherforecast.repository.Location
import br.com.cauezito.simpleweatherforecast.repository.LocationRepository


class LocationEntryFragment : Fragment() {

    private lateinit var locationRepository: LocationRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentLocationEntryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_entry, container, false)

        locationRepository = LocationRepository(requireContext())

        binding.btEnter.setOnClickListener{view ->
            val zipCode = binding.etZipcode.text.toString()

            if (zipCode.length < 5) Toast.makeText(context, "Invalid!", Toast.LENGTH_SHORT).show()
            else {
                locationRepository.saveLocation(Location.Zipcode(zipCode))
                findNavController().navigateUp()
            }
      }

        return binding.root
    }

    companion object {
        @JvmStatic // if your Kotlin code is called from Java, it makes sense to add the annotation
        fun newInstance() =
            LocationEntryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}