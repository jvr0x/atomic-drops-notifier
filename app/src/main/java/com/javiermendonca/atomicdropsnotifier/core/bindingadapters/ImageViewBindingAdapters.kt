package com.javiermendonca.atomicdropsnotifier.core.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.javiermendonca.atomicdropsnotifier.R

@BindingAdapter("collectionImage")
fun loadCollectionImage(view: ImageView, imageUrl: String?) = with(view) {
    Glide
        .with(context)
        .load(context.getString(R.string.ipfs_url, imageUrl))
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(R.drawable.ic_atom)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}