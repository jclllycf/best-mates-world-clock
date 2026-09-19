package com.bestmates.worldclock.ui.editor

import kotlin.math.roundToInt
import kotlin.math.atan2
import androidx.compose.ui.geometry.Offset

internal data class SnapResult(val value: Float, val guide: Float?)

/**
 * Computes the angle in degrees from the positive X axis (same convention as atan2).
 */
internal fun angleBetween(from: Offset, to: Offset): Float =
    Math.toDegrees(atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble())).toFloat()

/**
 * Continuously unwrap a raw angle delta so that a smooth gesture can exceed ±180°.
 * [rawDelta] is the current raw angle difference (degrees). [prevDelta] is the
 * previous unwrapped delta. Returns the new unwrapped delta.
 */
internal fun unwrapAngleDelta(rawDelta: Float, prevDelta: Float): Float {
    var d = rawDelta - prevDelta
    // Fold into (-180, 180] to find the shortest arc this frame.
    while (d > 180f) d -= 360f
    while (d <= -180f) d += 360f
    return prevDelta + d
}

internal fun snapCoordinate(value: Float, enabled: Boolean, divisions: Int = 16, extras: List<Float> = emptyList()): SnapResult {
    if (!enabled) return SnapResult(value, null)
    val linked = extras.minByOrNull { kotlin.math.abs(it - value) }
    if (linked != null && kotlin.math.abs(linked - value) <= .035f) return SnapResult(linked.coerceIn(-1f, 1f), linked.coerceIn(-1f, 1f))
    // Normalized coordinates span -1..1. A 16-division grid is therefore 1/8 apart.
    val step = 2f / divisions.coerceAtLeast(1)
    val nearest = ((value + 1f) / step).roundToInt() * step - 1f
    return SnapResult(nearest.coerceIn(-1f, 1f), nearest.coerceIn(-1f, 1f))
}

internal fun snapCanvasFraction(value: Float, step: Float, enabled: Boolean, extras: List<Float> = emptyList()): SnapResult {
    if (!enabled) return SnapResult(value, null)
    val linked = extras.minByOrNull { kotlin.math.abs(it - value) }
    if (linked != null && kotlin.math.abs(linked - value) <= .025f) return SnapResult(linked.coerceIn(0f, 1f), linked.coerceIn(0f, 1f))
    val snapped = (value / step).roundToInt() * step
    return SnapResult(snapped.coerceIn(0f, 1f), snapped.coerceIn(0f, 1f))
}

internal fun snapRotation(value: Float, enabled: Boolean): SnapResult {
    // Keep the raw continuous angle. Clamping here makes a continuous gesture
    // jump from +180 to -180 and breaks multi-turn rotation.
    val raw = value
    if (!enabled) return SnapResult(raw, null)
    val snapped = (raw / 15f).roundToInt() * 15f
    // Rotation remains fluid; only common design angles acquire a magnetic zone.
    return if (kotlin.math.abs(snapped - raw) <= 3.5f) SnapResult(snapped, snapped) else SnapResult(raw, null)
}
