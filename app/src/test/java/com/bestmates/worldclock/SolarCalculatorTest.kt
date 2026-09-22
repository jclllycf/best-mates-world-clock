package com.bestmates.worldclock

import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.solar.SolarCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class SolarCalculatorTest {
    @Test
    fun `isDay returns true during daytime`() {
        val city = City("Equator", "Earth", 0.0, 0.0, "UTC")
        val time = ZonedDateTime.now(ZoneId.of("UTC")).withHour(12).withMinute(0)
        assertTrue(SolarCalculator.isDay(city, time))
    }

    @Test
    fun `isDay returns false during nighttime`() {
        val city = City("Equator", "Earth", 0.0, 0.0, "UTC")
        val time = ZonedDateTime.now(ZoneId.of("UTC")).withHour(0).withMinute(0)
        assertFalse(SolarCalculator.isDay(city, time))
    }

    @Test
    fun `isDay behaves safely at extremes`() {
        val cityNorth = City("North Pole", "Earth", 90.0, 0.0, "UTC")
        val time = ZonedDateTime.now(ZoneId.of("UTC")).withMonth(6).withDayOfMonth(21).withHour(12).withMinute(0)
        assertTrue(SolarCalculator.isDay(cityNorth, time))
    }
}
