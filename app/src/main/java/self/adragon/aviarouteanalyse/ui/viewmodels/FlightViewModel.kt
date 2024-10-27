package self.adragon.aviarouteanalyse.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import self.adragon.aviarouteanalyse.data.database.FlightsDatabase
import self.adragon.aviarouteanalyse.data.model.Flight
import self.adragon.aviarouteanalyse.data.model.SortOrder
import self.adragon.aviarouteanalyse.data.repo.FlightRepository
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter
import java.time.LocalDate

class FlightViewModel(application: Application) : AndroidViewModel(application) {
    private val flightRepository: FlightRepository

    private val mediatorFLights = MediatorLiveData<List<Flight>>()

    val originalFlights: LiveData<List<Flight>>
    val flights: LiveData<List<Flight>> get() = mediatorFLights

    private val sortOrder = MutableLiveData(SortOrder.DEFAULT)
    private val minPrice = MutableLiveData<Float>()
    private val maxPrice = MutableLiveData<Float>()

    private val minDate = MutableLiveData<LocalDate>()
    private val maxDate = MutableLiveData<LocalDate>()

    private val localDateConverter = LocalDateConverter()

    init {
        val db: FlightsDatabase = FlightsDatabase.getDatabase(application)
        val flightDao = db.flightDAO()
        flightRepository = FlightRepository(flightDao)

        originalFlights = flightRepository.getAllFlights()

        mediatorFLights.addSource(originalFlights) { flights ->
            mediatorFLights.value = flights ?: emptyList()
        }

        mediatorFLights.addSource(sortOrder) { viewModelScope.launch { updateFlights() } }
        mediatorFLights.addSource(minPrice) { viewModelScope.launch { updateFlights() } }
        mediatorFLights.addSource(maxPrice) { viewModelScope.launch { updateFlights() } }
        mediatorFLights.addSource(minDate) { viewModelScope.launch { updateFlights() } }
        mediatorFLights.addSource(maxDate) { viewModelScope.launch { updateFlights() } }
    }

    fun insert(flight: Flight) = viewModelScope.launch {
        flightRepository.insert(flight)
    }

    fun setSortOrder(order: SortOrder) {
        if (order != sortOrder.value)
            sortOrder.value = order
    }

    fun getSortOrder() = sortOrder.value

    fun setPriceRange(mPrice: Float, mxPrice: Float) {
        minPrice.value = mPrice
        maxPrice.value = mxPrice
    }

    fun setDateRange(mDate: LocalDate, mxDate: LocalDate) {
        minDate.value = mDate
        maxDate.value = mxDate
    }

    private suspend fun updateFlights() {
        fun filterFlights(
            it: Flight,
            minPriceValue: Float?,
            maxPriceValue: Float?,
            minDateValue: LocalDate?,
            maxDateValue: LocalDate?
        ) = ((minPriceValue == null || it.price >= minPriceValue) &&
                (maxPriceValue == null || it.price <= maxPriceValue) &&
                (minDateValue == null || it.departureDate >= minDateValue) &&
                (maxDateValue == null || it.departureDate <= maxDateValue))

        val originalFlights = originalFlights.value ?: emptyList()
        var filteredList = when (sortOrder.value) {
            SortOrder.DEFAULT -> originalFlights
            SortOrder.PRICE_UP -> flightRepository.sortByPrice(true)
            SortOrder.PRICE_DOWN -> flightRepository.sortByPrice(false)
            SortOrder.DATE_UP -> flightRepository.sortByDate(true)
            SortOrder.DATE_DOWN -> flightRepository.sortByDate(false)

            else -> originalFlights
        }

        filteredList = filteredList.filter {
            filterFlights(it, minPrice.value, maxPrice.value, minDate.value, maxDate.value)
        }

        mediatorFLights.value = filteredList
    }

    fun getMinMaxPrice(): Pair<Float, Float> {
        val prices = originalFlights.value?.map { it.price }

        val min = (prices?.minOrNull() ?: 0).toFloat()
        val max = (prices?.maxOrNull() ?: 0).toFloat()

        return min to max
    }

    fun getMinMaxDate(): Pair<LocalDate, LocalDate> {
        val dates = originalFlights.value?.map { it.departureDate }

        val min = dates?.minOrNull() ?: LocalDate.now()
        val max = dates?.maxOrNull() ?: LocalDate.now()

        return min to max
    }
}