package com.bestmates.worldclock.solar

import com.bestmates.worldclock.model.City
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/** When the next sunrise or sunset happens among a set of cities. */
object SolarSchedule {
    private const val STEP = 5 * 60_000L
    private const val HORIZON = 26 * 3_600_000L

    /** Earliest instant within 26 hours at which any city switches between day and night. */
    fun nextTransition(cities: List<City>, nowMillis: Long): Long? {
        if (cities.isEmpty()) return null
        return cities.mapNotNull { city ->
            val zone = runCatching { ZoneId.of(city.zoneId) }.getOrNull() ?: return@mapNotNull null
            fun dayAt(t: Long) = SolarCalculator.isDay(city, ZonedDateTime.ofInstant(Instant.ofEpochMilli(t), zone))
            val start = dayAt(nowMillis)
            var t = nowMillis + STEP
            val end = nowMillis + HORIZON
            while (t <= end) {
                if (dayAt(t) != start) return@mapNotNull t
                t += STEP
            }
            null
        }.minOrNull()
    }
}
