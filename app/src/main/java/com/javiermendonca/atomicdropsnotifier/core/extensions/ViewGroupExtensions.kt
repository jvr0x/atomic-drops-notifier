package com.javiermendonca.atomicdropsnotifier.core.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

inline fun <reified VD : ViewDataBinding> ViewGroup.bind(
    layoutId: Int,
    attachToRoot: Boolean = false
): VD = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, attachToRoot)