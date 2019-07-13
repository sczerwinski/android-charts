package it.czerwinski.android.charts.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import it.czerwinski.android.charts.demo.databinding.PieChartFragmentBinding
import it.czerwinski.android.charts.piechart.PieChart
import it.czerwinski.android.charts.piechart.adapters.FloatListPieChartAdapter
import kotlinx.android.synthetic.main.pie_chart_fragment.*

class PieChartFragment : Fragment() {

    private val pieChartViewModel: PieChartViewModel
        get() = ViewModelProviders.of(this)[PieChartViewModel::class.java]

    private val adapter by lazy {
        FloatListPieChartAdapter(requireContext()).apply {
            registerObserver(object : PieChart.DataSetObserver {
                override fun onDataSetChanged() {
                    pieChart.clearSelection()
                }
            })
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

        dataList.adapter = DataPointsListAdapter { position ->
            pieChart.selectionIndex = position
        }.also {
            pieChartViewModel.dataSet.observe(this, it)
        }
    }

    override fun onResume() {
        super.onResume()

        pieChartViewModel.dataSet.observe(this, Observer { data ->
            adapter.data = data
        })
    }

    enum class ChartType(@StyleRes val theme: Int) {
        SIMPLE(theme = R.style.AppTheme_SimplePieChart),
        DONUT(theme = R.style.AppTheme_DonutPieChart)
    }
}
