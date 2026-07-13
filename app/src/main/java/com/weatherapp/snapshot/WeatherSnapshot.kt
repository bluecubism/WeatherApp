package com.weatherapp.snapshot

data class WeatherSnapshot(
    var timestamp : String = "",

    var stationName : String = "",
    var airTemp : String = "",
    var relHum : String = "",

    var windSpeed : String = "",
    var windDir : String = "",
    var windGust : String = "",

    var pcpn : String = "",
    var pcpnImg : Int = 0,

    var cloudAmt : String = "",
    var cloudAmtImg : Int = 0,

    var vis : String = "",
    var visImg : Int = 0,

    var weather : String = "",
    var weatherImg : Int = 0,
    var weatherImgUrl : String = ""
)