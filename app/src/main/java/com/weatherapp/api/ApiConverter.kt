package com.weatherapp.api

import android.content.Context
import com.weatherapp.R
import com.weatherapp.api.apidata.*
import com.weatherapp.snapshot.*
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.first

class ApiConverter {

    /**
     * Convert the current weather data (CityPageWeather Realtime).
     */
    fun CPWRealtimeToWeatherSnapshot(
        context: Context,
        features: List<CityPageWeatherRealtimeApiData.Feature>)
        : WeatherSnapshot
    {
        val data = WeatherSnapshot()
        val properties = features.first().properties
        val conds = properties.currentConditions

        data.stationName = properties.region.en // use region name
        data.airTemp = "${conds.temperature.value.en}°${conds.temperature.units.en}"
        data.relHum = "${conds.relativeHumidity?.value?.en}${conds.relativeHumidity?.units?.en}"

        val windSpeed = conds.wind?.speed?.value?.en.toString() // wind speed can be a number or "calm"
        if (windSpeed.all { c -> c.isDigit() || c == '.' }) { // if wind speed is a number
            data.windSpeed = "$windSpeed${conds.wind?.speed?.units?.en}" // add km/h at the end
            // add wind direction (i.e. E)
            if (!conds.wind?.direction?.windDirFull?.en.isNullOrEmpty()) {
                data.windDir = conds.wind.direction.windDirFull.en
            }
            else if (!conds.wind?.direction?.value?.en.isNullOrEmpty()) {
                data.windDir = conds.wind.direction.value.en
            }
        } else { // if wind speed is a string, capitalize the first letter
            data.windSpeed = windSpeed.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                else it.toString()
            }
        }

        // add wind gust
        if (!conds.wind?.gust?.units?.en.isNullOrEmpty()) {
            data.windGust = "${conds.wind.gust.value.en}${conds.wind.gust.units.en}"
        }

        // use a locally stored image if possible, and load the image from the url if not
        data.weatherImg = CPWRealtimeIconCodeToDrawable(conds.iconCode.value)
        if (!conds.iconCode.url.isNullOrEmpty()) { data.weatherImgUrl = conds.iconCode.url }

        // get the text to display from the icon code or condition
        if (!conds.condition?.en.isNullOrEmpty()) { data.weather = conds.condition.en }
        else { // get weather from the drawable
            when (data.weatherImg) {
                R.drawable.sunny
                    -> data.weather = "${context.getText(R.string.sunny)}"

                R.drawable.sunny_s_cloudy,
                R.drawable.partly_cloudy,
                R.drawable.cloudy_s_sunny
                    -> data.weather = "${context.getText(R.string.partly_cloudy)}"

                R.drawable.rain_light
                    -> data.weather = "${context.getText(R.string.light_rain)}"
                R.drawable.rain
                    -> data.weather = "${context.getText(R.string.rainy)}"
                R.drawable.rain_heavy
                    -> data.weather = "${context.getText(R.string.heavy_rain)}"

                R.drawable.snow_light
                    -> data.weather = "${context.getText(R.string.light_snow)}"
                R.drawable.snow
                    -> data.weather = "${context.getText(R.string.snowy)}"
                R.drawable.snow_heavy
                    -> data.weather = "${context.getText(R.string.heavy_snow)}"

                R.drawable.thunderstorms
                    -> data.weather = "${context.getText(R.string.thunder)}"

                else -> data.weather = "${context.getText(R.string.clear)}"
            }
        }

