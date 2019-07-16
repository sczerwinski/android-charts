/*
 * Copyright 2018–2019 Android Charts Open Source Project
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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.View.*
import it.czerwinski.android.charts.core.*
import it.czerwinski.android.graphics.AdvancedPath
import it.czerwinski.android.graphics.mixColors
import it.czerwinski.android.graphics.withRadialTranslation
import kotlin.math.max

class DonutPieChartUI @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : PieChartUI {

    private var colors = intArrayOf(Color.CYAN)
    private var selectedColors = intArrayOf(Color.BLUE)

    private var shadowColor = Color.BLACK

    private var selectedElevation = 4f

    private var donutWidth = 50f
    private var donutSpacing = 0f
    private var selectedDonutWidth = donutWidth
    private var selectedDonutShift = 0f

    private val path = AdvancedPath()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    init {
        context?.withStyledAttributes(
            attrs = attrs,
            stylables = R.styleable.DonutPieChartUI,
            defStyleRes = defStyleRes
        ) {
            initAttrs(context)
        }
    }

    private fun TypedArray.initAttrs(context: Context?) {
        colors = getResourceId(R.styleable.DonutPieChartUI_donutPieChartUI_colors, 0)
            .takeUnless { it == 0 }
            ?.let { context?.resources?.getIntArray(it) }
                ?: colors
        selectedColors = getResourceId(R.styleable.DonutPieChartUI_donutPieChartUI_selectionColors, 0)
            .takeUnless { it == 0 }
            ?.let { context?.resources?.getIntArray(it) }
                ?: selectedColors
        shadowColor =
                getColor(R.styleable.DonutPieChartUI_donutPieChartUI_shadowColor, Color.BLACK)
        selectedElevation =
                getDimension(R.styleable.DonutPieChartUI_donutPieChartUI_selectionElevation, 4f)
        donutWidth =
                getDimension(R.styleable.DonutPieChartUI_donutPieChartUI_donutWidth, 50f)
        donutSpacing =
                getDimension(R.styleable.DonutPieChartUI_donutPieChartUI_donutSpacing, 0f)
        selectedDonutWidth =
                getDimension(R.styleable.DonutPieChartUI_donutPieChartUI_selectionWidth, donutWidth)
        selectedDonutShift =
                getDimension(R.styleable.DonutPieChartUI_donutPieChartUI_selectionShift, 0f)
    }

    override fun onAttachedToView(view: View) {
        if (selectedElevation > 0) {
            view.setLayerType(LAYER_TYPE_SOFTWARE, paint)
        } else {
            view.setLayerType(LAYER_TYPE_HARDWARE, paint)
        }
    }

    override fun beforeDraw(canvas: Canvas) = Unit

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
        val outerRadius = radius - selectedDonutShift - selectedElevation
        val innerRadius = max(
            0f,
            outerRadius - (1 - selection) * donutWidth - selection * selectedDonutWidth
        )
        canvas.withRadialTranslation(
            distance = selection * selectedDonutShift,
            angle = middleAngle
        ) {
            path.reset()
            path.addRingSector(
                cx = cx,
                cy = cy,
                radius = outerRadius,
                startAngle = startAngle,
                sweepAngle = endAngle - startAngle,
                thickness = outerRadius - innerRadius,
                inset = donutSpacing / 2
            )
            drawPath(path, paint)
        }
    }

    override fun afterDraw(canvas: Canvas) {
        paint.clearShadowLayer()
    }
}
