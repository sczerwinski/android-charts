package it.czerwinski.android.charts.piechart

import android.graphics.Canvas
import android.view.View

/**
 * An interface for classes drawing UI of a pie chart.
 */
interface PieChartUI {

    /**
     * Draws UI of a pie chart.
     *
     * @param view An instance of a [PieChart].
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
        view: View,
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        index: Int,
        startAngle: Float,
        endAngle: Float,
        selection: Float
    )
}
