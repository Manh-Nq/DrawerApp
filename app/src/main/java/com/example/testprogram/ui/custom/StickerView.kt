package com.example.testprogram.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.testprogram.R

class StickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var stickerBitmap: Bitmap
    private var stickerX = 0f
    private var stickerY = 0f
    private var isDragging = false
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    init {
        // Load the sticker bitmap
        stickerBitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw the sticker bitmap at the specified position
        canvas.drawBitmap(stickerBitmap, stickerX, stickerY, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Check if touch is within the sticker bounds
                if (touchX >= stickerX && touchX <= stickerX + stickerBitmap.width &&
                    touchY >= stickerY && touchY <= stickerY + stickerBitmap.height
                ) {
                    isDragging = true
                    lastTouchX = touchX
                    lastTouchY = touchY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    val dx = touchX - lastTouchX
                    val dy = touchY - lastTouchY
                    stickerX += dx
                    stickerY += dy
                    lastTouchX = touchX
                    lastTouchY = touchY
                    invalidate() // Redraw the view
                }
            }
            MotionEvent.ACTION_UP -> {
                isDragging = false
            }
        }

        return true
    }
}
