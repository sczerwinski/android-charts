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

package it.czerwinski.android.charts.piechart.adapters

import android.content.Context
import it.czerwinski.android.charts.piechart.PieChart
import it.czerwinski.android.charts.piechart.R

/**
 * [PieChart] data set adapter for a list of `Float`s.
 *
 * @constructor Creates a [PieChart] data set adapter for a list of `Float`s.
 * @param initialData Initial data points.
 */
class FloatListPieChartAdapter @JvmOverloads constructor(
    context: Context,
    initialData: List<Float> = emptyList()
) : PieChart.DataSetAdapter() {

    private val labelFormat = context.getString(R.string.pie_chart_percent_format)

    /**
     * Data points.
     */
    var data: List<Float> = initialData
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * [PieChart] data set size.
     */
    override val size: Int get() = data.size

    /**
     * Sum of [PieChart] data set values.
     */
    override val sum: Float get() = data.sum()

    /**
     * Gets [PieChart] data set value at the given [index].
     */
    override fun get(index: Int): Float = data[index]

    /**
     * Gets [PieChart] data set label at the given [index].
     */
    override fun getLabel(index: Int): String =
        labelFormat.format((100f * get(index) / sum).toInt())
}
