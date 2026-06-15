package com.example.weatherapp

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.view.View
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var btnRefresh: Button
    private lateinit var progressRefresh: ProgressBar
    private lateinit var txtRefresh: TextView
    private lateinit var txtTimestamp: TextView
    private lateinit var txtStnName: TextView
    private lateinit var txtAirTemp: TextView
    private lateinit var txtRelHum: TextView
//    private lateinit var txtStnPres: TextView
    private lateinit var txtSnwDpth: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // fetch data on start up
        btnRefresh = findViewById(R.id.button_refresh)
        progressRefresh = findViewById(R.id.progress_refresh)
        txtRefresh = findViewById(R.id.text_refresh)
        txtTimestamp = findViewById(R.id.text_timestamp)
        txtStnName = findViewById(R.id.text_station_name)
        txtAirTemp = findViewById(R.id.text_air_temperature)
        txtRelHum = findViewById(R.id.text_relative_humidity)
//        txtStnPres = findViewById(R.id.text_station_pressure)
        txtSnwDpth = findViewById(R.id.text_snow_depth)
        fetchData()

        // fetch data again if user clicks the refresh button
        btnRefresh.setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        progressRefresh.visibility = View.VISIBLE
        txtRefresh.visibility = View.VISIBLE

        ApiCall().getData(this) { data ->
            try {
                val properties = data.features.first().properties
                txtTimestamp.text = data.timeStamp
                txtStnName.text = properties.stationName
                txtAirTemp.text = properties.airTemp.toString() + properties.airTempUnit
                txtSnwDpth.text = "Snow Depth: " + properties.snowDepth + " " + properties.snowDepthUnit
                txtRelHum.text = "Relative Humidity: " + properties.relativeHumidity + properties.relativeHumidityUnit
            } catch (e: Exception) {
                txtAirTemp.text = "Error fetching weather"
                e.printStackTrace()
            } finally {
                progressRefresh.visibility = View.GONE
                txtRefresh.visibility = View.GONE
            }
        }
    }
}