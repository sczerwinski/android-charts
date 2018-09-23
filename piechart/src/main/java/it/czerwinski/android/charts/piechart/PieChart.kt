package it.czerwinski.android.charts.piechart

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.database.Observable
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import it.czerwinski.android.charts.common.*
import kotlin.math.atan2
import kotlin.math.max
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

    /**
     * Zero value angle.
     */
    var rotationAngle: Float = 0f

    /**
     * Interpolator for data set changes animations.
     */
    var dataSetInterpolator: Interpolator = DecelerateInterpolator()

    /**
     * Duration of data set changes animations.
     */
    var dataSetAnimationDuration: Int = 0

    /**
     * Interpolator for selection changes animations.
     */
    var selectionInterpolator: Interpolator = DecelerateInterpolator()

    /**
     * Duration of selection changes animations.
     */
    var selectionAnimationDuration: Int = 0

    /**
     * Pie chart UI drawing object.
     */
    var ui: PieChartUI? = null
        set(value) {
            field = value
            invalidate()
        }

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

    private var dataPoints: FloatArray = floatArrayOf(0f)
        set(value) {
            field = value
                .fold(listOf(value.first())) { output, next ->
                    if (output.last() == next) output
                    else output + next
                }
                .toFloatArray()
            invalidate()
        }

    private var selections: FloatArray = floatArrayOf(0f)
        set(value) {
            field = value
            invalidate()
        }

    private var pieChartRect = Rect()

    init {
        isClickable = true
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
        dataPoints.asSequence()
            .map { rotationAngle + FULL_ANGLE * it }
            .zipWithNext()
            .forEachIndexed { index, (startAngle, endAngle) ->
                ui?.draw(
                    view = this,
                    canvas = canvas,
                    index = index,
                    cx = cx, cy = cy, radius = radius,
                    startAngle = startAngle,
                    endAngle = endAngle,
                    selection = selections.getOrElse(index) { 0f }
                )
            }
    }

    private fun onDataSetChanged() {
        val dataPointsSize = max((adapter?.size ?: 0) + 1, dataPoints.size)

        val oldDataPoints = dataPoints
            .asIterable()
            .withSize(size = dataPointsSize, valueIfEmpty = 0f)
            .toFloatArray()

        val newDataPoints = adapter
            ?.toList()
            .orEmpty()
            .normalize()
            .partialSums()
            .withSize(size = dataPointsSize, valueIfEmpty = 0f)
            .toFloatArray()

        ValueAnimator.ofObject(FloatArrayEvaluator(), oldDataPoints, newDataPoints)
            .apply {
                interpolator = dataSetInterpolator
                duration = dataSetAnimationDuration.toLong()
                addUpdateListener {
                    dataPoints = (it.animatedValue as? FloatArray) ?: dataPoints
                }
            }
            .start()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        updatePieChartRect()
        event
            ?.takeIf { it.action in listOf(MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP) }
            ?.also { motionEvent ->
                val x = motionEvent.x - pieChartRect.centerX()
                val y = motionEvent.y - pieChartRect.centerY()
                val angle = atan2(y, x).radToDeg() - rotationAngle

                var value = angle / FULL_ANGLE
                while (value < 0) value++
                while (value >= 1) value--

                val selectionIndex = dataPoints
                    .indexOfLast { dataPoint -> value >= dataPoint }

                onDataPointSelected(selectionIndex)
            }

        return super.onTouchEvent(event)
    }

    private fun onDataPointSelected(selectionIndex: Int) {
        val selectionsSize = dataPoints.size - 1

        val oldSelections = selections
            .asIterable()
            .withSize(size = selectionsSize, valueIfEmpty = 0f)
            .toFloatArray()

        val newSelections = oldSelections.indices
            .map { if (it == selectionIndex) 1f else 0f }
            .toFloatArray()

        ValueAnimator.ofObject(FloatArrayEvaluator(), oldSelections, newSelections)
            .apply {
                interpolator = selectionInterpolator
                duration = selectionAnimationDuration.toLong()
                addUpdateListener {
                    selections = (it.animatedValue as? FloatArray) ?: selections
                }
            }
            .start()
    }

    /**
     * [PieChart] data set adapter.
     */
    abstract class DataSetAdapter : Iterable<Float> {

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
         * Returns an `Iterator` that returns the values from the data set.
         */
        override fun iterator(): Iterator<Float> = IteratorImpl()

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

        private inner class IteratorImpl : Iterator<Float> {
            private var index: Int = 0
            override fun hasNext(): Boolean = index < size
            override fun next(): Float = get(index++)
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