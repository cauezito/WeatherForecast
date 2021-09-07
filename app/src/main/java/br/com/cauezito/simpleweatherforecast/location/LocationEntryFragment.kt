package br.com.cauezito.simpleweatherforecast.location

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


class LocationEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentLocationEntryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_entry, container, false)

        binding.btEnter.setOnClickListener{view ->
            val zipCode = binding.etZipcode.text.toString()
            if (zipCode.length <= 5) Toast.makeText(context, "Invalid!", Toast.LENGTH_SHORT).show()
            else findNavController().navigateUp()
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