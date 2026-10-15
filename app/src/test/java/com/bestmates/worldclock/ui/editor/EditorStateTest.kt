package com.bestmates.worldclock.ui.editor

import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EditorStateTest {
    private val base = WidgetConfig(
        layers = listOf(WidgetLayer(id = 1), WidgetLayer(id = 2)),
        cities = listOf(City("A", "", 0.0, 0.0, "UTC", componentId = 2)),
        texts = listOf(CustomText(content = "hi")),
    )

    @Test fun `a whole drag is one undo step`() {
        val s = EditorState(base)
        s.startGesture()
        repeat(10) { i -> s.live(s.withLayer(s.config, 0) { it.copy(x = i / 10f) }) }
        s.endGesture()
        assertEquals(.9f, s.config.layers[0].x, 1e-6f)
        s.undo()
        assertEquals(0f, s.config.layers[0].x, 1e-6f)
        assertFalse(s.canUndo)
        s.redo()
        assertEquals(.9f, s.config.layers[0].x, 1e-6f)
    }

    @Test fun `changing selection commits a pending live edit`() {
        val s = EditorState(base)
        s.selection = EditorSelection.TextItem(0)
        s.liveText(0) { it.copy(content = "hello") }
        val before = s.revision
        s.selection = null
        assertTrue(s.revision > before)
        s.undo()
        assertEquals("hi", s.config.texts[0].content)
    }

    @Test fun `a canvas gesture after typing is a separate undo step`() {
        val s = EditorState(base)
        s.liveText(0) { it.copy(content = "typed") }
        s.startGesture()
        s.live(s.withText(s.config, 0) { it.copy(x = .5f) })
        s.endGesture()
        s.undo()
        assertEquals("typed", s.config.texts[0].content)
        assertEquals(0f, s.config.texts[0].x, 1e-6f)
    }

    @Test fun `deleting a background re-homes its cities`() {
        val s = EditorState(base)
        s.selection = EditorSelection.Layer(1)
        s.deleteSelected()
        assertEquals(1, s.config.layers.size)
        assertEquals(1L, s.config.cities[0].componentId)
        assertNull(s.selection)
    }

    @Test fun `undo clears a selection that no longer exists`() {
        val s = EditorState(base)
        s.commit(s.config.copy(texts = s.config.texts + CustomText(content = "new")))
        s.selection = EditorSelection.TextItem(1)
        s.undo()
        assertNull(s.selection)
    }

    @Test fun `reordering moves within the element's own kind`() {
        val s = EditorState(base)
        s.selection = EditorSelection.Layer(0)
        s.reorder(+1)
        assertEquals(listOf(2L, 1L), s.config.layers.map { it.id })
        assertEquals(EditorSelection.Layer(1), s.selection)
    }
}
