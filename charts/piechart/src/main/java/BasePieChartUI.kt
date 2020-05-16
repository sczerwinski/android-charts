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

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.View.LAYER_TYPE_HARDWARE
import android.view.View.LAYER_TYPE_SOFTWARE
import androidx.core.graphics.withSave
import it.czerwinski.android.graphics.AdvancedPath
import it.czerwinski.android.graphics.degToRad
import it.czerwinski.android.graphics.mixColors
import kotlin.math.cos
import kotlin.math.sin

/**
 * Base implementation of pie chart UI.
 */
abstract class BasePieChartUI : PieChart.UI {

    /**
     * Colors of pie chart slices.
     */
    protected var colors = intArrayOf(Color.CYAN)

    /**
     * Colors of pie chart slices when selected.
     */
    protected var selectedColors = intArrayOf(Color.BLUE)

    /**
     * Elevated slice shadow color.
     */
    protected var shadowColor = Color.BLACK

    /**
     * Elevation of the selected slice.
     */
    protected var selectedElevation = 4f

    /**
     * Outwards shift of the selected slice.
     */
    protected var selectedShift = 0f

    /**
     * Path defining shape of a slice.
     */
    protected val path = AdvancedPath()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /**
     * Sets software layer type of the view if the UI draws shadows.
     *
     * @param view Pie chart view.
     */
    override fun onAttachedToView(view: View) {
        view.setLayerType(
            if (selectedElevation > 0) LAYER_TYPE_SOFTWARE else LAYER_TYPE_HARDWARE,
            paint
        )
    }

    override fun beforeDraw(canvas: Canvas) = Unit

    /**
     * Shifts the slice outwards.
     *
     * @param canvas Canvas to draw on.
     * @param radius Radius of the pie chart.
     * @param startAngle Start angle of the slice.
     * @param endAngle End angle of the slice.
     * @param selection Fraction of the slice being selected.
     */
    override fun transform(
        canvas: Canvas,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        selection: Float
    ) {
        val distance = selection * selectedShift
        val middleAngle = (startAngle + endAngle) / 2
        canvas.translate(
            distance * cos(middleAngle.degToRad()),
            distance * sin(middleAngle.degToRad())
        )
    }

    override fun draw(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        index: Int,
        startAngle: Float,
        endAngle: Float,
        selection: Float
    ) {
        val colorIndex = index % colors.size
        paint.color = mixColors(colors[colorIndex], selectedColors[colorIndex], selection)
        if (selection > 0.01f) {
            paint.setShadowLayer(
                selection * selectedElevation,
                0f,
                selection * selectedElevation,
                shadowColor
            )
        } else {
            paint.clearShadowLayer()
        }
        val outerRadius = radius - selectedShift - selectedElevation

        generateSlicePath(selection, cx, cy, outerRadius, startAngle, endAngle)

        canvas.withSave{
            transform(canvas, radius, startAngle, endAngle, selection)
            drawPath(path, paint)
        }
    }

    /**
     * Generates path defining shape of a slice.
     *
     * @param selection Fraction of the slice being selected.
     * @param cx X coordinate of the center of the pie chart.
     * @param cy Y coordinate of the center of the pie chart.
     * @param outerRadius Outer radius of the pie chart.
     * @param startAngle Start angle of the slice.
     * @param endAngle End angle of the slice.
     */
    protected abstract fun generateSlicePath(
        selection: Float,
        cx: Float,
        cy: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float
    )

    /**
     * Clears shadow layer.
     *
     * @param canvas Canvas to draw on.
     */
    override fun afterDraw(canvas: Canvas) {
        paint.clearShadowLayer()
    }
}
