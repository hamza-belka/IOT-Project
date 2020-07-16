package com.example.iotproject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterNode {


    @GET("/getLum")
    Call<Integer> getLum();

    @GET("/getSound")
    Call<Double> getsound();
}
