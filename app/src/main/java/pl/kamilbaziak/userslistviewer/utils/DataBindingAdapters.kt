package pl.kamilbaziak.userslistviewer.utils

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.transition.TransitionManager
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("android:animatedVisibility")
fun setAnimatedVisibility(target: View, isVisible: Boolean) {
    TransitionManager.beginDelayedTransition(target.rootView as ViewGroup)
    target.visibility = if (isVisible) View.VISIBLE else View.GONE
}