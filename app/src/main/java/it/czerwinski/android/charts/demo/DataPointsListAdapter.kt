package it.czerwinski.android.charts.demo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

class DataPointsListAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<DataPointViewHolder>(), Observer<List<Float>> {

    private var dataPoints: List<Float> = emptyList()

    override fun getItemCount(): Int = dataPoints.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataPointViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.data_point_list_item, parent, false)
            .let { DataPointViewHolder(it) }

    override fun onBindViewHolder(holder: DataPointViewHolder, position: Int) {
        holder.bind(position, dataPoints[position], onItemClicked)
    }

    override fun onChanged(value: List<Float>?) {
        dataPoints = value.orEmpty()
        notifyDataSetChanged()
    }
}

class DataPointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textView: TextView? = itemView.findViewById(R.id.text)

    fun bind(index: Int, value: Float, onItemClicked: (position: Int) -> Unit) {
        textView?.apply {
            text = context.resources
                .getString(R.string.format_chart_data, index + 1, value)
            setOnClickListener { onItemClicked(index) }
        }
    }
}
