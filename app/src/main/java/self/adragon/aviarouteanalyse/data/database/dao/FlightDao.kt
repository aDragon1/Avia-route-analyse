package self.adragon.aviarouteanalyse.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import self.adragon.aviarouteanalyse.data.model.Flight
import self.adragon.aviarouteanalyse.data.model.enums.SortOrder
import java.time.LocalDate

@Dao
interface FlightDao {
    @Query("SELECT * FROM flights")
    fun getAllFlights(): LiveData<List<Flight>>

    @Query("SELECT departureAirport from flights")
    suspend fun getDepartureAirports(): List<String>

    @Query("SELECT destinationAirport from flights")
    suspend fun getDestinationAirports(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flight: Flight)

    @Query(
        "SELECT * FROM flights WHERE " +
                "(:minPriceValue IS NULL OR price >= :minPriceValue) AND " +
                "(:maxPriceValue IS NULL OR price <= :maxPriceValue) AND " +
                "(:minDateValue IS NULL OR departureDate >= :minDateValue) AND " +
                "(:maxDateValue IS NULL OR departureDate <= :maxDateValue) " +

                "ORDER BY " +
                "CASE " +
                "WHEN :order = 'DEFAULT' THEN flightID " +
                "WHEN :order = 'PRICE_UP' THEN price " +
                "WHEN :order = 'DATE_UP' THEN departureDate " +
                "END ASC, " +

                "CASE " +
                "WHEN :order = 'PRICE_DOWN' THEN price " +
                "WHEN :order = 'DATE_DOWN' THEN departureDate " +
                "END DESC"
    )
    suspend fun filterFlightByEverything(
        minPriceValue: Float?, maxPriceValue: Float?,
        minDateValue: LocalDate?, maxDateValue: LocalDate?,
        order: SortOrder?
    ): List<Flight>
}
