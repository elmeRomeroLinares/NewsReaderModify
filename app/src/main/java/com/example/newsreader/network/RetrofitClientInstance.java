package com.example.newsreader.network;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit;

    private static String FROM_DATE = "from-date";
    private static String TO_DATE = "to-date";
    private static String SHOW_FIELDS = "show-fields";
    private static String SIZE = "page-size";
    private static String KEY = "api-key";

    private static String INITIAL_DATE = "2019-07-14";
    private static String END_DATE = "2019-07-21";
    private static String FIELDS = "all";
    private static String VALUE_SIZE = "10";
    private static String VALUE_KEY = "302d4a96-9bc7-49d3-8f61-7541b4bd9d17";

    private static final String BASE_URL = "https://content.guardianapis.com";

    public static GetDataService getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(provideOkHttpClient())
                    .build();
        }
        return retrofit.create(GetDataService.class);
    }

    //Method that returns OkHttp client
    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter(FROM_DATE, INITIAL_DATE)
                            .addQueryParameter(TO_DATE, END_DATE)
                            .addQueryParameter(SHOW_FIELDS, FIELDS)
                            .addQueryParameter(SIZE, VALUE_SIZE)
                            .addQueryParameter(KEY, VALUE_KEY)
                            .build();

                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();
    }


}
