package com.example.testprogram.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.example.testprogram.ui.custom.model.Sticker
import java.util.UUID

fun Context.dpToPx(dp: Float): Float {
    return resources.displayMetrics.density * dp + 0.5f
}

fun View.OnClickListener.assignViews(vararg views: View?) {
    views.forEach {
        it?.setOnClickListener(this)
    }
}

val Bitmap.toSticker: Sticker get() = Sticker(UUID.randomUUID().toString(), this)
