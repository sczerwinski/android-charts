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
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import it.czerwinski.android.charts.core.TextPaint
import it.czerwinski.android.charts.core.drawTextAdvanced
import it.czerwinski.android.graphics.FULL_ANGLE
import it.czerwinski.android.graphics.degToRad
import kotlin.math.cos
import kotlin.math.sin

/**
 * Simple labels UI for [PieChart].
 */
class SimplePieChartLabelsUI @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : PieChartLabelsUI {

    private val textPaint = TextPaint()

    private val measuredTextBounds = Rect()

    private var labelPosition: Int = 1

    private var labelSpacing: Float = 0f

    private var labelMinPercent: Int = 0

    init {
        context?.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.SimplePieChartLabelsUI,
            defStyleRes = defStyleRes
        ) {
            initAttrs(context, attrs)
        }
    }

    private fun TypedArray.initAttrs(context: Context, attrs: AttributeSet?) {
        labelPosition = getInt(
            R.styleable.SimplePieChartLabelsUI_simplePieChartLabelsUI_labelPosition, 1
        )
        labelSpacing = getDimension(
            R.styleable.SimplePieChartLabelsUI_simplePieChartLabelsUI_labelSpacing, 0f
        )
        labelMinPercent = getInt(
            R.styleable.SimplePieChartLabelsUI_simplePieChartLabelsUI_labelMinPercent, 0
        )
        context.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.TextPaint,
            defStyleRes = getResourceId(
                R.styleable.SimplePieChartLabelsUI_simplePieChartLabelsUI_textAppearance, 0
            )
        ) {
            textPaint.applyFrom(context, this)
        }
    }

    override fun measureText(text: String, bounds: Rect) {
        textPaint.getTextBounds(text, 0, text.length, bounds)
    }

    override fun draw(
        canvas: Canvas,
        cx: Float,
        cy: Float,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        label: String?
    ) {
        if ((label != null) && (endAngle - startAngle) / FULL_ANGLE * 100 >= labelMinPercent) {
            val midAngle = (startAngle + endAngle) / 2f
            val midAngleCos = cos(midAngle.degToRad())
            val midAngleSin = sin(midAngle.degToRad())

            textPaint.getTextBounds(label, 0, label.length, measuredTextBounds)
            val textHalfWidth = measuredTextBounds.width() / 2f
            val textHalfHeight = measuredTextBounds.height() / 2f

            val anchorRadius = radius + labelSpacing * labelPosition
            val textCX = cx + (anchorRadius + textHalfWidth * labelPosition) * midAngleCos
            val textCY = cy + (anchorRadius + textHalfHeight * labelPosition) * midAngleSin

            canvas.drawTextAdvanced(
                text = label,
                x = textCX - textHalfWidth,
                y = textCY + textHalfHeight,
                paint = textPaint
            )
        }
    }
}
