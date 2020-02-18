package com.kbline.kotlin_module.GalleryUtil

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.kbline.kotlin_module.GalleryUtil.Util.CameraValue

object KbPickerCall {

    fun open(activity: Activity, context : Context, type : Int, max_value : Int) {
        activity.startActivityForResult(
            Intent(context,KbPickerActivity::class.java)
                .putExtra(CameraValue.TYPE, type)
                .putExtra(CameraValue.MAX_VALUE,max_value)
            , CameraValue.RESULT)
    }
}