package br.com.cauezito.simpleweatherforecast.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import br.com.cauezito.simpleweatherforecast.R

class ForecastUtil {
    companion object {
        fun formatForecastForShow(
            temperature: Double,
            temperatureDisplaySetting: TemperatureDisplaySetting
        ): String {

            return when (temperatureDisplaySetting) {
                TemperatureDisplaySetting.Fahrenheit -> String.format("%.0f", temperature)
                TemperatureDisplaySetting.Celsius -> String.format("%.0f", temperature)
            }
        }

        fun showDialog(context: Context){
            val sharedPreferencesUtil = SharedPreferencesUtil(context)

            val dialogBuilder = AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.choose_display_unit))
                .setMessage(context.resources.getString(R.string.which_temperature))
                .setPositiveButton("Fº"){ _, _ ->
                   sharedPreferencesUtil.updateSetting(TemperatureDisplaySetting.Fahrenheit)
                }
                .setNeutralButton("Cº", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                       sharedPreferencesUtil.updateSetting(TemperatureDisplaySetting.Celsius)
                    }

                })
                .setOnDismissListener{
                    Toast.makeText(context, context.resources.getText(R.string.after_restart_app), Toast.LENGTH_SHORT).show()
                }
                .show()

        }
    }
}
