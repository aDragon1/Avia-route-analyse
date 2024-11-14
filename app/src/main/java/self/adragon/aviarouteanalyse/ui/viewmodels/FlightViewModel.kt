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
import self.adragon.aviarouteanalyse.data.model.enums.FlightEntries
import self.adragon.aviarouteanalyse.data.model.enums.SortOrder
import self.adragon.aviarouteanalyse.data.repo.FlightRepository
import java.time.LocalDate

class FlightViewModel(application: Application) : AndroidViewModel(application) {
    private val flightRepository: FlightRepository

    private val mediatorFLights = MediatorLiveData<List<Flight>>()

    val originalFlights: LiveData<List<Flight>>
    val flights: LiveData<List<Flight>> get() = mediatorFLights

    private val sortOrder = MutableLiveData(SortOrder.DEFAULT)
    val groupBy = MutableLiveData(FlightEntries.AIRLINE)

    private val minPrice = MutableLiveData<Float?>()
    private val maxPrice = MutableLiveData<Float?>()

    private val minDate = MutableLiveData<LocalDate?>()
    private val maxDate = MutableLiveData<LocalDate?>()

    private val departureAirport = MutableLiveData<String?>()
    private val destinationAirport = MutableLiveData<String?>()

    private var tempOrder: SortOrder? = null
    private var tempGBy: FlightEntries? = null

    private var tempDepAirport: String? = null
    private var tempDestAirport: String? = null

    private var tempMinDate: LocalDate? = null
    private var tempMaxDate: LocalDate? = null

    private var tempMinPrice: Float? = null
    private var tempMaxPrice: Float? = null

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
        mediatorFLights.addSource(destinationAirport) { viewModelScope.launch { updateFlights() } }
        mediatorFLights.addSource(departureAirport) { viewModelScope.launch { updateFlights() } }
    }

    fun insert(flight: Flight) = viewModelScope.launch {
        flightRepository.insert(flight)
    }

    suspend fun getDepartureAirports() = flightRepository.getDepartureAirports()
    suspend fun getDestinationAirports() = flightRepository.getDestinationAirports()

    fun applyTemp() {
        sortOrder.value = tempOrder

        groupBy.value = if (tempGBy != null) tempGBy else FlightEntries.AIRLINE

        minDate.value = tempMinDate
        maxDate.value = tempMaxDate

        minPrice.value = tempMinPrice
        maxPrice.value = tempMaxPrice

        departureAirport.value = tempDepAirport
        destinationAirport.value = tempDestAirport

        Log.d("mytag", "ApplyTemp - dep = $tempDepAirport , dest = $tempDestAirport")

    }

    fun setSortOrder(order: SortOrder) {
        if (order != sortOrder.value)
            tempOrder = order
    }

    fun setGroupBy(gBy: FlightEntries) {
        if (gBy != groupBy.value)
            tempGBy = gBy
    }

    fun setEndpointAirports(dest: String?, dep: String?) {
        if (departureAirport.value != dep && dep != null)
            tempDepAirport = dep.ifBlank { null }

        if (destinationAirport.value != dest && dest != null)
            tempDestAirport = dest.ifBlank { null }
        Log.d("mytag", "setEndpointAirports - dep = $tempDepAirport , dest = $tempDestAirport")
    }

    fun getSortOrder() = sortOrder.value
    fun getGroupBy() = groupBy.value

    fun setPriceRange(mPrice: Float, mxPrice: Float) {
        tempMinPrice = mPrice
        tempMaxPrice = mxPrice
    }

    fun setDateRange(mDate: LocalDate, mxDate: LocalDate) {
        tempMinDate = mDate
        tempMaxDate = mxDate
    }

    private suspend fun updateFlights() {
        val priceMin = minPrice.value
        val priceMax = maxPrice.value
        val dateMin = minDate.value
        val dateMax = maxDate.value
        val dest = destinationAirport.value
        val dep = departureAirport.value
        val order = sortOrder.value

        val filteredList = flightRepository.filterFlightByEverything(
            priceMin, priceMax, dateMin, dateMax, order
        ).filter { dest == null || it.destinationAirport == dest }
            .filter { dep == null || it.departureAirport == dep }

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