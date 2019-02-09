package it.czerwinski.android.charts.piechart

import android.graphics.Canvas
import android.view.View

/**
 * An interface for classes drawing UI of a pie chart.
 */
interface PieChartUI {

    /**
     * Called when the UI is being attached to a pie chart view.
     *
     * @param view An instance of a [PieChart].
     */
    fun onAttachedToView(view: View)

    /**
     * Called before the pie chart series are drawn.
     *
     * @param canvas The canvas on which the pie chart will be drawn.
     */
    fun beforeDraw(canvas: Canvas)

    /**
     * Draws UI of a pie chart.
     *
     * @param canvas The canvas on which the pie chart will be drawn.
     * @param cx X coordinate of the center of the pie chart.
     * @param cy Y coordinate of the center of the pie chart.
     * @param radius Radius of the pie chart.
     * @param index Index of the slice.
     * @param startAngle Start angle of the slice.
     * @param startAngle End angle of the slice.
     * @param selection Animated value of the slice being selected.
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
     * Called before the pie chart series have been drawn.
     *
     * @param canvas The canvas on which the pie chart has been drawn.
     */
    fun afterDraw(canvas: Canvas)
}
