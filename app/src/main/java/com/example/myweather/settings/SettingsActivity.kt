package com.example.myweather.settings

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.example.myweather.R
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_GPS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        this.setTitle("Settings")

        switchGps.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                turnGPSOn()
                //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    fun turnGPSOn() {


        if (!hasGpsPermissions()) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                showExplanationForPermissions()

            } else {
                askGetPermission()
            }

            return
        }

        enableGPS(baseContext)

    }

    fun askGetPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSIONS_REQUEST_GPS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_GPS -> {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enableGPS(getBaseContext())
                else
                    showPermissionDenied()


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
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(10000 / 2)

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback(object : ResultCallback<LocationSettingsResult> {
            override fun onResult(result: LocationSettingsResult) {

                val status = result.getStatus()
                when (status.getStatusCode()) {
                    LocationSettingsStatusCodes.SUCCESS -> Log.i("GPS", "All location settings are satisfied.")
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.i(
                            "GPS",
                            "Location settings are not satisfied. Show the user a dialog to upgrade location settings "
                        )

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(this@SettingsActivity, MY_PERMISSIONS_REQUEST_GPS)
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
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isGpsEnable(): Boolean {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(Context.LOCATION_SERVICE)
    }

    private fun showPermissionDenied() {
        AlertDialog.Builder(this)
            .setMessage("This permission is important")
            .setNeutralButton("Ok", DialogInterface.OnClickListener { dialog, which -> }).show()
    }

    private fun showExplanationForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("This permission is important)")
            .setNeutralButton("Ok") { dialog, which -> askGetPermission() }.show()
    }
    }

