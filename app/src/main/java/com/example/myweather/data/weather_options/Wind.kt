package com.example.myweather.data.weather_options

import com.google.gson.annotations.SerializedName


data class Wind(
    @SerializedName("speed")
    val speed: Float = 0.toFloat(),
    @SerializedName("deg")
    val deg: Float = 0.toFloat()
)
