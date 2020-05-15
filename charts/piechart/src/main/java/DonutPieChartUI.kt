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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import it.czerwinski.android.charts.core.getColors
import it.czerwinski.android.graphics.set
import kotlin.math.min

/**
 * Donut UI for pie chart view.
 *
 * Draws a donut chart.
 *
 * @constructor Creates a donut UI for pie chart view.
 * @param context The context.
 * @param attrs Set of styleable attributes.
 * @param defStyleRes Default style resource.
 */
class DonutPieChartUI @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    @StyleRes defStyleRes: Int = 0
) : BasePieChartUI() {

    private var donutWidth = 50f
    private var donutSpacing = 0f
    private var selectedDonutWidth = donutWidth

    init {
        context?.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.DonutPieChartUI,
            defStyleAttr = R.attr.donutPieChartStyle,
            defStyleRes = defStyleRes
        ) {
            initAttrs(context)
        }
    }

    private fun TypedArray.initAttrs(context: Context) {
        colors =
            getColors(context, R.styleable.DonutPieChartUI_donutPieChartUI_colors, Color.CYAN)
        selectedColors =
            getColors(context, R.styleable.DonutPieChartUI_donutPieChartUI_selectionColors, Color.BLUE)
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
        selectedShift =
            getDimension(R.styleable.DonutPieChartUI_donutPieChartUI_selectionShift, 0f)
    }

    /**
     * Generates path defining donut slice shape.
     *
     * @param selection Fraction of the slice being selected.
     * @param cx X coordinate of the center of the pie chart.
     * @param cy Y coordinate of the center of the pie chart.
     * @param outerRadius Outer radius of the pie chart.
     * @param startAngle Start angle of the slice.
     * @param endAngle End angle of the slice.
     */
    override fun generateSlicePath(
        selection: Float,
        cx: Float,
        cy: Float,
        outerRadius: Float,
        startAngle: Float,
        endAngle: Float
    ) {
        val thickness = min(
            outerRadius,
            (1 - selection) * donutWidth + selection * selectedDonutWidth
        )
        path.set {
            addRingSector(
                cx = cx,
                cy = cy,
                radius = outerRadius,
                startAngle = startAngle,
                sweepAngle = endAngle - startAngle,
                thickness = thickness,
                inset = donutSpacing / 2
            )
        }
    }
}
