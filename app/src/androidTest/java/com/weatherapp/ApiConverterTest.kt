package com.weatherapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.weatherapp.api.ApiConverter
import com.weatherapp.api.apidata.*
import com.weatherapp.snapshot.WeatherSnapshot
import org.junit.*

class ApiConverterTest {
    companion object {
        private lateinit var cpwFeatures: List<CityPageWeatherRealtimeApiData.Feature>
        private lateinit var currWeatherSnapshot: WeatherSnapshot
        private lateinit var forecastSnapshot: List<WeatherSnapshot>

        @JvmStatic
        @BeforeClass // execute once before running all tests
        fun setup() {
            val cpwProperties = CityPageWeatherRealtimeApiData.Properties(
                lastUpdated = "",
                identifier = "",
                name = CityPageWeatherRealtimeApiData.LocalizedText("Toronto", "Toronto"),
                region = CityPageWeatherRealtimeApiData.LocalizedText("Toronto", "Toronto"),
                url = CityPageWeatherRealtimeApiData.LocalizedText("", ""),
                currentConditions = CityPageWeatherRealtimeApiData.CurrentConditions(
                    iconCode = CityPageWeatherRealtimeApiData.IconCode(
                        format = "",
                        value = 0,
                        url = ""
                    ),
                    timestamp = CityPageWeatherRealtimeApiData.LocalizedText("2026-07-17T18:00:00Z", "2026-07-17T18:00:00Z"), // 2:00 PM in America/New_York
                    relativeHumidity = CityPageWeatherRealtimeApiData.Measurement(
                        unitType = null,
                        units = CityPageWeatherRealtimeApiData.LocalizedText("%", "%"),
                        value = CityPageWeatherRealtimeApiData.LocalizedValue(10, 10),
                        category = null,
                        change = null,
                        tendency = null,
                        qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                    ),
                    wind = CityPageWeatherRealtimeApiData.Wind(
                        speed = CityPageWeatherRealtimeApiData.Measurement(
                            unitType = null,
                            units = CityPageWeatherRealtimeApiData.LocalizedText("km/h", "km/h"),
                            value = CityPageWeatherRealtimeApiData.LocalizedValue(2, 2),
                            category = null,
                            change = null,
                            tendency = null,
                            qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                        ),
                        direction = CityPageWeatherRealtimeApiData.Direction(
                            windDirFull = CityPageWeatherRealtimeApiData.LocalizedText("North", "North"),
                            value = CityPageWeatherRealtimeApiData.LocalizedText("N", "N")
                        ),
                        gust = CityPageWeatherRealtimeApiData.Measurement(
                            unitType = null,
                            units = CityPageWeatherRealtimeApiData.LocalizedText("km/h", "km/h"),
                            value = CityPageWeatherRealtimeApiData.LocalizedValue(5, 5),
                            category = null,
                            change = null,
                            tendency = null,
                            qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                        ),
                    ),
                    pressure = null,
                    temperature = CityPageWeatherRealtimeApiData.Measurement(
                        unitType = null,
                        units = CityPageWeatherRealtimeApiData.LocalizedText("C", "C"),
                        value = CityPageWeatherRealtimeApiData.LocalizedValue(24, 24),
                        category = null,
                        change = null,
                        tendency = null,
                        qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                    ),
                    dewpoint = null,
                    station = CityPageWeatherRealtimeApiData.Station(
                        code = CityPageWeatherRealtimeApiData.LocalizedText("code", "code"),
                        lat = CityPageWeatherRealtimeApiData.LocalizedText("lat", "lat"),
                        lon = CityPageWeatherRealtimeApiData.LocalizedText("lon", "lon"),
                        value = CityPageWeatherRealtimeApiData.LocalizedText("value", "value")
                    ),
                    condition = CityPageWeatherRealtimeApiData.LocalizedText("Sunny", "Sunny"),
                    windChill = null
                ),
                forecastGroup = CityPageWeatherRealtimeApiData.ForecastGroup(
                    timestamp = null,
                    regionalNormals = null,
                    forecasts = null
                ),
                hourlyForecastGroup = CityPageWeatherRealtimeApiData.HourlyForecastGroup(
                    timestamp = CityPageWeatherRealtimeApiData.LocalizedText("2026-07-17T18:00:00Z", "2026-07-17T18:00:00Z"), // 2:00 PM in America/New_York
                    hourlyForecasts = listOf(
                        CityPageWeatherRealtimeApiData.HourlyForecast(
                            condition = CityPageWeatherRealtimeApiData.LocalizedText("Partly Cloudy", "Partly Cloudy"),
                            temperature = CityPageWeatherRealtimeApiData.Measurement(
                                unitType = null,
                                units = CityPageWeatherRealtimeApiData.LocalizedText("C", "C"),
                                value = CityPageWeatherRealtimeApiData.LocalizedValue(20, 20),
                                category = null,
                                change = null,
                                tendency = null,
                                qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                            ),
                            iconCode = CityPageWeatherRealtimeApiData.IconCode(
                                format = "",
                                value = 2,
                                url = ""
                            ),
                            uv = CityPageWeatherRealtimeApiData.HourlyUv(null),
                            lop = CityPageWeatherRealtimeApiData.Measurement(
                                unitType = null,
                                units = CityPageWeatherRealtimeApiData.LocalizedText("%", "%"),
                                value = CityPageWeatherRealtimeApiData.LocalizedValue(2, 2),
                                category = null,
                                change = null,
                                tendency = null,
                                qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                            ),
                            timestamp = "2026-07-17T19:00:00Z", // 3:00 PM in America/New_York
                            humidex = CityPageWeatherRealtimeApiData.Measurement(
                                unitType = null,
                                units = CityPageWeatherRealtimeApiData.LocalizedText("", ""),
                                value = CityPageWeatherRealtimeApiData.LocalizedValue(0, 0),
                                category = null,
                                change = null,
                                tendency = null,
                                qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                            ),
                            wind = CityPageWeatherRealtimeApiData.Wind(
                                speed = CityPageWeatherRealtimeApiData.Measurement(
                                    unitType = null,
                                    units = CityPageWeatherRealtimeApiData.LocalizedText("km/h", "km/h"),
                                    value = CityPageWeatherRealtimeApiData.LocalizedValue(4, 4),
                                    category = null,
                                    change = null,
                                    tendency = null,
                                    qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                                ),
                                direction = CityPageWeatherRealtimeApiData.Direction(
                                    windDirFull = CityPageWeatherRealtimeApiData.LocalizedText("Northeast", "Northeast"),
                                    value = CityPageWeatherRealtimeApiData.LocalizedText("NE", "NE")
                                ),
                                gust = CityPageWeatherRealtimeApiData.Measurement(
                                    unitType = null,
                                    units = CityPageWeatherRealtimeApiData.LocalizedText("km/h", "km/h"),
                                    value = CityPageWeatherRealtimeApiData.LocalizedValue(6, 6),
                                    category = null,
                                    change = null,
                                    tendency = null,
                                    qaValue = CityPageWeatherRealtimeApiData.LocalizedValue(100, 100),
                                ),
                            )
                        )
                    )
                )
            )

            cpwFeatures = listOf(CityPageWeatherRealtimeApiData.Feature(
                id = "id",
                type = "type",
                geometry = CityPageWeatherRealtimeApiData.Geometry(
                    type = "",
                    coordinates = listOf(0.0, 0.0)
                ),
                properties = cpwProperties
            ))

            currWeatherSnapshot = WeatherSnapshot()
            currWeatherSnapshot.region = "Toronto"
            currWeatherSnapshot.temperature = "24°C"
            currWeatherSnapshot.humidity = "10%"
            currWeatherSnapshot.timestamp = "2:00 PM"
            currWeatherSnapshot.windSpeed = "2km/h"
            currWeatherSnapshot.windDir = "North"
            currWeatherSnapshot.windGust = "5km/h"
            currWeatherSnapshot.weather = "Sunny"

            val futureSnap = WeatherSnapshot()
            futureSnap.temperature = "20°C"
            futureSnap.timestamp = "3:00 PM"
            futureSnap.windSpeed = "4km/h"
            futureSnap.windDir = "Northeast"
            futureSnap.windGust = "6km/h"
            futureSnap.weather = "Partly Cloudy"
            forecastSnapshot = listOf(
                futureSnap
            )
        }
    }

