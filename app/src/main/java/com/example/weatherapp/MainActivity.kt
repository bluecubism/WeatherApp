package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer

class MainActivity : ComponentActivity() {
    private lateinit var btnRefresh : Button
    private lateinit var progressRefresh : ProgressBar
    private lateinit var txtRefresh : TextView

    private lateinit var swob : SwobViewModel
    private lateinit var currWeather : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
        fetchData() // fetch on startup

        // fetch data if user clicks the refresh button
        btnRefresh.setOnClickListener {
            fetchData()
        }
    }

    private fun setup() {
        swob = SwobViewModel()

        // get views
        btnRefresh = findViewById(R.id.button_refresh)
        progressRefresh = findViewById(R.id.progress_refresh)
        txtRefresh = findViewById(R.id.text_refresh)

        // make these invisible by default
        progressRefresh.visibility = View.INVISIBLE
        txtRefresh.visibility = View.INVISIBLE

        // inflate main layout with the swob data layout
        val currWeatherView = findViewById<ViewGroup>(R.id.view_current_weather)
        currWeather = layoutInflater.inflate(R.layout.swob_data_layout, currWeatherView)

        // inflate swob data layout with the subweathers
        val subweathersView = currWeather.findViewById<ViewGroup>(R.id.view_subweathers)
        repeat (3) {
            layoutInflater.inflate(R.layout.weather_icon_text, subweathersView)
        }
    }

    private fun fetchData() {
        progressRefresh.visibility = View.VISIBLE
        txtRefresh.visibility = View.VISIBLE

        // fetch the data
        swob.fetch(this)

        // wait on the data
        swob.data.observe(this, Observer { data ->
            updateSwobUI(data)
            progressRefresh.visibility = View.INVISIBLE
            txtRefresh.visibility = View.INVISIBLE
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateSwobUI(data: SwobData) {
        var txt : TextView = currWeather.findViewById(R.id.text_station_name)
        txt.text = data.stationName

        txt = currWeather.findViewById(R.id.text_air_temperature)
        txt.text = data.airTemp

        txt = currWeather.findViewById(R.id.text_relative_humidity)
        txt.text = "Relative Humidity: " + data.relHum

        txt = currWeather.findViewById(R.id.text_wind_speed)
        if (data.windSpeed.isEmpty()) {
            txt.text = "Wind Speed: Calm"
        } else {
            var str = "Wind Speed: ${data.windSpeed}"
            if (!data.windDir.isEmpty()) str += " (${data.windDir})"
            if (!data.windGust.isEmpty()) str += "\nGusting to ${data.windGust}"
            txt.text = str
        }

        // update the 3 subweathers
        val currWeatherViewGroup : ViewGroup = currWeather.findViewById(R.id.view_current_weather)
        var subweatherViewGroup : ViewGroup = currWeatherViewGroup.getChildAt(0) as ViewGroup // linear layout of current weather
        subweatherViewGroup = subweatherViewGroup.findViewById(R.id.view_subweathers) // get the 3 subweathers

        // rainy/snowy
        var pcpnText = "" // used later to decide default main weather
        var subweather = subweatherViewGroup.getChildAt(0)
        var img : ImageView = subweather.findViewById(R.id.image)
        txt = subweather.findViewById(R.id.text)
        when (data.pcpnImg) {
            R.drawable.sunny -> pcpnText = "${resources.getText(R.string.no_rain)}"
            R.drawable.snow_light -> pcpnText = "${resources.getText(R.string.light_snow)}"
            R.drawable.rain_light -> pcpnText = "${resources.getText(R.string.light_rain)}"
            R.drawable.snow -> pcpnText = "${resources.getText(R.string.snowy)}"
            R.drawable.rain -> pcpnText = "${resources.getText(R.string.rainy)}"
            R.drawable.snow_heavy -> pcpnText = "${resources.getText(R.string.heavy_snow)}"
            R.drawable.rain_heavy -> pcpnText = "${resources.getText(R.string.heavy_rain)}"
        }
        img.setImageResource(data.pcpnImg)
        txt.text = data.pcpn + "\n" + pcpnText

        // cloudy
        subweather = subweatherViewGroup.getChildAt(1)
        img = subweather.findViewById(R.id.image)
        txt = subweather.findViewById(R.id.text)
        when (data.cloudAmtImg) {
            R.drawable.sunny -> data.cloudAmt += "\n${resources.getText(R.string.no_clouds)}"
            R.drawable.partly_cloudy -> data.cloudAmt += "\n${resources.getText(R.string.partly_cloudy)}"
            R.drawable.cloudy -> data.cloudAmt += "\n${resources.getText(R.string.cloudy)}"
        }
        img.setImageResource(data.cloudAmtImg)
        txt.text = data.cloudAmt

        // fog
        var fogText = "" // used later to decide the weather if precipitation status is sunny
        subweather = subweatherViewGroup.getChildAt(2)
        img = subweather.findViewById(R.id.image)
        txt = subweather.findViewById(R.id.text)
        when (data.visImg) {
            R.drawable.sunny -> fogText = "${resources.getText(R.string.no_fog)}"
            R.drawable.cloudy_s_sunny -> fogText = "${resources.getText(R.string.slightly_foggy)}"
            R.drawable.cloudy -> fogText = "${resources.getText(R.string.mostly_foggy)}"
            R.drawable.fog -> fogText = "${resources.getText(R.string.foggy)}"
        }
        img.setImageResource(data.visImg)
        txt.text = data.vis + "\n" + fogText

        // update the main weather
        /* possible weathers to display, ordered by priority:
         * snow_heavy / rain_heavy (heavy snow/rain)
         * snow / rain (moderate snow/rain & cloudy)
         * snow_s_cloud / rain_s_cloudy (moderate snow/rain & partly cloudy)
         * snow_light / rain_light (light snow/rain & cloudy)
         * cloudy_s_rain (light rain & partly cloudy)
         * sunny_s_rain (light rain & no clouds)
         * fog
         * cloudy
         * partly_cloudy
         * sunny
         */
        // set default weather to be what the current rain/snow status is
        // since those are the highest priorities
        var mainWeatherText = pcpnText
        var mainWeatherImage = data.pcpnImg
        // get the special cases
        when (mainWeatherImage) {
            R.drawable.snow -> {
                if (data.cloudAmtImg != R.drawable.cloudy)
                    mainWeatherImage = R.drawable.snow_s_cloudy
            }
            R.drawable.rain -> {
                if (data.cloudAmtImg != R.drawable.cloudy)
                    mainWeatherImage = R.drawable.rain_s_cloudy
            }
            R.drawable.rain_light -> {
                if (data.cloudAmtImg == R.drawable.partly_cloudy)
                    mainWeatherImage = R.drawable.cloudy_s_rain
                else if (data.cloudAmtImg == R.drawable.sunny)
                    mainWeatherImage = R.drawable.sunny_s_rain
            }
            R.drawable.sunny -> {
                if (data.visImg != R.drawable.sunny) {
                    mainWeatherText = fogText
                    mainWeatherImage = R.drawable.fog
                } else if (data.cloudAmtImg == R.drawable.cloudy) {
                    mainWeatherText = resources.getText(R.string.cloudy).toString()
                    mainWeatherImage = R.drawable.cloudy
                } else if (data.cloudAmtImg == R.drawable.partly_cloudy) {
                    mainWeatherText = resources.getText(R.string.partly_cloudy).toString()
                    mainWeatherImage = R.drawable.partly_cloudy
                } else {
                    mainWeatherText = resources.getText(R.string.sunny).toString()
                    mainWeatherImage = R.drawable.sunny
                }
            }
        }
        txt = currWeather.findViewById(R.id.text_main_weather)
        img = currWeather.findViewById(R.id.image_main_weather)
        txt.text = mainWeatherText
        img.setImageResource(mainWeatherImage)
    }
}