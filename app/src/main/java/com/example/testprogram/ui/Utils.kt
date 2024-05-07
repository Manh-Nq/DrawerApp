package com.example.testprogram.ui

import android.content.Context

fun Context.dpToPx(dp: Float): Float {
    return resources.displayMetrics.density * dp + 0.5f
}