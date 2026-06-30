package com.example.weatherapp

import com.google.gson.annotations.SerializedName

// hourly forecast data

data class PrognosHrdpsData( // root
    val type: String,
    val features: List<HrdpsFeature>,
    val numberMatched: Long,
    val numberReturned: Long,
    val links: List<HrdpsLink>,
    val timeStamp: String,
)

data class HrdpsFeature(
    val id: String,
    val type: String,
    val geometry: HrdpsGeometry,
    val properties: HrdpsProperties,
)

data class HrdpsGeometry(
    val type: String,
    val coordinates: List<Double>, // x,y,z coordinates
)

data class HrdpsLink(
    val type: String,
    val rel: String,
    val title: String,
    val href: String,
)

data class HrdpsProperties(
    val id: String,
    @SerializedName("spp_system") val sppSystem: String,
    @SerializedName("nwep_system_en") val nwepSystemEn: String,
    @SerializedName("nwep_system_fr") val nwepSystemFr: String,
    @SerializedName("stat_method") val statMethod: String,
    @SerializedName("predictor_set") val predictorSet: String,
    @SerializedName("ade_id") val adeId: String,
    @SerializedName("station_network") val stationNetwork: String,
    @SerializedName("prognos_station_id") val prognosStationId: String,
    @SerializedName("reference_datetime") val referenceDatetime: String,

    // ex. 2026-06-12T23:00:00Z (forecasted for 2026-06-12 23:00 UTC = 19:00 EDT = 7PM local time)
    @SerializedName("forecast_datetime") val forecastDatetime: String,

    @SerializedName("forecast_leadtime") val forecastLeadtime: String,
    @SerializedName("vertical_coordinate")  val verticalCoordinate: String,
    val variable: String, // ex. AirTemp
    val unit: String, // ex. K
    @SerializedName("forecast_value") val forecastValue: Double // ex. 297.9 (Kelvin = 24.75C)
)