package com.example.newsreader.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ArticleResponse {

    private Results_ response;

    public ArticleResponse(Results_ response) {
        this.response = response;
    }

    public Results_ getResponse() {
        return response;
    }

    public void setResponse(Results_ response) {
        this.response = response;
    }
}


