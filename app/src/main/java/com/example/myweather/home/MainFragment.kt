package com.example.myweather.home

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.os.Looper.myLooper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.myweather.BuildConfig
import com.example.myweather.R
import com.example.myweather.city.CityAdapter
import com.example.myweather.city.CityDetail
import com.example.myweather.city.CityHelper
import com.example.myweather.service.WeatherServiceApi
import com.example.myweather.util.isValid
import com.example.myweather.weather.WeatherResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


class MainFragment : Fragment() {

    private var cityName: String? = null
    private var lat: String? = null
    private var lon:String?=null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lt="38.4189"
    private var lg="27.1287"

    private var listener: OnFragmentInteractionListener? = null

    private val locationCallback: LocationCallback by lazy(LazyThreadSafetyMode.NONE) {
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
        arguments?.let {
            cityName = it.getString(CityHelper.COLUMN_CITY_NAME)
            lat=it.getString(CityHelper.COLUMN_COORD_LAT)
            lon=it.getString(CityHelper.COLUMN_COORD_LONG)

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MainFragment.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
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

    private fun getCurrentData(latitude: String?, longitude: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherServiceApi::class.java)
        val call = service.getCurrentWeatherData(latitude, longitude, BuildConfig.API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val weatherResponse = response.body()

                    weatherResponse?.let {
                        with(it) {
                            tv_cityName.text = name
                            if (main != null) {
                                tv_tempInDegree.text = main.temp.toString()
                                tv_tempMax.text = main.temp_max.toString()
                                tv_tempMin.text = main.temp_min.toString()
                                tv_Humidity.text = main.humidity.toString()
                                tv_Rain.text = main.h3.toString()
                            }
                        }
                    }
                }
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_main, container, false)
        val cityName= view.findViewById<TextView>(R.id.tv_cityName)
        val temp= view.findViewById<TextView>(R.id.tv_tempInDegree)
        val tempMax= view.findViewById<TextView>(R.id.tv_tempMax)
        val tempMin= view.findViewById<TextView>(R.id.tv_tempMin)
        val humidity= view.findViewById<TextView>(R.id.tv_Humidity)
        val rain= view.findViewById<TextView>(R.id.tv_Rain)

        val args=arguments
        if (args != null) {
            cityName.text=args.getString(CityHelper.COLUMN_CITY_NAME)
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getCurrentData(lat,lon)
        super.onViewCreated(view, savedInstanceState)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        fun getLocationRequest() =
            LocationRequest().apply {
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                interval = EXPIRATION_DURATION
                fastestInterval = INTERVAL_FASTEST
            }

        private const val EXPIRATION_DURATION = 4000L
        private const val INTERVAL_FASTEST = 1000L
        private const val PERMISSION_REQUEST_CODE = 1000

        @JvmStatic
        fun newInstance(city: CityDetail) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    arguments?.putString(CityHelper.COLUMN_CITY_NAME,city.getName())
                    arguments?.putString(CityHelper.COLUMN_COORD_LAT,city.getLat())
                    arguments?.putString(CityHelper.COLUMN_COORD_LONG,city.getLon())
                }
            }
    }
}
