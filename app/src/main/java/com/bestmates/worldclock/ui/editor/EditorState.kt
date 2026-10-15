package com.bestmates.worldclock.ui.editor

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer

sealed interface EditorSelection {
    val index: Int
    data class Layer(override val index: Int) : EditorSelection
    data class CityItem(override val index: Int) : EditorSelection
    data class TextItem(override val index: Int) : EditorSelection
}

const val MAX_LAYERS = 6
const val MAX_CITIES = 10
const val MAX_TEXTS = 5

/**
 * Editor model: the working config, an undo history in which a whole gesture is one
 * step, and the current selection. Persistence is the screen's concern; this class only
 * reports [revision] changes so the screen can autosave.
 */
@Stable
class EditorState(initial: WidgetConfig) {
    var config by mutableStateOf(initial)
        private set
    private var selectionState by mutableStateOf<EditorSelection?>(null)
    /** Changing the selection commits any pending live edit (typing, a slider) first. */
    var selection: EditorSelection?
        get() = selectionState
        set(value) { if (value != selectionState) endGesture(); selectionState = value }
    /** Increments on every committed (undoable) change. */
    var revision by mutableIntStateOf(0)
        private set

    private var history = listOf(initial)
    private var historyIndex by mutableIntStateOf(0)
    private var gestureBase: WidgetConfig? = null

    val canUndo get() = historyIndex > 0
    val canRedo get() = historyIndex < history.lastIndex
    val inGesture get() = gestureBase != null

    fun commit(next: WidgetConfig) {
        // [next] already contains any pending live edit, so it closes that edit too.
        gestureBase = null
        if (next == config) return
        history = (history.take(historyIndex + 1) + next).takeLast(HISTORY)
        historyIndex = history.lastIndex
        config = next
        revision++
    }

    /** Applies a value continuously during a drag; the gesture becomes one undo step. */
    fun beginGesture() { if (gestureBase == null) gestureBase = config }

    /** A canvas gesture always starts its own undo step, closing any pending edit. */
    fun startGesture() { endGesture(); gestureBase = config }
    fun live(next: WidgetConfig) { config = next }
    fun endGesture() {
        val base = gestureBase ?: return
        gestureBase = null
        val final = config
        if (base == final) return
        config = base
        commit(final)
    }

    fun undo() { if (canUndo) { historyIndex--; config = history[historyIndex]; revision++; validateSelection() } }
    fun redo() { if (canRedo) { historyIndex++; config = history[historyIndex]; revision++; validateSelection() } }

    private fun validateSelection() {
        val s = selection ?: return
        val valid = when (s) {
            is EditorSelection.Layer -> s.index in config.layers.indices
            is EditorSelection.CityItem -> s.index in config.cities.indices
            is EditorSelection.TextItem -> s.index in config.texts.indices
        }
        if (!valid) selection = null
    }

    /* ---------------------------------------------------------------- element edits */

    fun layer(i: Int) = config.layers.getOrNull(i)
    fun city(i: Int) = config.cities.getOrNull(i)
    fun text(i: Int) = config.texts.getOrNull(i)

    fun withLayer(c: WidgetConfig, i: Int, f: (WidgetLayer) -> WidgetLayer) =
        c.copy(layers = c.layers.mapIndexed { idx, l -> if (idx == i) f(l) else l })
    fun withCity(c: WidgetConfig, i: Int, f: (City) -> City) =
        c.copy(cities = c.cities.mapIndexed { idx, l -> if (idx == i) f(l) else l })
    fun withText(c: WidgetConfig, i: Int, f: (CustomText) -> CustomText) =
        c.copy(texts = c.texts.mapIndexed { idx, l -> if (idx == i) f(l) else l })

    fun updateLayer(i: Int, f: (WidgetLayer) -> WidgetLayer) = commit(withLayer(config, i, f))
    fun updateCity(i: Int, f: (City) -> City) = commit(withCity(config, i, f))
    fun updateText(i: Int, f: (CustomText) -> CustomText) = commit(withText(config, i, f))

    /** Slider-style continuous edits: live while dragging, one undo step when released. */
    fun liveLayer(i: Int, f: (WidgetLayer) -> WidgetLayer) { beginGesture(); live(withLayer(config, i, f)) }
    fun liveCity(i: Int, f: (City) -> City) { beginGesture(); live(withCity(config, i, f)) }
    fun liveText(i: Int, f: (CustomText) -> CustomText) { beginGesture(); live(withText(config, i, f)) }

