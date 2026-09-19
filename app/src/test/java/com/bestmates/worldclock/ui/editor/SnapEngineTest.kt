package com.bestmates.worldclock.ui.editor

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class SnapEngineTest {

    @Test
    fun snapCoordinate_withinGridTolerance_snapsToGrid() {
        val result = snapCoordinate(0.120f, enabled = true, divisions = 16)
        assertEquals(0.125f, result.value, 0.001f)
        assertEquals(0.125f, result.guide!!, 0.001f)
    }

    @Test
    fun snapCoordinate_siblingAlignment_winsOverGrid() {
        val extras = listOf(0.2f, 0.5f)
        val result = snapCoordinate(0.22f, enabled = true, divisions = 16, extras = extras)
        assertEquals(0.2f, result.value, 0.001f)
        assertEquals(0.2f, result.guide!!, 0.001f)
    }

    @Test
    fun snapCoordinate_disabled_returnsOriginalValue() {
        val result = snapCoordinate(0.22f, enabled = false, divisions = 16)
        assertEquals(0.22f, result.value, 0.001f)
        assertNull(result.guide)
    }

    @Test
    fun snapCanvasFraction_snapsToGrid() {
        val result = snapCanvasFraction(0.05f, step = 0.0625f, enabled = true)
        assertEquals(0.0625f, result.value, 0.001f)
        assertEquals(0.0625f, result.guide!!, 0.001f)
    }

    @Test
    fun snapCanvasFraction_siblingEdgeAlignmentWorks() {
        val extras = listOf(0.4f, 0.8f)
        val result = snapCanvasFraction(0.39f, step = 0.0625f, enabled = true, extras = extras)
        assertEquals(0.4f, result.value, 0.001f)
        assertEquals(0.4f, result.guide!!, 0.001f)
    }

    @Test
    fun snapCanvasFraction_disabled_returnsOriginalValue() {
        val result = snapCanvasFraction(0.39f, step = 0.0625f, enabled = false)
        assertEquals(0.39f, result.value, 0.001f)
        assertNull(result.guide)
    }

    @Test
    fun snapRotation_snapsTo15DegreeIncrements() {
        val result = snapRotation(89f, enabled = true)
        assertEquals(90f, result.value, 0.001f)
        assertEquals(90f, result.guide!!, 0.001f)
        
        val resultNeg = snapRotation(-14.5f, enabled = true)
        assertEquals(-15f, resultNeg.value, 0.001f)
        assertEquals(-15f, resultNeg.guide!!, 0.001f)
    }

    @Test
    fun snapRotation_outsideTolerance_remainsContinuous() {
        val result = snapRotation(85f, enabled = true)
        assertEquals(85f, result.value, 0.001f)
        assertNull(result.guide)
    }

    @Test
    fun unwrapAngleDelta_resolvesCrossing180Degrees() {
        val result = unwrapAngleDelta(-179f, 179f)
        assertEquals(181f, result, 0.001f)

        val result2 = unwrapAngleDelta(179f, -179f)
        assertEquals(-181f, result2, 0.001f)
    }

    @Test
    fun unwrapAngleDelta_normalSmallDeltas() {
        val result = unwrapAngleDelta(45f, 40f)
        assertEquals(45f, result, 0.001f)
    }
}
