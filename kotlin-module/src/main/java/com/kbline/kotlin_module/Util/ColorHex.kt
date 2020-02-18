package com.kbline.kotlin_module.Util

import android.graphics.Color


//Hex to Color
object ColorHex {
    fun set(hexCode : String) : Int {
        return  Color.parseColor(hexCode)
    }
}