package com.example.myweather.util

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

inline var View.isVisible: Boolean
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }

inline var View.isHidden: Boolean
    get() = visibility == GONE
    set(value) {
        visibility = if (value) GONE else VISIBLE
    }
