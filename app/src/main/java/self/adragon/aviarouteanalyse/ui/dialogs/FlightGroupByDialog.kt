package self.adragon.aviarouteanalyse.ui.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.enums.FlightEntries
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class FlightGroupByDialog : DialogFragment(R.layout.search_params_sort) {

    fun interface CustomDismiss {
        fun onDismiss()
    }

    fun setOnCustomDismissListener(dismiss: CustomDismiss) {
        customDismiss = dismiss
    }

    private var customDismiss = CustomDismiss {}

    private val flightViewModel: FlightViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        val mp = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(mp, mp)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        val b2 = createRadioButton("Авиакомпании")
        val b3 = createRadioButton("Аэропорту отправления")
        val b4 = createRadioButton("Аэропорту назначения")
        val b5 = createRadioButton("Цене")
        val b6 = createRadioButton("Дате отправления")

        val buttons = listOf(b2, b3, b4, b5, b6)
        buttons.forEach { radioGroup.addView(it) }

        val index = when (flightViewModel.getGroupBy()) {
            FlightEntries.AIRLINE -> 0
            FlightEntries.DESTINATION_AIRPORT -> 1
            FlightEntries.DEPARTURE_AIRPORT -> 2
            FlightEntries.PRICE -> 3
            FlightEntries.DEPARTURE_DATE -> 4
            else -> 0
        }
        buttons[index].isChecked = true

        radioGroup.setOnCheckedChangeListener { group: RadioGroup, checkedID: Int ->
            val button = group.findViewById<RadioButton>(checkedID)
            val groupBy = when (buttons.indexOf(button)) {
                0 -> FlightEntries.AIRLINE
                1 -> FlightEntries.DESTINATION_AIRPORT
                2 -> FlightEntries.DEPARTURE_AIRPORT
                3 -> FlightEntries.PRICE
                4 -> FlightEntries.DEPARTURE_DATE

                else -> FlightEntries.AIRLINE
            }
            flightViewModel.setGroupBy(groupBy)
        }
    }

    private fun createRadioButton(text: String): RadioButton {
        val b = RadioButton(context)
        b.text = text
        b.textSize = 16f

        return b
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        customDismiss.onDismiss()
    }
}