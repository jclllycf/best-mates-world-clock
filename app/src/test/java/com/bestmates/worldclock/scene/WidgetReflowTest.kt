package com.bestmates.worldclock.scene

import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

/**
 * BM-REAL-02: a design whose stored board differs in proportion from the launcher's real
 * cell must still reach the widget's edges instead of leaving bands on two sides.
 */
class WidgetReflowTest {
    private val fullBleed = WidgetLayer(id = 1, shape = WidgetShape.ROUNDED, width = 1f, height = 1f)
    private val roundBadge = WidgetLayer(id = 2, shape = WidgetShape.CIRCLE, x = .5f, y = 0f, width = .3f, height = .5f)
    /** A 2×1 board as 0.4.0 created it from the generic cell (86 × 98 dp). */
    private val strip = WidgetConfig(columns = 2, rows = 1, boardWidthDp = 172f, boardHeightDp = 98f, layers = listOf(fullBleed, roundBadge))

    @Test
    fun widerCell_growsBoardWidthOnly() {
        // A Samsung-like 2×1 cell is wider than the generic one.
        val (w, h) = SceneGeometry.adaptBoard(172f, 98f, 180f, 85f)
        assertEquals(98f, h, .01f)
        assertEquals(98f * 180f / 85f, w, .01f)
    }

    @Test
    fun tallerCell_growsBoardHeightOnly() {
        val (w, h) = SceneGeometry.adaptBoard(338f, 338f, 380f, 420f)
        assertEquals(338f, w, .01f)
        assertEquals(338f * 420f / 380f, h, .01f)
    }

    @Test
    fun matchingCell_keepsBoard() {
        val (w, h) = SceneGeometry.adaptBoard(172f, 98f, 344f, 196f)
        assertEquals(172f, w, .01f)
        assertEquals(98f, h, .01f)
    }

    @Test
    fun extremeMismatch_stretchesOnlyUpToTheLimit() {
        val (w, _) = SceneGeometry.adaptBoard(172f, 98f, 700f, 98f)
        assertEquals(172f * 1.35f, w, .01f)
    }

    @Test
    fun fullBleedShape_staysFullBleed_andOtherShapesKeepProportion() {
        val (w, h) = SceneGeometry.adaptBoard(172f, 98f, 180f, 85f)
        val out = SceneGeometry.reflow(strip, w, h)
        val bleed = SceneGeometry.layerFrame(out.layers[0], w, h)
        assertEquals(0f, bleed.left, .01f)
        assertEquals(w, bleed.right, .01f)
        assertEquals(0f, bleed.top, .01f)
        assertEquals(h, bleed.bottom, .01f)
        // The circle keeps its size in dp (still round) and its relative position.
        val before = SceneGeometry.layerFrame(roundBadge, 172f, 98f)
        val after = SceneGeometry.layerFrame(out.layers[1], w, h)
        assertEquals(before.w, after.w, .01f)
        assertEquals(before.h, after.h, .01f)
        assertEquals(before.cx / 172f, after.cx / w, .001f)
    }

    @Test
    fun explicitResize_scalesContentUniformly() {
        val design = strip.copy(texts = listOf(CustomText(content = "Hi", size = 20f)))
        val out = SceneGeometry.reflow(design, 344f, 196f)
        assertEquals(40f, out.texts[0].size, .01f)
        val after = SceneGeometry.layerFrame(out.layers[1], 344f, 196f)
        val before = SceneGeometry.layerFrame(roundBadge, 172f, 98f)
        assertEquals(before.w * 2f, after.w, .01f)
    }

    @Test
    fun unchangedBoard_returnsSameConfig() {
        assertSame(strip, SceneGeometry.reflow(strip, 172f, 98f))
    }
}