        return data
    }

    /**
     * Convert the forecasted weather data (CityPageWeather Realtime).
     */
    fun CPWRealtimeHourlyForecastToWeatherSnapshot(
        features: List<CityPageWeatherRealtimeApiData.Feature>)
        : List<WeatherSnapshot>
    {
        val data = mutableListOf<WeatherSnapshot>()
        val hourlyForecasts = features.first().properties.hourlyForecastGroup.hourlyForecasts

        for (forecast in hourlyForecasts) {
            val currData = WeatherSnapshot()

            val zoneId = ZonedDateTime.now().zone // ex. get zone id of "America/New_York"
            val timestamp = OffsetDateTime.parse(forecast.timestamp).atZoneSameInstant(zoneId)
            val timeFormat = DateTimeFormatter.ofPattern("h:mma") // ex. display time as 3:00PM or 10:00PM
            currData.timestamp = timeFormat.format(timestamp)

            currData.weather = forecast.condition.en
            currData.weatherImg = CPWRealtimeIconCodeToDrawable(forecast.iconCode.value)
            if (!forecast.iconCode.url.isNullOrEmpty()) { currData.weatherImgUrl = forecast.iconCode.url }

            currData.airTemp = "${forecast.temperature.value.en}°${forecast.temperature.units.en}"

            currData.windSpeed = "${forecast.wind.speed.value.en}${forecast.wind.speed.units.en}"
            if (!forecast.wind.direction?.windDirFull?.en.isNullOrEmpty()) {
                currData.windDir = forecast.wind.direction.windDirFull.en
            }
            else if (!forecast.wind.direction?.value?.en.isNullOrEmpty()) {
                currData.windDir = forecast.wind.direction.value.en
            }

            data.add(currData)
        }

        return data
    }

    /**
     * Convert the current raw weather data (SWOB Realtime).
     */
    fun SwobRealtimeToWeatherSnapshot(
        context: Context,
        features: List<SwobRealtimeApiData.Feature>)
        : WeatherSnapshot
    {
        val data = WeatherSnapshot()

        var properties = features.first().properties
        val isSnowy : Boolean = properties.airTemp <= 0

        var windSpeed = 0f
        var windSpeedUnit : String? = "" // in km/h
        var windDir : Long = 0
        var windDirUnit : String? = "" // in °
        var windGust = 0f
        var windGustUnit : String? = "" // in km/h
        var pcpnAmt = 0f
        var pcpnUnit : String? = "" // in mm
        var cloudAmt : Long = 0
        var cloudAmtUnit : String? = "" // in %
        var vis = 0f
        var visUnit : String? = "" // in km

        // these are always present in all entries
        data.stationName = properties.stnNamValue
        data.airTemp = properties.airTemp.toString() + properties.airTempUom
        data.relHum = properties.relHum.toString() + properties.relHumUom

        // if any unit is missing, get the last n entries or past x hours, whichever is closer
        var i = 1 // already checked the first property, go to the next
        while (i < features.size) {
            var allEntriesNonEmpty = true
            properties = features[i].properties

            // if an entry is missing, fill it

            // wind speed/direction/gust
            if (windSpeedUnit.isNullOrEmpty()) {
                windSpeed = properties.avgWndSpdPcpnGagPst10mts
                windSpeedUnit = properties.avgWndSpdPcpnGagPst10mtsUom

                if (windSpeedUnit.isNullOrEmpty()) {
                    allEntriesNonEmpty = false
                } else {
                    // wind direction & wind gust should match with the wind speed reported
                    // else you can get a wind speed of 1.5km/h and gust of 0.0km/h
                    windDir = properties.avgWndDir10mPst10mts
                    windDirUnit = properties.avgWndDir10mPst10mtsUom
                    windGust = properties.maxWndGstSpd10mPst10mts
                    windGustUnit = properties.maxWndGstSpd10mPst10mtsUom
                }
            }

            // precipitation
            if (pcpnUnit.isNullOrEmpty()) {
                pcpnAmt = properties.pcpnAmtPst10mts
                pcpnUnit = properties.pcpnAmtPst10mtsUom
                if (pcpnUnit.isNullOrEmpty()) allEntriesNonEmpty = false
            }

            // cloud amount
            if (cloudAmtUnit.isNullOrEmpty()) {
                cloudAmt = properties.totCldAmt
                cloudAmtUnit = properties.totCldAmtUom
                if (cloudAmtUnit.isNullOrEmpty()) allEntriesNonEmpty = false
            }

            // fog
            if (visUnit.isNullOrEmpty()) {
                vis = properties.vis
                visUnit = properties.visUom
                if (visUnit.isNullOrEmpty()) allEntriesNonEmpty = false
            }

            // if all entries are full, break
            if (allEntriesNonEmpty) {
                break
            }

            i++
        }

        if (!windSpeedUnit.isNullOrEmpty()) data.windSpeed = "$windSpeed$windSpeedUnit"
        if (!windDirUnit.isNullOrEmpty()) data.windDir = "$windDir$windDirUnit"
        if (!windGustUnit.isNullOrEmpty()) data.windGust = "$windGust$windGustUnit"

        // update precipitation
        var pcpnText : String // used later to decide default text for main weather
        if (!pcpnUnit.isNullOrEmpty()) {
            data.pcpn = "$pcpnAmt$pcpnUnit"
            if (pcpnAmt <= 0) { // 0 mm
                data.pcpnImg = R.drawable.sunny
                pcpnText = context.getText(R.string.no_rain).toString()
            } else if (pcpnAmt <= 2.5) {
                if (isSnowy) {
                    data.pcpnImg = R.drawable.snow_light
                    pcpnText = "${context.getText(R.string.light_snow)}"
                }
                else {
                    data.pcpnImg = R.drawable.rain_light
                    pcpnText = "${context.getText(R.string.light_rain)}"
                }
            } else if (pcpnAmt <= 10) {
                if (isSnowy) {
                    data.pcpnImg = R.drawable.snow
                    pcpnText = "${context.getText(R.string.snowy)}"
                }
                else {
                    data.pcpnImg = R.drawable.rain
                    pcpnText = "${context.getText(R.string.rainy)}"
                }
            } else {
                if (isSnowy) {
                    data.pcpnImg = R.drawable.snow_heavy
                    pcpnText = "${context.getText(R.string.heavy_snow)}"
                }
                else {
                    data.pcpnImg = R.drawable.rain_heavy
                    pcpnText = "${context.getText(R.string.heavy_rain)}"
                }
            }
        } else {
            data.pcpnImg = R.drawable.sunny
            pcpnText = "${context.getText(R.string.no_rain)}"
        }
        data.pcpn += "\n$pcpnText"

        // update cloudiness
        if (!cloudAmtUnit.isNullOrEmpty()) {
            data.cloudAmt = "$cloudAmt$cloudAmtUnit"
            if (cloudAmt <= 30) { // 30% clouds
                data.cloudAmtImg = R.drawable.sunny
                data.cloudAmt += "\n${context.getText(R.string.no_clouds)}"
            }
            else if (cloudAmt <= 60) {
                data.cloudAmtImg = R.drawable.partly_cloudy
                data.cloudAmt += "\n${context.getText(R.string.partly_cloudy)}"
            }
            else {
                data.cloudAmtImg = R.drawable.cloudy
                data.cloudAmt += "\n${context.getText(R.string.cloudy)}"
            }
        } else {
            data.cloudAmtImg = R.drawable.sunny
            data.cloudAmt += "\n${context.getText(R.string.no_clouds)}"
        }

        // update fogginess
        var fogText : String // used later to decide the weather if precipitation status is sunny
        if (!visUnit.isNullOrEmpty()) {
            data.vis = "$vis$visUnit"
            if (vis < 1) { // visibility is <1km
                data.visImg = R.drawable.fog
                fogText = "${context.getText(R.string.foggy)}"
            }
            else if (vis < 10) { // 10km
                data.visImg = R.drawable.cloudy
                fogText = "${context.getText(R.string.mostly_foggy)}"
            }
            else if (vis < 30) {
                data.visImg = R.drawable.cloudy_s_sunny
                fogText = "${context.getText(R.string.slightly_foggy)}"
            }
            else {
                data.visImg = R.drawable.sunny
                fogText = "${context.getText(R.string.no_fog)}"
            }
        } else {
            data.visImg = R.drawable.sunny
            fogText = "${context.getText(R.string.no_fog)}"
        }
        data.vis += "\n$fogText"

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
                    mainWeatherText = context.getText(R.string.cloudy).toString()
                    mainWeatherImage = R.drawable.cloudy
                } else if (data.cloudAmtImg == R.drawable.partly_cloudy) {
                    mainWeatherText = context.getText(R.string.partly_cloudy).toString()
                    mainWeatherImage = R.drawable.partly_cloudy
                } else {
                    mainWeatherText = context.getText(R.string.sunny).toString()
                    mainWeatherImage = R.drawable.sunny
                }
            }
        }
        data.weather = mainWeatherText
        data.weatherImg = mainWeatherImage

        return data
    }

    /**
     * Get the locally-stored image to use based on the icon code.
     * Local images using the following:
     * https://eccc-msc.github.io/open-data/msc-data/citypage-weather/readme_citypageweather-datamart_en/
     */
    private fun CPWRealtimeIconCodeToDrawable(iconCode : Int) : Int {
        when (iconCode) {
            0 -> return R.drawable.sunny // Sunny
            1 -> return R.drawable.sunny_s_cloudy // Mainly Sunny
            2 -> return R.drawable.partly_cloudy // Partly Cloudy
            3 -> return R.drawable.cloudy_s_sunny // Mostly Cloudy
            6 -> return R.drawable.rain_light // Light Rain Shower
            7 -> return R.drawable.rain_s_snow // Light Rain Shower & Flurries
            8 -> return R.drawable.snow_light // Light Flurries
            10 -> return R.drawable.cloudy // Cloudy
            11, 12 -> return R.drawable.rain
            13 -> return R.drawable.rain_heavy
            16, 17 -> return R.drawable.snow
            18 -> return R.drawable.snow_heavy
            19 -> return R.drawable.thunderstorms
            24 -> return R.drawable.fog
            // and more icon codes
            else -> return 0
        }
    }
}