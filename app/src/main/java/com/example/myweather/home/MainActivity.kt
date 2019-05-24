package com.example.myweather.home

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import com.example.myweather.R
import com.example.myweather.city.CityActivity
import com.example.myweather.city.CityDetail
import com.example.myweather.city.CityHelper
import com.example.myweather.settings.SettingsFragment
import com.example.myweather.view_pager.MainViewPagerAdapter
import com.example.myweather.view_pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() , MainActivityContract.View{

    internal lateinit var presenter: MainActivityContract.Presenter

    private var lat: String? = null
    private var lon: String? = null
    private var cityId:String?=null
    val factory: SQLiteDatabase.CursorFactory? = null
    private  var cityDbHelper: CityHelper?=null
    private  var pagerAdapter: ViewPagerAdapter?=null
    private var mainPager:MainViewPagerAdapter?=null
    private  var cityLis:ArrayList<CityDetail> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(mainPager !=null){
            val adapt=MainViewPagerAdapter(supportFragmentManager)
            viewPager.adapter=adapt
        }
        cityDbHelper = CityHelper(this, "city.db", factory, 2)
       // setViewPagerAdapter()
        getAllFavCities()

        cityId = intent.getStringExtra("name")
        lat = intent.getStringExtra("latitude")
        lon = intent.getStringExtra("longitude")


        floatingButton.setOnClickListener {
            viewPager.currentItem=3
            val intent = Intent(this, CityActivity::class.java)
            startActivity(intent)
        }

        bottom_navigation.setOnNavigationItemSelectedListener {item->
            var selectedFragment: Fragment? =null
            when(item.itemId){
                R.id.home ->{
                    viewPager.currentItem = 1
                    selectedFragment=MainFragment()
                }
                R.id.settings ->{
                    viewPager.currentItem=2
                    selectedFragment=SettingsFragment()
                }else -> return@setOnNavigationItemSelectedListener false
            }

            var fragmentTransaction=supportFragmentManager.beginTransaction()
            selectedFragment.let { fragmentTransaction.replace(R.id.frame_container, it) }
            fragmentTransaction.commit()
            return@setOnNavigationItemSelectedListener true

        }

    }


    private fun setViewPagerAdapter() {
        viewPager.offscreenPageLimit=cityLis.size
        pagerAdapter= ViewPagerAdapter(supportFragmentManager,cityLis)
        viewPager.adapter=pagerAdapter
        pagerAdapter?.notifyDataSetChanged()
    }

    fun getAllFavCities() {
        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        class FavoritesTask : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void): Void? {
                cityLis.clear()
                cityLis.addAll(cityDbHelper!!.getAddAll())

                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                pagerAdapter?.notifyDataSetChanged()
            }
        }
        FavoritesTask().execute()
    }

    override fun showProgress() {

        weatherProgress.visibility = View.VISIBLE
    }

    override fun hideProgress() {

        weatherProgress.visibility = View.GONE

    }

}

