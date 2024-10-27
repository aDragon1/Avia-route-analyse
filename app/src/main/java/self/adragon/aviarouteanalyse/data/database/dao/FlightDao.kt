package self.adragon.aviarouteanalyse.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import self.adragon.aviarouteanalyse.data.model.Flight

@Dao
interface FlightDao {
    @Query("SELECT * FROM flights")
    fun getAllFlights(): LiveData<List<Flight>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flight: Flight)

    @Query("SELECT * FROM flights ORDER BY price")
    suspend fun sortByPriceAsc(): List<Flight>

    @Query("SELECT * FROM flights ORDER BY price DESC")
    suspend fun sortByPriceDesc(): List<Flight>

    @Query("SELECT * FROM flights ORDER BY departureDate")
    suspend fun sortByDateAsc(): List<Flight>

    @Query("SELECT * FROM flights ORDER BY departureDate DESC")
    suspend fun sortByDateDesc(): List<Flight>
}