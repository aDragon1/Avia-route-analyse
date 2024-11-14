package self.adragon.aviarouteanalyse.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.ui.adapters.ViewPagerAdapter
import self.adragon.aviarouteanalyse.ui.dialogs.FlightsListSearchParamsDialog.CustomDismiss
import self.adragon.aviarouteanalyse.ui.dialogs.flightListSearchParamsFragments.SearchParamsFilter
import self.adragon.aviarouteanalyse.ui.dialogs.flightListSearchParamsFragments.SearchParamsSearch
import self.adragon.aviarouteanalyse.ui.dialogs.flightListSearchParamsFragments.SearchParamsSort
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class FlightsListSearchParamsDialog : DialogFragment(R.layout.flight_list_search_params) {

    fun interface CustomDismiss {
        fun onDismiss()
    }

    fun setOnCustomDismissListener(dismiss: CustomDismiss) {
        customDismiss = dismiss
    }

    private var customDismiss = CustomDismiss {}
    override fun onStart() {
        super.onStart()

        val mp = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(mp, mp)
    }

    private val flightViewModel: FlightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeImgButton = view.findViewById<ImageButton>(R.id.closeImgButton)
        closeImgButton.setOnClickListener { dismiss() }

        val tabLayout = view.findViewById<TabLayout>(R.id.searchParamsTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.searchParamsViewPager)

        val applyFilterButton = view.findViewById<Button>(R.id.applyFilterButton)

        val fragments = listOf(SearchParamsSearch(), SearchParamsSort(), SearchParamsFilter())
        val adapter = ViewPagerAdapter(fragments, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, i ->
            tab.text = when (i) {
                0 -> "Поиск"
                1 -> "Сортировка"
                2 -> "Фильтр"
                else -> "Забыл добавить текст вкладки"
            }
        }.attach()

        applyFilterButton.setOnClickListener {
            flightViewModel.applyTemp()
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        customDismiss.onDismiss()
    }
}