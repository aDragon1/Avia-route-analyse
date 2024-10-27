package self.adragon.aviarouteanalyse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import self.adragon.aviarouteanalyse.data.database.dao.FlightDao
import self.adragon.aviarouteanalyse.data.model.Flight
import self.adragon.aviarouteanalyse.data.model.LocalDateConverter


@Database(entities = [Flight::class], version = 5, exportSchema = true)
@TypeConverters(LocalDateConverter::class)
abstract class FlightsDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: FlightsDatabase? = null
        private const val DBNAME = "FLIGHTS_TABLE"

        fun getDatabase(context: Context): FlightsDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlightsDatabase::class.java,
                    DBNAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }

        fun deleteDatabase(context: Context) {
            context.deleteDatabase(DBNAME)
            INSTANCE = null
        }
    }

    abstract fun flightDAO(): FlightDao
}