package self.adragon.aviarouteanalyse.ui.adapters.flightGroupByAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.Flight
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter
import self.adragon.aviarouteanalyse.data.model.enums.FlightEntries
import self.adragon.aviarouteanalyse.ui.adapters.flightGroupByAdapters.ChildAdapter.ChildViewHolder
import self.adragon.aviarouteanalyse.ui.adapters.flightGroupByAdapters.ChildAdapter.OnItemClickListener

class ChildAdapter(private val groupBy: FlightEntries) :
    RecyclerView.Adapter<ChildViewHolder>() {
    private var data: List<Flight> = emptyList()
    private var itemClickListener: OnItemClickListener = OnItemClickListener { }

    private val localDateConverter = LocalDateConverter()

    fun interface OnItemClickListener {
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.flight_list_item, parent, false)
        return ChildViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val child = data[position]

        holder.apply {


            idTextView.text = child.flightID.toString()
            airlineTextView.text = child.airline
            dateTextView.text = localDateConverter.fromDateToString(child.departureDate)
            priceTextView.text = child.price.toString()
            destinationTextView.text = child.destinationAirport
            departureTextView.text = child.departureAirport
        }
    }

    fun fillData(newData: List<Flight>) {
        data = newData
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flightCardView: CardView = itemView.findViewById(R.id.flightCardView)

        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val airlineTextView: TextView = itemView.findViewById(R.id.airlineTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val destinationTextView: TextView = itemView.findViewById(R.id.destinationTextView)
        val departureTextView: TextView = itemView.findViewById(R.id.departureTextView)

        init {
            flightCardView.setOnClickListener {
                itemClickListener.onItemClick()
            }
            when (groupBy) {
                FlightEntries.FLIGHT_ID -> idTextView
                FlightEntries.AIRLINE -> airlineTextView
                FlightEntries.DEPARTURE_AIRPORT -> departureTextView
                FlightEntries.DESTINATION_AIRPORT -> destinationTextView
                FlightEntries.PRICE -> priceTextView
                FlightEntries.DEPARTURE_DATE -> dateTextView
            }.visibility = View.GONE
        }
    }
}