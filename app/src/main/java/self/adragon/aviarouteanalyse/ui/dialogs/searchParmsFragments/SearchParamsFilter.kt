package self.adragon.aviarouteanalyse.ui.dialogs.searchParmsFragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel
import java.time.LocalDate

class SearchParamsFilter : Fragment(R.layout.search_params_filter) {
    private val flightViewModel: FlightViewModel by activityViewModels()
    private lateinit var priceRangeSlider: RangeSlider
    private lateinit var dateRangeSlider: RangeSlider

    private lateinit var minPriceTextView: TextView
    private lateinit var maxPriceTextView: TextView
    private lateinit var minDateTextView: TextView
    private lateinit var maxDateTextView: TextView

    private val localDateConverted = LocalDateConverter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setSliderValues()
        setSliderListeners()
    }

    fun initializeViews(view: View) {
        priceRangeSlider = view.findViewById(R.id.priceRangeSlider)
        dateRangeSlider = view.findViewById(R.id.dateRangeSlider)

        priceRangeSlider.stepSize = 0.5f
        priceRangeSlider.isTickVisible = false

        dateRangeSlider.stepSize = 1f
        dateRangeSlider.isTickVisible = false

        minPriceTextView = view.findViewById(R.id.minPriceTextView)
        maxPriceTextView = view.findViewById(R.id.maxPriceTextView)
        minDateTextView = view.findViewById(R.id.minDateTextView)
        maxDateTextView = view.findViewById(R.id.maxDateTextView)

    }

    fun setSliderValues() {
        val (minPrice, maxPrice) = flightViewModel.getMinMaxPrice()
        val (minDate, maxDate) = flightViewModel.getMinMaxDate()

        val minDateValue = minDate.toEpochDay().toFloat()
        val maxDateValue = maxDate.toEpochDay().toFloat()

        priceRangeSlider.values = listOf(minPrice, maxPrice)
        dateRangeSlider.values = listOf(minDateValue, maxDateValue)

        priceRangeSlider.valueFrom = minPrice
        priceRangeSlider.valueTo = maxPrice
        dateRangeSlider.valueFrom = minDateValue
        dateRangeSlider.valueTo = maxDateValue

        updatePriceTextView(minPrice, maxPrice)
        updateDateTextView(minDateValue, maxDateValue)
    }

    fun setSliderListeners() {
        priceRangeSlider.addOnChangeListener { slider, value, fromUser ->
            updatePriceTextView(slider.values[0], slider.values[1])
        }

        dateRangeSlider.addOnChangeListener { slider, _, _ ->
            updateDateTextView(slider.values[0], slider.values[1])
        }
        dateRangeSlider.setLabelFormatter(object : LabelFormatter {
            override fun getFormattedValue(value: Float): String {
                val date = LocalDate.ofEpochDay(value.toLong())
                return localDateConverted.fromDateToString(date)
            }
        })

        priceRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val minSelectedValue = slider.values[0]
                val maxSelectedValue = slider.values[1]

                flightViewModel.setPriceRange(minSelectedValue, maxSelectedValue)
            }
        })

        dateRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val minSelectedValue = LocalDate.ofEpochDay(slider.values[0].toLong())
                val maxSelectedValue = LocalDate.ofEpochDay(slider.values[1].toLong())

                flightViewModel.setDateRange(minSelectedValue, maxSelectedValue)
            }
        })
    }

    fun updatePriceTextView(minValue: Float, maxValue: Float) {
        minPriceTextView.text = "$minValue"
        maxPriceTextView.text = "$maxValue"
    }

    fun updateDateTextView(minValue: Float, maxValue: Float) {
        val minDate = LocalDate.ofEpochDay(minValue.toLong())
        val maxDate = LocalDate.ofEpochDay(maxValue.toLong())

        val minDateStr = localDateConverted.fromDateToString(minDate)
        val maxDateStr = localDateConverted.fromDateToString(maxDate)

        minDateTextView.text = minDateStr
        maxDateTextView.text = maxDateStr
    }

}