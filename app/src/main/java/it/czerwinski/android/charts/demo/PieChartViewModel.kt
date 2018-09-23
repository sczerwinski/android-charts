package it.czerwinski.android.charts.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class PieChartViewModel : ViewModel() {

    private val data = MutableLiveData<List<Float>>()

    val dataSet: LiveData<List<Float>> get() = data

    fun generateRandomData() {
        val random = Random()
        random.setSeed(Date().time)
        data.postValue((0 until DATA_SET_SIZE).map { random.nextFloat() + MIN_VALUE })
    }

    companion object {
        const val DATA_SET_SIZE = 5
        const val MIN_VALUE = .1f
    }
}