    @Test
    fun CpwRealtimeCurrentWeatherTest() {
        // use ApplicationProvider bc method being tested uses resources (R.drawables)
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = ApiConverter().CPWRealtimeToWeatherSnapshot(context, cpwFeatures)

        assert(result.weather == currWeatherSnapshot.weather)
        assert(result.temperature == currWeatherSnapshot.temperature)
        assert(result.humidity == currWeatherSnapshot.humidity)
        assert(result.windSpeed == currWeatherSnapshot.windSpeed)
        assert(result.windDir == currWeatherSnapshot.windDir)
        assert(result.windGust == currWeatherSnapshot.windGust)
        assert(result.weather == currWeatherSnapshot.weather)
    }

    @Test
    fun CpwRealtimePredictedWeatherTest() {
        val result = ApiConverter().CPWRealtimeHourlyForecastToWeatherSnapshot(cpwFeatures)
        val resultSnap = result.first()
        val futureSnap = forecastSnapshot.first()

        assert(resultSnap.weather == futureSnap.weather)
        assert(resultSnap.temperature == futureSnap.temperature)
        assert(resultSnap.humidity == futureSnap.humidity)
        assert(resultSnap.windSpeed == futureSnap.windSpeed)
        assert(resultSnap.windDir == futureSnap.windDir)
        assert(resultSnap.windGust == futureSnap.windGust)
        assert(resultSnap.weather == futureSnap.weather)
    }
}