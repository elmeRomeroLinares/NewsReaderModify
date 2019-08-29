package com.example.newsreader.network;

import com.example.newsreader.model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    // Query parameters
    String FROM_DATE = "from-date";
    String TO_DATE = "to-date";
    String SHOW_FIELDS = "show-fields";
    String PAGE = "page";
    String SIZE = "page-size";
    String ASK = "q";
    String KEY = "api-key";

    // Query Values
    String INITIAL_DATE = "2019-07-14";
    String END_DATE = "2019-07-21";
    String FIELDS = "all";
    String VALUE_SIZE = "10";
    String VALUE_KEY = "302d4a96-9bc7-49d3-8f61-7541b4bd9d17";

    @GET("/search")
    Call<ArticleResponse> getData(
            @Query(FROM_DATE) String initialDate,
            @Query(TO_DATE) String endDate,
            @Query(SHOW_FIELDS) String fields,
            @Query(PAGE) Integer pageNumber,
            @Query(SIZE) String pageSize,
            @Query(ASK) String queryParameter,
            @Query(KEY) String keyValue
    );
}
