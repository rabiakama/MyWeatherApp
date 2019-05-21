package com.example.myweather.view_pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.myweather.city.CityDetail


class ViewPagerAdapter(manager:FragmentManager,private val cityList:ArrayList<CityDetail>) :FragmentStatePagerAdapter(manager){

    override fun getItem(position: Int): Fragment {

        return MainFragment.newInstance(cityList[position % cityList.size] )
    }

    override fun getCount(): Int {

        return cityList.size* MAX_VALUE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return cityList[position % cityList.size].getName().toString()
    }

    companion object {
        private const val MAX_VALUE = 200
    }


}