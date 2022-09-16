package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnRepeat
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private var backColor = 0
    private var textColor = 0
    private var progress = 0
    private val loadingRectangle = Rect()

    private val textPaint = Paint().apply {
        color = resources.getColor(R.color.white, null)
        textSize = 55F
        textAlign = Paint.Align.CENTER
    }

    private val rectPaint = Paint().apply {
        color = resources.getColor(R.color.colorPrimary, null)
    }

    private val arcPaint = Paint().apply {
        color = resources.getColor(R.color.colorPrimaryDark, null)
    }

    private var valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        if (new == ButtonState.Loading) {
            startLoadingAnimation()
        } else {
            stopLoadingAnimation()
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0) {
            backColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }

        isClickable = true
        buttonState = ButtonState.Completed
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerText = (textPaint.ascent() + textPaint.descent()) / 2

        when (buttonState) {
            ButtonState.Loading -> {
                loadingRectangle.set(0, 0, width * progress / 360, height)
                canvas.drawRect(loadingRectangle, rectPaint)

                canvas.drawArc(
                    (widthSize - 150f),
                    (heightSize / 2) - 30f,
                    (widthSize - 80f),
                    (heightSize / 2) + 30f,
                    0f,
                    progress.toFloat(),
                    true,
                    arcPaint
                )

                canvas.drawText(
                    "Downloading...",
                    (width / 2).toFloat(),
                    ((height / 2).toFloat() - centerText),
                    textPaint
                )
            }

            ButtonState.Completed -> {
                canvas.drawRect(
                    0f,
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    rectPaint
                )

                canvas.drawText(
                    "Start downloading",
                    (width / 2).toFloat(),
                    ((height / 2).toFloat() - centerText),
                    textPaint
                )
            }

            ButtonState.Clicked -> {
                canvas.drawRect(
                    0f,
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    rectPaint
                )

                canvas.drawText(
                    "Start downloading",
                    (width / 2).toFloat(),
                    ((height / 2).toFloat() - centerText),
                    textPaint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()

        isClickable = false
        buttonState = ButtonState.Loading
        invalidate()

        return true
    }

    private fun startLoadingAnimation() {
        valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(1000).apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }

            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

            doOnRepeat {
                buttonState = ButtonState.Completed
                isClickable = true
                invalidate()
            }

            start()
        }
    }

    private fun stopLoadingAnimation() {

    }

    fun changeButtonState(newButtonState: ButtonState) {
        buttonState = newButtonState
    }

}