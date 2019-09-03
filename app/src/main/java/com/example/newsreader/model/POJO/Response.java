package com.example.newsreader.model.POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Response {
    @SerializedName("status")
    String status;

    @SerializedName("userTier")
    String userTier;

    @SerializedName("total")
    int total;

    @SerializedName("startIndex")
    int startIndex;

    @SerializedName("pageSize")
    int pageSize;

    @SerializedName("currentPage")
    int currentPage;

    @SerializedName("pages")
    int pages;

    @SerializedName("orderBy")
    String orderBy;

    @SerializedName("results")
    ArrayList<Result> results;
}
