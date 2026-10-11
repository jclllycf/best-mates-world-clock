package com.bestmates.worldclock.solar

import com.bestmates.worldclock.model.City
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class SolarScheduleTest {
    private val london = City("London", "UK", 51.5085, -0.1257, "Europe/London")

    @Test fun `no cities means no refresh`() {
        assertNull(SolarSchedule.nextTransition(emptyList(), 0L))
    }

    @Test fun `next London transition is the coming sunrise or sunset`() {
        // 2026-06-21 12:00 London: next transition is sunset around 21:20 local.
        val noon = ZonedDateTime.of(2026, 6, 21, 12, 0, 0, 0, ZoneId.of("Europe/London")).toInstant().toEpochMilli()
        val next = SolarSchedule.nextTransition(listOf(london), noon)
        assertNotNull(next)
        val local = ZonedDateTime.ofInstant(Instant.ofEpochMilli(next!!), ZoneId.of("Europe/London"))
        assertEquals(21, local.hour)
        assertTrue(!SolarCalculator.isDay(london, local))
    }

    @Test fun `earliest transition across cities wins`() {
        val tokyo = City("Tokyo", "Japan", 35.6895, 139.6917, "Asia/Tokyo")
        val t = ZonedDateTime.of(2026, 6, 21, 12, 0, 0, 0, ZoneId.of("Europe/London")).toInstant().toEpochMilli()
        val both = SolarSchedule.nextTransition(listOf(london, tokyo), t)!!
        val onlyLondon = SolarSchedule.nextTransition(listOf(london), t)!!
        assertTrue(both <= onlyLondon)
    }
}
