package com.bestmates.worldclock.scene

import com.bestmates.worldclock.model.WidgetLayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SceneGeometryTest {

    @Test fun `fit centres the board and scales uniformly`() {
        val fit = SceneGeometry.fit(200f, 100f, 0f, 0f, 400f, 400f)
        assertEquals(2f, fit.scale, 1e-6f)
        assertEquals(0f, fit.originX, 1e-6f)
        assertEquals(100f, fit.originY, 1e-6f)
        assertEquals(50f, fit.boardX(fit.x(50f)), 1e-4f)
    }

    @Test fun `layer frame matches the historical board mapping`() {
        val f = SceneGeometry.layerFrame(WidgetLayer(x = 0f, y = 0f, width = .5f, height = .25f), 338f, 338f)
        assertEquals(169f, f.cx, 1e-4f); assertEquals(169f, f.cy, 1e-4f)
        assertEquals(169f, f.w, 1e-4f); assertEquals(84.5f, f.h, 1e-4f)
    }

    @Test fun `layer frame is kept on the board`() {
        val f = SceneGeometry.layerFrame(WidgetLayer(x = 1f, y = -1f, width = .5f, height = .5f), 200f, 100f)
        assertEquals(150f, f.cx, 1e-4f); assertEquals(25f, f.cy, 1e-4f)
    }

    @Test fun `layer round-trips through its frame`() {
        val layer = WidgetLayer(x = -.3f, y = .2f, width = .4f, height = .6f, rotation = 21f)
        val back = SceneGeometry.layerFromFrame(layer, SceneGeometry.layerFrame(layer, 300f, 180f), 300f, 180f)
        assertEquals(layer.x, back.x, 1e-5f); assertEquals(layer.y, back.y, 1e-5f)
        assertEquals(layer.width, back.width, 1e-5f); assertEquals(layer.height, back.height, 1e-5f)
        assertEquals(21f, back.rotation, 1e-6f)
    }

    @Test fun `rotated frames hit-test in their own space`() {
        val f = Frame(100f, 100f, 80f, 20f, 90f)
        assertTrue(f.contains(100f, 135f))   // along the rotated long axis
        assertFalse(f.contains(135f, 100f))  // outside the now-narrow width
        val (bx, by) = f.toBoard(40f, 0f)
        assertEquals(100f, bx, 1e-4f); assertEquals(140f, by, 1e-4f)
        val (lx, ly) = f.toLocal(bx, by)
        assertEquals(40f, lx, 1e-4f); assertEquals(0f, ly, 1e-4f)
    }

    @Test fun `snapping picks the nearest edge or centre within the threshold`() {
        val s = SnapMath.snapAxis(lo = 48f, mid = 98f, hi = 148f, targets = listOf(0f, 100f, 200f), threshold = 5f)
        assertEquals(2f, s.value, 1e-6f); assertEquals(100f, s.guide)
        val none = SnapMath.snapAxis(10f, 60f, 110f, listOf(0f, 100f, 200f), 5f)
        assertEquals(0f, none.value, 1e-6f); assertNull(none.guide)
    }

    @Test fun `rotation snaps only near common angles and never wraps`() {
        assertEquals(45f, SnapMath.rotation(47f, true).value, 1e-6f)
        assertEquals(52f, SnapMath.rotation(52f, true).value, 1e-6f)
        assertEquals(720f, SnapMath.rotation(721f, true).value, 1e-6f)
        assertEquals(47f, SnapMath.rotation(47f, false).value, 1e-6f)
    }
}
