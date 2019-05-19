package com.example.myweather.data.weather_options

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("sunrise")
    val sunrise: Long = 0,
    @SerializedName("sunset")
    val sunset: Long = 0
)