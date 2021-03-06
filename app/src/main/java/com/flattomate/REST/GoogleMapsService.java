package com.flattomate.REST;

import com.flattomate.GoogleMaps.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsService {
    String BASE_URL = "https://maps.googleapis.com";


    @GET("/maps/api/geocode/json")
    Call<Map> getCityResults(@Query("address") String address,
                             @Query("key") String key);
}