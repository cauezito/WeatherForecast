package br.com.cauezito.simpleweatherforecast.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import br.com.cauezito.simpleweatherforecast.R

class ForecastUtil {

    companion object {
        fun formatForecastForShow(temperature: Float): String {
            return String.format("%.2fº", temperature)
        }

        fun showDialog(context: Context){
            val dialogBuilder = AlertDialog.Builder(context)
                .setTitle(context.resources.getString(R.string.choose_display_unit))
                .setMessage(context.resources.getString(R.string.which_temperature))
                .setPositiveButton("Fº"){ _, _ ->
                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("Cº", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
                    }

                })
                .setOnDismissListener{
                    Toast.makeText(context, context.resources.getText(R.string.after_restart_app), Toast.LENGTH_SHORT).show()
                }
                .show()

        }
    }
}
