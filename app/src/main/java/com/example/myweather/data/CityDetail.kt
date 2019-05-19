package com.example.myweather.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import android.R.id





class CityDetail {


    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("name")
    @Expose
    private var name: String? = null
    @SerializedName("latitude")
    @Expose
    private var latitude: String? = null
    @SerializedName("longitude")
    @Expose
    private var longitude: String? = null
    @SerializedName("population")
    @Expose
    private var population: String? = null
    @SerializedName("region")
    @Expose
    private var region: String? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getLatitude(): String? {
        return latitude
    }

    fun setLatitude(latitude: String) {
        this.latitude = latitude
    }

    fun getLongitude(): String? {
        return longitude
    }

    fun setLongitude(longitude: String) {
        this.longitude = longitude
    }

    fun getPopulation(): String? {
        return population
    }

    fun setPopulation(population: String) {
        this.population = population
    }

    fun getRegion(): String? {
        return region
    }

    fun setRegion(region: String) {
        this.region = region
    }


}