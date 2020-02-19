package com.kbline.kotlin_module.DateUtil

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtil  {

    fun convertDate(dateInMilliseconds: Long): String? {
        return DateFormat.format("aa h:mm", dateInMilliseconds).toString()
    }

    fun convertDateMonth(dateInMilliseconds: Long): String? {
        return DateFormat.format("MM월 dd일", dateInMilliseconds).toString()
    }

    fun compareToDay(date1: Date?, date2: Date?): Int {
        if (date1 == null || date2 == null) {
            return 0
        }
        val sdf = SimpleDateFormat("yyyyMMdd")
        return sdf.format(date1).compareTo(sdf.format(date2))
    }
}