package com.example.myweather.data

import com.google.gson.annotations.SerializedName

 class WeatherResponse(
    @SerializedName("coord")
    var coord: Coord? = null,
    @SerializedName("sys")
    var sys: Sys? = null,
    @SerializedName("weather")
    var weather: List<Weather> = emptyList(),
    @SerializedName("main")
    var main: WeatherMain? = null,
    @SerializedName("wind")
    var wind: Wind? = null,
    @SerializedName("rain")
    var rain: Rain? = null,
    @SerializedName("clouds")
    var clouds: Clouds? = null,
    @SerializedName("dt")
    var dt: Float = 0.toFloat(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("cod")
    var cod: Float = 0.toFloat()
)