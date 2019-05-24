package com.example.myweather.view_pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.myweather.city.CityDetail
import com.example.myweather.home.MainFragment


class ViewPagerAdapter(manager:FragmentManager,private var cityList:ArrayList<CityDetail>) :FragmentStatePagerAdapter(manager){

    override fun getItem(position: Int): Fragment? {
        return MainFragment.newInstance(cityList[position] )
    }

    override fun getCount(): Int {

        return cityList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return cityList[position].getName().toString()
    }
}