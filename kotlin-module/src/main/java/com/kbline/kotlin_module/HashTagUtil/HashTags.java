package com.kbline.kotlin_module.HashTagUtil;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.view.ViewCompat;

import com.styleranker.styleranker.UtilFolder.HashTagView.CalloutLink;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HashTags {
    private int atBackgroundColor;
    private int atColor;
    private Typeface atFont;
    private float atSize;
    private boolean boldAt;
    private boolean boldHash;
    private int hashBackgroundColor;
    private int hashColor;
    private float hashSize;
    private Typeface hashTagFont;
    private boolean italicAt;
    private boolean italicHash;
    private boolean trackAt;
    private boolean trackHash;
    private boolean underlineAt;
    private boolean underlineHash;
    private final HashTagView view;

    public HashTags() {
        this(null);
    }

    public HashTags(HashTagView view) {
        this.hashColor = ViewCompat.MEASURED_STATE_MASK;
        this.hashBackgroundColor = 0;
        this.atColor = ViewCompat.MEASURED_STATE_MASK;
        this.atBackgroundColor = 0;
        this.hashSize = 1.0f;
        this.atSize = 1.0f;
        this.trackHash = true;
        this.view = view;
    }

    public void createHashTags(CharSequence text) {
        Spannable spannable = (Spannable) text;
        int i = 0;
        CharacterStyle[] spans = (CharacterStyle[]) spannable.getSpans(0, text.length(), CharacterStyle.class);
        int length = spans.length;
        while (i < length) {
            spannable.removeSpan(spans[i]);
            i++;
        }
        createTags(text);
    }

    public List<String> getHashTags(CharSequence text) {
        return getTags(text, '#');
    }

    public List<String> getAts(CharSequence text) {
        return getTags(text, '@');
    }

    private List<String> getTags(CharSequence text, char c) {
        Set<String> tags = new LinkedHashSet();
        Spannable spannable = (Spannable) text;
        int i = 0;
        CharacterStyle[] characterStyleArr = (CharacterStyle[]) spannable.getSpans(0, text.length(), CharacterStyle.class);
        int length = characterStyleArr.length;
        while (i < length) {
            CharacterStyle span = characterStyleArr[i];
            if (text.charAt(spannable.getSpanStart(span)) == c) {
                tags.add(text.toString().substring(spannable.getSpanStart(span) + 1, spannable.getSpanEnd(span)));
            }
            i++;
        }
        return new ArrayList(tags);
    }

    private void createTags(CharSequence text) {
        int index = 0;
        while (true) {
            boolean z = true;
            if (index < text.length() - 1) {
                char c = text.charAt(index);
                int nextItemIndex = index + 1;
                if (isUpdateRequired(c)) {
                    int itemIndex = index;
                    nextItemIndex = getIndex(text, itemIndex);
                    if (c != '#') {
                        z = false;
                    }
                    updateSpans(text, itemIndex, nextItemIndex, z);
                }
                index = nextItemIndex;
            } else {
                return;
            }
        }
    }

    private boolean isUpdateRequired(char c) {
        if (this.trackHash && c == '#') {
            return true;
        }
        if (this.trackAt && c == '@') {
            return true;
        }
        return false;
    }

    private int getIndex(CharSequence text, int itemIndex) {
        int index = -1;
        for (int i = itemIndex + 1; i < text.length(); i++) {
            if (!Character.isLetterOrDigit(text.charAt(i))) {
                index = i;
                break;
            }
        }
        return index == -1 ? text.length() : index;
    }

    private void updateSpans(CharSequence text, int start, int end, boolean isHash) {
        Spannable spannable = (Spannable) text;
        spannable.setSpan(new ForegroundColorSpan(getTextColor(isHash)), start, end, 33);
        if (this.view != null && isHash) {
            spannable.setSpan(new CalloutLink(this.view.getContext(), text.toString().substring(start, end)), start, end, 33);
        }
        spannable.setSpan(new BackgroundColorSpan(getBackgroundColor(isHash)), start, end, 33);
        spannable.setSpan(new StyleSpan(getTextStyle(isHash)), start, end, 33);


    }

    private int getTextColor(boolean isHash) {
        return isHash ? this.hashColor : this.atColor;
    }

    private int getBackgroundColor(boolean isHash) {
        return isHash ? this.hashBackgroundColor : this.atBackgroundColor;
    }

    private int getTextStyle(boolean isHash) {
        if (isHash) {
            if (this.boldHash && this.italicHash) {
                return 3;
            }
            if (!this.boldHash || this.italicHash) {
                return (this.boldHash || !this.italicHash) ? 0 : 2;
            } else {
                return 1;
            }
        } else if (this.boldAt && this.italicAt) {
            return 3;
        } else {
            if (!this.boldAt || this.italicAt) {
                return (this.boldAt || !this.italicAt) ? 0 : 2;
            } else {
                return 1;
            }
        }
    }

    private boolean isUnderlineRequired(boolean isHash) {
        return isHash ? this.underlineHash : this.underlineAt;
    }

    private Typeface getFont(boolean isHash) {
        return isHash ? this.hashTagFont : this.atFont;
    }

    public int getHashColor() {
        return this.hashColor;
    }

    public void setHashColor(int color) {
        this.hashColor = color;
    }

    public int getHashBackgroundColor() {
        return this.hashBackgroundColor;
    }

    public void setHashBackgroundColor(int color) {
        this.hashBackgroundColor = color;
    }

    public int getAtBackgroundColor() {
        return this.atBackgroundColor;
    }

    public void setAtBackgroundColor(int color) {
        this.atBackgroundColor = color;
    }

    public int getAtColor() {
        return this.atColor;
    }

    public void setAtColor(int color) {
        this.atColor = color;
    }

    public float getHashTextSize() {
        if (this.hashSize <= 0.0f) {
            this.hashSize = 1.0f;
        }
        return this.hashSize;
    }

    public void setHashTextSize(float size) {
        this.hashSize = size;
    }

    public float getAtTextSize() {
        if (this.atSize <= 0.0f) {
            this.atSize = 1.0f;
        }
        return this.atSize;
    }

    public void setAtTextSize(float size) {
        this.atSize = size;
    }

    public boolean isHashTracked() {
        return this.trackHash;
    }

    public void setTrackHash(boolean track) {
        this.trackHash = track;
    }

    public boolean isAtTracked() {
        return this.trackAt;
    }

    public void setTrackAt(boolean track) {
        this.trackAt = track;
    }

    public boolean isHashBold() {
        return this.boldHash;
    }

    public void setBoldHash(boolean bold) {
        this.boldHash = bold;
    }

    public boolean isAtBold() {
        return this.boldAt;
    }

    public void setBoldAt(boolean bold) {
        this.boldAt = bold;
    }

    public boolean isHashItalic() {
        return this.italicHash;
    }

    public void setItalicHash(boolean italic) {
        this.italicHash = italic;
    }

    public boolean isAtItalic() {
        return this.italicAt;
    }

    public void setItalicAt(boolean italic) {
        this.italicAt = italic;
    }

    public boolean isHashUnderlined() {
        return this.underlineHash;
    }

    public void setUnderlineHash(boolean underline) {
        this.underlineHash = underline;
    }

    public boolean isAtUnderlined() {
        return this.underlineAt;
    }

    public void setUnderlineAt(boolean underline) {
        this.underlineAt = underline;
    }

    public void setHashTagFont(Typeface font) {
        this.hashTagFont = font;
    }

    public void setAtFont(Typeface font) {
        this.atFont = font;
    }
}
