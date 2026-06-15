package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// toronto weather
// https://api.weather.gc.ca/collections/swob-realtime/items?bbox=-79.7,43.5,-79.1,43.9&f=json

// other link for reference
// https://api.weather.gc.ca/collections/swob-realtime/items/?limit=10&offset=0&stn_nam-value=toronto

interface ApiInterface {
    @GET("https://api.weather.gc.ca/collections/swob-realtime/items?bbox=-79.7,43.5,-79.1,43.9&f=json")
    fun getWeather(): Call<DataModel>
}