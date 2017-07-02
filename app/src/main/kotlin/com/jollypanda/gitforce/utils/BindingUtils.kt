package com.jollypanda.gitforce.utils

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget

/**
 * @author Yamushev Igor
 * @since  19.06.17
 */

@BindingAdapter("imageUrl")
public fun loadImage(iv: ImageView, url: String) {
    Glide.with(iv.context)
            .load(url)
            .asBitmap()
            .centerCrop()
            .into(object : BitmapImageViewTarget(iv) {
                override fun setResource(resource: Bitmap) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(iv.context.resources, resource)
                    circularBitmapDrawable.isCircular = true
                    iv.setImageDrawable(circularBitmapDrawable)
                }

                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    super.onResourceReady(resource, glideAnimation)
                }
            })
}
