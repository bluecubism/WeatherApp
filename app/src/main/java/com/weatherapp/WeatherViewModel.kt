package com.weatherapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.api.*
import com.weatherapp.snapshot.WeatherSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weathers = MutableStateFlow<Pair<WeatherSnapshot, List<WeatherSnapshot>>>(
        Pair(WeatherSnapshot(), listOf())
    )
    val weathers : StateFlow<Pair<WeatherSnapshot, List<WeatherSnapshot>>> get() = _weathers

    /**
     * Fetch current & predicted weather data.
     */
    fun fetch(context: Context) {
        try {
            viewModelScope.launch {
                var currWeather = WeatherSnapshot()
                var predictedWeather = listOf<WeatherSnapshot>()

                // use dispatchers.io for api calling
                val a1 = async(Dispatchers.IO) { currWeather = ApiCall().getCurrentData(context) }
                val a2 = async(Dispatchers.IO) { predictedWeather = ApiCall().getPredictedData(context) }

                a1.await()
                a2.await()

                Log.d("WeatherViewModel", "Finished fetching weather API data")

                _weathers.emit(Pair(currWeather, predictedWeather))
            }
        } catch (e: Exception) {
            Log.e("WeatherViewModel", "Exception occurred while fetching weather API data: $e")
            e.printStackTrace()
        }
    }
}