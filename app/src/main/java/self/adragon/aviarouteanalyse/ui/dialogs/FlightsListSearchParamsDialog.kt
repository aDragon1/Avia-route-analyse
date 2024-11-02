package self.adragon.aviarouteanalyse.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.ui.adapters.ViewPagerAdapter
import self.adragon.aviarouteanalyse.ui.dialogs.flightListSearchParamsFragments.SearchParamsFilter
import self.adragon.aviarouteanalyse.ui.dialogs.flightListSearchParamsFragments.SearchParamsSort

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeImgButton = view.findViewById<ImageButton>(R.id.closeImgButton)
        closeImgButton.setOnClickListener { dismiss() }

        val tabLayout = view.findViewById<TabLayout>(R.id.searchParamsTabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.searchParamsViewPager)

        val fragments = listOf(SearchParamsSort(), SearchParamsFilter())
        val adapter = ViewPagerAdapter(fragments, requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, i ->
            tab.text = when (i) {
                0 -> "Сортировка"
                1 -> "Фильтр"
                else -> ""
            }
        }.attach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        customDismiss.onDismiss()
    }
}