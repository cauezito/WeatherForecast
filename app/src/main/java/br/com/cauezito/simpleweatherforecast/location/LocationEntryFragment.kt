package br.com.cauezito.simpleweatherforecast.location

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import br.com.cauezito.simpleweatherforecast.AppNavigatorInterface
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.databinding.FragmentLocationEntryBinding


class LocationEntryFragment : Fragment() {

    private lateinit var appNavigatorInterface: AppNavigatorInterface

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigatorInterface = context as AppNavigatorInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentLocationEntryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_entry, container, false)

        binding.btEnter.setOnClickListener{view ->
            val zipCode = binding.etZipcode.text.toString()
            if (zipCode.length <= 5) Toast.makeText(context, "Invalid!", Toast.LENGTH_SHORT).show()
            else appNavigatorInterface.navigateToCurrentForecast(zipCode)
      }

        return binding.root
    }

    companion object {
        @JvmStatic // if your Kotlin code is called from Java, it makes sense to add the annotation
        fun newInstance(param1: String, param2: String) =
            LocationEntryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}