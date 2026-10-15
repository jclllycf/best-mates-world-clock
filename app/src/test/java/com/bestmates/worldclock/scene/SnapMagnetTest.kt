package com.bestmates.worldclock.scene

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** BM-REAL-04: typed magnet lines, hysteresis, size and proportion magnets. */
class SnapMagnetTest {
    private val lines = SnapMath.boardLines(300f)

    @Test
    fun centre_snapsToBoardCentre() {
        val s = SnapMath.snapLines(110f, 146f, 182f, lines, 6f)
        assertEquals(4f, s.value, .001f)
        assertEquals(150f, s.guide!!, .001f)
    }

    @Test
    fun boardEdgeLine_doesNotAttractCentre() {
        // Centre 4 dp from x = 0 is not a magnet; only edges snap to the board edges.
        val s = SnapMath.snapLines(-40f, 4f, 48f, listOf(SnapLine(0f, centre = false)), 6f)
        assertNull(s.guide)
    }

    @Test
    fun heldLine_holdsWithinHoldRadius_andReleasesBeyond() {
        val caught = SnapMath.snapLines(146f, 196f, 246f, lines, 6f)
        assertEquals(150f, caught.guide!!, .001f)
        val held = SnapMath.snapLines(141f, 191f, 241f, lines, 6f, held = 150f)
        assertEquals(150f, held.guide!!, .001f)
        val free = SnapMath.snapLines(138f, 188f, 238f, lines, 6f, held = 150f)
        assertNull(free.guide)
    }

    @Test
    fun rankBreaksTies() {
        val s = SnapMath.snapLines(97f, 120f, 143f, listOf(SnapLine(100f, rank = 1), SnapLine(100f, rank = 3)), 6f)
        assertEquals(100f, s.guide!!, .001f)
    }

    @Test
    fun sizeMagnet_nearestWithinThreshold() {
        val s = SnapMath.snapSize(147f, SnapMath.fractionMagnets(300f), 6f)
        assertEquals(150f, s.value, .001f)
        assertEquals("½", s.label)
        assertNull(SnapMath.snapSize(130f, SnapMath.fractionMagnets(300f), 6f).label)
    }

    @Test
    fun aspectMagnet_relativeTolerance() {
        val magnets = listOf(AspectMagnet(1f, "1:1"), AspectMagnet(16f / 9f, "16:9"))
        assertEquals("1:1", SnapMath.snapAspect(102f, 100f, magnets)!!.label)
        assertEquals("16:9", SnapMath.snapAspect(176f, 100f, magnets)!!.label)
        assertNull(SnapMath.snapAspect(130f, 100f, magnets))
    }
}
