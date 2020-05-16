/*
 * Copyright 2018–2020 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.AttrRes
import androidx.core.content.withStyledAttributes
import it.czerwinski.android.charts.core.*
import it.czerwinski.android.graphics.FULL_ANGLE
import it.czerwinski.android.graphics.radToDeg
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

/**
 * Pie chart view.
 *
 * @constructor Creates a pie chart view.
 * @param context The context.
 * @param attrs Set of styleable attributes.
 * @param defStyleAttr Default style attribute.
 */
class PieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.pieChartStyle
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
     * Horizontal padding for labels.
     */
    var labelsHorizontalPadding: Int = 0

    /**
     * Vertical padding for labels.
     */
    var labelsVerticalPadding: Int = 0

    /**
     * Reference text for measuring padding for labels.
     */
    var labelsPaddingReferenceText: String? = null

    /**
     * Pie chart UI drawing object.
     */
    var ui: UI? = null
        set(value) {
            field = value
            value?.onAttachedToView(view = this)
            invalidate()
        }

    /**
     * Pie chart labels UI drawing object.
     */
    var labelsUI: LabelsUI? = null
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

    /**
     * Index of selected data point.
     */
    var selectionIndex: Int = -1
        set(value) {
            val oldValue = field
            field = value
            onDataPointSelected(oldValue, value)
        }

    private val onSelectionChangedListeners = mutableListOf<OnSelectionChangedListener>()

    private val observer = PieChartDataSetObserver()

    private var dataPointsAngles: FloatArray = floatArrayOf(0f)
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

    private val measuredTextBounds = Rect()

    init {
        isClickable = true
        context.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.PieChart,
            defStyleAttr = defStyleAttr,
            defStyleRes = android.R.style.Widget
        ) {
            initAttrs(context, attrs)
        }
    }

    private fun TypedArray.initAttrs(context: Context, attrs: AttributeSet?) {
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
        labelsHorizontalPadding = getDimensionPixelOffset(
            R.styleable.PieChart_pieChart_labelsPaddingHorizontal,
            0
        )
        labelsVerticalPadding = getDimensionPixelOffset(
            R.styleable.PieChart_pieChart_labelsPaddingVertical,
            0
        )
        labelsPaddingReferenceText = getString(R.styleable.PieChart_pieChart_labelsPaddingFromText)

        getString(R.styleable.PieChart_pieChart_ui)
            ?.let { uiClassName ->
                val defStyleId = getResourceId(R.styleable.PieChart_pieChart_uiAppearance, 0)
                ui = createUI(uiClassName, attrs, defStyleId)
            }

        getString(R.styleable.PieChart_pieChart_labelsUI)
            ?.let { uiClassName ->
                val defStyleId = getResourceId(R.styleable.PieChart_pieChart_labelsAppearance, 0)
                labelsUI = createUI(uiClassName, attrs, defStyleId)
            }
    }

    private inline fun <reified T> createUI(
        className: String,
        attrs: AttributeSet?,
        defStyleId: Int
    ): T? {
        return try {
            val uiClass = Class.forName(className)
            try {
                uiClass.getConstructor(
                    Context::class.java,
                    AttributeSet::class.java,
                    Int::class.java
                )
                    .newInstance(context, attrs, defStyleId)
            } catch (throwable: Throwable) {
                uiClass.getConstructor(Context::class.java, AttributeSet::class.java)
                    .newInstance(context, attrs)
            } catch (throwable: Throwable) {
                uiClass.getConstructor(Context::class.java)
                    .newInstance(context)
            } catch (throwable: Throwable) {
                uiClass.newInstance()
            } catch (throwable: Throwable) {
                Log.e(TAG, "Error instantiating UI", throwable)
                null
            } as T
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error instantiating UI", throwable)
            null
        }
    }

    /**
     * Measures pie chart bounds.
     *
     * @param widthMeasureSpec Horizontal space requirements as imposed by the parent view.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent view.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        updatePieChartRect()
    }

    private fun updatePieChartRect() {
        var labelsPaddingHorizontal = labelsHorizontalPadding
        var labelsPaddingVertical = labelsVerticalPadding
        val referenceLabel = labelsPaddingReferenceText

        if (referenceLabel != null) {
            labelsUI?.measureText(referenceLabel, measuredTextBounds)
            labelsPaddingHorizontal = max(labelsPaddingHorizontal, measuredTextBounds.width())
            labelsPaddingVertical = max(labelsPaddingVertical, measuredTextBounds.height())
        }

        val measuredSize = min(
            measuredWidth - paddingLeft - paddingRight - 2 * labelsPaddingHorizontal,
            measuredHeight - paddingTop - paddingBottom - 2 * labelsPaddingVertical
        )
        pieChartRect.set(
            paddingLeft + labelsPaddingHorizontal,
            paddingTop + labelsPaddingVertical,
            measuredWidth - paddingRight - labelsPaddingHorizontal,
            measuredHeight - paddingBottom - labelsPaddingVertical
        )
        Gravity.apply(gravity, measuredSize, measuredSize, pieChartRect, pieChartRect)
    }

    /**
     * Draws a pie chart on the canvas.
     *
     * @param canvas Canvas to draw on.
     */
    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            val cx = pieChartRect.centerX().toFloat()
            val cy = pieChartRect.centerY().toFloat()
            val radius = pieChartRect.width() / 2f

            if (isInEditMode) drawInEditMode(canvas, cx, cy, radius)
            else drawDataPoints(canvas, cx, cy, radius)
        }
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
        if (ui == null) Log.w(TAG, "No UI was set. Pie chart will not be drawn.")
        if (labelsUI == null) Log.w(TAG, "No labels UI was set. Labels will not be drawn.")

        ui?.beforeDraw(canvas)

        dataPointsAngles.asSequence()
            .map { rotationAngle + FULL_ANGLE * it }
            .zipWithNext()
            .forEachIndexed { index, (startAngle, endAngle) ->
                ui?.draw(
                    canvas = canvas,
                    index = index,
                    cx = cx,
                    cy = cy,
                    radius = radius,
                    startAngle = startAngle,
                    endAngle = endAngle,
                    selection = selections.getOrElse(index) { 0f }
                )
                labelsUI?.draw(
                    canvas = canvas,
                    cx = cx,
                    cy = cy,
                    radius = radius,
                    startAngle = startAngle,
                    endAngle = endAngle,
                    selection = selections.getOrElse(index) { 0f },
                    label = adapter?.takeIf { it.size > index }?.getLabel(index),
                    transformation = ui
                )
            }

        ui?.afterDraw(canvas)
    }

    private fun onDataSetChanged() {
        val dataPointsAnglesSize = max((adapter?.size ?: 0) + 1, dataPointsAngles.size)

        val oldDataPointsAngles = dataPointsAngles
            .asIterable()
            .withSize(size = dataPointsAnglesSize, valueIfEmpty = 0f)
            .toFloatArray()

        val newDataPointsAngles = adapter
            ?.toList()
            .orEmpty()
            .normalize()
            .partialSums()
            .withSize(size = dataPointsAnglesSize, valueIfEmpty = 0f)
            .toFloatArray()

        ValueAnimator.ofObject(FloatArrayEvaluator(), oldDataPointsAngles, newDataPointsAngles)
            .apply {
                interpolator = dataSetInterpolator
                duration = dataSetAnimationDuration.toLong()
                addUpdateListener {
                    dataPointsAngles = (it.animatedValue as? FloatArray) ?: dataPointsAngles
                }
            }
            .start()
    }

    /**
     * Handles touch screen motion events.
     *
     * @param event The event to handle.
     * @return True if the event has been handled, false otherwise.
     */
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

                selectionIndex = dataPointsAngles.indexOfLast { dataPoint -> value >= dataPoint }
            }

        return super.onTouchEvent(event)
    }

    private fun onDataPointSelected(oldSelectionIndex: Int, newSelectionIndex: Int) {
        val selectionsSize = dataPointsAngles.size - 1

        val oldSelections = selections
            .asIterable()
            .withSize(size = selectionsSize, valueIfEmpty = 0f)
            .toFloatArray()

        val newSelections = oldSelections.indices
            .map { if (it == newSelectionIndex) 1f else 0f }
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

        notifySelectionChanged(oldSelectionIndex, newSelectionIndex)
    }

    private fun notifySelectionChanged(oldSelectionIndex: Int, newSelectionIndex: Int) {
        for (listener in onSelectionChangedListeners) {
            listener.onSelectionChangedListener(
                view = this,
                oldSelectionIndex = oldSelectionIndex,
                newSelectionIndex = newSelectionIndex
            )
        }
    }

    /**
     * Adds a listener of selection index changes.
     *
     * @param listener Listener to add.
     */
    fun addOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        onSelectionChangedListeners.add(listener)
    }

    /**
     * Adds a listener of selection index changes.
     *
     * @param listener Listener to remove.
     */
    fun removeOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        onSelectionChangedListeners.remove(listener)
    }

    /**
     * Clears current pie chart selection.
     */
    fun clearSelection() {
        selectionIndex = -1
    }

    companion object {
        private const val TAG = "PieChart"
    }

    /**
     * Pie chart data set adapter.
     */
    abstract class DataSetAdapter : Iterable<Float> {

        private val observable = DataSetObservable()

        /**
         * Data set size.
         */
        abstract val size: Int

        /**
         * Sum of all values in the data set.
         */
        abstract val sum: Float

        /**
         * Gets data set value at the given index.
         *
         * @param index Index in the data set.
         * @return Value at the given index.
         */
        abstract operator fun get(index: Int): Float

        /**
         * Gets data set label at the given index.
         *
         * @param index Index in the data set.
         * @return Label at the given index.
         */
        abstract fun getLabel(index: Int): String

        /**
         * Returns an `Iterator` for the values in the data set.
         *
         * @return `Iterator` for the values in the data set.
         */
        override fun iterator(): Iterator<Float> = IteratorImpl()

        /**
         * Returns an `Iterable` that iterates through the labels for the data set.
         *
         * @return `Iterable` that iterates through the labels for the data set.
         */
        fun getLabels(): Iterable<String> = Iterable { LabelIteratorImpl() }

        /**
         * Registers an observer of data set changes.
         *
         * @param observer Observer to register.
         */
        fun registerObserver(observer: DataSetObserver) {
            observable.registerObserver(observer)
        }

        /**
         * Unregisters the observer from observing data set changes.
         *
         * @param observer Observer to unregister.
         */
        fun unregisterObserver(observer: DataSetObserver) {
            observable.unregisterObserver(observer)
        }

        /**
         * Notifies all registered observers that the data set has been changed.
         */
        protected fun notifyDataSetChanged() {
            observable.notifyDataSetChanged()
        }

        private inner class IteratorImpl : Iterator<Float> {
            private var index: Int = 0
            override fun hasNext(): Boolean = index < size
            override fun next(): Float = get(index++)
        }

        private inner class LabelIteratorImpl : Iterator<String> {
            private var index: Int = 0
            override fun hasNext(): Boolean = index < size
            override fun next(): String = getLabel(index++)
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
     * An interface for observers of `DataSetAdapter`s changes.
     */
    interface DataSetObserver {

        /**
         * Called when data set has been changed.
         */
        fun onDataSetChanged()
    }

    private inner class PieChartDataSetObserver : DataSetObserver {
        override fun onDataSetChanged() {
            this@PieChart.onDataSetChanged()
        }
    }

    /**
     * Pie chart UI slice transformation.
     */
    interface UITransformation {

        /**
         * Pre-concatenates the current matrix of the canvas with a transformation
         * determined by the UI of a pie chart.
         *
         * @param canvas Canvas to draw on.
         * @param radius Radius of the pie chart.
         * @param startAngle Start angle of the slice.
         * @param endAngle End angle of the slice.
         * @param selection Fraction of the slice being selected.
         */
        fun transform(
            canvas: Canvas,
            radius: Float,
            startAngle: Float,
            endAngle: Float,
            selection: Float
        )
    }

    /**
     * An interface for classes drawing UI of a pie chart.
     */
    interface UI : UITransformation {

        /**
         * Called when the UI is being attached to a pie chart view.
         *
         * @param view Pie chart view.
         */
        fun onAttachedToView(view: View)

        /**
         * Called before the pie chart series are drawn.
         *
         * @param canvas Canvas to draw on.
         */
        fun beforeDraw(canvas: Canvas)

        /**
         * Draws UI of a pie chart.
         *
         * @param canvas Canvas to draw on.
         * @param cx X coordinate of the center of the pie chart.
         * @param cy Y coordinate of the center of the pie chart.
         * @param radius Radius of the pie chart.
         * @param index Index of the slice.
         * @param startAngle Start angle of the slice.
         * @param endAngle End angle of the slice.
         * @param selection Fraction of the slice being selected.
         */
        fun draw(
            canvas: Canvas,
            cx: Float,
            cy: Float,
            radius: Float,
            index: Int,
            startAngle: Float,
            endAngle: Float,
            selection: Float
        )

        /**
         * Called after the pie chart series have been drawn.
         *
         * @param canvas Canvas to draw on.
         */
        fun afterDraw(canvas: Canvas)
    }

    /**
     * An interface for classes drawing labels for a pie chart.
     */
    interface LabelsUI {

        /**
         * Measures dimensions of a given text drawn as a label.
         *
         * @param text Text to measure.
         * @param bounds Rectangle to be set to measured bounds of the text.
         */
        fun measureText(text: String, bounds: Rect)

        /**
         * Draws a pie chart label for a single slice.
         *
         * @param canvas Canvas to draw on.
         * @param cx X coordinate of the center of the pie chart.
         * @param cy Y coordinate of the center of the pie chart.
         * @param radius Radius of the pie chart slice.
         * @param startAngle Start angle of the slice.
         * @param endAngle End angle of the slice.
         * @param selection Fraction of the slice being selected.
         * @param label Label of the slice.
         * @param transformation Slice transformation determined by the UI of a pie chart.
         */
        fun draw(
            canvas: Canvas,
            cx: Float,
            cy: Float,
            radius: Float,
            startAngle: Float,
            endAngle: Float,
            selection: Float,
            label: String?,
            transformation: UITransformation?
        )
    }

    /**
     * An interface for listeners of pie chart selection index changes.
     */
    interface OnSelectionChangedListener {

        /**
         * Called when selection index of the pie chart has been changed.
         *
         * @param view The pie chart.
         * @param oldSelectionIndex Old selection index.
         * @param newSelectionIndex New selection index.
         */
        fun onSelectionChangedListener(
            view: PieChart,
            oldSelectionIndex: Int,
            newSelectionIndex: Int
        )
    }
}
