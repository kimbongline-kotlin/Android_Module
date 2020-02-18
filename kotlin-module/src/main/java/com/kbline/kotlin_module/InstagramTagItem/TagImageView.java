package com.kbline.kotlin_module.InstagramTagItem;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class TagImageView extends AppCompatImageView {
    public TagImageView(Context context) {
        super(context);
    }

    public TagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
