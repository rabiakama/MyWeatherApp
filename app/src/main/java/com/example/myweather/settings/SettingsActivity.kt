package com.example.myweather.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.example.myweather.R


class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        this.title = "Settings"

        val fragment = SettingsFragment()
        val tr:FragmentTransaction=supportFragmentManager.beginTransaction()
        tr.replace(R.id.frame_container,fragment)
        tr.commit()
        //val fragmentManager = supportFragmentManager
       // fragmentManager.beginTransaction().replace(R.id.content, fragment).commit()


    }









    }

