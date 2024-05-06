package com.example.accidentsos.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DistanceClient {


    private static final String URL="https://maps.googleapis.com/maps/api/distancematrix/";
    private static Retrofit getRetrofit()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL)  //Change server URL
                .client(okHttpClient).build();

        return retrofit;
    }
    public static Interface makeApi()
    {
        Interface api = getRetrofit().create(Interface.class);
        return api;
    }
}
