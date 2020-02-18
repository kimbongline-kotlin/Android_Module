package com.kbline.kotlin_module.Validation

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern


//이메일 비밀번호 유효성 검사.
object ValidCheck  {

    fun email(target: CharSequence): Boolean {
        return if (target == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }


    fun passPattern(paramString: String, pattern: String): Boolean {
        val localPattern =
            Pattern.compile(pattern)
        val bool: Boolean
        bool = if (!TextUtils.isEmpty(paramString) && localPattern.matcher(paramString).matches()) {
            true
        } else {
            false
        }
        return bool
    }

}