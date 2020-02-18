package com.kbline.kotlin_module.Validation

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern


//이메일 비밀번호 유효성 검사.
object ValidCheck  {

    fun email(email: CharSequence): Boolean {
        return if (email == null) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }


    fun passPattern(pass: String, pattern: String): Boolean {
        val localPattern =
            Pattern.compile(pattern)
        val bool: Boolean
        bool = if (!TextUtils.isEmpty(pass) && localPattern.matcher(pass).matches()) {
            true
        } else {
            false
        }
        return bool
    }

}