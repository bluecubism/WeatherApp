import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("type") val type: String? = null,
    @SerializedName("features") val features: List<Feature>? = null
)

data class Feature(
    @SerializedName("type") val type: String? = null,
    @SerializedName("properties") val properties: Properties? = null
)

data class Properties(
    @SerializedName("lastUpdated") val lastUpdated: String? = null,
    @SerializedName("identifier") val identifier: String? = null,
    @SerializedName("name") val name: LocalizedText? = null,
    @SerializedName("region") val region: LocalizedText? = null,
    @SerializedName("url") val url: LocalizedText? = null,
    @SerializedName("currentConditions") val currentConditions: CurrentConditions? = null,
    @SerializedName("forecastGroup") val forecastGroup: ForecastGroup? = null,
    @SerializedName("hourlyForecastGroup") val hourlyForecastGroup: HourlyForecastGroup? = null
)

data class CurrentConditions(
    @SerializedName("iconCode") val iconCode: IconCode? = null,
    @SerializedName("timestamp") val timestamp: LocalizedText? = null,
    @SerializedName("relativeHumidity") val relativeHumidity: Measurement? = null,
    @SerializedName("wind") val wind: Wind? = null,
    @SerializedName("pressure") val pressure: Measurement? = null,
    @SerializedName("temperature") val temperature: Measurement? = null,
    @SerializedName("dewpoint") val dewpoint: Measurement? = null,
    @SerializedName("station") val station: Station? = null,
    @SerializedName("condition") val condition: LocalizedText? = null,
    @SerializedName("windChill") val windChill: Measurement? = null
)

data class ForecastGroup(
    @SerializedName("timestamp") val timestamp: LocalizedText? = null,
    @SerializedName("regionalNormals") val regionalNormals: RegionalNormals? = null,
    @SerializedName("forecasts") val forecasts: List<Forecast>? = null
)

data class RegionalNormals(
    @SerializedName("textSummary") val textSummary: LocalizedText? = null,
    @SerializedName("temperature") val temperature: List<Measurement>? = null
)

data class Forecast(
    @SerializedName("precipitation") val precipitation: Precipitation? = null,
    @SerializedName("uv") val uv: Uv? = null,
    @SerializedName("period") val period: Period? = null,
    @SerializedName("temperatures") val temperatures: Temperatures? = null,
    @SerializedName("winds") val winds: Winds? = null,
    @SerializedName("cloudPrecip") val cloudPrecip: LocalizedText? = null,
    @SerializedName("relativeHumidity") val relativeHumidity: Measurement? = null,
    @SerializedName("abbreviatedForecast") val abbreviatedForecast: AbbreviatedForecast? = null,
    @SerializedName("textSummary") val textSummary: LocalizedText? = null
)

data class Temperatures(
    @SerializedName("temperature") val temperature: List<Measurement>? = null,
    @SerializedName("textSummary") val textSummary: LocalizedText? = null
)

data class Winds(
    @SerializedName("periods") val periods: List<WindPeriod>? = null,
    @SerializedName("textSummary") val textSummary: LocalizedText? = null
)

data class WindPeriod(
    @SerializedName("bearing") val bearing: Measurement? = null,
    @SerializedName("index") val index: LocalizedValue? = null,
    @SerializedName("rank") val rank: LocalizedText? = null,
    @SerializedName("speed") val speed: Measurement? = null,
    @SerializedName("gust") val gust: Measurement? = null,
    @SerializedName("direction") val direction: LocalizedText? = null
)

data class Precipitation(
    @SerializedName("precipPeriods") val precipPeriods: List<PrecipPeriod>? = null
)

data class PrecipPeriod(
    @SerializedName("start") val start: LocalizedValue? = null,
    @SerializedName("end") val end: LocalizedValue? = null,
    @SerializedName("value") val value: LocalizedText? = null
)

data class Uv(
    @SerializedName("index") val index: LocalizedValue? = null,
    @SerializedName("category") val category: LocalizedText? = null,
    @SerializedName("textSummary") val textSummary: LocalizedText? = null
)

data class Period(
    @SerializedName("textForecastName") val textForecastName: LocalizedText? = null,
    @SerializedName("value") val value: LocalizedText? = null
)

data class AbbreviatedForecast(
    @SerializedName("icon") val icon: IconCode? = null,
    @SerializedName("textSummary") val textSummary: LocalizedText? = null
)

data class HourlyForecastGroup(
    @SerializedName("timestamp") val timestamp: LocalizedText? = null,
    @SerializedName("hourlyForecasts") val hourlyForecasts: List<HourlyForecast>? = null
)

data class HourlyForecast(
    @SerializedName("uv") val uv: HourlyUv? = null,
    @SerializedName("condition") val condition: LocalizedText? = null,
    @SerializedName("temperature") val temperature: Measurement? = null,
    @SerializedName("iconCode") val iconCode: IconCode? = null,
    @SerializedName("lop") val lop: Measurement? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("wind") val wind: Wind? = null
)

data class HourlyUv(
    @SerializedName("index") val index: IndexHolder? = null
)

data class IndexHolder(
    @SerializedName("value") val value: LocalizedValue? = null
)

data class Station(
    @SerializedName("code") val code: LocalizedText? = null,
    @SerializedName("lat") val lat: LocalizedText? = null,
    @SerializedName("lon") val lon: LocalizedText? = null,
    @SerializedName("value") val value: LocalizedText? = null
)

data class Measurement(
    @SerializedName("unitType") val unitType: LocalizedText? = null,
    @SerializedName("units") val units: LocalizedText? = null,
    @SerializedName("change") val change: LocalizedValue? = null,
    @SerializedName("tendency") val tendency: LocalizedText? = null,
    @SerializedName("qaValue") val qaValue: LocalizedValue? = null,
    @SerializedName("value") val value: LocalizedValue? = null,
    @SerializedName("class") val className: LocalizedText? = null,
    @SerializedName("category") val category: LocalizedText? = null,
    @SerializedName("index") val index: LocalizedValue? = null,
    @SerializedName("textSummary") val textSummary: LocalizedText? = null,
    @SerializedName("start") val start: LocalizedValue? = null,
    @SerializedName("end") val end: LocalizedValue? = null,
    @SerializedName("temperature") val temperature: List<Measurement>? = null,
    @SerializedName("periods") val periods: List<WindPeriod>? = null,
    @SerializedName("precipPeriods") val precipPeriods: List<PrecipPeriod>? = null
)

data class Wind(
    @SerializedName("speed") val speed: Measurement? = null,
    @SerializedName("direction") val direction: Direction? = null,
    @SerializedName("gust") val gust: Measurement? = null
)

data class Direction(
    @SerializedName("windDirFull") val windDirFull: LocalizedText? = null,
    @SerializedName("value") val value: LocalizedText? = null
)

data class IconCode(
    @SerializedName("format") val format: String? = null,
    @SerializedName("value") val value: Int? = null,
    @SerializedName("url") val url: String? = null
)

data class LocalizedText(
    @SerializedName("en") val en: String? = null,
    @SerializedName("fr") val fr: String? = null
)

data class LocalizedValue(
    @SerializedName("en") val en: Any? = null,
    @SerializedName("fr") val fr: Any? = null
)
