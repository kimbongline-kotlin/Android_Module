package com.kbline.kotlin_module.Util

import java.text.DecimalFormat

//금액 ###,#
object PriceUtil {
    fun set(str: String): String {
        if (str.toString() != "") {
            if (str != null) {
                return if (str.toString() == "-") {
                    ""
                } else DecimalFormat("#,###")
                    .format(Integer.valueOf(str).toLong())
            }
        }
        return ""
    }
}