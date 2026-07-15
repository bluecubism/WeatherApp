package com.weatherapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import coil3.load
import coil3.request.placeholder
import com.weatherapp.snapshot.WeatherSnapshot
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

// TODO:
//  auto refresh data if a setting changed
//  add settings for weather interval (1 hr, 3 hrs, etc)
//  unit test, integration test, api test, ui test

class MainActivity : ComponentActivity() {
    private var _useRawData = false
    private lateinit var _weatherViewModel : WeatherViewModel

    private lateinit var _btnSettings : ImageButton

    private lateinit var _btnRefresh : Button
    private lateinit var _progressRefresh : ProgressBar
    private lateinit var _txtRefresh : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val useRawData = prefs.getBoolean("use_raw_data_key", _useRawData)
        _useRawData = useRawData

        setup()
        fetchData() // fetch on startup

        // schedule task to run every hour, i.e. at 8:00AM, then 9:00AM, etc
        val scheduler = ScheduledThreadPoolExecutor(1)
        val nextHr = Calendar.getInstance()
        nextHr.add(Calendar.HOUR, 1) // get next hour
        nextHr.set(Calendar.MINUTE, 0)
        nextHr.set(Calendar.SECOND, 0)
        nextHr.set(Calendar.MILLISECOND, 0)
        val initialDelay = nextHr.timeInMillis - Calendar.getInstance().timeInMillis
        scheduler.scheduleWithFixedDelay(
            {
                Log.d("MainActivity", "Run scheduled task (update weathers)")
                fetchData()
            },
            // start at next hour, run again in 1 hr (3,600,000 ms)
            initialDelay, 3600000, TimeUnit.MILLISECONDS
        )
    }

    private fun fetchData() {
        lifecycleScope.launch {
            _progressRefresh.visibility = View.VISIBLE
            _txtRefresh.visibility = View.VISIBLE

            // hide previous predicted weather forecasts
            val currView = findViewById<ViewGroup>(R.id.view_predicted_weather).getChildAt(0) as ViewGroup
            currView.visibility = View.INVISIBLE

            // fetch the data
            _weatherViewModel.fetch(this@MainActivity)

            _weatherViewModel.weathers.collect { data ->
                Log.v("MainActivity", "${data.first}")
                setupCurrentWeatherView(true)
                updateCurrentWeatherUI(data.first)
                updatePredictedWeatherUI(data.second)
                _progressRefresh.visibility = View.INVISIBLE
                _txtRefresh.visibility = View.INVISIBLE
            }

        }
    }

    /* =============================
              SETUP
    ============================= */
    private fun setup() {
        _weatherViewModel = WeatherViewModel()

        _btnSettings = findViewById(R.id.button_settings)
        _btnRefresh = findViewById(R.id.button_refresh)
        _progressRefresh = findViewById(R.id.progress_refresh)
        _txtRefresh = findViewById(R.id.text_refresh)

        // make these invisible by default
        _progressRefresh.visibility = View.INVISIBLE
        _txtRefresh.visibility = View.INVISIBLE


        // set button on-clicks
        _btnSettings.setOnClickListener {
            val i = Intent(this, SettingsActivity::class.java)
            startActivity(i)
        }

        _btnRefresh.setOnClickListener { // fetch data if user clicks the refresh button
            fetchData()
        }

//        // add self as observer
//        _weatherViewModel.weathers.observe(this) { data ->
//            Log.v("MainActivity", "$data.first")
//            setupCurrentWeatherView(true)
//            updateCurrentWeatherUI(data.first)
//            updatePredictedWeatherUI(data.second)
//            _progressRefresh.visibility = View.INVISIBLE
//            _txtRefresh.visibility = View.INVISIBLE
//        }

        // inflate current weather view group
        setupCurrentWeatherView(false)
    }

    private fun setupCurrentWeatherView(alreadyInflatedView : Boolean) {
        // inflate with views
        var currWeatherView = findViewById<ViewGroup>(R.id.view_current_weather)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val useRawData = prefs.getBoolean("use_raw_data_key", _useRawData)

        if (useRawData != _useRawData) {
            _useRawData = useRawData
            currWeatherView.removeAllViews()
        } else if (alreadyInflatedView) {
            // leave if there's no change in the setting
            // and the view has already been inflated
            return
        }

        if (!_useRawData) { // (only if using city page weather realtime)
            // inflate activity_main
            val currWeather = layoutInflater.inflate(R.layout.citypageweather_data_layout, currWeatherView)
            currWeatherView = currWeather.findViewById(R.id.view)

            // add humidity
            var inflated = layoutInflater.inflate(R.layout.weather_icon_text, currWeatherView)
            // change humidity image
            var img = currWeatherView.getChildAt(0).findViewById<ImageView>(R.id.image_temp)
            img.setImageResource(R.drawable.humidity)
            (inflated.layoutParams as LinearLayout.LayoutParams).bottomMargin = 10

            // add wind speed
            inflated = layoutInflater.inflate(R.layout.weather_icon_text, currWeatherView)
            // change wind speed image
            img = currWeatherView.getChildAt(1).findViewById(R.id.image_temp)
            img.setImageResource(R.drawable.windy)
            (inflated.layoutParams as LinearLayout.LayoutParams).bottomMargin = 10
        } else { // (only if using swob realtime)
            // inflate main layout with the swob data layout
            val currWeather = layoutInflater.inflate(R.layout.swob_data_layout, currWeatherView)
            // inflate weather data layout with the subweathers
            val subweathersView = currWeather.findViewById<ViewGroup>(R.id.view_subweathers)
            repeat (3) {
                layoutInflater.inflate(R.layout.weather_icon_text, subweathersView)
            }
        }
    }

    /* =============================
              UPDATE UI
    ============================= */
    private fun updateCurrentWeatherUI(data: WeatherSnapshot) {
        var txt : TextView
        var img : ImageView

        // update text views that both XML layouts have

        // main weather
        txt = findViewById(R.id.text_main_weather)
        img = findViewById(R.id.image_main_weather)
        txt.text = data.weather

        // station or region name
        txt = findViewById(R.id.text_station_name)
        txt.text = data.stationName

        // air temperature
        txt = findViewById(R.id.text_air_temperature)
        txt.text = data.airTemp

        if (!_useRawData) {
            // main weather image
            // use local image files first, if one exists and was assigned
            // (since images from the provided url doesn't look as good)
            if (data.weatherImg != 0) {
                img.setImageResource(data.weatherImg)
            } else {
                img.load(data.weatherImgUrl) { // load image from url
                    placeholder(R.drawable.sunny)
                }
            }

            // get current weather's linear layout to update sub-info
            var currView = findViewById<ViewGroup>(R.id.view_current_weather).getChildAt(0) as ViewGroup
            currView = currView.findViewById(R.id.view)

            // update humidity
            var subView = currView.getChildAt(0)
            txt = subView.findViewById(R.id.text)
            // no need to change image for humidity
            txt.text = "${data.relHum}\n${getText(R.string.humidity)}"

            // update wind speed
            subView = currView.getChildAt(1)
            txt = subView.findViewById(R.id.text)
            // no need to change image for wind
            var str = data.windSpeed
            if (!data.windDir.isEmpty()) str += " ${data.windDir}"
            if (!data.windGust.isEmpty()) str += "\n${getText(R.string.gust_to)} ${data.windGust}"
            txt.text = str
        } else { /* (only if using swob realtime) */
            // main weather image
            img.setImageResource(data.weatherImg)

            // relative humidity
            txt = findViewById(R.id.text_relative_humidity)
            txt.text = "${getText(R.string.relative_humidity)}: " + data.relHum

            // wind speed
            txt = findViewById(R.id.text_wind_speed)
            if (data.windSpeed.isEmpty()) {
                txt.text = "${getText(R.string.wind_speed)}: ${getText(R.string.calm)}"
            } else {
                var str = "${getText(R.string.wind_speed)}: ${data.windSpeed}"
                if (!data.windDir.isEmpty()) str += " (${data.windDir})"
                if (!data.windGust.isEmpty()) str += "\n${getText(R.string.gusting_to)} ${data.windGust}"
                txt.text = str
            }

            // 1) update the 3 subweathers
            var currView = findViewById<ViewGroup>(R.id.view_current_weather) // get current weather view group
            currView = currView.findViewById(R.id.view_subweathers) // get the 3 subweathers

            // 1.1) rainy/snowy
            var currSubweather = currView.getChildAt(0)
            img = currSubweather.findViewById(R.id.image_temp)
            txt = currSubweather.findViewById(R.id.text)
            img.setImageResource(data.pcpnImg)
            txt.text = data.pcpn

            // 1.2) cloudy
            currSubweather = currView.getChildAt(1)
            img = currSubweather.findViewById(R.id.image_temp)
            txt = currSubweather.findViewById(R.id.text)
            img.setImageResource(data.cloudAmtImg)
            txt.text = data.cloudAmt

            // 1.3) fog
            currSubweather = currView.getChildAt(2)
            img = currSubweather.findViewById(R.id.image_temp)
            txt = currSubweather.findViewById(R.id.text)
            img.setImageResource(data.visImg)
            txt.text = data.vis
        }

        Log.d("MainActivity", "Done updating current weather UI")
    }

    private fun updatePredictedWeatherUI(dataList: List<WeatherSnapshot>) {
        // get the linear layout inside the horizontal scroll view
        val currView = findViewById<ViewGroup>(R.id.view_predicted_weather).getChildAt(0) as ViewGroup

        currView.removeAllViews()
        currView.visibility = View.VISIBLE

        for (data in dataList) {
            val forecast = layoutInflater.inflate(R.layout.forecast_horizontal, currView, false)

            var txt : TextView = forecast.findViewById(R.id.time)
            txt.text = data.timestamp

            txt = forecast.findViewById(R.id.weather)
            txt.text = data.weather

            val img : ImageView = forecast.findViewById(R.id.image_temp)
            if (data.weatherImg != 0) {
                img.setImageResource(data.weatherImg)
            } else {
                img.load(data.weatherImgUrl) { // load image from url
                    placeholder(R.drawable.sunny)
                }
            }
            txt = forecast.findViewById(R.id.temp)
            txt.text = data.airTemp

            txt = forecast.findViewById(R.id.wind)
            txt.text = data.windSpeed

            val params = forecast.layoutParams as LinearLayout.LayoutParams
            params.weight = 1f
            params.bottomMargin = 8
            forecast.layoutParams = params

            currView.addView(forecast)
        }

        Log.d("MainActivity", "Done updating predicted weather UI")
    }
}