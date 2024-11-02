package self.adragon.aviarouteanalyse.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
@Entity(tableName = "flights")
data class Flight(
    @ColumnInfo(name = "flightID")
    @PrimaryKey(autoGenerate = true)
    val flightID: Int,

    @ColumnInfo(name = "airline")
    val airline: String,

    @ColumnInfo(name = "departureAirport")
    val departureAirport: String,

    @ColumnInfo(name = "destinationAirport")
    val destinationAirport: String,

    @ColumnInfo(name = "price")
    val price: Float,

    @ColumnInfo(name = "departureDate")
    @Contextual
    val departureDate: LocalDate,

    val stringDate: String = LocalDateConverter().fromDateToString(departureDate)
)