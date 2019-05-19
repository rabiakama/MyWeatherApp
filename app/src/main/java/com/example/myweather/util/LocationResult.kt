package com.example.myweather.util

import com.google.android.gms.location.LocationResult

private const val INVALID_LOCATION = 0.0

    fun LocationResult.isValid() =
        lastLocation.longitude != INVALID_LOCATION && lastLocation.latitude != INVALID_LOCATION
