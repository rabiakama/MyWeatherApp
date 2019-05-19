package com.example.myweather.data



class CityList {

    var mCreator:String?=null

    constructor(mCreator: String?) {
        this.mCreator = mCreator
    }

    fun getCreator(): String? {
        return mCreator
    }


}