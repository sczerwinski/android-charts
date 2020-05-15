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

/**
 * Pie chart UI slice transformation.
 */
interface PieChartUITransformation {

    /**
     * Pre-concatenates the current matrix of the canvas with a transformation
     * determined by the UI of a pie chart.
     *
     * @param canvas Canvas to draw on.
     * @param radius Radius of the pie chart.
     * @param startAngle Start angle of the slice.
     * @param endAngle End angle of the slice.
     * @param selection Fraction of the slice being selected.
     */
    fun transform(
        canvas: Canvas,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        selection: Float
    )
}
