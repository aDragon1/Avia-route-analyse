package self.adragon.aviarouteanalyse.utils

import self.adragon.aviarouteanalyse.data.model.Flight
import java.time.LocalDate
import kotlin.math.roundToInt

@Suppress("SpellCheckingInspection")
class Generator {

    private val airportNames = listOf(
        "Шереметьево", "Домодедово", "Внуково", "Пулково", "Кольцово", "Толмачево",
        "Сочи", "Казань", "Уфа", "Платов", "Минеральные Воды", "Мурманск",
        "Стригино", "Омск", "Баландино", "Гумрак",
        "Иркутск", "Курумоч", "Новый", "Краснодар", "Симферополь",
        "Кневичи (Владивосток)", "Якутск", "Кемерово", "Алыкель", "Абакан", "Нарьян-Мар",
        "Байкал", "Кольцово", "Елизово"
    )
    private val airlineNames = listOf(
        "Аэрофлот", "Россия", "Победа", "Сибирь (S7 Airlines)", "Уральские авиалинии",
        "ЮТэйр", "Азимут", "Нордавиа", "Алроса", "Ред Вингс", "Якутия", "Северный Ветер (Nordwind)",
        "Икар (Pegas Fly)", "Аврора", "Руслайн", "Белавиа", "Глобус", "Азур Эйр",
        "ВИМ Авиа", "Псковавиа", "Таймыр (NordStar)", "ЮВТ Аэро", "Комиавиатранс"
    )


    fun generateFlights(n: Int = Int.MAX_VALUE): List<Flight> {
        val flights = mutableListOf<Flight>()

        val minDate = LocalDate.now()
        val maxDate = LocalDate.of(2026, 1, 1)

        val min = 1f
        val max = 100f

        var id = 1
        for (i in airportNames.indices) {
            val departure = airportNames[i]
            for (j in i + 1 until airportNames.size) {
                val destination = airportNames[j]
                val airline = airlineNames.random()

                val date = randomDate(minDate, maxDate)
                val price1 = (min + Math.random() * (max - min)).toFloat().round()
                val price2 = (min + Math.random() * (max - min)).toFloat().round()

                val f1 = Flight(id++, airline, departure, destination, price1, date)
                val f2 = Flight(id++, airline, destination, departure, price2, date)

                flights.add(f1)
                flights.add(f2)

                if (id > n) break
            }
            if (id > n) break
        }

        return flights
    }

    private fun randomDate(minDate: LocalDate, maxDate: LocalDate): LocalDate {
        val minDay = minDate.toEpochDay()
        val maxDay = maxDate.toEpochDay()

        return LocalDate.ofEpochDay((minDay..maxDay).random())
    }

    private fun Float.round() = (this * 100).roundToInt() / 100f
}

