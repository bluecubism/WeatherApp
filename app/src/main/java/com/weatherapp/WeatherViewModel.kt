package com.weatherapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.api.*
import com.weatherapp.snapshot.WeatherSnapshot
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weathers = MutableLiveData<Pair<WeatherSnapshot, List<WeatherSnapshot>>>()
    val weathers : LiveData<Pair<WeatherSnapshot, List<WeatherSnapshot>>> get() = _weathers

    /**
     * Fetch current & predicted weather data.
     */
    fun fetch(context: Context, useRawData: Boolean) {
        try {
            viewModelScope.launch {
                var currWeather = WeatherSnapshot()
                var predictedWeather = listOf<WeatherSnapshot>()

                val a1 = async { currWeather = ApiCall().getCurrentData(context, useRawData) }
                val a2 = async { predictedWeather = ApiCall().getPredictedData(context) }

                a1.await()
                a2.await()

                _weathers.postValue(Pair(currWeather, predictedWeather))

                Log.d("INFO", "Finished fetching weather API data")
            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Exception occurred while fetching weather API data: $e")
            e.printStackTrace()
        }
    }
}