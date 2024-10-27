package self.adragon.aviarouteanalyse.ui.dialogs.searchParmsFragments

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.SortOrder
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class SearchParamsSort : Fragment(R.layout.search_params_sort) {

    private val flightViewModel: FlightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val group = view.findViewById<RadioGroup>(R.id.radioGroup)

        // Look current seq in .xml file. So wrong but meh
        val radioButtons = mutableListOf<RadioButton>()
        group.children.forEach {
            it as RadioButton
            radioButtons.add(it)
        }

        val index = when (flightViewModel.getSortOrder()) {
            SortOrder.DEFAULT -> 0
            SortOrder.PRICE_UP -> 1
            SortOrder.PRICE_DOWN -> 2
            SortOrder.DATE_UP -> 3
            SortOrder.DATE_DOWN -> 4
            null -> 0
        }
        radioButtons[index].isChecked = true

        group.setOnCheckedChangeListener { group: RadioGroup, checkedID: Int ->
            val button = group.findViewById<RadioButton>(checkedID)
            val order = when (radioButtons.indexOf(button)) {
                0 -> SortOrder.DEFAULT
                1 -> SortOrder.PRICE_UP
                2 -> SortOrder.PRICE_DOWN
                3 -> SortOrder.DATE_UP
                4 -> SortOrder.DATE_DOWN

                else -> SortOrder.DEFAULT
            }

            flightViewModel.setSortOrder(order)
        }
    }
}