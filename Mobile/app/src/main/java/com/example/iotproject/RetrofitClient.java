package com.example.iotproject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static retrofit2.Retrofit instance;

    public static retrofit2.Retrofit getInstance(){
        if(instance == null)
            instance =new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl("http://10.0.2.2:3000/")
                    .build();
        return instance;
    }
}

