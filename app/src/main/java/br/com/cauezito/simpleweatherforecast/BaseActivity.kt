package br.com.cauezito.simpleweatherforecast

import android.R
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import java.util.ArrayList

open class BaseActivity : Fragment() {

    lateinit var viewTranslucent: View
    var upModals = ArrayList<ModalTaskListener>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewTranslucent = View(context)
        viewTranslucent.setBackgroundColor(resources.getColor(R.color.transparent))
        viewTranslucent.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        viewTranslucent.setAlpha(0f)

        viewTranslucent.setOnClickListener(View.OnClickListener { cancelTopModal() })
        viewTranslucent.setClickable(false)

    }

    fun animateModalUpWithMargin(modalTaskListener: ModalTaskListener, topMargin: Int) {
        val view: View? = modalTaskListener.getView()
        if (view != null) {
            val rectangle = Rect()
            val window = activity?.window
            window?.decorView?.getWindowVisibleDisplayFrame(rectangle)

            val screenBottom = rectangle.bottom
            val rootView = window?.findViewById<ViewGroup>(android.R.id.content)
            val params = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                rootView?.measuredHeight?.minus(topMargin)!!
            )
            rootView?.addView(view, params)

            view.translationY = (screenBottom + 50).toFloat()
            view.visibility = View.VISIBLE

            val animatorUp = ObjectAnimator.ofFloat(view, "translationY", topMargin.toFloat())
            val animatorAlphaIn = ObjectAnimator.ofFloat(viewTranslucent, "alpha", 1f)

            val set = AnimatorSet()
            set.playTogether(animatorAlphaIn, animatorUp)
            set.interpolator = AccelerateDecelerateInterpolator()

            viewTranslucent.setClickable(true)
            upModals.add(0, modalTaskListener)
            set.start()
        }
    }

    private fun cancelTopModal() {
        if (upModals != null && upModals.size > 0) {
            val modalTaskListener: ModalTaskListener = upModals.get(0)
            modalTaskListener.onModalCancelled()
            animateModalDown(modalTaskListener)
        }
    }

    fun animateModalDown(modalTaskListener: ModalTaskListener) {
        val view = modalTaskListener.getView()
        upModals.remove(modalTaskListener)

        Utils.hideKeyboard(activity)

        if (view != null) {
            val rectangle = Rect()
            val window = activity?.window
            window?.decorView?.getWindowVisibleDisplayFrame(rectangle)

            val screenEnd = rectangle.bottom
            val rootView = view.findViewById<ViewGroup>(R.id.content)
            val animatorUp = ObjectAnimator.ofFloat(view, "translationY", screenEnd.toFloat())
            val animatorAlphaOut = ObjectAnimator.ofFloat(viewTranslucent, "alpha", 0f)
            val set = AnimatorSet()

            if (upModals.isEmpty()) set.playTogether(
                animatorAlphaOut,
                animatorUp
            ) else set.playTogether(animatorUp)
            set.interpolator = AccelerateDecelerateInterpolator()
            set.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    rootView.removeView(view)
                    view.visibility = View.GONE
                    viewTranslucent.isClickable = false
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

            set.start()
        }
    }
}