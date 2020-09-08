package com.javiermendonca.atomicdropsnotifier.core.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}