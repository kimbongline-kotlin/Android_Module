package com.kbline.kotlin_module.Util

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.text.ClipboardManager
import android.widget.Toast

object ClipboardUtil  {

    fun copy(
        context: Context,
        text: String,
        toast: String
    ) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.text = text
        } else {
            val clipboard =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("클립보드 저장", text)
           clipboard.setPrimaryClip(clip)
        }

        Toast.makeText(context,toast,Toast.LENGTH_LONG).show()

    }

}

