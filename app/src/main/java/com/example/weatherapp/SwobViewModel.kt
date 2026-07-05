package com.example.weatherapp

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.*
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class SwobData {
    var stationName : String = ""
    var airTemp : String = ""
    var relHum : String = ""

    var windSpeed : String = ""
    var windDir : String = ""
    var windGust : String = ""

    var pcpn : String = ""
    var pcpnImg : Int = 0

    var cloudAmt : String = ""
    var cloudAmtImg : Int = 0

    var vis : String = ""
    var visImg : Int = 0
}

class SwobViewModel : ViewModel {
        companion object {
        // how many hours before the current time to get info from
        // if the property is missing
        const val HR_BEFORE : Long = 12
        // max # of entries to search, in case there's more than
        // n entries in the last HR_BEFORE hours
        const val NUM_ENTRIES_TO_SEARCH = 50
    }
    private val _mutableLiveData = MutableLiveData<SwobData>()
    val data : LiveData<SwobData> get() = _mutableLiveData

    constructor() { }

    fun fetch(m: MainActivity) {
        Log.d("INFO", "Fetching SWOB API data")
        viewModelScope.launch {
            val dt = OffsetDateTime.now()
            val dtBefore = dt.minusHours(HR_BEFORE)
            // get entries up to n hours before
            ApiCall().getApiData(m, dt, dtBefore) { apiData ->
                try {
                    val dataEntries = getDataEntries(apiData.features)
                    _mutableLiveData.postValue(dataEntries)
                    Log.d("INFO", "Finished fetching SWOB API data")
                } catch (e: Exception) {
                    Log.d("ERROR", "Exception occurred while fetching SWOB API data: $e")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getDataEntries(features: List<SwobFeature>) : SwobData {
        val data = SwobData()
        var properties = features.first().properties
        val isSnowy : Boolean = properties.airTemp <= 0

        var windSpeed : Float = 0f
        var windSpeedUnit : String? = "" // in km/h
        var windDir : Long = 0
        var windDirUnit : String? = "" // in °
        var windGust : Float = 0f
        var windGustUnit : String? = "" // in km/h
        var pcpnAmt : Float = 0f
        var pcpnUnit : String? = "" // in mm
        var cloudAmt : Long = 0
        var cloudAmtUnit : String? = "" // in %
        var vis : Float = 0f
        var visUnit : String? = "" // in km

        // these are always present in all entries
        data.stationName = properties.stnNamValue
        data.airTemp = properties.airTemp.toString() + properties.airTempUom
        data.relHum = properties.relHum.toString() + properties.relHumUom

        // if any unit is missing, get the last n entries or past x hours, whichever is closer
        var i : Int = 1 // already checked the first property, go to the next
        while (i < NUM_ENTRIES_TO_SEARCH) {
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

        if (!pcpnUnit.isNullOrEmpty()) {
            data.pcpn = "$pcpnAmt$pcpnUnit"
            if (pcpnAmt <= 0) { // 0 mm
                data.pcpnImg = R.drawable.sunny
            } else if (pcpnAmt <= 2.5) {
                if (isSnowy) data.pcpnImg = R.drawable.snow_light
                else data.pcpnImg = R.drawable.rain_light
            } else if (pcpnAmt <= 10) {
                if (isSnowy) data.pcpnImg = R.drawable.snow
                else data.pcpnImg = R.drawable.rain
            } else {
                if (isSnowy) data.pcpnImg = R.drawable.snow_heavy
                else data.pcpnImg = R.drawable.rain_heavy
            }
        } else data.pcpnImg = R.drawable.sunny

        if (!cloudAmtUnit.isNullOrEmpty()) {
            data.cloudAmt = "$cloudAmt$cloudAmtUnit"
            if (cloudAmt <= 30) data.cloudAmtImg = R.drawable.sunny // 30% clouds
            else if (cloudAmt <= 60) data.cloudAmtImg = R.drawable.partly_cloudy
            else data.cloudAmtImg = R.drawable.cloudy
        } else data.cloudAmtImg = R.drawable.sunny

        if (!visUnit.isNullOrEmpty()) {
            data.vis = "$vis$visUnit"
            if (vis < 1) data.visImg = R.drawable.fog // visibility is <1km
            else if (vis < 10) data.visImg = R.drawable.cloudy // <10km
            else if (vis < 30) data.visImg = R.drawable.cloudy_s_sunny
            else data.visImg = R.drawable.sunny
        } else data.visImg = R.drawable.sunny

        return data
    }
}