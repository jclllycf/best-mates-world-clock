package com.bestmates.worldclock.ui.editor

import com.bestmates.worldclock.scene.Frame
import kotlin.math.abs
import kotlin.math.max

/** One selectable element as the canvas hit test sees it, in board dp. */
data class HitCandidate(val selection: EditorSelection, val frame: Frame, val locked: Boolean)

/**
 * What a touch-down on the canvas is aimed at: [tap] is selected if the finger lifts
 * without moving, [drag] is moved if it drags. They differ only when a locked element sits
 * in front of the selected one.
 */
data class HitResult(val tap: EditorSelection?, val drag: EditorSelection?)

/**
 * Canvas hit testing, kept free of Compose so it can be unit tested.
 *
 * The visually front-most element under the finger wins, whatever is selected. Until
 * 0.4.1 a selected element won whenever the finger was anywhere inside it, so with a
 * full-board shape selected, a city or text on top of it could never be tapped or dragged
 * directly (BM-REAL-03). Handles of the selection are tested before this, by the canvas.
 */
object EditorHitTest {
    /**
     * @param frontToBack every selectable element, front-most first (texts, cities, shapes;
     *   later items of each kind in front of earlier ones)
     * @param slop extra margin around every frame, in board dp
     * @param minTarget minimum touch size in board dp; smaller elements get a larger target
     *   so a tiny text or city can still be picked, but only where nothing is hit directly
     */
    fun pick(
        frontToBack: List<HitCandidate>,
        x: Float,
        y: Float,
        selected: EditorSelection?,
        slop: Float,
        minTarget: Float,
    ): HitResult {
        val direct = frontToBack.filter { it.frame.contains(x, y, slop) }
        val front = direct.firstOrNull() ?: assisted(frontToBack, x, y, minTarget) ?: return HitResult(null, null)
        // A locked element in front never stops the selected element behind it from moving.
        val held = selected?.let { s -> direct.firstOrNull { it.selection == s } }
        val drag = if (front.locked && held != null && !held.locked) held.selection else front.selection
        return HitResult(front.selection, drag)
    }

    /** Small elements whose enlarged target contains the point; the nearest centre wins. */
    private fun assisted(candidates: List<HitCandidate>, x: Float, y: Float, minTarget: Float): HitCandidate? =
        candidates.filter { c ->
            val f = c.frame
            val ex = max(0f, (minTarget - f.w) / 2f)
            val ey = max(0f, (minTarget - f.h) / 2f)
            if (ex == 0f && ey == 0f) return@filter false
            val (lx, ly) = f.toLocal(x, y)
            abs(lx) <= f.w / 2f + ex && abs(ly) <= f.h / 2f + ey
        }.minByOrNull { c -> val (lx, ly) = c.frame.toLocal(x, y); lx * lx + ly * ly }
}
