package com.kbline.kotlin_module.Util

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object KbImage {

    fun gifimage(url: Any, imageView: ImageView) {
        Glide.with(imageView.context).load(url).apply(
            RequestOptions().format(DecodeFormat.PREFER_RGB_565).centerCrop().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
        ).into(imageView)
    }

    fun image(url: Any, imageView: ImageView) {
        Glide.with(imageView.context).load(url).apply(
            RequestOptions().format(DecodeFormat.PREFER_RGB_565).centerCrop().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
        ).thumbnail(0.5f).into(imageView)
    }

    fun cornerImage(
        url: Any,
        corner: Int,
        imageView: ImageView
    ) {
        val requestOptions = RequestOptions().transforms(
            *arrayOf<Transformation<Bitmap>>(
                CenterCrop(), RoundedCorners(corner)
            )
        )
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(imageView.context).asBitmap().load(url).thumbnail(0.5f)
            .apply(requestOptions).into(imageView)
    }

    fun circleImage(url: Any, imageView: ImageView) {
        val requestOptions_circle = RequestOptions().optionalCircleCrop()
        requestOptions_circle.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(imageView.context).asBitmap().load(url).apply(requestOptions_circle)
            .thumbnail(0.5f).into(imageView)
    }

}