package com.bestmates.worldclock.ui.editor

import com.bestmates.worldclock.scene.AspectMagnet
import com.bestmates.worldclock.scene.Frame
import com.bestmates.worldclock.scene.SnapMath
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** BM-REAL-04: resizing magnets — edges, sizes and proportions. */
class LayerResizeTest {
    private val bw = 340f
    private val bh = 200f
    private fun magnets(threshold: Float = 6f, aspects: List<AspectMagnet> = listOf(AspectMagnet(1f, "1:1"))) = LayerResize.Magnets(
        xLines = SnapMath.boardLines(bw), yLines = SnapMath.boardLines(bh),
        widths = SnapMath.fractionMagnets(bw, "↔ "), heights = SnapMath.fractionMagnets(bh, "↕ "),
        aspects = aspects, threshold = threshold,
    )

    @Test
    fun rightEdge_snapsToBoardEdge() {
        val f = Frame(100f, 100f, 120f, 80f)
        val r = LayerResize.resize(f, 1, 0, 176f to 0f, bw, bh, magnets())
        // Right edge would land at 336; the board edge 340 is within 6 dp.
        assertEquals(340f, r.frame.right, .01f)
        assertEquals(40f, r.frame.left, .01f)
        assertEquals(listOf(340f), r.xs)
    }

    @Test
    fun leftHandle_movesLeftEdgeAndKeepsRightEdge() {
        val f = Frame(200f, 100f, 100f, 80f)
        val r = LayerResize.resize(f, -1, 0, -40f to 0f, bw, bh, null)
        assertEquals(250f, r.frame.right, .01f)
        assertEquals(110f, r.frame.left, .01f)
    }

    @Test
    fun width_snapsToBoardFraction() {
        // Rotated, so edge lines are off; the half-board width is still a magnet.
        val f = Frame(170f, 100f, 100f, 60f, rotation = 30f)
        val r = LayerResize.resize(f, 1, 0, 66f to 0f, bw, bh, magnets())
        assertEquals(bw / 2f, r.frame.w, .01f)
        assertEquals("↔ ½", r.label)
    }

    @Test
    fun cornerDrag_catchesSquareProportion() {
        val f = Frame(100f, 100f, 60f, 50f)
        // 60+31 by 50+40 → 91 × 90: within 3.5 % of square.
        val r = LayerResize.resize(f, 1, 1, 31f to 40f, bw, bh, magnets(threshold = .5f))
        assertEquals(1f, r.frame.w / r.frame.h, .001f)
        assertTrue(r.label!!.contains("1:1"))
    }

    @Test
    fun freeResize_withoutMagnets_isExact() {
        val f = Frame(100f, 100f, 60f, 50f)
        val r = LayerResize.resize(f, 1, 1, 31f to 40f, bw, bh, null)
        assertEquals(91f, r.frame.w, .01f)
        assertEquals(90f, r.frame.h, .01f)
        assertNull(r.label)
    }

    @Test
    fun rotatedResize_keepsOppositeCornerFixed() {
        val f = Frame(170f, 100f, 100f, 60f, rotation = 90f)
        val (ox, oy) = f.toBoard(-f.w / 2f, -f.h / 2f)
        val r = LayerResize.resize(f, 1, 1, 20f to 10f, bw, bh, null)
        val (nx, ny) = r.frame.toBoard(-r.frame.w / 2f, -r.frame.h / 2f)
        assertEquals(ox, nx, .01f)
        assertEquals(oy, ny, .01f)
    }

    @Test
    fun heldMagnet_survivesSmallOvershoot_thenReleases() {
        val f = Frame(100f, 100f, 120f, 80f)
        val caught = LayerResize.resize(f, 1, 0, 176f to 0f, bw, bh, magnets())
        // 8 dp past the line is beyond the 6 dp catch radius but inside the hold radius.
        val held = LayerResize.resize(f, 1, 0, 172f to 0f, bw, bh, magnets(), caught.held)
        assertEquals(340f, held.frame.right, .01f)
        val released = LayerResize.resize(f, 1, 0, 160f to 0f, bw, bh, magnets(), held.held)
        assertEquals(320f, released.frame.right, .01f)
    }

    @Test
    fun sizeNeverExceedsBoard() {
        val f = Frame(170f, 100f, 300f, 150f)
        val r = LayerResize.resize(f, 1, 1, 500f to 500f, bw, bh, null)
        assertTrue(r.frame.w <= bw + .01f && r.frame.h <= bh + .01f)
    }
}
