package br.com.cauezito.simpleweatherforecast

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class Utils {

    companion object {
        fun hideKeyboard(activity: Activity?) {
            if (activity != null) {
                val focusedView = activity.currentFocus
                if (focusedView != null) {
                    val inputMethodManager =
                        activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(focusedView.windowToken, 0)
                    focusedView.clearFocus()
                }
            }
        }
    }

}