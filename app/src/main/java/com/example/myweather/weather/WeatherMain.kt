package com.example.myweather.weather

import com.google.gson.annotations.SerializedName

data class WeatherMain(
    @SerializedName("temp")
    val temp: Float = 0.toFloat(),
    @SerializedName("humidity")
    val humidity: Float = 0.toFloat(),
    @SerializedName("h3")
    val h3: Float = 0.toFloat(),
    @SerializedName("temp_min")
    val temp_min: Float = 0.toFloat(),
    @SerializedName("temp_max")
    val temp_max: Float = 0.toFloat(),
    @SerializedName("pressure")
    var pressure: Float = 0.toFloat()
)
