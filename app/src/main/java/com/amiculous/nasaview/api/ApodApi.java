package com.amiculous.nasaview.api;

import com.amiculous.nasaview.data.ApodEntity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApodApi {

    String BASE_URL = "https://api.nasa.gov/planetary/";

 //   @GET("apod?date=2019-02-25")
    @GET("apod")
    Call<ApodEntity> getApod(@Query("api_key") String api_key);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
}