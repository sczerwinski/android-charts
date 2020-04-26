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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.View.*
import it.czerwinski.android.charts.core.*
import it.czerwinski.android.graphics.AdvancedPath
import it.czerwinski.android.graphics.mixColors
import it.czerwinski.android.graphics.set
import it.czerwinski.android.graphics.withRadialTranslation

/**
 * Simple UI for [PieChart]. Draws a classic pie chart.
 */
class SimplePieChartUI @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : PieChartUI {

    private var colors = intArrayOf(Color.CYAN)
    private var selectedColors = intArrayOf(Color.BLUE)

    private var shadowColor = Color.BLACK

    private var selectedElevation = 4f

    private var sliceSpacing = 0f
    private var selectedSliceShift = 0f

    private val path = AdvancedPath()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val labelUIDelegate = SimplePieChartLabelUIDelegate()

    init {
        context?.withStyledAttributes(
            attrs = attrs,
            stylables = R.styleable.SimplePieChartUI,
            defStyleRes = defStyleRes
        ) {
            initAttrs(context)
            initTextAttrs(context, attrs)
        }
    }

    private fun TypedArray.initAttrs(context: Context?) {
        colors = getResourceId(R.styleable.SimplePieChartUI_simplePieChartUI_colors, 0)
            .takeUnless { it == 0 }
            ?.let { context?.resources?.getIntArray(it) }
                ?: colors
        selectedColors = getResourceId(R.styleable.SimplePieChartUI_simplePieChartUI_selectionColors, 0)
            .takeUnless { it == 0 }
            ?.let { context?.resources?.getIntArray(it) }
                ?: selectedColors
        shadowColor =
                getColor(R.styleable.SimplePieChartUI_simplePieChartUI_shadowColor, Color.BLACK)
        selectedElevation =
                getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_selectionElevation, 4f)
        sliceSpacing =
                getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_sliceSpacing, 0f)
        selectedSliceShift =
                getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_selectionShift, 0f)
    }

    private fun TypedArray.initTextAttrs(context: Context, attrs: AttributeSet?) {
        val labelPosition =
            getInt(R.styleable.SimplePieChartUI_simplePieChartUI_labelPosition, 1)
        val labelSpacing =
            getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_labelSpacing, 0f)
        val labelMinPercent =
            getInt(R.styleable.SimplePieChartUI_simplePieChartUI_labelMinPercent, 0)
        context.withStyledAttributes(
            attrs = attrs,
            stylables = R.styleable.TextPaint,
            defStyleRes = getResourceId(
                R.styleable.SimplePieChartUI_simplePieChartUI_labelAppearance, 0
            )
        ) {
            labelUIDelegate.applyFrom(
                context, this, labelPosition, labelSpacing, labelMinPercent
            )
        }
    }

    override fun onAttachedToView(view: View) {
        if (selectedElevation > 0) {
            view.setLayerType(LAYER_TYPE_SOFTWARE, paint)
        } else {
            view.setLayerType(LAYER_TYPE_HARDWARE, paint)
        }
    }

    override fun applyLabelsPadding(labels: Iterable<String>, pieChartRect: Rect) {
        labelUIDelegate.applyLabelsPadding(labels, pieChartRect)
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
        selection: Float,
        label: String?
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
        val outerRadius = radius - selectedSliceShift - selectedElevation
        canvas.withRadialTranslation(
            distance = selection * selectedSliceShift,
            angle = middleAngle
        ) {
            path.set {
                addCircleSector(
                    cx = cx,
                    cy = cy,
                    radius = outerRadius,
                    startAngle = startAngle,
                    sweepAngle = endAngle - startAngle,
                    inset = sliceSpacing / 2
                )
            }
            drawPath(path, paint)
            if (label != null) {
                labelUIDelegate.draw(
                    canvas = canvas,
                    cx = cx,
                    cy = cy,
                    radius = outerRadius,
                    startAngle = startAngle,
                    endAngle = endAngle,
                    label = label
                )
            }
        }
    }

    override fun afterDraw(canvas: Canvas) {
        paint.clearShadowLayer()
    }
}
