package self.adragon.aviarouteanalyse.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.database.FlightsDatabase
import self.adragon.aviarouteanalyse.ui.adapters.ViewPagerAdapter
import self.adragon.aviarouteanalyse.ui.fragments.ListData
import self.adragon.aviarouteanalyse.ui.fragments.Table
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel
import self.adragon.aviarouteanalyse.utils.Generator

@Suppress("SpellCheckingInspection")
class MainActivity : AppCompatActivity() {

    private val flightViewModel: FlightViewModel by viewModels()

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.mainViewPager2)
        tabLayout = findViewById(R.id.tabLayout)

        FlightsDatabase.deleteDatabase(applicationContext)
        val data = Generator().generateFlights()
        data.forEach { flightViewModel.insert(it) }

        val fragments = listOf(ListData(), Table())
        viewPager2.adapter = ViewPagerAdapter(fragments, this)
        viewPager2.isUserInputEnabled = false

        TabLayoutMediator(tabLayout, viewPager2) { tab, i ->
            tab.text = when (i) {
                0 -> "Список"
                1 -> "Таблица"
                2 -> ""
                else -> ""
            }
        }.attach()
    }
}

