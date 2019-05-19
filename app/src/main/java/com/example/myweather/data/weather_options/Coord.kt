package com.example.myweather.data.weather_options

import com.google.gson.annotations.SerializedName


data class Coord(
    @SerializedName("lon")
    val lon: Float = 0.toFloat(),
    @SerializedName("lat")
    val lat: Float = 0.toFloat()
)