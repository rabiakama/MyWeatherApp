package com.example.myweather.home

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper.myLooper
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.example.myweather.BuildConfig
import com.example.myweather.R
import com.example.myweather.city.CityActivity
import com.example.myweather.city.CityAdapter
import com.example.myweather.city.CityDetail
import com.example.myweather.city.CityHelper
import com.example.myweather.weather.WeatherResponse
import com.example.myweather.service.WeatherServiceApi
import com.example.myweather.settings.SettingsActivity
import com.example.myweather.util.isHidden
import com.example.myweather.util.isValid
import com.example.myweather.view_pager.ViewPagerAdapter
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_OK
import kotlin.LazyThreadSafetyMode.NONE


class MainActivity : AppCompatActivity() , MainActivityContract.View{

    internal lateinit var presenter: MainActivityContract.Presenter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: String? = null
    private var lon: String? = null
    private var lt="38.4189"
    private var lg="27.1287"
    private var cityId:String?=null
    val factory: SQLiteDatabase.CursorFactory? = null
    private  var cityDbHelper: CityHelper?=null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private  var cityAdapter: CityAdapter?=null
    private  var cityLis:ArrayList<CityDetail> = arrayListOf()

    private val locationCallback: LocationCallback by lazy(NONE) {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.isValid()) {
                    lat = locationResult.lastLocation.latitude.toString()
                    lon = locationResult.lastLocation.longitude.toString()
                    getCurrentData(lat, lon)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityDbHelper = CityHelper(this, "city.db", factory, 2)

        getAllFavCities()

        cityId = intent.getStringExtra("name")
        lat=intent.getStringExtra("latitude")
        lon=intent.getStringExtra("longitude")

         //getIzmir(lt,lg)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        val isPermissionProvided = checkLocationPermission()
        if (isPermissionProvided) {
            getLocationUpdates()
        }

        floatingButton.setOnClickListener {
            val intent = Intent(this, CityActivity::class.java)
            startActivity(intent)
        }


        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    this.startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.home -> {
                    getCurrentData(lat, lon)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false

            }
        }
    }

    private fun setViewPagerAdapter() {

        pagerAdapter.notifyDataSetChanged()
        viewPager.offscreenPageLimit=cityLis.size
        pagerAdapter= ViewPagerAdapter(supportFragmentManager,cityLis)
        viewPager.adapter=pagerAdapter
    }

    private fun getCurrentData(latitude: String?, longitude: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherServiceApi::class.java)
        val call = service.getCurrentWeatherData(latitude, longitude, BuildConfig.API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == HTTP_OK) {
                    val weatherResponse = response.body()

                    weatherResponse?.let {
                        with(it) {
                            val stringBuilder =
                                "Country: " +
                                        sys?.country +
                                        "\n" +
                                        "City: " +
                                        name +
                                        "\n" +
                                        "Temperature: " +
                                        main?.temp + "°C" +
                                        "\n" +
                                        "Temperature(Min): " +
                                        main?.temp_min + "°C" +
                                        "\n" +
                                        "Temperature(Max): " +
                                        main?.temp_max + "°C" +
                                        "\n" +
                                        "Humidity: " +
                                        " %" +
                                        main?.humidity +
                                        "\n" +
                                        "Rain: " +
                                        "%"+
                                        main?.h3+
                                        "\n" +
                                        "Pressure: " +
                                        main?.pressure

                            //weatherProgress.isHidden = response.isSuccessful
                            hideProgress()

                        }
                    }
                }
            }
        })

    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_CODE
                )
            }
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PERMISSION_GRANTED) {
                getLocationUpdates()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(getLocationRequest(), locationCallback, myLooper())
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        fun getLocationRequest() =
            LocationRequest().apply {
                priority = PRIORITY_BALANCED_POWER_ACCURACY
                interval = EXPIRATION_DURATION
                fastestInterval = INTERVAL_FASTEST
            }

        private const val EXPIRATION_DURATION = 4000L
        private const val INTERVAL_FASTEST = 1000L
        private const val PERMISSION_REQUEST_CODE = 1000
    }

    private fun getIzmir(latitude: String?, longitude: String?){
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherServiceApi::class.java)
        val call = service.getCurrentWeatherData(latitude, longitude, BuildConfig.API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == HTTP_OK) {
                    val weatherResponse = response.body()

                    weatherResponse?.let {
                        with(it) {
                            val stringBuilder =
                                "Country: " +
                                        sys?.country +
                                        "\n" +
                                        "City: " +
                                        name +
                                        "\n" +
                                        "Temperature: " +
                                        main?.temp + "°C" +
                                        "\n" +
                                        "Temperature(Min): " +
                                        main?.temp_min + "°C" +
                                        "\n" +
                                        "Temperature(Max): " +
                                        main?.temp_max + "°C" +
                                        "\n" +
                                        "Humidity: " +
                                        " %" +
                                        main?.humidity +
                                        "\n" +
                                        "Rain: " +
                                        "%"+
                                        main?.h3+
                                        "\n" +
                                        "Pressure: " +
                                        main?.pressure

                            weatherProgress.isHidden = response.isSuccessful
                        }
                    }
                }
            }
        })
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
                cityAdapter?.notifyDataSetChanged()
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

