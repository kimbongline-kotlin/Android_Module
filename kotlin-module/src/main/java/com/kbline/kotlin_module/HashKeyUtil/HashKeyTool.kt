package com.kbline.kotlin_module.HashKeyUtil

import android.util.Base64
import android.util.Log


//byteArray HashKey 변환.
object HashKeyTool {
    fun Key(byteArray: ByteArray) : String {
        val sha1 = byteArray
        return Base64.encodeToString(sha1,Base64.NO_WRAP)
    }
}