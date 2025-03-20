package com.example.testprogram.ui.custom

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.view.MotionEvent
import com.example.testprogram.ui.custom.model.Sticker
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class StickerDrawer(val invalidate: () -> Unit) {

    private val stickers: MutableList<Sticker> = ArrayList()
    private var currentStickerIndex = -1
    private var movingStickerIndex = -1
    private var lastTouch: PointF = PointF()
    private var isMoving = false
    private var isRotating = false

    // For pinch-to-zoom
    private var isMultiTouch = false
    private var oldDistance = 0f
    private var oldRotation = 0f
    private val multiTouchPoint1 = PointF()
    private val multiTouchPoint2 = PointF()

    // Constants
    private val MAX_ZOOM = 5f
    private val MIN_ZOOM = 0.2f


    fun onDraw(canvas: Canvas) {
        stickers.forEach { sticker ->
            canvas.drawBitmap(sticker.bitmap, sticker.matrix, null)
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchY = event.y

                /*if (currentStickerIndex != -1) {
                    // Check if rotate icon was touched
                    val sticker = stickers[currentStickerIndex]
                    val bounds = sticker.getBounds()

                    val rotateIconCenterX = bounds.right - CONTROL_SIZE / 2
                    val rotateIconCenterY = bounds.bottom - CONTROL_SIZE / 2

                    // Check if rotate icon was touched
                    if (distance(touchX, touchY, rotateIconCenterX, rotateIconCenterY) < CONTROL_SIZE) {
                        isRotating = true
                        lastTouch.x = touchX
                        lastTouch.y = touchY
                        return true
                    }
                }*/

                // Check if a sticker was touched (for moving)
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

            MotionEvent.ACTION_POINTER_DOWN -> {
                // Second finger down - start pinch gesture if on current sticker
                if (event.pointerCount == 2 && currentStickerIndex != -1) {
                    // Cancel any single-touch operations
                    isMoving = false
                    isRotating = false

                    // Start multi-touch operations
                    isMultiTouch = true

                    // Save initial points
                    multiTouchPoint1.set(event.getX(0), event.getY(0))
                    multiTouchPoint2.set(event.getX(1), event.getY(1))

                    // Calculate initial distance and angle for scaling and rotation
                    oldDistance = distance(
                        multiTouchPoint1.x, multiTouchPoint1.y,
                        multiTouchPoint2.x, multiTouchPoint2.y
                    )
                    oldRotation = getRotationAngle(multiTouchPoint1, multiTouchPoint2)

                    return true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isMultiTouch && event.pointerCount == 2 && currentStickerIndex != -1) {
                    return scaleAction(event)
                }

                if (isRotating && currentStickerIndex != -1) {
                    return rotateAction(event)
                }

                if (isMoving && movingStickerIndex != -1) {
                    return movingAction(event)
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                // End multi-touch if we're dropping below 2 fingers
                if (event.pointerCount <= 2 && isMultiTouch) {
                    isMultiTouch = false
                    oldDistance = 0f
                    invalidate()
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                isMoving = false
                isRotating = false
                isMultiTouch = false
                movingStickerIndex = -1
                invalidate()
                return true
            }
        }
        return false
    }

    private fun scaleAction(event: MotionEvent): Boolean {
        // Update points
        multiTouchPoint1.set(event.getX(0), event.getY(0))
        multiTouchPoint2.set(event.getX(1), event.getY(1))

        val sticker = stickers[currentStickerIndex]
        val bounds = sticker.getBounds()
        val centerX = (bounds.left + bounds.right) / 2
        val centerY = (bounds.top + bounds.bottom) / 2

        // Calculate new distance and determine scale factor
        val newDistance = distance(
            multiTouchPoint1.x, multiTouchPoint1.y,
            multiTouchPoint2.x, multiTouchPoint2.y
        )

        if (oldDistance > 0) {
            val scaleFactor = newDistance / oldDistance
            sticker.scale(scaleFactor, centerX, centerY, MIN_ZOOM, MAX_ZOOM)
        }

        // Calculate new rotation angle
        val newRotation = getRotationAngle(multiTouchPoint1, multiTouchPoint2)
        val rotation = newRotation - oldRotation
        sticker.rotate(rotation, centerX, centerY)

        // Save current values for next move
        oldDistance = newDistance
        oldRotation = newRotation

        invalidate()
        return true
    }

    private fun rotateAction(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        val sticker = stickers[currentStickerIndex]
        val bounds = sticker.getBounds()
        val centerX = (bounds.left + bounds.right) / 2
        val centerY = (bounds.top + bounds.bottom) / 2

        // Calculate rotation angle based on touch movement
        val lastAngle = Math.toDegrees(
            Math.atan2(
                (lastTouch.y - centerY).toDouble(),
                (lastTouch.x - centerX).toDouble()
            )
        ).toFloat()
        val newAngle = Math.toDegrees(
            Math.atan2(
                (touchY - centerY).toDouble(),
                (touchX - centerX).toDouble()
            )
        ).toFloat()
        val rotation = newAngle - lastAngle

        sticker.rotate(rotation, centerX, centerY)

        lastTouch.x = touchX
        lastTouch.y = touchY
        invalidate()
        return true
    }

    private fun movingAction(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        val dx = touchX - lastTouch.x
        val dy = touchY - lastTouch.y
        val sticker = stickers[movingStickerIndex]
        sticker.move(dx, dy)

        lastTouch.x = touchX
        lastTouch.y = touchY
        invalidate()
        return true
    }

    fun addSticker(sticker: Sticker) {
        stickers.add(sticker)
        currentStickerIndex = stickers.size - 1
        invalidate()
    }

    fun removeSticker(sticker: Sticker) {
        stickers.removeIf { item -> item.id == sticker.id }

        invalidate()
    }

    fun reorderStickers(newOrder: List<Sticker>): Boolean {
        if (newOrder.size != stickers.size || !newOrder.containsAll(stickers) || !stickers.containsAll(newOrder)) {
            return false
        }

        // Save the currently selected sticker reference if any
        val currentSticker = if (currentStickerIndex >= 0) stickers[currentStickerIndex] else null
        val movingSticker = if (movingStickerIndex >= 0) stickers[movingStickerIndex] else null

        // Clear and repopulate the list with the new order
        stickers.clear()
        stickers.addAll(newOrder)

        // Restore selection indexes
        currentStickerIndex = if (currentSticker != null) stickers.indexOf(currentSticker) else -1
        movingStickerIndex = if (movingSticker != null) stickers.indexOf(movingSticker) else -1

        // Trigger redraw
        invalidate()
        return true
    }

    private fun createRotateIcon(size: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Draw circle background
        paint.color = Color.WHITE
        canvas.drawCircle(size/2f, size/2f, size/2f, paint)

        // Draw rotate icon
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawArc(5f, 5f, size-5f, size-5f, 0f, 270f, false, paint)

        // Draw arrow
        val path = Path()
        path.moveTo(size-10f, size/2f)
        path.lineTo(size-5f, size/2-5f)
        path.lineTo(size-5f, size/2+5f)
        path.close()
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)

        return bitmap
    }

    private fun findStickerIndexAtPosition(x: Float, y: Float): Int {
        // Search in reverse order to get the topmost sticker first
        for (index in stickers.indices.reversed()) {
            if (stickers[index].contains(x, y)) {
                return index
            }
        }
        return -1
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt(
            (x2 - x1).toDouble().pow(2.0) +
                    (y2 - y1).toDouble().pow(2.0)
        ).toFloat()
    }

    private fun getRotationAngle(point1: PointF, point2: PointF): Float {
        return Math.toDegrees(
            atan2(
                (point2.y - point1.y).toDouble(),
                (point2.x - point1.x).toDouble()
            )
        ).toFloat()
    }
}