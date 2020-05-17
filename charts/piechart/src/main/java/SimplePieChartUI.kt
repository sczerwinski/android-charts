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

/**
 * Simple UI for pie chart view.
 *
 * Draws a classic pie chart.
 *
 * @constructor Creates a simple UI for pie chart view.
 * @param context The context.
 * @param attrs Set of styleable attributes.
 * @param defStyleRes Default style resource.
 */
class SimplePieChartUI @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    @StyleRes defStyleRes: Int = 0
) : BasePieChartUI() {

    private var sliceSpacing = 0f

    init {
        context?.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.SimplePieChartUI,
            defStyleAttr = R.attr.simplePieChartStyle,
            defStyleRes = defStyleRes
        ) {
            initAttrs(context)
        }
    }

    private fun TypedArray.initAttrs(context: Context) {
        colors =
            getColors(context, R.styleable.SimplePieChartUI_simplePieChartUI_colors, Color.CYAN)
        shadowColor =
            getColor(R.styleable.SimplePieChartUI_simplePieChartUI_shadowColor, Color.BLACK)
        selectedElevation =
            getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_selectionElevation, 4f)
        sliceSpacing =
            getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_sliceSpacing, 0f)
        selectedShift =
            getDimension(R.styleable.SimplePieChartUI_simplePieChartUI_selectionShift, 0f)
    }

    /**
     * Generates path defining classic pie chart slice shape.
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
    }
}
