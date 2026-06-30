package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("collections/swob-realtime/items")
    fun getSwobRealtime(
        @Query("bbox") bbox: String,
        @Query("f") f: String
    ): Call<SwobRealtimeData>

    @GET("collections/prognos-hrdps-realtime/items")
    fun getPrognosHrdps(
        @Query("bbox") bbox: String,
        @Query("f") f: String
    ): Call<PrognosHrdpsData>
}