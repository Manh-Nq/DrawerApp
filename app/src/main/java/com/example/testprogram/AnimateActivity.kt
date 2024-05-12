package com.example.testprogram

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.testprogram.databinding.AnimateActivityBinding


class AnimateActivity : AppCompatActivity() {

    private lateinit var binding: AnimateActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = AnimateActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        runAnimateViews()
    }

    private fun initViews() = with(binding) {
        progress.setProgress(2)
    }

    private fun runAnimateViews() {

        val translateAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_animate)
        val scaleAnimate = AnimationUtils.loadAnimation(this, R.anim.scale_animate)
        binding.parentHeartCt.startAnimation(translateAnimation)
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.heart1Icon.startAnimation(scaleAnimate)
                binding.heart2Icon.startAnimation(scaleAnimate)
                scaleAnimate.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        binding.heart1Icon.setImageResource(R.drawable.heart_icon_active)
                        binding.heart2Icon.setImageResource(R.drawable.heart_icon_active)
                        animateViewToView(binding.heart1Icon, binding.heartPointIcon)
                        animateViewToView(binding.heart2Icon, binding.heartPointIcon)
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })

            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
    }


    fun animateViewToView(movingView: View, targetView: View) {
        val startLocation = IntArray(2)
        movingView.getLocationOnScreen(startLocation)

        val targetLocation = IntArray(2)
        targetView.getLocationOnScreen(targetLocation)

        val deltaX = targetLocation[0] - startLocation[0]
        val deltaY = targetLocation[1] - startLocation[1]

        val targetWidth = targetView.width
        val targetHeight = targetView.height

        val scaleAnimation = ScaleAnimation(
            1f,
            targetWidth.toFloat() / movingView.width,
            1f,
            targetHeight.toFloat() / movingView.height
        )

        val translateAnimation = TranslateAnimation(0f, deltaX.toFloat(), 0f, deltaY.toFloat())

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(translateAnimation)
        animationSet.setDuration(1000)

        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                solvedEndAnimate()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        movingView.startAnimation(animationSet)
    }

    private fun solvedEndAnimate() = with(binding) {
        progress.setProgress(4)
        pointTxt.text = String.format("%s/%s", 4, 10)
        successTitle.visibility = View.VISIBLE
        actionLn.visibility = View.VISIBLE
        suggestTxt.visibility = View.VISIBLE

        val translateAnimation = AnimationUtils.loadAnimation(this@AnimateActivity, R.anim.translate_animate)
        val translateAnimationInfinite = AnimationUtils.loadAnimation(this@AnimateActivity, R.anim.translate_animate_infinity)

        successTitle.startAnimation(translateAnimation)
        actionLn.startAnimation(translateAnimation)
        suggestTxt.startAnimation(translateAnimationInfinite)
    }


}