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

/**
 * An interface for classes drawing labels for a pie chart.
 */
interface PieChartLabelsUI {

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
     * @param canvas The canvas on which the pie chart will be drawn.
     * @param cx X coordinate of the center of the pie chart.
     * @param cy Y coordinate of the center of the pie chart.
     * @param radius Radius of the pie chart slice.
     * @param startAngle Start angle of the slice.
     * @param endAngle End angle of the slice.
     * @param label Label of the slice.
     */
    fun draw(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        label: String?
    )
}
