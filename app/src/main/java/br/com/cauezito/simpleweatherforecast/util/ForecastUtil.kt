package br.com.cauezito.simpleweatherforecast.util

class ForecastUtil {

    companion object Formatter {
        fun formatForecastForShow(temperature: Float): String {
            return String.format("%.2fº", temperature)
        }
    }
}
