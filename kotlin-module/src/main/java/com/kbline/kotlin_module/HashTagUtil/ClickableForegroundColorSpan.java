package com.kbline.kotlin_module.HashTagUtil;

import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;

public class ClickableForegroundColorSpan extends ClickableSpan {
    private final int mColor;
    private OnHashTagClickListener mOnHashTagClickListener;

    public interface OnHashTagClickListener {
        void onHashTagClicked(String str);
    }

    public ClickableForegroundColorSpan(@ColorInt int color, OnHashTagClickListener listener) {
        this.mColor = color;
        this.mOnHashTagClickListener = listener;
        if (this.mOnHashTagClickListener == null) {
            throw new RuntimeException("constructor, click listener not specified. Are you sure you need to use this class?");
        }
    }

    public void updateDrawState(TextPaint ds) {
        ds.setColor(this.mColor);
    }

    public void onClick(View widget) {
        CharSequence text = ((TextView) widget).getText();
        Spanned s = (Spanned) text;
        int start = s.getSpanStart(this);
        this.mOnHashTagClickListener.onHashTagClicked(text.subSequence(start + 1, s.getSpanEnd(this)).toString());
    }
}
