package com.example.testprogram.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class CustomProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var maxProgress: Int = 10
    private var progress: Int = 0
    private var progressColor: Int = Color.BLUE
    private var backgroundColor: Int = Color.GRAY
    private val paint: Paint = Paint()
    private var animator: ValueAnimator? = null

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = backgroundColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        val unitWidth = width.toFloat() / maxProgress

        paint.color = progressColor
        val progressWidth = progress * unitWidth
        canvas.drawRect(0f, 0f, progressWidth, height.toFloat(), paint)
    }

    fun setProgress(progress: Int) {
        val newProgress = when {
            progress < 0 -> 0
            progress > maxProgress -> maxProgress
            else -> progress
        }

        animator?.cancel()
        animator = ValueAnimator.ofInt(this.progress, newProgress).apply {
            duration = 350
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                this@CustomProgressView.progress = animation.animatedValue as Int
                invalidate()
            }
        }
        animator?.start()
    }
}

