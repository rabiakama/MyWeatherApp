package com.example.myweather.data

import com.google.gson.annotations.SerializedName

data class City (

    @SerializedName("name")
    private var cityName : String?=null,
    @SerializedName("country")
    private var country: String?=null,
    @SerializedName("sys")
    var sys: Sys? = null

){
    fun getName(): String? {
        return cityName
    }

    fun setName(name: String) {
        this.cityName = name
    }
}