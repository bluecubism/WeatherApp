package com.example.weatherapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiInterface {
    @GET("collections/{collection}/items")
    fun getApiData(
        @Path("collection") collection: String,
        @Query("datetime") datetime: String,
        @Query("bbox") bbox: String,
        @Query("f") f: String
    ): Call<SwobRealtimeAPIData>
}