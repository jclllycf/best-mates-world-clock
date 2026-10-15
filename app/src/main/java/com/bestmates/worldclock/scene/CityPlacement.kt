package com.bestmates.worldclock.scene

import com.bestmates.worldclock.model.City
import kotlin.math.abs
import kotlin.math.hypot

/**
 * Where a newly added city goes. Pure so it is unit-tested.
 *
 *  - A new city matches the size of the cities already there (or a size suited to the board).
 *  - If the existing cities form one unrotated row, the row is re-spaced evenly to include
 *    the newcomer — the common "Tokyo · Paris · New York" layout composes itself.
 *  - Otherwise it takes the free spot farthest from every existing city.
 */
object CityPlacement {
    fun defaultScale(boardW: Float, boardH: Float): Float = (minOf(boardW, boardH) / 230f).coerceIn(.75f, 1.4f)

    fun add(existing: List<City>, newCity: City, boardW: Float, boardH: Float): List<City> {
        val scale = existing.firstOrNull()?.scale ?: defaultScale(boardW, boardH)
        val incoming = newCity.copy(scale = scale, rotation = 0f)
        if (existing.isEmpty()) return listOf(incoming.copy(x = 0f, y = 0f))

        val rowY = existing.first().y
        val isRow = existing.size <= 3 && existing.all { it.rotation % 360f == 0f && abs(it.y - rowY) < .06f }
        if (isRow) {
            val n = existing.size + 1
            val xs = (0 until n).map { i -> ((i + .5f) / n * 2f - 1f) * .92f }
            val ordered = existing.withIndex().sortedBy { it.value.x }
            val respaced = existing.toMutableList()
            ordered.forEachIndexed { slot, (index, city) -> respaced[index] = city.copy(x = xs[slot], y = rowY) }
            return respaced + incoming.copy(x = xs.last(), y = rowY)
        }

        val candidates = listOf(0f, -.55f, .55f).flatMap { y -> listOf(0f, -.6f, .6f, -.3f, .3f).map { x -> x to y } }
        val best = candidates.maxBy { (x, y) ->
            existing.minOf { hypot((it.x - x) * boardW / 2f, (it.y - y) * boardH / 2f) }
        }
        return existing + incoming.copy(x = best.first, y = best.second)
    }
}
