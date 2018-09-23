package it.czerwinski.android.charts.piechart.adapters

import it.czerwinski.android.charts.piechart.PieChart

/**
 * [PieChart] data set adapter for a list of `Float`s.
 *
 * @constructor Creates a [PieChart] data set adapter for a list of `Float`s.
 * @param initialData Initial data points.
 */
class FloatListPieChartAdapter @JvmOverloads constructor(
    initialData: List<Float> = emptyList()
) : PieChart.DataSetAdapter() {

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
}
