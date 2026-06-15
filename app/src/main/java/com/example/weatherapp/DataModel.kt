package com.example.weatherapp

import com.google.gson.annotations.SerializedName

data class DataModel( // root
    val type: String,
    val features: List<Feature>,
    val numberMatched: Long,
    val numberReturned: Long,
    val links: List<Link>,
    val timeStamp: String,
)

data class Feature(
    val id: String,
    val type: String,
    val geometry: Geometry,
    val properties: Properties,
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>, // x,y,z coordinates
)

data class Link(
    val type: String,
    val rel: String,
    val title: String,
    val href: String,
):java.io.Serializable

//@Serializable
data class Properties(
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
    @SerializedName("rel_hum-uom") var relativeHumidityUnit : String,

    @SerializedName("stn_pres") var stationPressure : Float,
    @SerializedName("stn_pres-uom") var stationPressureUnit : String,

    @SerializedName("snw_dpth") var snowDepth : Long,
    @SerializedName("snw_dpth-uom") var snowDepthUnit : String,
):java.io.Serializable