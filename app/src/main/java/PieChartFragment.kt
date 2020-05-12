package it.czerwinski.android.charts.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import it.czerwinski.android.charts.demo.databinding.PieChartFragmentBinding
import it.czerwinski.android.charts.piechart.adapters.FloatListPieChartAdapter
import it.czerwinski.android.charts.piechart.addOnSelectionChangedListener
import it.czerwinski.android.charts.piechart.registerObserver
import kotlinx.android.synthetic.main.pie_chart_fragment.*
import kotlinx.android.synthetic.main.pie_chart_fragment_controls.*

class PieChartFragment : Fragment() {

    private val pieChartViewModel: PieChartViewModel by viewModels()

    private val adapter by lazy {
        FloatListPieChartAdapter(requireContext()).apply {
            registerObserver {
                pieChart.clearSelection()
            }
        }
    }

    private val chartType: ChartType
        get() = requireNotNull(arguments?.getSerializable("type") as? ChartType)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<PieChartFragmentBinding>(
            inflater.cloneInContext(ContextThemeWrapper(requireContext(), chartType.theme)),
            R.layout.pie_chart_fragment,
            container,
            false
        ).apply {
            viewModel = pieChartViewModel
            lifecycleOwner = this@PieChartFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart.adapter = adapter
        pieChart.addOnSelectionChangedListener { _, _, newSelectionIndex ->
            dataSelection.text =
                if (newSelectionIndex in 0 until adapter.size) {
                    getString(
                        R.string.format_chart_selection,
                        getString(
                            R.string.format_chart_data,
                            newSelectionIndex + 1,
                            adapter[newSelectionIndex]
                        )
                    )
                } else {
                    getString(R.string.format_chart_no_selection)
                }
        }

        dataList.layoutManager = GridLayoutManager(context, 3)
        dataList.adapter = DataPointsListAdapter { position ->
            pieChart.selectionIndex = position
        }.also {
            pieChartViewModel.dataSet.observe(viewLifecycleOwner, it)
        }
    }

    override fun onResume() {
        super.onResume()

        pieChartViewModel.dataSet.observe(this, Observer { data ->
            adapter.data = data
        })

        pieChartViewModel.generateRandomData()
    }

    enum class ChartType(@StyleRes val theme: Int) {
        SIMPLE(theme = R.style.AppTheme_SimplePieChart),
        DONUT(theme = R.style.AppTheme_DonutPieChart)
    }
}
