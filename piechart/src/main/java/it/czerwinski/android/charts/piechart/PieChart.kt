package it.czerwinski.android.charts.piechart

import android.content.Context
import android.content.res.TypedArray
import android.database.Observable
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import it.czerwinski.android.charts.common.getInterpolator
import kotlin.math.min

/**
 * Pie chart view.
 *
 * @constructor Creates a pie chart view.
 */
class PieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.pieChartStyle
) : View(context, attrs, defStyleAttr) {

    /**
     * Pie chart gravity inside the view.
     */
    var gravity: Int = Gravity.CENTER

    var rotationAngle: Float = 0f

    var dataSetInterpolator: Interpolator = DecelerateInterpolator()
    var dataSetAnimationDuration: Int = 0

    var selectionInterpolator: Interpolator = DecelerateInterpolator()
    var selectionAnimationDuration: Int = 0

    private var pieChartRect = Rect()

    /**
     * Data set adapter.
     */
    var adapter: DataSetAdapter? = null
        set(value) {
            field?.unregisterObserver(observer)
            field = value
            value?.registerObserver(observer)
            onDataSetChanged()
        }

    private val observer = PieChartDataSetObserver()

    init {
        val attrsArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.PieChart,
            defStyleAttr,
            android.R.style.Widget
        )
        if (attrsArray != null) {
            attrsArray.initAttrs(context)
            attrsArray.recycle()
        }
    }

    private fun TypedArray.initAttrs(context: Context) {
        gravity = getInteger(R.styleable.PieChart_android_gravity, Gravity.CENTER)
        rotationAngle = getFloat(R.styleable.PieChart_pieChart_rotationAngle, 0f)
        dataSetInterpolator = getInterpolator(
            context,
            R.styleable.PieChart_pieChart_dataSetInterpolator,
            dataSetInterpolator
        )
        dataSetAnimationDuration = getInteger(
            R.styleable.PieChart_pieChart_dataSetAnimationDuration,
            0
        )
        selectionInterpolator = getInterpolator(
            context,
            R.styleable.PieChart_pieChart_selectionInterpolator,
            dataSetInterpolator
        )
        selectionAnimationDuration = getInteger(
            R.styleable.PieChart_pieChart_dataSetAnimationDuration,
            0
        )
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

    private fun onDataSetChanged() {
    }

    /**
     * [PieChart] data set adapter.
     */
    abstract class DataSetAdapter {

        private val observable = DataSetObservable()

        /**
         * [PieChart] data set size.
         */
        abstract val size: Int

        /**
         * Sum of [PieChart] data set values.
         */
        abstract val sum: Float

        /**
         * Gets [PieChart] data set value at the given [index].
         */
        abstract operator fun get(index: Int): Float

        /**
         * Registers an [observer] of data set changes.
         */
        fun registerObserver(observer: DataSetObserver) {
            observable.registerObserver(observer)
        }

        /**
         * Unregisters ths [observer] from observing data set changes.
         */
        fun unregisterObserver(observer: DataSetObserver) {
            observable.unregisterObserver(observer)
        }

        /**
         * Notifies the [PieChart] that the data set has been changed.
         */
        protected fun notifyDataSetChanged() {
            observable.notifyDataSetChanged()
        }
    }

    private class DataSetObservable : Observable<DataSetObserver>() {
        fun notifyDataSetChanged() {
            for (observer in mObservers) {
                observer.onDataSetChanged()
            }
        }
    }

    /**
     * An interface for observers of [DataSetAdapter]s changes.
     */
    interface DataSetObserver {

        /**
         * Called when a data set in the [DataSetAdapter] has been changed.
         */
        fun onDataSetChanged()
    }

    private inner class PieChartDataSetObserver : DataSetObserver {
        override fun onDataSetChanged() {
            this@PieChart.onDataSetChanged()
        }
    }
}