    fun isLocked(s: EditorSelection?): Boolean = when (s) {
        is EditorSelection.Layer -> layer(s.index)?.locked == true
        is EditorSelection.CityItem -> city(s.index)?.locked == true
        is EditorSelection.TextItem -> text(s.index)?.locked == true
        null -> false
    }

    fun toggleLock() {
        when (val s = selection ?: return) {
            is EditorSelection.Layer -> updateLayer(s.index) { it.copy(locked = !it.locked) }
            is EditorSelection.CityItem -> updateCity(s.index) { it.copy(locked = !it.locked) }
            is EditorSelection.TextItem -> updateText(s.index) { it.copy(locked = !it.locked) }
        }
    }

    fun deleteSelected() {
        val s = selection ?: return
        val c = config
        val next = when (s) {
            is EditorSelection.Layer -> {
                val removed = c.layers.getOrNull(s.index) ?: return
                val replacement = c.layers.firstOrNull { it.id != removed.id }?.id ?: 0L
                c.copy(
                    layers = c.layers.filterIndexed { i, _ -> i != s.index },
                    cities = c.cities.map { if (it.componentId == removed.id) it.copy(componentId = replacement) else it },
                    texts = c.texts.map { if (it.componentId == removed.id) it.copy(componentId = replacement) else it },
                )
            }
            is EditorSelection.CityItem -> c.copy(cities = c.cities.filterIndexed { i, _ -> i != s.index })
            is EditorSelection.TextItem -> c.copy(texts = c.texts.filterIndexed { i, _ -> i != s.index })
        }
        selection = null
        commit(next)
    }

    fun duplicateSelected() {
        val s = selection ?: return
        val c = config
        when (s) {
            is EditorSelection.Layer -> if (c.layers.size < MAX_LAYERS) c.layers.getOrNull(s.index)?.let { l ->
                commit(c.copy(layers = c.layers + l.copy(id = newId(c), x = (l.x + .08f).coerceIn(-1f, 1f), y = (l.y + .08f).coerceIn(-1f, 1f), locked = false)))
                selection = EditorSelection.Layer(c.layers.size)
            }
            is EditorSelection.CityItem -> if (c.cities.size < MAX_CITIES) c.cities.getOrNull(s.index)?.let { city ->
                commit(c.copy(cities = c.cities + city.copy(x = (city.x + .1f).coerceIn(-1f, 1f), y = (city.y + .1f).coerceIn(-1f, 1f), locked = false)))
                selection = EditorSelection.CityItem(c.cities.size)
            }
            is EditorSelection.TextItem -> if (c.texts.size < MAX_TEXTS) c.texts.getOrNull(s.index)?.let { t ->
                commit(c.copy(texts = c.texts + t.copy(x = (t.x + .1f).coerceIn(-1f, 1f), y = (t.y + .1f).coerceIn(-1f, 1f), locked = false)))
                selection = EditorSelection.TextItem(c.texts.size)
            }
        }
    }

    /** Moves the selected element one step up (+1) or down (−1) within its own kind. */
    fun reorder(step: Int) {
        val s = selection ?: return
        fun <T> List<T>.moved(from: Int, to: Int): List<T> = toMutableList().also { it.add(to, it.removeAt(from)) }
        val c = config
        when (s) {
            is EditorSelection.Layer -> { val to = (s.index + step).coerceIn(0, c.layers.lastIndex); if (to != s.index) { commit(c.copy(layers = c.layers.moved(s.index, to))); selection = EditorSelection.Layer(to) } }
            is EditorSelection.CityItem -> { val to = (s.index + step).coerceIn(0, c.cities.lastIndex); if (to != s.index) { commit(c.copy(cities = c.cities.moved(s.index, to))); selection = EditorSelection.CityItem(to) } }
            is EditorSelection.TextItem -> { val to = (s.index + step).coerceIn(0, c.texts.lastIndex); if (to != s.index) { commit(c.copy(texts = c.texts.moved(s.index, to))); selection = EditorSelection.TextItem(to) } }
        }
    }

    fun updateConfig(f: (WidgetConfig) -> WidgetConfig) = commit(f(config))

    fun newId(c: WidgetConfig = config): Long = (c.layers.maxOfOrNull { it.id } ?: 0L).coerceAtLeast(System.currentTimeMillis()) + 1

    companion object { const val HISTORY = 60 }
}
