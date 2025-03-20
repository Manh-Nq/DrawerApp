package com.example.testprogram.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testprogram.ui.adapter.StickerData
import com.example.testprogram.ui.custom.dpToPx
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


fun Context.createBitmapFromRes(it: StickerData, size: Float = 56f): Bitmap? {
    return createBitmapFromRes(it.resId, size)
}

fun Context.createBitmapFromRes(resourceId: Int, sizeInDp: Float): Bitmap? {
    return try {
        val drawable = ContextCompat.getDrawable(this, resourceId) ?: return null
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDp, resources.displayMetrics
        ).toInt()

        if (drawable is BitmapDrawable) {
            Bitmap.createScaledBitmap(drawable.bitmap, sizeInPx, sizeInPx, true)
        } else {
            // Handle vector drawables or other types
            val bitmap = Bitmap.createBitmap(sizeInPx, sizeInPx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, sizeInPx, sizeInPx)
            drawable.draw(canvas)
            bitmap
        }
    } catch (e: Exception) {
        Log.e("BitmapUtil", "Error creating bitmap from resource", e)
        null
    }
}