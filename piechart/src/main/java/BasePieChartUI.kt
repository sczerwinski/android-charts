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
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.View.LAYER_TYPE_HARDWARE
import android.view.View.LAYER_TYPE_SOFTWARE
import it.czerwinski.android.graphics.AdvancedPath
import it.czerwinski.android.graphics.mixColors
import it.czerwinski.android.graphics.withRadialTranslation

/**
 * Base implementation of UI for [PieChart].
 */
abstract class BasePieChartUI : PieChartUI {

    protected var colors = intArrayOf(Color.CYAN)
    protected var selectedColors = intArrayOf(Color.BLUE)

    protected var shadowColor = Color.BLACK

    protected var selectedElevation = 4f
    protected var selectedShift = 0f

    protected val path = AdvancedPath()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    override fun onAttachedToView(view: View) {
        view.setLayerType(
            if (selectedElevation > 0) LAYER_TYPE_SOFTWARE else LAYER_TYPE_HARDWARE,
            paint
        )
    }

    override fun beforeDraw(canvas: Canvas) = Unit

    override fun afterDraw(canvas: Canvas) {
        paint.clearShadowLayer()
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
        val middleAngle = (startAngle + endAngle) / 2
        val outerRadius = radius - selectedShift - selectedElevation
        generateSlicePath(selection, cx, cy, outerRadius, startAngle, endAngle)
        canvas.withRadialTranslation(
            distance = selection * selectedShift,
            angle = middleAngle
        ) {
            drawPath(path, paint)
        }
    }

    protected abstract fun generateSlicePath(
        selection: Float,
        cx: Float,
        cy: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float
    )
}
