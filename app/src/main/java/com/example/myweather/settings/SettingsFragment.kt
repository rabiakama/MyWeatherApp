package com.example.myweather.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast

import com.example.myweather.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private val MY_PERMISSIONS_REQUEST_GPS = 0
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Settings"

        val view= inflater.inflate(R.layout.fragment_settings, container, false)
        val text=view.findViewById<TextView>(R.id.txttitle)
        val switch=view.findViewById<Switch>(R.id.switchGps)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        switchGps.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                turnGPSOn()

            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }


    }
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun turnGPSOn() {

        if (!hasGpsPermissions()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this.activity, "error", Toast.LENGTH_SHORT).show()

            } else {
                askGetPermission()
            }
            return
        }
        enableGPS(activity!!.baseContext)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_GPS -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enableGPS(activity!!.baseContext)
                else
                    Toast.makeText(this.activity, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun enableGPS(context: Context) {
        if (!isGpsEnable())
            displayLocationSettingsRequest(context)
    }

    private fun displayLocationSettingsRequest(context: Context) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback(object : ResultCallback<LocationSettingsResult> {
            override fun onResult(result: LocationSettingsResult) {

                val status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> Log.i("GPS", "All location settings are satisfied.")
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            "GPS",
                            "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                        )

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(activity, MY_PERMISSIONS_REQUEST_GPS)
                        } catch (e: IntentSender.SendIntentException) {
                            Log.i("GPS", "PendingIntent unable to execute request.")
                        }

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> Log.i(
                        "GPS",
                        "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                    )
                }
            }
        })
    }

    private fun hasGpsPermissions(): Boolean {
        return this.context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this.context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun isGpsEnable(): Boolean {
        val locationManager = activity!!.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(Context.LOCATION_SERVICE)
    }

    fun askGetPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                MY_PERMISSIONS_REQUEST_GPS
            )
        }
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
