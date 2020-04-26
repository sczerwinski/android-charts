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
import it.czerwinski.android.charts.core.TextPaint
import it.czerwinski.android.graphics.FULL_ANGLE
import it.czerwinski.android.graphics.degToRad
import kotlin.math.*

/**
 * A delegate for a [PieChartUI], drawing pie chart labels.
 */
class SimplePieChartLabelUIDelegate {

    private val textPaint = TextPaint()

    private val measuredTextBounds = Rect()

    private var labelPosition: Int = 1

    private var labelSpacing: Float = 0f

    private var labelMinPercent: Int = 0

    /**
     * Applies the style from the [typedArray] in a given [context].
     */
    fun applyFrom(
        context: Context?,
        typedArray: TypedArray,
        labelPosition: Int,
        labelSpacing: Float,
        labelMinPercent: Int
    ) {
        textPaint.applyFrom(context, typedArray)
        this.labelPosition = labelPosition
        this.labelSpacing = labelSpacing
        this.labelMinPercent = labelMinPercent
    }

    /**
     * Measures padding required to draw the labels, and applies it to the given `Rect`.
     *
     * @param labels Pie chart labels.
     * @param pieChartRect An instance of `RectF`, to which the padding will be applied.
     */
    fun applyLabelsPadding(labels: Iterable<String>, pieChartRect: Rect) {
        if (labelPosition <= 0) return

        var horizontalPadding = 0f
        var verticalPadding = 0f

        for (label in labels) {
            textPaint.getTextBounds(label, 0, label.length, measuredTextBounds)
            horizontalPadding = max(horizontalPadding, measuredTextBounds.width() + labelSpacing)
            verticalPadding = max(verticalPadding, measuredTextBounds.height() + labelSpacing)
        }

        pieChartRect.inset(horizontalPadding.roundToInt(), verticalPadding.roundToInt())
    }

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
        label: String
    ) {
        if ((endAngle - startAngle) / FULL_ANGLE * 100 >= labelMinPercent) {
            val midAngle = (startAngle + endAngle) / 2f
            val midAngleCos = cos(midAngle.degToRad())
            val midAngleSin = sin(midAngle.degToRad())

            textPaint.getTextBounds(label, 0, label.length, measuredTextBounds)
            val textHalfWidth = measuredTextBounds.width() / 2f
            val textHalfHeight = measuredTextBounds.height() / 2f

            val anchorRadius = radius + labelSpacing * labelPosition
            val textCX = cx + (anchorRadius + textHalfWidth * labelPosition) * midAngleCos
            val textCY = cy + (anchorRadius + textHalfHeight * labelPosition) * midAngleSin

            canvas.drawText(
                label,
                textCX - textHalfWidth,
                textCY + textHalfHeight,
                textPaint
            )
        }
    }
}
