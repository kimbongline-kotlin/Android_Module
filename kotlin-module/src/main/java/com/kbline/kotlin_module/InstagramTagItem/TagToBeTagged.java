package com.kbline.kotlin_module.InstagramTagItem;

import android.os.Parcel;
import android.os.Parcelable;

public class TagToBeTagged implements Parcelable {

    public String title;
    public String info;
    public Double x_co_ord;
    public Double y_co_ord;


    public TagToBeTagged(String title, String info, Double x_co_ord, Double y_co_ord) {
        this.title = title;
        this.info = info;
        this.x_co_ord = x_co_ord;
        this.y_co_ord = y_co_ord;

    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getX_co_ord() {
        return x_co_ord;
    }

    public void setX_co_ord(Double x_co_ord) {
        this.x_co_ord = x_co_ord;
    }

    public Double getY_co_ord() {
        return y_co_ord;
    }

    public void setY_co_ord(Double y_co_ord) {
        this.y_co_ord = y_co_ord;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(this.info);
        dest.writeValue(this.x_co_ord);
        dest.writeValue(this.y_co_ord);
    }

    private TagToBeTagged(Parcel in) {
        title = in.readString();
        info = in.readString();

        this.x_co_ord = (Double) in.readValue(Double.class.getClassLoader());
        this.y_co_ord = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<TagToBeTagged> CREATOR =
            new Parcelable.Creator<TagToBeTagged>() {
                @Override
                public TagToBeTagged createFromParcel(Parcel source) {
                    return new TagToBeTagged(source);
                }

                @Override
                public TagToBeTagged[] newArray(int size) {
                    return new TagToBeTagged[size];
                }
            };



}
