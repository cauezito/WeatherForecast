package br.com.cauezito.simpleweatherforecast.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import br.com.cauezito.simpleweatherforecast.R
import br.com.cauezito.simpleweatherforecast.weather.CurrentForecastFragment
import com.google.android.material.textfield.TextInputEditText

class CustomDialog(
    context: Context,
    insertLocationInterface: CurrentForecastFragment.OnInsertLocation
) : Dialog(context) {

    private val insertLocationInterface = insertLocationInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog_text)

        val ivSearch = findViewById<ImageView>(R.id.ivSearch)
        val tietLocation = findViewById<TextInputEditText>(R.id.tietLocation)

        ivSearch.setOnClickListener(View.OnClickListener {
            insertLocationInterface.setLocation(location = tietLocation.text.toString())
            this.dismiss()
        })
    }
}