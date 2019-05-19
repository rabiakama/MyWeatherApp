package com.example.myweather.weather

import com.example.myweather.data.weather_options.*
import com.google.gson.annotations.SerializedName

 class WeatherResponse(
    @SerializedName("coord")
    val coord: Coord? = null,
    @SerializedName("sys")
    val sys: Sys? = null,
    @SerializedName("weather")
    val weather: List<Weather> = emptyList(),
    @SerializedName("main")
    val main: WeatherMain? = null,
    @SerializedName("wind")
    val wind: Wind? = null,
    @SerializedName("rain")
    val rain: Rain? = null,
    @SerializedName("clouds")
    val clouds: Clouds? = null,
    @SerializedName("dt")
    val dt: Float = 0.toFloat(),
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("cod")
    val cod: Float = 0.toFloat()
)