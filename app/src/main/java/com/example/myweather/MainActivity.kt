package com.example.myweather

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper.myLooper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.example.myweather.data.City
import com.example.myweather.data.CityActivity
import com.example.myweather.data.WeatherResponse
import com.example.myweather.service.WeatherServiceApi
import com.example.myweather.util.isHidden
import com.example.myweather.util.isValid
import com.example.myweather.util.isVisible
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: String? = null
    private var lon: String? = null

    private var name: String? = null
    private var country: String? = null

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        val isPermissionProvided = checkLocationPermission()
        if (isPermissionProvided) {
            getLocationUpdates()
        }

        floatingButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //country?.let { loadCities(name.toString(), it) }

                val intent = Intent(this@MainActivity, CityActivity::class.java)
                startActivity(intent)

            }

        })

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    this.startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.home -> {
                    getCurrentData(lat,lon)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false

            }
        }
    }

    private fun loadCities(name:String,country:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherServiceApi::class.java)
        val call = service.getCityList(name, country, BuildConfig.API_KEY)
        call.enqueue(object : Callback<City> {
            override fun onFailure(call: Call<City>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<City>, response: Response<City>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()

                    weatherResponse?.let {
                        with(it) {
                            val stringBuilder =
                                    "Country: " + sys?.country +
                                    "\n" +
                                    "City: " + name +
                                    "\n"

                            weatherProgress.isHidden = response.isSuccessful
                            weatherText.text = stringBuilder
                            weatherText.isVisible = response.isSuccessful
                        }
                    }
                }
            }
        })
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
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()

                    weatherResponse?.let {
                        with(it) {
                            val stringBuilder = "Country: " +
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
                                    "Pressure: " +
                                    main?.pressure

                            weatherProgress.isHidden = response.isSuccessful
                            weatherText.text = stringBuilder
                            weatherText.isVisible = response.isSuccessful
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
                    this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE
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
}
