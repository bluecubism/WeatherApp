package com.weatherapp.api.apidata

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// city page weather readme: https://eccc-msc.github.io/open-data/msc-data/citypage-weather/readme_citypageweather_en/
// public weather forecast guide: https://www.canada.ca/en/environment-climate-change/services/types-weather-forecasts-use/public/guide.html

interface CityPageWeatherRealtimeApiInterface {
    @GET("collections/citypageweather-realtime/items")
    fun getApiData(
        @Query("datetime") datetime: String?,
        @Query("bbox") bbox: String?,
        @Query("limit") limit: Int?,
        @Query("f") f: String
    ): Call<CityPageWeatherRealtimeApiData>
}

/**
 * Current real-time weather data (processed data)
 */
data class CityPageWeatherRealtimeApiData(
    val type: String,
    val features: List<Feature>,
    val numberMatched: Int,
    val numberReturned: Int,
    val links: List<Link>,
    val timeStamp: String
) {
    data class Feature(
        val id: String,
        val type: String,
        val geometry: Geometry,
        val properties: Properties
    )

    data class Link(
        val type: String,
        val rel: String,
        val title: String,
        val href: String
    )

    data class Geometry(
        val type: String,
        val coordinates: List<Double>
    )

    data class Properties(
        @SerializedName("lastUpdated") val lastUpdated: String,
        @SerializedName("identifier") val identifier: String,
        @SerializedName("name") val name: LocalizedText,
        @SerializedName("region") val region: LocalizedText,
        @SerializedName("url") val url: LocalizedText,
        @SerializedName("currentConditions") val currentConditions: CurrentConditions,
        @SerializedName("forecastGroup") val forecastGroup: ForecastGroup,
        @SerializedName("hourlyForecastGroup") val hourlyForecastGroup: HourlyForecastGroup
    )

    data class CurrentConditions(
        @SerializedName("iconCode") val iconCode: IconCode,
        @SerializedName("timestamp") val timestamp: LocalizedText,
        @SerializedName("relativeHumidity") val relativeHumidity: Measurement?,
        @SerializedName("wind") val wind: Wind?,
        @SerializedName("pressure") val pressure: Measurement?,
        @SerializedName("temperature") val temperature: Measurement,
        @SerializedName("dewpoint") val dewpoint: Measurement?,
        @SerializedName("station") val station: Station,
        @SerializedName("condition") val condition: LocalizedText?,
        @SerializedName("windChill") val windChill: Measurement?
    )

    data class ForecastGroup(
        @SerializedName("timestamp") val timestamp: LocalizedText?,
        @SerializedName("regionalNormals") val regionalNormals: RegionalNormals?,
        @SerializedName("forecasts") val forecasts: List<Forecast>?
    )

    data class RegionalNormals(
        @SerializedName("textSummary") val textSummary: LocalizedText?,
        @SerializedName("temperature") val temperature: List<Measurement>?
    )

    data class Forecast(
        @SerializedName("precipitation") val precipitation: Precipitation?,
        @SerializedName("uv") val uv: Uv?,
        @SerializedName("period") val period: Period? = null,
        @SerializedName("temperatures") val temperatures: Temperatures?,
        @SerializedName("winds") val winds: Winds? = null,
        @SerializedName("cloudPrecip") val cloudPrecip: LocalizedText?,
        @SerializedName("relativeHumidity") val relativeHumidity: Measurement?,
        @SerializedName("abbreviatedForecast") val abbreviatedForecast: AbbreviatedForecast?,
        @SerializedName("textSummary") val textSummary: LocalizedText?
    )

    data class Temperatures(
        @SerializedName("temperature") val temperature: List<Measurement>?,
        @SerializedName("textSummary") val textSummary: LocalizedText?
    )

    data class Winds(
        @SerializedName("periods") val periods: List<WindPeriod>?,
        @SerializedName("textSummary") val textSummary: LocalizedText?
    )

    data class WindPeriod(
        @SerializedName("bearing") val bearing: Measurement?,
        @SerializedName("index") val index: LocalizedValue?,
        @SerializedName("rank") val rank: LocalizedText?,
        @SerializedName("speed") val speed: Measurement?,
        @SerializedName("gust") val gust: Measurement?,
        @SerializedName("direction") val direction: LocalizedText?
    )

    data class Precipitation(
        @SerializedName("precipPeriods") val precipPeriods: List<PrecipPeriod>?
    )

    data class PrecipPeriod(
        @SerializedName("start") val start: LocalizedValue?,
        @SerializedName("end") val end: LocalizedValue?,
        @SerializedName("value") val value: LocalizedText?
    )

    data class Uv(
        @SerializedName("index") val index: LocalizedValue?,
        @SerializedName("category") val category: LocalizedText?,
        @SerializedName("textSummary") val textSummary: LocalizedText?
    )

    data class Period(
        @SerializedName("textForecastName") val textForecastName: LocalizedText?,
        @SerializedName("value") val value: LocalizedText?
    )

    data class AbbreviatedForecast(
        @SerializedName("icon") val icon: IconCode?,
        @SerializedName("textSummary") val textSummary: LocalizedText?
    )

    data class HourlyForecastGroup(
        @SerializedName("timestamp") val timestamp: LocalizedText,
        @SerializedName("hourlyForecasts") val hourlyForecasts: List<HourlyForecast>
    )

    data class HourlyForecast(
        /**
         * i.e. "Clear"
         */
        @SerializedName("condition") val condition: LocalizedText,
        @SerializedName("temperature") val temperature: Measurement,
        @SerializedName("iconCode") val iconCode: IconCode,
        @SerializedName("uv") val uv: HourlyUv,
        /**
         * Likelihood of precipitation.
         */
        @SerializedName("lop") val lop: Measurement,
        @SerializedName("humidex") val humidex: Measurement,
        @SerializedName("timestamp") val timestamp: String,
        @SerializedName("wind") val wind: Wind
    )

    data class HourlyUv(
        @SerializedName("index") val index: IndexHolder?
    )

    data class IndexHolder(
        @SerializedName("value") val value: LocalizedValue?
    )

    data class Station(
        @SerializedName("code") val code: LocalizedText,
        @SerializedName("lat") val lat: LocalizedText,
        @SerializedName("lon") val lon: LocalizedText,
        @SerializedName("value") val value: LocalizedText
    )

    data class Measurement(
        /**
         * i.e. metric
         */
        @SerializedName("unitType") val unitType: LocalizedText?,
        /**
         * i.e. kPa, km/h, %, etc
         */
        @SerializedName("units") val units: LocalizedText,
        /**
         * The value of the measurement, i.e. 5.6
         */
        @SerializedName("value") val value: LocalizedValue,
        /**
         * Used for lop, i.e. "Low"
         */
        @SerializedName("category") val category: LocalizedValue,
        /**
         * Used only for measuring pressure, i.e. 0.01
         */
        @SerializedName("change") val change: LocalizedValue?,
        /**
         * Used only for measuring pressure, i.e. "falling"
         */
        @SerializedName("tendency") val tendency: LocalizedText?,
        @SerializedName("qaValue") val qaValue: LocalizedValue
    )

    data class Wind(
        @SerializedName("speed") val speed: Measurement,
        @SerializedName("direction") val direction: Direction?,
        @SerializedName("gust") val gust: Measurement?
    )

    data class Direction(
        /**
         * i.e. North, Northeast
         */
        @SerializedName("windDirFull") val windDirFull: LocalizedText?,
        /**
         * i.e. N, NE
         */
        @SerializedName("value") val value: LocalizedText?
    )

    data class IconCode(
        @SerializedName("format") val format: String?,
        @SerializedName("value") val value: Int,
        @SerializedName("url") val url: String?
    )

    data class LocalizedText(
        @SerializedName("en") val en: String,
        @SerializedName("fr") val fr: String
    )

    data class LocalizedValue(
        @SerializedName("en") val en: Any,
        @SerializedName("fr") val fr: Any
    )
}
