package com.kbline.kotlin_module.Util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager
import java.util.*

object Uuid {

    @SuppressLint("MissingPermission")
    fun get(mContext: Context): String? {
        val tm =
            mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val tmDevice: String
        val tmSerial: String
        val androidId: String
        tmDevice = "" + tm.deviceId
        tmSerial = "" + tm.simSerialNumber
        androidId = "" + Settings.Secure.getString(
            mContext.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )
        val deviceUuid = UUID(
            androidId.hashCode().toLong(),
            tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong()
        )
        return deviceUuid.toString()
    }
}