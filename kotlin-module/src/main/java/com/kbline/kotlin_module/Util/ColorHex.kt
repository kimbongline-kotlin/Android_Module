package com.kbline.kotlin_module.Util

import android.graphics.Color

object ColorHex {
    fun set(hexCode : String) : Int {
        return  Color.parseColor(hexCode)
    }
}