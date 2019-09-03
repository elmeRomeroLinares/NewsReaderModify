package com.example.newsreader.network;

import com.example.newsreader.model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    // Query parameters
    String PAGE = "page";
    String ASK = "q";

    @GET("/search")
    Call<ArticleResponse> getData(
            @Query(PAGE) Integer pageNumber,
            @Query(ASK) String queryParameter
    );


}
