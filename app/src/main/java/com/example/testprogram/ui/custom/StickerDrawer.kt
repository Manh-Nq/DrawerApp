package com.example.testprogram.ui.custom

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.view.MotionEvent

class StickerDrawer(val invalidate: () -> Unit) {

    private val stickers: MutableList<Sticker> = ArrayList()
    private var currentStickerIndex = -1
    private var movingStickerIndex = -1
    private var lastTouch: PointF = PointF()
    private var isMoving = false


    fun onDraw(canvas: Canvas) {
        for (sticker in stickers) {
            canvas.drawBitmap(sticker.bitmap, sticker.matrix, null)
        }
    }


    fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                movingStickerIndex = findStickerIndexAtPosition(touchX, touchY)
                if (movingStickerIndex != -1) {
                    isMoving = true
                    lastTouch.x = touchX
                    lastTouch.y = touchY
                    currentStickerIndex = movingStickerIndex
                    invalidate()
                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isMoving && movingStickerIndex != -1) {
                    val dx = touchX - lastTouch.x
                    val dy = touchY - lastTouch.y
                    stickers[movingStickerIndex].matrix.postTranslate(dx, dy)
                    lastTouch.x = touchX
                    lastTouch.y = touchY
                    invalidate()
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                if (isMoving) {
                    isMoving = false
                    movingStickerIndex = -1
                    invalidate()
                    return true
                }
            }
        }
        return false


    }

    fun addBitmap(bitmap: Bitmap) {
        stickers.add(Sticker(bitmap))
        invalidate()
    }

    private fun findStickerIndexAtPosition(x: Float, y: Float): Int {
        for ((index, sticker) in stickers.withIndex()) {
            if (sticker.contains(x, y)) {
                return index
            }
        }
        return -1
    }

    data class Sticker(val bitmap: Bitmap) {
        val matrix = Matrix()

        fun contains(x: Float, y: Float): Boolean {
            val invertedMatrix = Matrix()
            matrix.invert(invertedMatrix)

            val points = floatArrayOf(x, y)
            invertedMatrix.mapPoints(points)
            return bitmap.width > points[0] && points[0] >= 0 && bitmap.height > points[1] && points[1] >= 0
        }
    }

}