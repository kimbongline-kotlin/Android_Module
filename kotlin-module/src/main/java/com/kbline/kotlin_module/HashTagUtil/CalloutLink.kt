package com.styleranker.styleranker.UtilFolder.HashTagView

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View



class CalloutLink(internal var context: Context, internal var text: String) : ClickableSpan() {

    override fun updateDrawState(ds: TextPaint) {}

    override fun onClick(widget: View) {

    }
}
