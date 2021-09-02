package br.com.cauezito.simpleweatherforecast.util

import android.content.Context

enum class TemperatureDisplaySetting{
    Fahrenheit, Celsius
}

class SharedPreferencesUtil (context: Context) {
    private val spSettings = "settings"

    private val preferences = context.getSharedPreferences(spSettings, Context.MODE_PRIVATE)

    fun updateSetting(setting: TemperatureDisplaySetting){
        preferences.edit().putString("temp_display", setting.name).commit()
    }

    fun getTemperatureDisplay() : TemperatureDisplaySetting{
        val settingValue = preferences.getString("temp_display", TemperatureDisplaySetting.Celsius.name) ?: TemperatureDisplaySetting.Celsius.name
        return TemperatureDisplaySetting.valueOf(settingValue)
    }
}