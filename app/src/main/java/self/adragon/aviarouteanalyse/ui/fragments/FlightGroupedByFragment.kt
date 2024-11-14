package self.adragon.aviarouteanalyse.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter
import self.adragon.aviarouteanalyse.data.model.enums.FlightEntries
import self.adragon.aviarouteanalyse.ui.adapters.flightGroupByAdapters.ParentAdapter
import self.adragon.aviarouteanalyse.ui.dialogs.FlightGroupByDialog
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class FlightGroupedByFragment : Fragment(R.layout.flight_grouped_by) {
    private lateinit var groupDataRecyclerView: RecyclerView
    private lateinit var groupByImageButton: ImageButton

    private val flightViewModel: FlightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupByImageButton = view.findViewById(R.id.groupByImageButton)

        groupDataRecyclerView = view.findViewById(R.id.groupDataRecyclerView)
        groupDataRecyclerView.layoutManager = LinearLayoutManager(context)

        val groupDateDialog = FlightGroupByDialog()
        var isGroupDataDialogShown = false
        groupByImageButton.setOnClickListener {
            if (!isGroupDataDialogShown) {
                groupDateDialog.show(childFragmentManager, "GroupDataDialogTag")
                isGroupDataDialogShown = true
            }
        }

        groupDateDialog.setOnCustomDismissListener {
            isGroupDataDialogShown = false
        }

        // TODO rework it later (so complicated, I guess)
        flightViewModel.flights.observe(viewLifecycleOwner) { flights ->
            flightViewModel.groupBy.observe(viewLifecycleOwner) { groupBy ->
                val grouped = flights.groupBy {
                    when (groupBy) {
                        FlightEntries.AIRLINE -> it.airline
                        FlightEntries.DEPARTURE_AIRPORT -> it.departureAirport
                        FlightEntries.DESTINATION_AIRPORT -> it.destinationAirport
                        FlightEntries.PRICE -> it.price.toString()
                        FlightEntries.DEPARTURE_DATE -> LocalDateConverter().fromDateToString(it.departureDate)

                        else -> it.airline
                    }
                }

                if (groupBy != null) {
                    val adapter = ParentAdapter(groupBy)
                    adapter.fillData(grouped)
                    groupDataRecyclerView.adapter = adapter
                }
            }
        }
    }
}
