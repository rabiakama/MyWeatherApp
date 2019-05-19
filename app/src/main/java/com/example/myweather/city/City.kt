package com.example.myweather.city

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("cityDetail")
    val cityDetail: List<CityDetail> = emptyList()
)