package self.adragon.aviarouteanalyse.data.repo

import androidx.lifecycle.LiveData
import self.adragon.aviarouteanalyse.data.database.dao.FlightDao
import self.adragon.aviarouteanalyse.data.model.Flight
import self.adragon.aviarouteanalyse.data.model.enums.SortOrder
import java.time.LocalDate


class FlightRepository(private val flightDao: FlightDao) {

    fun getAllFlights(): LiveData<List<Flight>> = flightDao.getAllFlights()

    suspend fun getDepartureAirports() = flightDao.getDepartureAirports()
    suspend fun getDestinationAirports() = flightDao.getDestinationAirports()

    suspend fun insert(flight: Flight) = flightDao.insert(flight)

    suspend fun filterFlightByEverything(
        minPriceValue: Float?, maxPriceValue: Float?,
        minDateValue: LocalDate?, maxDateValue: LocalDate?,
        order: SortOrder?
    ) =
        flightDao.filterFlightByEverything(
            minPriceValue, maxPriceValue, minDateValue, maxDateValue,  order
        )
}