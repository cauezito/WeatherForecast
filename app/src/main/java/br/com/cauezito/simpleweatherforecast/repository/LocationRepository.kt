package br.com.cauezito.simpleweatherforecast.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.cauezito.simpleweatherforecast.util.SharedPreferencesUtil

sealed class Location {
    data class Zipcode(val zipcode: String) : Location()
}

private const val SP_ZIPCODE = "SP_ZIPCODE"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key != SP_ZIPCODE) return@registerOnSharedPreferenceChangeListener

            broadcastSavedZipcode()
        }

        broadcastSavedZipcode()
    }

    fun saveLocation(location: Location){
        when (location) {
            is Location.Zipcode -> {
                preferences.edit().putString(SP_ZIPCODE, location.zipcode).apply()

                broadcastSavedZipcode()
            }
        }
    }

    private fun broadcastSavedZipcode(){
        val zipcode = preferences.getString(SP_ZIPCODE, "")
        if (zipcode != null && zipcode.isNotBlank()){
            _savedLocation.value = Location.Zipcode(zipcode)
        }
    }
}