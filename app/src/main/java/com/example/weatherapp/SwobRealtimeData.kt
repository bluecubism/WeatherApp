package com.example.weatherapp

import com.google.gson.annotations.SerializedName

// real time data

data class SwobRealtimeData( // root
    val type: String,
    val features: List<SwobFeature>,
    val numberMatched: Long,
    val numberReturned: Long,
    val links: List<SwobLink>,
    val timeStamp: String,
)

data class SwobFeature(
    val id: String,
    val type: String,
    val geometry: SwobGeometry,
    val properties: SwobProperties,
)

data class SwobGeometry(
    val type: String,
    val coordinates: List<Double>, // x,y,z coordinates
)

data class SwobLink(
    val type: String,
    val rel: String,
    val title: String,
    val href: String,
)

data class SwobProperties(
    // uom = unit of measure
    @SerializedName("dataset") var dataset : String,
    @SerializedName("id") var id : String,
    @SerializedName("url") var url : String,

    @SerializedName("date_tm-value") var dateTime : String,
    @SerializedName("date_tm-uom") var dateTimeUnit : String,

    @SerializedName("stn_nam-value") var stationName : String,

    @SerializedName("air_temp") var airTemp : Float,
    @SerializedName("air_temp-uom") var airTempUnit : String,

    @SerializedName("rel_hum") var relativeHumidity : Long,
    @SerializedName("rel_hum-uom") var relativeHumidityUnit : String?,

//    @SerializedName("stn_pres") var stationPressure : Float,
//    @SerializedName("stn_pres-uom") var stationPressureUnit : String?,

//    @SerializedName("snw_dpth") var snowDepth : Long,
//    @SerializedName("snw_dpth-uom") var snowDepthUnit : String?,

    // average wind speed at precipitation gauge past 1 minute
    @SerializedName("avg_wnd_spd_pcpn_gag_pst1mt") var avgWindSpeedPst1mt : Float,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst1mt-uom") var avgWindSpeedPst1mtUnit : String?,

    // average wind speed at precipitation gauge past 10 minutes
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts") var avgWindSpeedPst10mts : Float,
    @SerializedName("avg_wnd_spd_pcpn_gag_pst10mts-uom") var avgWindSpeedPst10mtsUnit : String?,

    // precipitation amount past 10 minutes
    @SerializedName("pcpn_amt_pst10mts") var pcpnAmtPst10mts : Float,
    @SerializedName("pcpn_amt_pst10mts-uom") var pcpnAmtPst10mtsUnit : String?,

    // precipitation amount past 1 hour
    @SerializedName("pcpn_amt_pst1hr") var pcpnAmtPst1hr : Float,
    @SerializedName("pcpn_amt_pst1hr-uom") var pcpnAmtPst1hrUnit : String?,

    // average wind direction 10 meters past 10 minutes
    @SerializedName("avg_wnd_dir_10m_pst10mts") var avgWindDir10mPst10mts : Long,
    @SerializedName("avg_wnd_dir_10m_pst10mts-uom") var avgWindDir10mPst10mtsUnit : String?,

    // average wind gust (highest wind speed) 10 meters past 10 minutes
    @SerializedName("avg_wnd_gst_10m_pst10mts") var avgWindGust10mPst10mts : Float,
    @SerializedName("avg_wnd_gst_10m_pst10mts-uom") var avgWindGust10mPst10mtsUnit : String?,

    // visibility
    @SerializedName("vis") var vis : Float,
    @SerializedName("vis-uom") var visUnit : String?,

    // average visibility past 10 minutes
    @SerializedName("avg_vis_pst10mts") var avgVisPst10mts : Float,
    @SerializedName("avg_vis_pst10mts-uom") var avgVisPst10mtsUnit : String?,

    // total cloud amount (in %, ex. 75%)
    @SerializedName("tot_cld_amt") var totCldAmt : Long,
    @SerializedName("tot_cld_amt-uom") var totCldAmtUnit : String?,

    // present weather
//    @SerializedName("prsnt_wx_1") var presentWeather1 : Long,
//    @SerializedName("prsnt_wx_2") var presentWeather2 : Long,
//    @SerializedName("prsnt_wx_3") var presentWeather3 : Long,
):java.io.Serializable