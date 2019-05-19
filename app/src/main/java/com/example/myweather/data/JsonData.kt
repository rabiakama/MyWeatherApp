package com.example.myweather.data

class JsonData {

     var cities:String?=null

    constructor(cities: String) {
        this.cities = cities
    }


    fun getCity() : String?{
        return cities
    }

}

class Cit {

    private var name = String
    private var latitude :Float = 0.toFloat()
    private var longitude :Float = 0.toFloat()

}
