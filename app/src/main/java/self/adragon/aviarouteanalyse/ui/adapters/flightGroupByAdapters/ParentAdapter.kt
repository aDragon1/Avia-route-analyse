package self.adragon.aviarouteanalyse.ui.adapters.flightGroupByAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.Flight
import self.adragon.aviarouteanalyse.data.model.enums.FlightEntries
import self.adragon.aviarouteanalyse.ui.adapters.flightGroupByAdapters.ParentAdapter.ParentViewHolder

class ParentAdapter(private val groupBy: FlightEntries) :
    RecyclerView.Adapter<ParentViewHolder>() {
    private var parentItems: Map<String, List<Flight>> = emptyMap()
    private var parentsIsExpanded: MutableList<Boolean> = MutableList(0) { false }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.flight_group_by_expandable_parent_item, parent, false)
        return ParentViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        fun changeState(newState: Boolean) {
            parentsIsExpanded[position] = newState
            notifyItemChanged(position)
        }

        val parentTitle = parentItems.keys.toList()[position]
        val childrenData = parentItems[parentTitle] ?: emptyList()
        val isExpanded = parentsIsExpanded[position]

        holder.apply {
            parentArrowTextView.text = if (isExpanded) "↓" else "↑"
            parentTextView.text =
                if (groupBy == FlightEntries.PRICE) "$parentTitle у.е." else parentTitle

            parentRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            parentRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            parentRecyclerView.adapter = childAdapter

            parentCardView.setOnClickListener {
                changeState(!isExpanded)
            }
            childAdapter.setOnItemClickListener {
                changeState(false)
            }

            childAdapter.fillData(childrenData)
        }
    }

    override fun getItemCount(): Int = parentItems.size

    fun fillData(parents: Map<String, List<Flight>>) {
        parentItems = parents
        parentsIsExpanded = MutableList(parents.size) { false }
    }

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentCardView: CardView = itemView.findViewById(R.id.parentCardView)

        val parentTextView: TextView = itemView.findViewById(R.id.parentTextView)
        val parentArrowTextView: TextView = itemView.findViewById(R.id.parentArrowTextView)
        val parentRecyclerView: RecyclerView = itemView.findViewById(R.id.parentRecyclerView)

        val childAdapter = ChildAdapter(groupBy)
    }
}