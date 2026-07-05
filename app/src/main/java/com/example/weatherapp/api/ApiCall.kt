package com.example.weatherapp.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.OffsetDateTime

class ApiCall {
    companion object {
        val WEATHER_API_URL = "https://api.weather.gc.ca/"
        val TORONTO_BBOX = "-79.64,43.58,-79.11,43.86"
    }

    /**
     * Get current real-time weather data.
     */
    fun getApiData(context: Context,
                   dt : OffsetDateTime,
                   dtBefore : OffsetDateTime,
                   callback: (SwobRealtimeAPIData) -> Unit)
    {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        // make api request
        val call: Call<SwobRealtimeAPIData> = service.getApiData(
            "swob-realtime",
            "$dtBefore/$dt",
            TORONTO_BBOX,
            "json")

        // use enqueue() to make async api request
        call.enqueue(object : Callback<SwobRealtimeAPIData> {
            // api response received successfully
            override fun onResponse(call: Call<SwobRealtimeAPIData>, response: Response<SwobRealtimeAPIData>) {
                if (response.isSuccessful) {
                    val data : SwobRealtimeAPIData = response.body() as SwobRealtimeAPIData
                    callback(data)
                }
            }

            // api request fails
            override fun onFailure(call: Call<SwobRealtimeAPIData>, throwable: Throwable) {
                Log.e("ERROR", "API Request Fail: $throwable")
                Toast.makeText(context, "API Request Fail", Toast.LENGTH_LONG).show()
            }
        })
    }
}