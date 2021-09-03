package br.com.cauezito.simpleweatherforecast

interface AppNavigatorInterface {
    fun navigateToCurrentForecast(zipCode : String)
    fun navigateToLocationEntry()
    fun navigateToForecastDetails(forecast : DailyForecast)
}