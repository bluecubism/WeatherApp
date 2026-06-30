package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.math.max
import java.time.OffsetDateTime

class MainActivity : ComponentActivity() {
    companion object {
        // how many hours before the current time to get info from
        // if the property is missing
        const val HR_BEFORE : Long = 48
        // max # of entries to search in case there's more than
        // n entries in the last HR_BEFORE hours
        const val NUM_ENTRIES_TO_SEARCH = 100
    }
    private lateinit var btnRefresh : Button
    private lateinit var progressRefresh : ProgressBar
    private lateinit var txtRefresh : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // fetch data on start up
        btnRefresh = findViewById(R.id.button_refresh)
        progressRefresh = findViewById(R.id.progress_refresh)
        txtRefresh = findViewById(R.id.text_refresh)
        initializeSwob()
        fetchData()

        // fetch data again if user clicks the refresh button
        btnRefresh.setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        progressRefresh.visibility = View.VISIBLE
        txtRefresh.visibility = View.VISIBLE

        fetchSwob()

        progressRefresh.visibility = View.INVISIBLE
        txtRefresh.visibility = View.INVISIBLE
    }

    // =====================
    //  ====== SWOB ======
    // =====================
    private lateinit var txtTimestamp : TextView
    private lateinit var txtStnName : TextView
    private lateinit var txtAirTemp : TextView
    private lateinit var txtMainWeather : TextView
    private lateinit var imgMainWeather : ImageView
    private lateinit var txtRelHum: TextView
    private lateinit var txtWindSpeed: TextView
    private lateinit var precipitation : View
    private lateinit var fogVisibility : View
    private lateinit var cloudAmount : View

    private fun initializeSwob() {
        txtTimestamp = findViewById(R.id.text_timestamp)
        txtStnName = findViewById(R.id.text_station_name)
        txtAirTemp = findViewById(R.id.text_air_temperature)

        txtMainWeather = findViewById(R.id.text_main_weather)
        imgMainWeather = findViewById(R.id.image_main_weather)

        txtRelHum = findViewById(R.id.text_relative_humidity)
        txtWindSpeed = findViewById(R.id.text_wind_speed)

        // inflate info layout with views
        val infoLayout : LinearLayout = findViewById(R.id.layout_info)

        // add precipitation (no rain/light rain/rainy/heavy rain + snow variants) to info layout
        precipitation = layoutInflater.inflate(R.layout.weather_icon_text, null)
        var text : TextView = precipitation.findViewById(R.id.text)
        text.setTextColor(getColor(R.color.white))
        infoLayout.addView(precipitation)

        // add cloud amount (no clouds/partly cloudy/cloudy) to info layout
        cloudAmount = layoutInflater.inflate(R.layout.weather_icon_text, null)
        text = cloudAmount.findViewById(R.id.text)
        text.setTextColor(getColor(R.color.white))
        infoLayout.addView(cloudAmount)

        // add visibility (no fog/slightly foggy/mostly foggy/foggy) to info layout
        fogVisibility = layoutInflater.inflate(R.layout.weather_icon_text, null)
        text = fogVisibility.findViewById(R.id.text)
        text.setTextColor(getColor(R.color.white))
        infoLayout.addView(fogVisibility)
    }

    @SuppressLint("SetTextI18n")
    private fun fetchSwob() {
        ApiCall().getSwobRealtime(this) { data ->
            try {
                val properties = data.features.first().properties

                txtTimestamp.text = data.timeStamp
                txtStnName.text = properties.stationName
                txtAirTemp.text = properties.airTemp.toString() + properties.airTempUnit

                txtRelHum.text = "Relative Humidity: " + properties.relativeHumidity +
                        properties.relativeHumidityUnit // in %
                updateWindSpeed(data.features)

                updatePrecipitation(data.features)
                updateCloudAmount(data.features)
                updateVisibility(data.features)
                updateMainWeather()
            } catch (e: Exception) {
                txtTimestamp.text = e.toString()
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateWindSpeed(features: List<SwobFeature>) {
        var windStr : String

        var windSpeed : Float = 0f
        var windSpeedUnit : String? = "" // in km/h

        var windDir : Long = 0
        var windDirUnit : String? = "" // in °

        var windGust : Float = 0f
        var windGustUnit : String? = "" // in km/h

        // if any unit is missing, get the last n entries or past x hours, whichever is closer
        var i : Int = 0
        val pastDatetime = OffsetDateTime.now().minusHours(HR_BEFORE)
        while (i < NUM_ENTRIES_TO_SEARCH) {
            val properties : SwobProperties = features[i].properties
            if (windSpeedUnit.isNullOrEmpty()) {
                windSpeed = properties.avgWindSpeedPst10mts
                windSpeedUnit = properties.avgWindSpeedPst10mtsUnit
            }
            if (windDirUnit.isNullOrEmpty()) {
                windDir = properties.avgWindDir10mPst10mts
                windDirUnit = properties.avgWindDir10mPst10mtsUnit
            }

            if (windGustUnit.isNullOrEmpty()) {
                windGust = properties.avgWindGust10mPst10mts
                windGustUnit = properties.avgWindGust10mPst10mtsUnit
            }
            // if all info has been gathered, break
            if (!windSpeedUnit.isNullOrEmpty() &&
                !windDirUnit.isNullOrEmpty() &&
                !windGustUnit.isNullOrEmpty()) {
                break
            }
            // if the property's datetime is past x hours from now, break
            val prDatetime = OffsetDateTime.parse(properties.dateTime)
            if (pastDatetime > prDatetime) {
                break
            }
            i++
        }

        // update wind speed & gust
        if (windSpeedUnit.isNullOrEmpty()) {
            windStr = "Wind Speed: Calm"
        } else {
            windStr = if (windDirUnit.isNullOrEmpty()) {
                "Wind Speed: $windSpeed$windSpeedUnit"
            } else {
                "Wind Speed: $windSpeed$windSpeedUnit ($windDir$windDirUnit)"
            }

            if (!windGustUnit.isNullOrEmpty()) {
                windStr += "\nGusting to $windGust$windGustUnit"
            }
        }

        txtWindSpeed.text = windStr
    }

    @SuppressLint("SetTextI18n")
    private fun updatePrecipitation(features: List<SwobFeature>) {
        val txt : TextView = precipitation.findViewById(R.id.text)
        val img : ImageView = precipitation.findViewById(R.id.image)
        var properties : SwobProperties = features.first().properties

        val isSnowy : Boolean = properties.airTemp <= 0 // airTemp is always available in properties
        var pcpnAmt : Float = 0f
        var pcpnUnit : String? = "" // in mm

        // if any unit is missing, get the last n entries or past x hours, whichever is closer
        var i : Int = 0
        val pastDatetime = OffsetDateTime.now().minusHours(HR_BEFORE)
        while (i < NUM_ENTRIES_TO_SEARCH) {
            properties = features[i].properties
            if (pcpnUnit.isNullOrEmpty()) {
                pcpnAmt = properties.pcpnAmtPst10mts
                pcpnUnit = properties.pcpnAmtPst10mtsUnit
            }
            // if it's still null/empty, get it in past 1 hr rather than past 10 mins
            if (pcpnUnit.isNullOrEmpty()) {
                pcpnAmt = properties.pcpnAmtPst1hr
                pcpnUnit = properties.pcpnAmtPst1hrUnit
            }

            // if all info has been gathered, break
            if (!pcpnUnit.isNullOrEmpty()) {
                break
            }
            // if the property's datetime is past x hours from now, break
            val prDatetime = OffsetDateTime.parse(properties.dateTime)
            if (pastDatetime > prDatetime) {
                break
            }
            i++
        }

        // update precipitation
        if (pcpnAmt <= 0) { // 0 mm
            txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.no_rain)
            img.setImageResource(R.drawable.sunny)
        } else if (pcpnAmt < 2.5) { // 2.5 mm
            if (isSnowy) {
                txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.light_snow)
                img.setImageResource(R.drawable.snow_light)
            } else {
                txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.light_rain)
                img.setImageResource(R.drawable.rain_light)
            }
        } else if (pcpnAmt < 10) {
            if (isSnowy) {
                txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.snowy)
                img.setImageResource(R.drawable.snow)
            } else {
                txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.rainy)
                img.setImageResource(R.drawable.rain)
            }
        } else {
            if (isSnowy) {
                txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.heavy_snow)
                img.setImageResource(R.drawable.snow_heavy)
            } else {
                txt.text = "$pcpnAmt$pcpnUnit\n" + resources.getText(R.string.heavy_rain)
                img.setImageResource(R.drawable.rain_heavy)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateCloudAmount(features: List<SwobFeature>) {
        // states for this app: sunny, partly cloudy, cloudy
        val txt : TextView = cloudAmount.findViewById(R.id.text)
        val img : ImageView = cloudAmount.findViewById(R.id.image)

        var cloudAmt : Long = 0
        var cloudUnit : String? = "" // in %

        // if any unit is missing, get the last n entries or past x hours, whichever is closer
        var i : Int = 0
        val pastDatetime = OffsetDateTime.now().minusHours(HR_BEFORE)
        while (i < NUM_ENTRIES_TO_SEARCH) {
            val properties = features[i].properties
            if (cloudUnit.isNullOrEmpty()) {
                cloudAmt = properties.totCldAmt
                cloudUnit = properties.totCldAmtUnit

                // if all info has been gathered, break
                if (!cloudUnit.isNullOrEmpty()) {
                    break
                }
            }
            // if the property's datetime is past x hours from now, break
            val prDatetime = OffsetDateTime.parse(properties.dateTime)
            if (pastDatetime > prDatetime) {
                break
            }
            i++
        }

        // update cloud amount
        if (cloudUnit.isNullOrEmpty()) {
            img.setImageResource(R.drawable.sunny)
            txt.text = resources.getText(R.string.no_clouds)
        } else if (cloudAmt <= 30) { // 30% clouds
            txt.text = "$cloudAmt$cloudUnit\n" + resources.getText(R.string.no_clouds)
            img.setImageResource(R.drawable.sunny)
        } else if (cloudAmt <= 60) { // 60% clouds
            txt.text = "$cloudAmt$cloudUnit\n"  + resources.getText(R.string.partly_cloudy)
            img.setImageResource(R.drawable.partly_cloudy)
        } else {
            txt.text = "$cloudAmt$cloudUnit\n"  + resources.getText(R.string.cloudy)
            img.setImageResource(R.drawable.cloudy)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateVisibility(features: List<SwobFeature>) {
        val txt : TextView = fogVisibility.findViewById(R.id.text)
        val img : ImageView = fogVisibility.findViewById(R.id.image)
        var vis : Float = 0f
        var visUnit : String? = "" // in km

        // if any unit is missing, get the last n entries or past x hours, whichever is closer
        var i : Int = 0
        val pastDatetime = OffsetDateTime.now().minusHours(HR_BEFORE)
        while (i < NUM_ENTRIES_TO_SEARCH) {
            val properties = features[i].properties
            if (visUnit.isNullOrEmpty()) {
                vis = properties.vis
                visUnit = properties.visUnit
            }
            // if still empty, gather info from a different property
            if (visUnit.isNullOrEmpty()) {
                vis = properties.avgVisPst10mts
                visUnit = properties.avgVisPst10mtsUnit
            }
            // if all info has been gathered, break
            if (!visUnit.isNullOrEmpty()) {
                break
            }
            // if the property's datetime is past x hours from now, break
            val prDatetime = OffsetDateTime.parse(properties.dateTime)
            if (pastDatetime > prDatetime) {
                break
            }
            i++
        }

        // update visibility
        if (visUnit.isNullOrEmpty()) {
            txt.text = resources.getText(R.string.no_fog)
            img.setImageResource(R.drawable.sunny)
        } else if (vis < 1) { // if visibility is <1km, then it's foggy
            txt.text = "$vis$visUnit\n" + resources.getText(R.string.foggy)
            img.setImageResource(R.drawable.fog)
        } else if (vis < 10) { //<10km, slightly foggy
            txt.text = "$vis$visUnit\n" + resources.getText(R.string.mostly_foggy)
            img.setImageResource(R.drawable.cloudy)
        } else if (vis < 30) {
            txt.text = "$vis$visUnit\n" + resources.getText(R.string.slightly_foggy)
            img.setImageResource(R.drawable.cloudy_s_sunny)
        } else {
            txt.text = "$vis$visUnit\n" + resources.getText(R.string.no_fog)
            img.setImageResource(R.drawable.sunny)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateMainWeather() {
        val pcpnStatus : String
        val cloudStatus : String
        val fogStatus : String
        val textToUse : String
        val imageToUse : Int

        var txt : TextView = precipitation.findViewById(R.id.text)
        pcpnStatus = txt.text.substring(max(txt.text.indexOf("\n"),0), txt.text.length) // get all characters after \n

        txt = cloudAmount.findViewById(R.id.text)
        cloudStatus = txt.text.substring(max(txt.text.indexOf("\n"),0), txt.text.length)

        txt = fogVisibility.findViewById(R.id.text)
        fogStatus = txt.text.substring(max(txt.text.indexOf("\n"),0), txt.text.length)

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

        when (pcpnStatus) {
            resources.getText(R.string.heavy_snow) -> {
                textToUse = pcpnStatus
                imageToUse = R.drawable.snow_heavy
            }
            resources.getText(R.string.heavy_rain) -> {
                textToUse = pcpnStatus
                imageToUse = R.drawable.rain_heavy
            }

            resources.getText(R.string.snowy) -> {
                textToUse = pcpnStatus
                imageToUse = if (cloudStatus == resources.getText(R.string.cloudy)) {
                    R.drawable.snow
                } else {
                    R.drawable.snow_s_cloudy
                }
            }
            resources.getText(R.string.rainy) -> {
                textToUse = pcpnStatus
                imageToUse = if (cloudStatus == resources.getText(R.string.cloudy)) {
                    R.drawable.rain
                } else {
                    R.drawable.rain_s_cloudy
                }
            }

            resources.getText(R.string.light_snow) -> {
                textToUse = pcpnStatus
                imageToUse = R.drawable.snow_light
            }
            resources.getText(R.string.light_rain) -> {
                textToUse = pcpnStatus
                imageToUse = when (cloudStatus) {
                    resources.getText(R.string.cloudy) -> {
                        R.drawable.rain_light
                    }
                    resources.getText(R.string.partly_cloudy) -> {
                        R.drawable.cloudy_s_rain
                    }
                    else -> {
                        R.drawable.sunny_s_rain
                    }
                }
            }

            else -> {
                if (fogStatus != resources.getText(R.string.no_fog)) {
                    textToUse = fogStatus
                    imageToUse = R.drawable.fog
                } else if (cloudStatus == resources.getText(R.string.cloudy)) {
                    textToUse = cloudStatus
                    imageToUse = R.drawable.cloudy
                } else if (cloudStatus == resources.getText(R.string.partly_cloudy)) {
                    textToUse = cloudStatus
                    imageToUse = R.drawable.partly_cloudy
                } else {
                    textToUse = resources.getText(R.string.sunny).toString()
                    imageToUse = R.drawable.sunny
                }
            }
        }

        txtMainWeather.text = textToUse
        imgMainWeather.setImageResource(imageToUse)
    }

    // =====================
    //  ====== HRDPS ======
    // =====================
}