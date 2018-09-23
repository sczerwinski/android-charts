package it.czerwinski.android.charts.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import it.czerwinski.android.charts.demo.databinding.PieChartFragmentBinding
import it.czerwinski.android.charts.piechart.adapters.FloatListPieChartAdapter
import kotlinx.android.synthetic.main.pie_chart_fragment.*

class PieChartFragment : Fragment() {

    private val pieChartViewModel: PieChartViewModel
        get() = ViewModelProviders.of(this)[PieChartViewModel::class.java]

    private val adapter = FloatListPieChartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<PieChartFragmentBinding>(
            inflater,
            R.layout.pie_chart_fragment,
            container,
            false
        ).apply {
            viewModel = pieChartViewModel
            setLifecycleOwner(this@PieChartFragment)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        pieChartViewModel.dataSet.observe(this, Observer { data ->
            adapter.data = data
        })
        pieChartViewModel.generateRandomData()
    }
}
