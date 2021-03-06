package com.kbline.kotlin_module.DisplayUtil

import android.content.Context
import android.util.DisplayMetrics

//DP to Pixcel, Pixcel to DP 변환
object Convert {

    fun toPixcel(dp: Float, context: Context): Float {
        return context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT * dp
    }

    fun toDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

}