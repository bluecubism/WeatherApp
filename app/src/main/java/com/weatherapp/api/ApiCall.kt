package com.weatherapp.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.weatherapp.api.apidata.*
import com.weatherapp.snapshot.*
import java.time.OffsetDateTime
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resumeWithException

class ApiCall {
    private val _weatherApiUrl = "https://api.weather.gc.ca/"
    private val _torontoBbox = "-79.64,43.58,-79.11,43.86"

    /**
     * How many hours before the current time to call entries from.
     */
    private val _hrBefore : Long = 6
    /**
     * Max number of entries to search to be returned.
     */
    private val _maxLimit = 10

    /**
     * Get current real-time data (raw data)
     */
    private fun getSwobRealtimeApiData(
        context: Context,
        datetime: String? = null,
        bbox: String? = null,
        callback: (SwobRealtimeApiData) -> Unit)
    {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(_weatherApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: SwobRealtimeApiInterface = retrofit.create(SwobRealtimeApiInterface::class.java)
        // make api request
        val call: Call<SwobRealtimeApiData> = service.getApiData(
            datetime,
            bbox,
            _maxLimit,
            "json")

        // use enqueue() to make async api request
        call.enqueue(object : Callback<SwobRealtimeApiData> {
            // api response received successfully
            override fun onResponse(call: Call<SwobRealtimeApiData>, response: Response<SwobRealtimeApiData>) {
                if (response.isSuccessful) {
                    val data : SwobRealtimeApiData = response.body() as SwobRealtimeApiData
                    callback(data)
                }
            }

            // api request fails
            override fun onFailure(call: Call<SwobRealtimeApiData>, throwable: Throwable) {
                Log.e("Swob Realtime API Call", "API Request Fail: $throwable")
                Toast.makeText(context, "API Request Fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    /**
     * Get current real-time data (processed data)
     */
    private fun getCityPageWeatherRealtimeApiData(
        context: Context,
        datetime : String? = null,
        bbox : String? = null,
        callback: (CityPageWeatherRealtimeApiData) -> Unit)
    {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(_weatherApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: CityPageWeatherRealtimeApiInterface = retrofit.create(CityPageWeatherRealtimeApiInterface::class.java)
        // make api request
        val call: Call<CityPageWeatherRealtimeApiData> = service.getApiData(
            datetime,
            bbox,
            _maxLimit,
            "json")

        // use enqueue() to make async api request
        call.enqueue(object : Callback<CityPageWeatherRealtimeApiData> {
            // api response received successfully
            override fun onResponse(call: Call<CityPageWeatherRealtimeApiData>, response: Response<CityPageWeatherRealtimeApiData>) {
                if (response.isSuccessful) {
                    val data : CityPageWeatherRealtimeApiData = response.body() as CityPageWeatherRealtimeApiData
                    callback(data)
                }
            }

            // api request fails
            override fun onFailure(call: Call<CityPageWeatherRealtimeApiData>, throwable: Throwable) {
                Log.e("CPW Realtime API Call", "API Request Fail: $throwable")
                Toast.makeText(context, "API Request Fail", Toast.LENGTH_LONG).show()
            }
        })
    }

    suspend fun getCurrentData(context: Context, useRawData: Boolean) : WeatherSnapshot {
        return suspendCancellableCoroutine { continuation ->
            try {
                if (!useRawData) {
                    getCityPageWeatherRealtimeApiData(context, null, _torontoBbox) { data ->
                        val weather = ApiConverter().CPWRealtimeToWeatherSnapshot(context, data.features)
                        continuation.resume(weather)
                    }
                } else {
                    val dt = OffsetDateTime.now()
                    val dtBefore = dt.minusHours(_hrBefore)
                    getSwobRealtimeApiData(context, "$dtBefore/$dt", _torontoBbox) { data ->
                        val weather = ApiConverter().SwobRealtimeToWeatherSnapshot(context, data.features)
                        continuation.resume(weather)
                    }
                }
            } catch(e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    suspend fun getPredictedData(context: Context) : List<WeatherSnapshot> {
        return suspendCancellableCoroutine { continuation ->
            getCityPageWeatherRealtimeApiData(context) { data ->
                val weather = ApiConverter().CPWRealtimeHourlyForecastToWeatherSnapshot(data.features)
                continuation.resume(weather)
            }
        }
    }
}