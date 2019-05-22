package com.example.myweather.home

class MainActivityPresenter:MainActivityContract.Presenter {

    private val view:MainActivityContract.View?=null

    override fun getIzmir(latitude: String?, longitude: String?) {

    }

    override fun getCurrentData(latitude: String?, longitude: String?) {
        if (view != null) {
            view.showProgress()
        }
    }
}