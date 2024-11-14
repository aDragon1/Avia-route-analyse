package self.adragon.aviarouteanalyse.ui.dialogs.flightListSearchParamsFragments

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.enums.SortOrder
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class SearchParamsSort : Fragment(R.layout.search_params_sort) {

    private val flightViewModel: FlightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        val b1 = createRadioButton("По умолчанию")
        val b2 = createRadioButton("Цене ↑")
        val b3 = createRadioButton("Цене ↓")
        val b4 = createRadioButton("Дате ↑")
        val b5 = createRadioButton("Дате ↓")

        val radioButtons = listOf(b1, b2, b3, b4, b5)
        radioButtons.forEach { radioGroup.addView(it) }

        val index = when (flightViewModel.getSortOrder()) {
            SortOrder.DEFAULT -> 0
            SortOrder.PRICE_UP -> 1
            SortOrder.PRICE_DOWN -> 2
            SortOrder.DATE_UP -> 3
            SortOrder.DATE_DOWN -> 4
            null -> 0
        }
        radioButtons[index].isChecked = true

        radioGroup.setOnCheckedChangeListener { group: RadioGroup, checkedID: Int ->
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

    private fun createRadioButton(text: String): RadioButton {
        val b = RadioButton(context)
        b.text = text
        b.textSize = 16f

        return b
    }
}