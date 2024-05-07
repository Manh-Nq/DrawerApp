package com.example.testprogram.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.testprogram.R


class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val lineDrawer by lazy { LineDrawer(context, invalidate = { invalidate() }) }
    private lateinit var mBitmap: Bitmap
    private val mBitmapPaint: Paint by lazy { Paint(Paint.DITHER_FLAG) }

    private fun initCanvasBitmap(w: Int, h: Int) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        lineDrawer.initCanvasBitmap(w, h, mBitmap)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initCanvasBitmap(w, h)

        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)

        lineDrawer.draw(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        lineDrawer.onTouchEvent(event)
        return true
    }

    fun erasePaint() {
        lineDrawer.erasePaint()
    }

    fun editPaint() {
        lineDrawer.editPaint()
    }

    fun getSavingBitmap(): Bitmap {
        return mBitmap
    }

    fun setWidthPaint(width: Int) {
        lineDrawer.setWidthPaint(width)

    }
}

