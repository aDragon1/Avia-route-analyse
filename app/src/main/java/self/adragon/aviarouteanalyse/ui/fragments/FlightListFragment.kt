package self.adragon.aviarouteanalyse.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter
import self.adragon.aviarouteanalyse.ui.adapters.FlightRecyclerViewAdapter
import self.adragon.aviarouteanalyse.ui.dialogs.FlightsListSearchParamsDialog
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class FlightListFragment : Fragment(R.layout.flight_list) {

    private val flightViewModel: FlightViewModel by activityViewModels()
    private val localDateConverter = LocalDateConverter()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.listFragmentRecyclerView)
        val filterSettingImgButton = view.findViewById<ImageButton>(R.id.filterSettingImgButton)
        val countElementsTextView = view.findViewById<TextView>(R.id.countElementsTextView)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = FlightRecyclerViewAdapter(R.layout.flight_list_item, false)
        flightViewModel.flights.observe(viewLifecycleOwner) { flights ->
            if (flights == null) return@observe
            val mappedFlights = flights.map {
                listOf(
                    it.flightID.toString(), it.airline,
                    it.destinationAirport, it.departureAirport,
                    "${it.price}", localDateConverter.fromDateToString(it.departureDate)
                )
            }
            adapter.fillData(mappedFlights)

            recyclerView.adapter = adapter
            countElementsTextView.text = "Элементов в списке: ${flights.size}"
        }

        val paramsDialog = FlightsListSearchParamsDialog()
        var isParamsDialogShown = false

        paramsDialog.setOnCustomDismissListener {
            isParamsDialogShown = false
        }

        val searchParamsTag = "searchParamsDialog"
        filterSettingImgButton.setOnClickListener {
            if (!isParamsDialogShown) {
                paramsDialog.show(childFragmentManager, searchParamsTag)
                isParamsDialogShown = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        flightViewModel.flights.removeObservers(viewLifecycleOwner)
    }
}