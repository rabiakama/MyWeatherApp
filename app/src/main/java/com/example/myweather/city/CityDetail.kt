package com.example.myweather.city

import com.google.gson.annotations.SerializedName


 class  CityDetail{
    @SerializedName("id")
    private var id: String? = null
    @SerializedName("name")
    internal var name: String? = null
    @SerializedName("latitude")
    var latitude: String? = null
    @SerializedName("longitude")
    var longitude: String? = null
    @SerializedName("population")
    val population: String? = null
    @SerializedName("region")
    val region: String? = null



     fun getId(): String? {
         return id
     }

     fun setId(id: String?) {
         this.id = id
     }

     fun getName(): String? {
         return name
     }

     fun setName(name: String?) {
         this.name = name
     }

     fun getLat(): String? {
         return latitude
     }

     fun setLat(latitude: String?) {
         this.latitude = latitude
     }

     fun getLon(): String? {
         return longitude
     }

     fun setLon(lon: String?) {
         this.longitude = lon
     }
}