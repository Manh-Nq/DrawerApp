package com.example.testprogram.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.MotionEvent
import com.example.testprogram.R
import com.example.testprogram.ui.dpToPx
import kotlin.math.abs

class LineDrawer(private val context: Context, val invalidate: () -> Unit) {
    private val pathPaint by lazy {
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 12f
        }
        paint
    }

    private val circlePaint by lazy {
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.GRAY
            style = Paint.Style.FILL
        }
        paint
    }

    private val backgroundPaint by lazy {
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        paint
    }

    private lateinit var mCanvas: Canvas

    private val mPath = Path()
    private var isClearMode: Boolean = false
    private var isMoving: Boolean = false
    private var circleWidth = 5f

    private var mX = 0f
    private var mY = 0f

    private val bmFile: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.test)

    companion object {
        private const val TOUCH_TOLERANCE = 4f
    }

    fun draw(canvas: Canvas) {
        canvas.drawPath(mPath, pathPaint)
        if (isClearMode && isMoving) {
            canvas.drawCircle(mX, mY, context.dpToPx(circleWidth), circlePaint)
        }
        mCanvas.drawBitmap(bmFile, 0f, 0f, null)
    }

    fun initCanvasBitmap(w: Int,h:Int,bitmap: Bitmap) {
        mCanvas = Canvas(bitmap)

        mCanvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), backgroundPaint)
    }



    fun onTouchEvent(event: MotionEvent) {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart(x, y)

            MotionEvent.ACTION_MOVE -> touchMove(x, y)

            MotionEvent.ACTION_UP -> touchUp()
        }
    }

    private fun touchStart(x: Float, y: Float) {
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
        invalidate()
    }

    private fun touchMove(x: Float, y: Float) {
        isMoving = true

        val dx = abs(x - mX)
        val dy = abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
        invalidate()
    }

    private fun touchUp() {
        isMoving = false
        mPath.lineTo(mX, mY)
        mCanvas.drawPath(mPath, pathPaint)
        mPath.reset()

        invalidate()
    }

    fun erasePaint() {
        if (!isClearMode) {
            isClearMode = true
            pathPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    private fun restorePaint() {
        if (isClearMode) {
            pathPaint.xfermode = null
            isClearMode = false
        }
    }

    fun editPaint() {
        restorePaint()
    }

    fun setWidthPaint(width: Int) {
        if (width < 0) return
        val strokeW = context.dpToPx(width.toFloat())
        pathPaint.strokeWidth = strokeW
        circleWidth = strokeW / 2

    }
}