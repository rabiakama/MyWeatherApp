package com.example.myweather

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myweather.city.City
import com.example.myweather.city.CityDetail
import com.example.myweather.city.CityHelper


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        val tempMin= view.findViewById<TextView>(R.id. tv_tempMin)
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


    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(city: CityDetail) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    arguments?.putString(CityHelper.COLUMN_CITY_NAME,city.getName())
                    //buraya rain,humidity vs gelecek
                }
            }
    }
}
