package com.example.newsreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Fields implements Parcelable {
    private String headline;
    private String thumbnail;
    private String body;

    public Fields(String headline, String thumbnail, String body) {
        this.headline = headline;
        this.thumbnail = thumbnail;
        this.body = body;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headline);
        dest.writeString(this.thumbnail);
        dest.writeString(this.body);
    }

    private Fields(Parcel in) {
        this.headline = in.readString();
        this.thumbnail = in.readString();
        this.body = in.readString();
    }

    public static final Creator<Fields> CREATOR = new Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel source) {
            return new Fields(source);
        }

        @Override
        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };
}
