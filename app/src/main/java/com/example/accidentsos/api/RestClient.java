package com.example.accidentsos.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static final String BASE_URL = "https://bb1a-2401-4900-634a-a951-40d-ceb2-bd29-2c73.ngrok-free.app/Accident%20SOS/api/";

    private static Retrofit getRetrofit()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)  //Change server URL
                .client(okHttpClient)
                .build();


        return retrofit;
    }
    public static Interface makeApi()
    {
        Interface api = getRetrofit().create(Interface.class);
        return api;
    }
}
