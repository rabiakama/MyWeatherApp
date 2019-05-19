package com.example.myweather.service


import com.example.myweather.data.City
import com.example.myweather.data.WeatherResponse
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("appid") appId: String
    ): Call<WeatherResponse>


    @GET("weather")
    fun getCityList(
        @Query("country") country:String,
        @Query("name") name:String,
        @Query("appid") appId: String
    ):Call<City>

}