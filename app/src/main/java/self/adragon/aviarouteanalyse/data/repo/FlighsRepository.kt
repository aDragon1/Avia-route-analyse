package self.adragon.aviarouteanalyse.data.repo

import androidx.lifecycle.LiveData
import self.adragon.aviarouteanalyse.data.database.dao.FlightDao
import self.adragon.aviarouteanalyse.data.model.Flight


class FlightRepository(private val flightDao: FlightDao) {

    fun getAllFlights(): LiveData<List<Flight>> = flightDao.getAllFlights()

    suspend fun insert(flight: Flight) = flightDao.insert(flight)

    suspend fun sortByPrice(reverse: Boolean) =
        if (reverse) flightDao.sortByPriceDesc() else flightDao.sortByPriceAsc()

    suspend fun sortByDate(reverse: Boolean) =
        if (reverse) flightDao.sortByDateDesc() else flightDao.sortByDateAsc()
}