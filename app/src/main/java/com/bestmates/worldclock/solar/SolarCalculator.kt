package com.bestmates.worldclock.solar

import com.bestmates.worldclock.model.City
import java.time.ZonedDateTime
import kotlin.math.*

/** NOAA-inspired solar elevation calculation; positive elevation means daytime. */
object SolarCalculator {
    fun isDay(city: City, at: ZonedDateTime = ZonedDateTime.now(java.time.ZoneId.of(city.zoneId))): Boolean {
        val day = at.dayOfYear.toDouble()
        val hour = at.hour + at.minute / 60.0 + at.second / 3600.0
        val gamma = 2.0 * Math.PI / 365.0 * (day - 1 + (hour - 12) / 24.0)
        val equation = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) - 0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))
        val declination = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) - 0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma) - 0.002697 * cos(3 * gamma) + 0.00148 * sin(3 * gamma)
        val timeOffset = equation + 4 * city.longitude - 60 * at.offset.totalSeconds / 3600.0
        val trueSolarMinutes = (hour * 60 + timeOffset + 1440) % 1440
        val hourAngle = if (trueSolarMinutes / 4 < 0) trueSolarMinutes / 4 + 180 else trueSolarMinutes / 4 - 180
        val latitude = Math.toRadians(city.latitude)
        val zenith = acos(sin(latitude) * sin(declination) + cos(latitude) * cos(declination) * cos(Math.toRadians(hourAngle)))
        return Math.toDegrees(zenith) < 90.833
    }
}
