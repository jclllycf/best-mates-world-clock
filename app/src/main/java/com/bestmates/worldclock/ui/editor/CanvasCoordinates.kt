package com.bestmates.worldclock.ui.editor

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

/**
 * Rotates a vector using Compose's clockwise-on-screen degree convention.
 *
 * A pointer handler inside graphicsLayer(rotationZ) receives local deltas. Apply
 * the object's positive rotation once to recover canvas movement, then convert
 * pixels to dp/normalized coordinates. Translation and the pivot cancel for a
 * delta; do not add the object's position or apply an inverse rotation here.
 * This also positions the existing unrotated sibling rotation handle.
 */
internal fun rotateCanvasVector(vector: Offset, rotationDegrees: Float): Offset {
    val radians = Math.toRadians(rotationDegrees.toDouble())
    val cosine = cos(radians).toFloat()
    val sine = sin(radians).toFloat()
    return Offset(
        x = vector.x * cosine - vector.y * sine,
        y = vector.x * sine + vector.y * cosine
    )
}
