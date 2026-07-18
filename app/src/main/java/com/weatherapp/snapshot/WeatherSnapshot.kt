package com.weatherapp.snapshot

data class WeatherSnapshot(
    var timestamp : String = "",

    var region : String = "",
    var temperature : String = "",
    var humidity : String = "",

    var windSpeed : String = "",
    var windDir : String = "",
    var windGust : String = "",

    /**
     * Precipitation
     */
    var pcpn : String = "",
    var pcpnImg : Int = 0,

    /**
     * Cloud amount
     */
    var cloudAmt : String = "",
    var cloudAmtImg : Int = 0,

    /**
     * Visibility distance (for fog)
     */
    var vis : String = "",
    var visImg : Int = 0,

    var weather : String = "",
    var weatherImg : Int = 0,
    var weatherImgUrl : String = ""
)