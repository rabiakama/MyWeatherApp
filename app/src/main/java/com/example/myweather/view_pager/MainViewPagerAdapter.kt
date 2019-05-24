package com.example.myweather.view_pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.myweather.home.MainFragment
import com.example.myweather.settings.SettingsFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager) :FragmentStatePagerAdapter(fragmentManager){

    private val COUNT=2
    override fun getItem(position: Int): Fragment? {

        var fragment:Fragment?=null
        when(position){
            0 -> fragment=MainFragment()
            1 -> fragment=SettingsFragment()
        }
        return fragment
    }

    override fun getCount(): Int {
        return COUNT

    }

}