package it.czerwinski.android.charts.demo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val navController: NavController
        get() = (supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment)
            .findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initTabs()
    }

    private fun initTabs() {
        tabs.getTabAt(mainViewModel.selectedTabIndex.value ?: 0)?.select()
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let(this@MainActivity::onTabSelected)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun onTabSelected(tab: TabLayout.Tab) {
        mainViewModel.selectedTabIndex.postValue(tab.position)
        with (Tab.values()[tab.position]) {
            navController.navigate(action)
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()

    enum class Tab(@IdRes val action: Int) {
        PIE_CHART(R.id.actionPieChart),
        DONUT_CHART(R.id.actionDonutChart)
    }
}
