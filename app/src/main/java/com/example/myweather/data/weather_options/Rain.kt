package com.example.myweather.data.weather_options

import com.google.gson.annotations.SerializedName


data class Rain(
    @SerializedName("3h")
    val h3: Float = 0.toFloat()
)