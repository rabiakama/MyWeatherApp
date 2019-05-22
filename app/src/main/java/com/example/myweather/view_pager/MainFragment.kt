package com.example.myweather.view_pager

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.myweather.BuildConfig
import com.example.myweather.R
import com.example.myweather.city.CityDetail
import com.example.myweather.city.CityHelper
import com.example.myweather.service.WeatherServiceApi
import com.example.myweather.weather.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


class MainFragment : Fragment() {
    private var cityName: String? = null
    private var lat: Float? = null
    private var lon:Float?=null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cityName = it.getString(CityHelper.COLUMN_CITY_NAME)
            lat=it.getFloat(CityHelper.COLUMN_COORD_LAT)
            lon=it.getFloat(CityHelper.COLUMN_COORD_LONG)

        }
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
                            //hideProgress()

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
            temp.text= args.getFloat(CityHelper.COLUMN_TEMP).toString()
            tempMax.text=args.getFloat(CityHelper.COLUMN_TEMP_MAX).toString()
            tempMin.text=args.getFloat(CityHelper.COLUMN_TEMP_MIN).toString()
            humidity.text=args.getFloat(CityHelper.COLUMN_HUMIDITY).toString()
            rain.text=args.getFloat(CityHelper.COLUMN_RAIN).toString()

        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getCurrentData()
        super.onViewCreated(view, savedInstanceState)
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(city: CityDetail) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    arguments?.putString(CityHelper.COLUMN_CITY_NAME,city.getName())
                    arguments?.putString(CityHelper.COLUMN_COORD_LAT,city.getLat())
                    arguments?.putString(CityHelper.COLUMN_COORD_LONG,city.getLon())
                    //buraya rain,humidity vs gelecek
                }
            }
    }
}
