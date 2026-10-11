package com.bestmates.worldclock.scene

import com.bestmates.worldclock.model.WidgetLayer
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * A rotated rectangle in design-board dp, origin at the board's top-left corner.
 * Rotation is in degrees, clockwise on screen, about the centre.
 */
data class Frame(val cx: Float, val cy: Float, val w: Float, val h: Float, val rotation: Float = 0f) {
    val left get() = cx - w / 2f
    val top get() = cy - h / 2f
    val right get() = cx + w / 2f
    val bottom get() = cy + h / 2f

    /** Converts a board point into this frame's unrotated local space (centre origin). */
    fun toLocal(x: Float, y: Float): Pair<Float, Float> {
        val r = Math.toRadians(-rotation.toDouble())
        val dx = x - cx; val dy = y - cy
        val c = cos(r).toFloat(); val s = sin(r).toFloat()
        return (dx * c - dy * s) to (dx * s + dy * c)
    }

    fun toBoard(lx: Float, ly: Float): Pair<Float, Float> {
        val r = Math.toRadians(rotation.toDouble())
        val c = cos(r).toFloat(); val s = sin(r).toFloat()
        return (cx + lx * c - ly * s) to (cy + lx * s + ly * c)
    }

    fun contains(x: Float, y: Float, slop: Float = 0f): Boolean {
        val (lx, ly) = toLocal(x, y)
        return abs(lx) <= w / 2f + slop && abs(ly) <= h / 2f + slop
    }
}

/** Uniform board-to-target mapping: target = origin + boardDp × scale. */
data class BoardFit(val originX: Float, val originY: Float, val scale: Float, val boardW: Float, val boardH: Float) {
    fun x(dp: Float) = originX + dp * scale
    fun y(dp: Float) = originY + dp * scale
    fun boardX(target: Float) = (target - originX) / scale
    fun boardY(target: Float) = (target - originY) / scale
}

object SceneGeometry {
    const val MIN_LAYER_FRACTION = .06f

    /** Largest uniform scale of the design board that fits inside the target area, centred. */
    fun fit(boardW: Float, boardH: Float, areaX: Float, areaY: Float, areaW: Float, areaH: Float): BoardFit {
        val scale = min(areaW / boardW, areaH / boardH).coerceAtLeast(0.0001f)
        val w = boardW * scale; val h = boardH * scale
        return BoardFit(areaX + (areaW - w) / 2f, areaY + (areaH - h) / 2f, scale, boardW, boardH)
    }

    /** Matches the historical renderer: a layer is sized from the board and kept on it. */
    fun layerFrame(layer: WidgetLayer, boardW: Float, boardH: Float): Frame {
        val w = boardW * layer.width.coerceIn(MIN_LAYER_FRACTION, 1f)
        val h = boardH * layer.height.coerceIn(MIN_LAYER_FRACTION, 1f)
        val cx = (boardW / 2f + layer.x * boardW / 2f).coerceIn(w / 2f, boardW - w / 2f)
        val cy = (boardH / 2f + layer.y * boardH / 2f).coerceIn(h / 2f, boardH - h / 2f)
        return Frame(cx, cy, w, h, layer.rotation)
    }

    /** Places a content box whose centre is stored as normalised −1…1 board coordinates. */
    fun contentFrame(x: Float, y: Float, w: Float, h: Float, rotation: Float, boardW: Float, boardH: Float): Frame {
        val cw = min(w, boardW); val ch = min(h, boardH)
        val cx = (boardW / 2f + x * boardW / 2f).coerceIn(cw / 2f, boardW - cw / 2f)
        val cy = (boardH / 2f + y * boardH / 2f).coerceIn(ch / 2f, boardH - ch / 2f)
        return Frame(cx, cy, w, h, rotation)
    }

    fun normX(cx: Float, boardW: Float) = ((cx - boardW / 2f) / (boardW / 2f)).coerceIn(-1f, 1f)
    fun normY(cy: Float, boardH: Float) = ((cy - boardH / 2f) / (boardH / 2f)).coerceIn(-1f, 1f)

    /** Layer centre/size back to the stored fractional form. */
    fun layerFromFrame(layer: WidgetLayer, frame: Frame, boardW: Float, boardH: Float): WidgetLayer {
        val wf = (frame.w / boardW).coerceIn(MIN_LAYER_FRACTION, 1f)
        val hf = (frame.h / boardH).coerceIn(MIN_LAYER_FRACTION, 1f)
        val w = wf * boardW; val h = hf * boardH
        val cx = frame.cx.coerceIn(w / 2f, boardW - w / 2f)
        val cy = frame.cy.coerceIn(h / 2f, boardH - h / 2f)
        return layer.copy(x = normX(cx, boardW), y = normY(cy, boardH), width = wf, height = hf, rotation = frame.rotation)
    }
}

/** Result of snapping one axis value: the snapped value and the guide position, if any. */
data class Snap(val value: Float, val guide: Float?)

/**
 * Magnetic snapping in board dp. Candidates are board edges, centre, eighth-grid lines
 * and the edges/centres of other elements. [threshold] is in board dp and should be
 * derived from a constant on-screen distance so the magnet feels the same at any zoom.
 */
object SnapMath {
    /**
     * Snaps an element whose edges/centre sit at [lo], [mid], [hi] on one axis.
     * Returns the delta to apply to the element and the guide line position.
     */
    fun snapAxis(lo: Float, mid: Float, hi: Float, targets: List<Float>, threshold: Float): Snap {
        var best: Float? = null
        var bestGuide: Float? = null
        for (t in targets) {
            for (p in floatArrayOf(lo, mid, hi)) {
                val d = t - p
                if (abs(d) <= threshold && (best == null || abs(d) < abs(best))) { best = d; bestGuide = t }
            }
        }
        return if (best == null) Snap(0f, null) else Snap(best, bestGuide)
    }

    fun boardTargets(size: Float, grid: Int = 8): List<Float> = (0..grid).map { size * it / grid }

    fun rotation(value: Float, enabled: Boolean, step: Float = 15f, window: Float = 3.5f): Snap {
        if (!enabled) return Snap(value, null)
        val snapped = Math.round(value / step) * step
        return if (abs(snapped - value) <= window) Snap(snapped, snapped) else Snap(value, null)
    }
}
