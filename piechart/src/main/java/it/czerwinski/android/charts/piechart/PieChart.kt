package it.czerwinski.android.charts.piechart

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import kotlin.math.min

/**
 * Pie chart view.
 *
 * @constructor Creates a pie chart view.
 */
class PieChart @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.pieChartStyle
) : View(context, attrs, defStyleAttr) {

    /**
     * Pie chart gravity inside the view.
     */
    var gravity: Int = Gravity.CENTER

    private var pieChartRect = Rect()

    init {
        val attrsArray = context?.obtainStyledAttributes(
            attrs,
            R.styleable.PieChart,
            defStyleAttr,
            android.R.style.Widget
        )
        if (attrsArray != null) {
            initAttrs(attrsArray)
            attrsArray.recycle()
        }
    }

    private fun initAttrs(attrsArray: TypedArray) {
        gravity = attrsArray.getInteger(R.styleable.PieChart_android_gravity, Gravity.CENTER)
    }

    /**
     * Draws a pie chart on the [canvas].
     */
    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            updatePieChartRect()

            val cx = pieChartRect.centerX().toFloat()
            val cy = pieChartRect.centerY().toFloat()
            val radius = pieChartRect.width() / 2f

            if (isInEditMode) drawInEditMode(canvas, cx, cy, radius)
            else drawDataPoints(canvas, cx, cy, radius)
        }
    }

    private fun updatePieChartRect() {
        val measuredSize = min(
            measuredWidth - paddingLeft - paddingRight,
            measuredHeight - paddingTop - paddingBottom
        )
        pieChartRect.set(
            paddingLeft,
            paddingTop,
            measuredWidth,
            measuredHeight - paddingBottom
        )
        Gravity.apply(gravity, measuredSize, measuredSize, pieChartRect, pieChartRect)
    }

    private fun drawInEditMode(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.alpha = 0x80
        canvas.drawCircle(cx, cy, radius, Paint(Paint.ANTI_ALIAS_FLAG).apply { alpha = 128 })
        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.WHITE
        paint.alpha = 0xff
        canvas.drawText(javaClass.simpleName, cx, cy, paint)
    }

    private fun drawDataPoints(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
    }
}
