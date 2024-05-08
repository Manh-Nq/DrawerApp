package com.example.testprogram.ui

import android.content.Context
import android.view.View

fun Context.dpToPx(dp: Float): Float {
    return resources.displayMetrics.density * dp + 0.5f
}

fun View.OnClickListener.assignViews(vararg views: View?) {
    views.forEach {
        it?.setOnClickListener(this)
    }
}
