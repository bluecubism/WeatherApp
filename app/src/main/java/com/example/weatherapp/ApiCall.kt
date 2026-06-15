package com.example.weatherapp

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCall {
    fun getData(context: Context, callback: (DataModel) -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://api.weather.gc.ca/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service: ApiInterface = retrofit.create(ApiInterface::class.java)
        val call: Call<DataModel> = service.getWeather() // make api request using getWeather()

        // use enqueue() to make async api request
        call.enqueue(object : Callback<DataModel> {
            // api response received successfully
            override fun onResponse(call: Call<DataModel>, response: Response<DataModel>) {
                if (response.isSuccessful) {
                    val data : DataModel = response.body() as DataModel
                    callback(data)
                }
            }

            // api request fails
            override fun onFailure(call: Call<DataModel>, throwable: Throwable) {
                Toast.makeText(context, "Request Fail", Toast.LENGTH_SHORT).show()
            }
        })
    }
}