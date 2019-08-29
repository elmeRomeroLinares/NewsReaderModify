package com.example.newsreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    // Model fields
    private String sectionName;
    private String webUrl;
    private String headline;
    private String thumbnail;
    private String body;

    public Article(String sectionName, String webUrl, String headline,
                   String thumbnail, String body) {
        this.sectionName = sectionName;
        this.headline = headline;
        this.thumbnail = thumbnail;
        this.webUrl = webUrl;
        this.body = body;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getBody() {
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sectionName);
        dest.writeString(this.webUrl);
        dest.writeString(this.headline);
        dest.writeString(this.thumbnail);
        dest.writeString(this.body);
    }

    protected Article(Parcel in) {
        this.sectionName = in.readString();
        this.webUrl = in.readString();
        this.headline = in.readString();
        this.thumbnail = in.readString();
        this.body = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}