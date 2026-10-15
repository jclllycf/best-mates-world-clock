package com.bestmates.worldclock.ui.editor

import com.bestmates.worldclock.scene.Frame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** BM-REAL-03: tapping an element in front of a selected shape must select it directly. */
class EditorHitTestTest {
    private val board = HitCandidate(EditorSelection.Layer(0), Frame(170f, 100f, 340f, 200f), locked = false)
    private val city = HitCandidate(EditorSelection.CityItem(0), Frame(100f, 100f, 80f, 60f), locked = false)
    private val text = HitCandidate(EditorSelection.TextItem(0), Frame(250f, 40f, 90f, 30f), locked = false)
    /** Front-most first, as the canvas builds it. */
    private val scene = listOf(text, city, board)

    private fun pick(x: Float, y: Float, selected: EditorSelection?, items: List<HitCandidate> = scene) =
        EditorHitTest.pick(items, x, y, selected, slop = 2f, minTarget = 30f)

    @Test
    fun tappingCityOnTopOfSelectedFullBoardShape_selectsTheCity() {
        val hit = pick(100f, 100f, selected = EditorSelection.Layer(0))
        assertEquals(EditorSelection.CityItem(0), hit.tap)
        assertEquals(EditorSelection.CityItem(0), hit.drag)
    }

    @Test
    fun tappingTextOnTopOfSelectedShape_selectsTheText() {
        val hit = pick(250f, 40f, selected = EditorSelection.Layer(0))
        assertEquals(EditorSelection.TextItem(0), hit.tap)
    }

    @Test
    fun tappingEmptyPartOfShape_whileCitySelected_selectsTheShape() {
        val hit = pick(300f, 170f, selected = EditorSelection.CityItem(0))
        assertEquals(EditorSelection.Layer(0), hit.tap)
        assertEquals(EditorSelection.Layer(0), hit.drag)
    }

    @Test
    fun selectedShapeStillDragsFromItsOwnEmptyArea() {
        val hit = pick(300f, 170f, selected = EditorSelection.Layer(0))
        assertEquals(EditorSelection.Layer(0), hit.drag)
    }

    @Test
    fun switchingBetweenCityAndText_worksBothWays() {
        assertEquals(EditorSelection.TextItem(0), pick(250f, 40f, EditorSelection.CityItem(0)).tap)
        assertEquals(EditorSelection.CityItem(0), pick(100f, 100f, EditorSelection.TextItem(0)).tap)
    }

    @Test
    fun overlappingShapes_frontMostWins() {
        val back = HitCandidate(EditorSelection.Layer(0), Frame(170f, 100f, 340f, 200f), false)
        val front = HitCandidate(EditorSelection.Layer(1), Frame(170f, 100f, 100f, 100f), false)
        val hit = pick(170f, 100f, selected = EditorSelection.Layer(0), items = listOf(front, back))
        assertEquals(EditorSelection.Layer(1), hit.tap)
    }

    @Test
    fun rotatedElement_isHitInItsOwnFrame() {
        // A thin bar turned 45°: its centre line is hit, the empty corner of its AABB is not.
        val bar = HitCandidate(EditorSelection.TextItem(1), Frame(170f, 100f, 160f, 20f, rotation = 45f), false)
        val items = listOf(bar, board)
        assertEquals(EditorSelection.TextItem(1), pick(170f + 40f, 100f + 40f, null, items).tap)
        assertEquals(EditorSelection.Layer(0), pick(170f + 50f, 100f - 50f, null, items).tap)
    }

    @Test
    fun lockedElementInFront_stillSelectsOnTap_butLetsSelectedShapeDrag() {
        val lockedCity = city.copy(locked = true)
        val hit = pick(100f, 100f, selected = EditorSelection.Layer(0), items = listOf(text, lockedCity, board))
        assertEquals(EditorSelection.CityItem(0), hit.tap)
        assertEquals(EditorSelection.Layer(0), hit.drag)
    }

    @Test
    fun lockedElement_withNothingSelected_isTheTarget() {
        val hit = pick(100f, 100f, selected = null, items = listOf(city.copy(locked = true), board))
        assertEquals(EditorSelection.CityItem(0), hit.tap)
        assertEquals(EditorSelection.CityItem(0), hit.drag)
    }

    @Test
    fun tinyElement_getsAnEnlargedTarget() {
        val dot = HitCandidate(EditorSelection.TextItem(2), Frame(20f, 20f, 6f, 6f), false)
        // 10 dp from its centre: outside the frame and the slop, inside the minimum target.
        assertEquals(EditorSelection.TextItem(2), pick(30f, 20f, null, listOf(dot)).tap)
    }

    @Test
    fun tinyElementAssist_neverStealsADirectHit() {
        val dot = HitCandidate(EditorSelection.TextItem(2), Frame(20f, 20f, 6f, 6f), false)
        assertEquals(EditorSelection.Layer(0), pick(30f, 20f, null, listOf(dot, board)).tap)
    }

    @Test
    fun tappingOutsideEverything_deselects() {
        val hit = pick(500f, 500f, selected = EditorSelection.CityItem(0))
        assertNull(hit.tap)
        assertNull(hit.drag)
    }
}
