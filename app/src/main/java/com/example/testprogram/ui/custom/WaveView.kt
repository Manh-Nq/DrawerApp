package com.example.testprogram.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator

class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mainRect: RectF = RectF()

    private val strokeWidth: Float get() = context.dpToPx(20f)
    private val numOfWaveView: Int = 5

    private val waveRectArr = mutableListOf<RectF>()

    private val wavePaint: Paint by lazy {
        Paint().apply {
            color = Color.YELLOW
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    private val animators = mutableListOf<ValueAnimator>()

    private val isPaused
        get() = animators.all { it.isPaused }

    override fun onSizeChanged(
        w: Int, h: Int, oldw: Int, oldh: Int
    ) {
        super.onSizeChanged(
            w, h, oldw, oldh
        )
        if (w != oldw || h != oldh) {
            mainRect.set(
                0f, 0f, w.toFloat(), h.toFloat()
            )
            initializeWaveRects()
            startAnimation()
        }
    }

    private fun initializeWaveRects() {
        if (waveRectArr.isNotEmpty()) return
        val totalStroke = strokeWidth * (numOfWaveView + 1)
        val waveWidth =
            (mainRect.width() - totalStroke) / numOfWaveView
        for (i in 0 until numOfWaveView) {
            val left = (i + 1) * strokeWidth + (i * waveWidth)
            val top = 0f
            val right = left + waveWidth
            val bottom = top + mainRect.height()
            val rect = RectF(
                left, top, right, bottom
            )
            waveRectArr.add(rect)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawViews(canvas)
    }

    private fun drawViews(canvas: Canvas) {
        waveRectArr.forEach { rect ->
            val roundedF = context.dpToPx(8f)
            canvas.drawRoundRect(rect, roundedF, roundedF, wavePaint)
        }
    }

    private fun startAnimation() {
        if (waveRectArr.isEmpty() || animators.isNotEmpty()) return

        val delayBetweenRect = TIME_RUNNING / (waveRectArr.size + 1)
        waveRectArr.forEachIndexed { index, rect ->
            val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = TIME_RUNNING
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    val rectOffset = animatedValue.convertValue(
                        0f, 1f, -mainRect.height(), mainRect.height() / 2f
                    )
                    rect.top =
                        (mainRect.height() / 2f) - rectOffset
                    rect.bottom = mainRect.height() / 2f + rectOffset
                    Log.d(
                        "ManhNQ",
                        "startAnimation: top: ${rect.top} bottom: ${rect.bottom}  \n height : ${rect.top + rect.bottom}\nmain height: ${mainRect.height()}"
                    )
                    invalidate()
                }
                repeatCount = ValueAnimator.INFINITE
                repeatMode =
                    ValueAnimator.RESTART
                interpolator = LinearInterpolator()
                startDelay = index * delayBetweenRect
            }
            animators.add(animator)
            animator.start()
        }
    }

    fun runOrPause() {
        if (!isPaused) {
            animators.forEach { it.pause() }
        } else {
            animators.forEach { it.resume() }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animators.forEach { it.cancel() }
    }

    companion object {
        const val TIME_RUNNING = 1_500L
    }

}

fun Float.convertValue(min1: Float, max1: Float, min2: Float, max2: Float): Float {
    return ((this - min1) * ((max2 - min2) / (max1 - min1)) + min2)
}

fun Context.dpToPx(dp: Float): Float {
    return (dp * resources.displayMetrics.density + 0.5f)
}