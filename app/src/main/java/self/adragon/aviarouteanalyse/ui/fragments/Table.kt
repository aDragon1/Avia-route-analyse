package self.adragon.aviarouteanalyse.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter
import self.adragon.aviarouteanalyse.ui.adapters.FlightRecyclerViewAdapter
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class Table : Fragment(R.layout.table) {

    private val flightViewModel: FlightViewModel by viewModels()
    private val localDateConverter = LocalDateConverter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tableRecyclerView = view.findViewById<RecyclerView>(R.id.tableRecyclerView)

        tableRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapterData =
            mutableListOf(listOf("ID", "Авиакомпания", "Откуда", "Куда", "Цена", "Дата"))

        val adapter = FlightRecyclerViewAdapter(R.layout.table_row, true )
        flightViewModel.originalFlights.observe(viewLifecycleOwner) { flights ->
            val mappedFlights = flights.map {
                listOf(
                    it.flightID.toString(), it.airline,
                    it.destinationAirport, it.departureAirport,
                    "${it.price}" , localDateConverter.fromDateToString(it.departureDate)
                )
            }

            adapterData.addAll(mappedFlights)

            adapter.fillData(adapterData)
            tableRecyclerView.adapter = adapter
        }
    }

}