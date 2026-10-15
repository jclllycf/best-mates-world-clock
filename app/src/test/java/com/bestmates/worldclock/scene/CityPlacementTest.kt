package com.bestmates.worldclock.scene

import com.bestmates.worldclock.model.City
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class CityPlacementTest {
    private fun city(name: String, x: Float = 0f, y: Float = 0f, scale: Float = 1f, rotation: Float = 0f) =
        City(name, "", 0.0, 0.0, "UTC", x = x, y = y, scale = scale, rotation = rotation)

    @Test fun `first city is centred at a board-appropriate size`() {
        val out = CityPlacement.add(emptyList(), city("A", .7f, .7f, 3f), 292f, 236f)
        assertEquals(0f, out[0].x); assertEquals(0f, out[0].y)
        assertEquals(CityPlacement.defaultScale(292f, 236f), out[0].scale, 1e-6f)
    }

    @Test fun `cities in a row are re-spaced evenly to include the newcomer`() {
        val out = CityPlacement.add(listOf(city("A", scale = .9f)), city("B"), 292f, 236f)
        assertEquals(listOf(-.46f, .46f), out.map { (it.x * 100).toInt() / 100f })
        assertTrue(out.all { it.scale == .9f && it.y == 0f })
    }

    @Test fun `row order follows the existing left-to-right order`() {
        val existing = listOf(city("Right", x = .5f), city("Left", x = -.5f))
        val out = CityPlacement.add(existing, city("New"), 300f, 300f)
        assertTrue(out[1].x < out[0].x && out[0].x < out[2].x)
    }

    @Test fun `a non-row layout gets the farthest free spot`() {
        val existing = listOf(city("A", 0f, 0f), city("B", -.5f, .5f, rotation = 20f))
        val out = CityPlacement.add(existing, city("C"), 300f, 300f)
        val c = out.last()
        assertTrue(abs(c.x - 0f) + abs(c.y - 0f) > .5f)
        assertEquals(3, out.size)
    }
}
