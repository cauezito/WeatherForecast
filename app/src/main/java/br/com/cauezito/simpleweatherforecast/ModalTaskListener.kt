package br.com.cauezito.simpleweatherforecast

import android.view.View

abstract class ModalTaskListener(view: View) {
    private var view: View? = null

    init {
        this.view = view
    }

    fun getView(): View? {
        return view
    }

    abstract fun onModalCancelled()

}
