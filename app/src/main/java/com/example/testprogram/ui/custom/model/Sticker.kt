package com.example.testprogram.ui.custom.model

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF


data class Sticker(val id: String, val bitmap: Bitmap) {
    val matrix = Matrix()
    private var currentScale = 1f
    val width get() = bitmap.width
    val height get() = bitmap.height

    fun initPosition(x: Float, y: Float) {
        matrix.postTranslate(x, y)
    }

    fun move(x: Float, y: Float) {
        matrix.postTranslate(x, y)
    }

    fun rotate(degrees: Float, pivotX: Float, pivotY: Float) {
        matrix.postRotate(degrees, pivotX, pivotY)
    }

    fun scale(factor: Float, pivotX: Float, pivotY: Float, minScale: Float, maxScale: Float) {
        // Calculate new scale and enforce limits
        val newScale = currentScale * factor
        if (newScale < minScale || newScale > maxScale) {
            // Adjust factor to respect limits
            val adjustedFactor = when {
                newScale < minScale -> minScale / currentScale
                newScale > maxScale -> maxScale / currentScale
                else -> factor
            }

            matrix.postScale(adjustedFactor, adjustedFactor, pivotX, pivotY)
            currentScale *= adjustedFactor
        } else {
            matrix.postScale(factor, factor, pivotX, pivotY)
            currentScale *= factor
        }
    }

    fun contains(x: Float, y: Float): Boolean {
        val invertedMatrix = Matrix()
        matrix.invert(invertedMatrix)

        val points = floatArrayOf(x, y)
        invertedMatrix.mapPoints(points)
        return bitmap.width > points[0] && points[0] >= 0 && bitmap.height > points[1] && points[1] >= 0
    }

    fun getBounds(): RectF {
        val bounds = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
        matrix.mapRect(bounds)
        return bounds
    }
}