package com.example.newsreader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Results implements Parcelable {

    private String sectionName;

    private String webUrl;

    private Fields fields;

    Results(String sectionName, String webUrl, Fields fields) {
        this.sectionName = sectionName;
        this.webUrl = webUrl;
        this.fields = fields;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sectionName);
        dest.writeString(this.webUrl);
        dest.writeParcelable(this.fields, flags);
    }

    private Results(Parcel in) {
        this.sectionName = in.readString();
        this.webUrl = in.readString();
        this.fields = in.readParcelable(Fields.class.getClassLoader());
    }

    public static final Creator<Results> CREATOR = new Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel source) {
            return new Results(source);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };
}
