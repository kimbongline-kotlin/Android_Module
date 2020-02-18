package com.kbline.kotlin_module.Util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

object KbImage {


    //gifImage
    fun gifimage(url: Any, imageView: ImageView) {
        Glide.with(imageView.context).load(url).apply(
            RequestOptions().format(DecodeFormat.PREFER_RGB_565).centerCrop().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
        ).into(imageView)

    }

    //Image
    fun image(url: Any, imageView: ImageView) {
        Glide.with(imageView.context).load(url).apply(
            RequestOptions().format(DecodeFormat.PREFER_RGB_565).centerCrop().diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
        ).thumbnail(0.5f).into(imageView)
    }

    //둥근 모서리 Image
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

    //원형 Image
    fun circleImage(url: Any, imageView: ImageView) {
        val requestOptions_circle = RequestOptions().optionalCircleCrop()
        requestOptions_circle.diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(imageView.context).asBitmap().load(url).apply(requestOptions_circle)
            .thumbnail(0.5f).into(imageView)
    }


    //이미지 실제주소
    fun getRealPathFromURI(
        context: Context,
        contentUri: Uri
    ): String {
        val cursor = context.contentResolver
            .query(contentUri!!, arrayOf("_data"), null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow("_data")
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}