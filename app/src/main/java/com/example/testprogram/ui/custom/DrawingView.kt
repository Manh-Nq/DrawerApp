package com.example.testprogram.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.testprogram.ui.custom.model.Sticker


class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val stickerDrawer by lazy { StickerDrawer(context, invalidate = { invalidate() }) }
    private val mainRect: RectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mainRect.set(0f, 0f, w.toFloat(), h.toFloat())
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    fun addSticker(sticker: Sticker) {
        stickerDrawer.addSticker(sticker)
    }

    fun removeSticker(sticker: Sticker) {
        stickerDrawer.removeSticker(sticker)
    }

    fun reorderStickers(stickers: List<Sticker>) {
        stickerDrawer.reorderStickers(stickers)
    }

    fun getRect(): RectF {
        return mainRect
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        stickerDrawer.onDraw(canvas)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        stickerDrawer.onTouchEvent(event)
        return true
    }

}

