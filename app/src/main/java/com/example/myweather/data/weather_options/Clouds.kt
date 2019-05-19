package com.example.myweather.data.weather_options

import com.google.gson.annotations.SerializedName


data class Clouds(
    @SerializedName("all")
    val all: Float = 0.toFloat()
)