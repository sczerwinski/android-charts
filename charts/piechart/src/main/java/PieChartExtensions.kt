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

@file:JvmName("PieChartExtensions")

package it.czerwinski.android.charts.piechart

/**
 * Adds a listener of selection index changes.
 *
 * @receiver Pie chart view.
 * @param listener Listener to add.
 */
fun PieChart.addOnSelectionChangedListener(
    listener: (view: PieChart, oldSelectionIndex: Int, newSelectionIndex: Int) -> Unit
) {
    addOnSelectionChangedListener(object : PieChart.OnSelectionChangedListener {
        override fun onSelectionChangedListener(
            view: PieChart,
            oldSelectionIndex: Int,
            newSelectionIndex: Int
        ) {
            listener(view, oldSelectionIndex, newSelectionIndex)
        }
    })
}

/**
 * Registers an observer of data set changes.
 *
 * @receiver Pie chart data set adapter.
 * @param observer Observer to register.
 */
fun PieChart.DataSetAdapter.registerObserver(observer: () -> Unit) {
    registerObserver(object : PieChart.DataSetObserver {
        override fun onDataSetChanged() {
            observer()
        }
    })
}
