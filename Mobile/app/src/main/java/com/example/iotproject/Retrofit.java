package com.example.iotproject;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    private static retrofit2.Retrofit instance;

    public static retrofit2.Retrofit getInstance(){
        if(instance == null)
            instance =new retrofit2.Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.43.59:3000/")
                    .build();
        return instance;
}}
