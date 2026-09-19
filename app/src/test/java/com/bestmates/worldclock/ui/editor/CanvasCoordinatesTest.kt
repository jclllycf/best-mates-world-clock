package com.bestmates.worldclock.ui.editor

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Test

class CanvasCoordinatesTest {

    @Test
    fun rotateCanvasVector_zeroDegrees_remainsSame() {
        val vector = Offset(10f, 20f)
        val rotated = rotateCanvasVector(vector, 0f)
        assertEquals(10f, rotated.x, 0.001f)
        assertEquals(20f, rotated.y, 0.001f)
    }

    @Test
    fun rotateCanvasVector_90Degrees_rotatesClockwiseOnScreen() {
        // (1, 0) rotated by 90 degrees clockwise becomes (0, 1)
        val vector = Offset(1f, 0f)
        val rotated = rotateCanvasVector(vector, 90f)
        assertEquals(0f, rotated.x, 0.001f)
        assertEquals(1f, rotated.y, 0.001f)
    }

    @Test
    fun rotateCanvasVector_180Degrees_invertsVector() {
        val vector = Offset(10f, 20f)
        val rotated = rotateCanvasVector(vector, 180f)
        assertEquals(-10f, rotated.x, 0.001f)
        assertEquals(-20f, rotated.y, 0.001f)
    }

    @Test
    fun rotateCanvasVector_negative90Degrees_rotatesCounterClockwise() {
        // (1, 0) rotated by -90 degrees becomes (0, -1)
        val vector = Offset(1f, 0f)
        val rotated = rotateCanvasVector(vector, -90f)
        assertEquals(0f, rotated.x, 0.001f)
        assertEquals(-1f, rotated.y, 0.001f)
    }

    @Test
    fun rotateCanvasVector_composition() {
        val vector = Offset(10f, 0f)
        val intermediate = rotateCanvasVector(vector, 45f)
        val finalResult = rotateCanvasVector(intermediate, 45f)
        
        // Equivalent to rotating 90 degrees
        val direct = rotateCanvasVector(vector, 90f)
        assertEquals(direct.x, finalResult.x, 0.001f)
        assertEquals(direct.y, finalResult.y, 0.001f)
    }
}
