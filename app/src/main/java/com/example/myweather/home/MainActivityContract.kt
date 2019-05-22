package com.example.myweather.home


interface MainActivityContract{


    interface View{

        fun hideProgress()
        fun showProgress()
    }


    interface Presenter{

        fun getCurrentData(latitude: String?, longitude: String?)
        fun getIzmir(latitude: String?, longitude: String?)
    }

}