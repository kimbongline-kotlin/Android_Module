package com.kbline.kotlin_module.DisplayUtil

import android.content.Context
import android.util.DisplayMetrics

object Convert {

    fun toPixcel(dp: Float, context: Context): Float {
        return context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT * dp
    }

    fun toDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}