package com.bestmates.worldclock.ui.editor

import com.bestmates.worldclock.scene.AspectMagnet
import com.bestmates.worldclock.scene.Frame
import com.bestmates.worldclock.scene.SceneGeometry
import com.bestmates.worldclock.scene.SizeMagnet
import com.bestmates.worldclock.scene.SnapLine
import com.bestmates.worldclock.scene.SnapMath
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Resizing a shape from one of its handles, with magnets. Pure geometry in board dp so it
 * can be unit tested.
 *
 * A handle moves one or two sides of the shape's own (rotated) frame; the opposite sides
 * stay where they are. With magnets, each moving side lands on board and peer lines (for
 * upright shapes) or on useful sizes (board fractions, other shapes' sizes), whichever is
 * nearer; a corner drag also catches useful proportions (1:1, the widget's, the shape's
 * natural one, and the proportion it started with).
 */
object LayerResize {
    data class Magnets(
        val xLines: List<SnapLine>,
        val yLines: List<SnapLine>,
        val widths: List<SizeMagnet>,
        val heights: List<SizeMagnet>,
        val aspects: List<AspectMagnet>,
        val threshold: Float,
    )

    /** Which magnets held the previous step of this gesture (hysteresis). */
    data class Held(val x: Float? = null, val y: Float? = null, val w: String? = null, val h: String? = null, val aspect: String? = null)

    data class Result(val frame: Frame, val xs: List<Float>, val ys: List<Float>, val label: String?, val held: Held)

    private data class AxisSnap(val length: Float, val guide: Float?, val label: String?, val heldLine: Float?, val heldSize: String?)

    /**
     * @param sx which vertical side moves: +1 right, −1 left, 0 neither (same for [sy])
     * @param localDelta finger movement in the frame's own unrotated axes, board dp
     */
    fun resize(
        frame: Frame,
        sx: Int,
        sy: Int,
        localDelta: Pair<Float, Float>,
        boardW: Float,
        boardH: Float,
        snap: Magnets?,
        held: Held = Held(),
    ): Result {
        val minW = boardW * SceneGeometry.MIN_LAYER_FRACTION
        val minH = boardH * SceneGeometry.MIN_LAYER_FRACTION
        var w = if (sx != 0) (frame.w + sx * localDelta.first).coerceIn(minW, boardW) else frame.w
        var h = if (sy != 0) (frame.h + sy * localDelta.second).coerceIn(minH, boardH) else frame.h
        var xs = emptyList<Float>(); var ys = emptyList<Float>()
        val labels = mutableListOf<String>()
        var next = Held()
        if (snap != null) {
            val upright = abs(frame.rotation % 360f) < .01f
            var wSnapped = false; var hSnapped = false
            if (sx != 0) {
                axis(w, sx, frame.left, frame.right, snap.xLines, snap.widths, snap.threshold, upright, minW, boardW, held.x, held.w)?.let { s ->
                    w = s.length; wSnapped = true
                    xs = listOfNotNull(s.guide); s.label?.let(labels::add)
                    next = next.copy(x = s.heldLine, w = s.heldSize)
                }
            }
            if (sy != 0) {
                axis(h, sy, frame.top, frame.bottom, snap.yLines, snap.heights, snap.threshold, upright, minH, boardH, held.y, held.h)?.let { s ->
                    h = s.length; hSnapped = true
                    ys = listOfNotNull(s.guide); s.label?.let(labels::add)
                    next = next.copy(y = s.heldLine, h = s.heldSize)
                }
            }
            if (sx != 0 && sy != 0 && !(wSnapped && hSnapped)) {
                SnapMath.snapAspect(w, h, snap.aspects, held = held.aspect)?.let { a ->
                    val nw = if (hSnapped) h * a.ratio else w
                    val nh = if (hSnapped) h else w / a.ratio
                    if (nw in minW..boardW && nh in minH..boardH) {
                        if (!hSnapped && nh != h) ys = emptyList()
                        if (hSnapped && nw != w) xs = emptyList()
                        w = nw; h = nh
                        labels += a.label
                        next = next.copy(aspect = a.label)
                    }
                }
            }
        }
        val r = Math.toRadians(frame.rotation.toDouble()); val c = cos(r).toFloat(); val s = sin(r).toFloat()
        val lx = (w - frame.w) / 2f * sx; val ly = (h - frame.h) / 2f * sy
        val nf = Frame(frame.cx + lx * c - ly * s, frame.cy + lx * s + ly * c, w, h, frame.rotation)
        return Result(nf, xs, ys, labels.takeIf { it.isNotEmpty() }?.joinToString("  ·  "), next)
    }

    /** The better of an edge-line snap and a size snap for one moving side, or null. */
    private fun axis(
        length: Float, side: Int, lo: Float, hi: Float, lines: List<SnapLine>, sizes: List<SizeMagnet>,
        threshold: Float, upright: Boolean, min: Float, max: Float, heldLine: Float?, heldSize: String?,
    ): AxisSnap? {
        var edge: AxisSnap? = null
        if (upright) {
            val pos = if (side > 0) lo + length else hi - length
            val s = SnapMath.snapLines(pos, pos, pos, lines, threshold, heldLine)
            if (s.guide != null) {
                val len = if (side > 0) s.guide - lo else hi - s.guide
                if (len in min..max) edge = AxisSnap(len, s.guide, null, s.guide, null)
            }
        }
        val sz = SnapMath.snapSize(length, sizes, threshold, heldSize)
        val size = sz.label?.let { AxisSnap(sz.value.coerceIn(min, max), null, it, null, it) }
        return when {
            edge == null -> size
            size == null -> edge
            // A magnet that is already holding keeps priority; otherwise the nearer one wins.
            heldLine != null && edge.guide == heldLine -> edge
            heldSize != null && size.label == heldSize -> size
            abs(edge.length - length) <= abs(size.length - length) -> edge
            else -> size
        }
    }
}
