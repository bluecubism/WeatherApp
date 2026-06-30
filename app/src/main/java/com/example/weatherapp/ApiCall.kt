package com.example.weatherapp

import android.content.Context
import android.util.Log
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCall {
    companion object {
        val WEATHER_API_URL = "https://api.weather.gc.ca/"
        val TORONTO_BBOX = "-79.64,43.58,-79.11,43.86"
    }

    /**
     * Get current real-time weather data.
     */
    fun getSwobRealtime(context: Context, callback: (SwobRealtimeData) -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<SwobRealtimeData> = service.getSwobRealtime(TORONTO_BBOX, "json") // make api request

        // use enqueue() to make async api request
        call.enqueue(object : Callback<SwobRealtimeData> {
            // api response received successfully
            override fun onResponse(call: Call<SwobRealtimeData>, response: Response<SwobRealtimeData>) {
                if (response.isSuccessful) {
                    val data : SwobRealtimeData = response.body() as SwobRealtimeData
                    callback(data)
                }
            }

            // api request fails
            override fun onFailure(call: Call<SwobRealtimeData>, throwable: Throwable) {
                Log.e("Swob API Request Fail", "$throwable")
                Toast.makeText(context, "Swob API Request Fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Get predicted future hourly forecast data.
     */
    fun getPrognosHrdps(context: Context, callback: (PrognosHrdpsData) -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(WEATHER_API_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<PrognosHrdpsData> = service.getPrognosHrdps(TORONTO_BBOX, "json")

        // use enqueue() to make async api request
        call.enqueue(object : Callback<PrognosHrdpsData> {
            // api response received successfully
            override fun onResponse(call: Call<PrognosHrdpsData>, response: Response<PrognosHrdpsData>) {
                if (response.isSuccessful) {
                    val data : PrognosHrdpsData = response.body() as PrognosHrdpsData
                    callback(data)
                }
            }

            // api request fails
            override fun onFailure(call: Call<PrognosHrdpsData>, throwable: Throwable) {
                Log.e("Hrdps API Request Fail", "$throwable")
                Toast.makeText(context, "Hrdps API Request Fail", Toast.LENGTH_LONG).show()
            }
        })
    }
}