package com.weatherapp.api.apidata

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PrognosHrdpsApiInterface {
    @GET("collections/prognos-hrdps-realtime/items")
    fun getApiData(
        @Query("datetime") datetime: String?,
        @Query("bbox") bbox: String?,
        @Query("f") f: String
    ): Call<PrognosHrdpsAPIData>
}

/**
 * Hourly forecast data
 */
data class PrognosHrdpsAPIData( // root
    val type: String,
    val features: List<Feature>,
    val numberMatched: Long,
    val numberReturned: Long,
    val links: List<Link>,
    val timeStamp: String,
) {

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
    )

    data class Properties(
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
        @SerializedName("vertical_coordinate") val verticalCoordinate: String,
        val variable: String, // ex. AirTemp
        val unit: String, // ex. K (Kelvin if variable is AirTemp)
        @SerializedName("forecast_value") val forecastValue: Double // ex. 297.9 (in Kelvin, converted to Celsius = 24.75C)
    )
}