/*
 * Copyright 2018–2020 Android Charts Open Source Project
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

import android.graphics.Canvas
import android.graphics.Rect
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
     * Measures padding required to draw the labels, and applies it to the given `Rect`.
     *
     * @param labels Pie chart labels.
     * @param pieChartRect An instance of `RectF`, to which the padding will be applied.
     */
    fun applyLabelsPadding(labels: Iterable<String>, pieChartRect: Rect)

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
     * @param endAngle End angle of the slice.
     * @param selection Animated value of the slice being selected.
     * @param label Label of the slice.
     */
    fun draw(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        index: Int,
        startAngle: Float,
        endAngle: Float,
        selection: Float,
        label: String?
    )

    /**
     * Called after the pie chart series have been drawn.
     *
     * @param canvas The canvas on which the pie chart has been drawn.
     */
    fun afterDraw(canvas: Canvas)
}
