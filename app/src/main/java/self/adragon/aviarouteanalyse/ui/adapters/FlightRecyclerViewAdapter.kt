package self.adragon.aviarouteanalyse.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import self.adragon.aviarouteanalyse.R
import kotlin.math.max

// Adapter for table and list view
class FlightRecyclerViewAdapter(private val layoutID: Int, private val isTable: Boolean) :
    RecyclerView.Adapter<FlightRecyclerViewAdapter.TableRVVieHolder>() {

    private var data: List<List<String>> = emptyList()
    private var maxColumnWidths: IntArray = IntArray(6)

    inner class TableRVVieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val airlineTextView: TextView = itemView.findViewById(R.id.airlineTextView)
        val departureTextView: TextView = itemView.findViewById(R.id.departureTextView)
        val destinationTextView: TextView = itemView.findViewById(R.id.destinationTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableRVVieHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(layoutID, parent, false)

        return TableRVVieHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TableRVVieHolder, position: Int) {
        val elements = data[position]

        holder.apply {
            idTextView.text = elements[0]
            airlineTextView.text = elements[1]
            departureTextView.text = elements[2]
            destinationTextView.text = elements[3]
            priceTextView.text = "${elements[4]} у.е."
            dateTextView.text = elements[5]

            if (!isTable) return@apply
            setDrawable(holder, R.drawable.table_framed_drawable)

            if (position == 0)
                setDrawable(holder, R.drawable.table_header_framed_drawable)

            setWidth(holder)
        }
    }

    private fun setDrawable(holder: TableRVVieHolder, drawableID: Int) {
        holder.apply {
            idTextView.setBackgroundResource(drawableID)
            airlineTextView.setBackgroundResource(drawableID)
            departureTextView.setBackgroundResource(drawableID)
            destinationTextView.setBackgroundResource(drawableID)
            priceTextView.setBackgroundResource(drawableID)
            dateTextView.setBackgroundResource(drawableID)
        }
    }

    private fun setWidth(holder: TableRVVieHolder) {
        holder.apply {
            idTextView.layoutParams.width = maxColumnWidths[0]
            airlineTextView.layoutParams.width = maxColumnWidths[1]
            departureTextView.layoutParams.width = maxColumnWidths[2]
            destinationTextView.layoutParams.width = maxColumnWidths[3]
            priceTextView.layoutParams.width = maxColumnWidths[4]
            dateTextView.layoutParams.width = maxColumnWidths[5]
        }
    }

    fun fillData(newData: List<List<String>>) {
        data = newData

        if (isTable)
            calculateMaxColumnWidths()
    }

    private fun calculateMaxColumnWidths() {
        for (element in data) {
            maxColumnWidths[0] = max(maxColumnWidths[0], element[0].length)
            maxColumnWidths[1] = max(maxColumnWidths[1], element[1].length)
            maxColumnWidths[2] = max(maxColumnWidths[2], element[2].length)
            maxColumnWidths[3] = max(maxColumnWidths[3], element[3].length)
            maxColumnWidths[4] = max(maxColumnWidths[4], element[4].length)
            maxColumnWidths[5] = max(maxColumnWidths[5], element[5].length)
        }

        for (i in maxColumnWidths.indices)
            maxColumnWidths[i] *= 30
        maxColumnWidths[0] *= 2 // random magic numbers, work 4 me, idc
    }
}