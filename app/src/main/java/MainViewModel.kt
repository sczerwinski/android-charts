package it.czerwinski.android.charts.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val selectedTabIndex = MutableLiveData(0)
}
