package com.bestmates.worldclock.scene

import com.bestmates.worldclock.model.WidgetConfig
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

    /**
     * The board a widget of [viewW] × [viewH] dp actually shows.
     *
     * A launcher cell rarely has exactly the project's proportions (launchers differ, and
     * projects made before calibration used a generic cell). Fitting the board unchanged
     * leaves transparent bands at two sides, and a full-bleed card then stops short of
     * the widget's edges. Instead the board grows along the short axis up to [maxStretch];
     * any remaining difference stays a transparent margin.
     */
    fun adaptBoard(boardW: Float, boardH: Float, viewW: Float, viewH: Float, maxStretch: Float = 1.35f): Pair<Float, Float> {
        if (viewW <= 0f || viewH <= 0f || boardW <= 0f || boardH <= 0f) return boardW to boardH
        val viewAspect = viewW / viewH
        val boardAspect = boardW / boardH
        return when {
            abs(viewAspect / boardAspect - 1f) < .002f -> boardW to boardH
            viewAspect > boardAspect -> boardW * min(viewAspect / boardAspect, maxStretch) to boardH
            else -> boardW to boardH * min(boardAspect / viewAspect, maxStretch)
        }
    }

    /**
     * Re-proportions a design to a board of [newW] × [newH] dp without distorting it.
     *
     * The composition stays together about the centre, scaled uniformly by the design
     * scale (1 when only the proportions change), so a city that sat on a card still sits
     * on it. Only shapes that span the board on an axis stretch to keep spanning it, and
     * shapes touching one edge stay on that edge.
     */
    fun reflow(config: WidgetConfig, newW: Float, newH: Float): WidgetConfig {
        val bw = config.boardWidthDp; val bh = config.boardHeightDp
        if (newW <= 0f || newH <= 0f || (abs(newW - bw) < .01f && abs(newH - bh) < .01f)) return config
        val s = min(newW / bw, newH / bh)
        val scaled = abs(s - 1f) > .005f
        // Stored positions are −1…1 of the half board; keep their dp offset from the centre.
        val kx = bw * s / newW; val ky = bh * s / newH
        return config.copy(
            boardWidthDp = newW, boardHeightDp = newH,
            layers = config.layers.map { reflowLayer(it, bw, bh, newW, newH) },
            cities = config.cities.map { c ->
                c.copy(x = (c.x * kx).coerceIn(-1f, 1f), y = (c.y * ky).coerceIn(-1f, 1f), scale = if (scaled) (c.scale * s).coerceIn(.35f, 3f) else c.scale)
            },
            texts = config.texts.map { t ->
                t.copy(x = (t.x * kx).coerceIn(-1f, 1f), y = (t.y * ky).coerceIn(-1f, 1f), size = if (scaled) (t.size * s).coerceIn(6f, 120f) else t.size)
            },
        )
    }

    private fun reflowLayer(layer: WidgetLayer, bw: Float, bh: Float, newW: Float, newH: Float): WidgetLayer {
        val f = layerFrame(layer, bw, bh)
        // Uniform scale of the design itself: 1 when only the proportions change.
        val s = min(newW / bw, newH / bh)
        fun axis(lo: Float, hi: Float, size: Float, newSize: Float): Pair<Float, Float> {
            val eps = size * .01f
            val length = hi - lo
            val atStart = lo <= eps; val atEnd = hi >= size - eps
            if (atStart && atEnd) return 0f to newSize
            val len = min(length * s, newSize)
            val start = when {
                atStart -> 0f
                atEnd -> newSize - len
                else -> newSize / 2f + ((lo + hi) / 2f - size / 2f) * s - len / 2f
            }
            return start to start + len
        }
        // Rotated shapes keep their frame; their centre keeps its offset from the board centre.
        fun centred(c: Float, size: Float, newSize: Float) = newSize / 2f + (c - size / 2f) * s
        val (x0, x1) = if (f.rotation % 180f == 0f) axis(f.left, f.right, bw, newW) else (centred(f.cx, bw, newW) - f.w * s / 2f) to (centred(f.cx, bw, newW) + f.w * s / 2f)
        val (y0, y1) = if (f.rotation % 180f == 0f) axis(f.top, f.bottom, bh, newH) else (centred(f.cy, bh, newH) - f.h * s / 2f) to (centred(f.cy, bh, newH) + f.h * s / 2f)
        return layerFromFrame(layer, Frame((x0 + x1) / 2f, (y0 + y1) / 2f, x1 - x0, y1 - y0, f.rotation), newW, newH)
    }

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
 * A magnet line on one axis, in board dp. [edges] attracts an element's edges, [centre]
 * its centre. [rank] breaks ties between lines at the same distance (higher wins), so a
 * board edge beats a peer edge that happens to sit at the same place.
 */
data class SnapLine(val value: Float, val edges: Boolean = true, val centre: Boolean = true, val rank: Int = 0)

/** A size worth landing on, with a short label for the readout. */
data class SizeMagnet(val value: Float, val label: String)

/** Result of snapping a size: the snapped size and the label of the magnet, if any. */
data class SizeSnap(val value: Float, val label: String?)

/** A proportion magnet for corner resizing (width ÷ height). */
data class AspectMagnet(val ratio: Float, val label: String)

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

    const val HOLD = 1.6f

    /**
     * Like [snapAxis] with typed magnet lines. A line that is already holding the element
     * ([held]) keeps it until the finger moves [HOLD] times the threshold away. That
     * hysteresis makes magnets feel deliberate without making free placement sticky.
     */
    fun snapLines(lo: Float, mid: Float, hi: Float, lines: List<SnapLine>, threshold: Float, held: Float? = null): Snap {
        var best: Float? = null
        var bestGuide: Float? = null
        var bestScore = Float.MAX_VALUE
        var bestRank = Int.MIN_VALUE
        for (line in lines) {
            val isHeld = held != null && abs(line.value - held) < .01f
            val limit = if (isHeld) threshold * HOLD else threshold
            val anchors = buildList { if (line.edges) { add(lo); add(hi) }; if (line.centre) add(mid) }
            for (p in anchors) {
                val d = line.value - p
                if (abs(d) > limit) continue
                // The held line wins outright while in reach; otherwise nearest, then rank.
                val score = if (isHeld) -1f else abs(d)
                if (score < bestScore || (score == bestScore && line.rank > bestRank)) {
                    best = d; bestGuide = line.value; bestScore = score; bestRank = line.rank
                }
            }
        }
        return if (best == null) Snap(0f, null) else Snap(best, bestGuide)
    }

    /** Board magnets for one axis: edges (attract edges), centre, thirds and quarters. */
    fun boardLines(size: Float): List<SnapLine> = listOf(
        SnapLine(0f, centre = false, rank = 3), SnapLine(size, centre = false, rank = 3),
        SnapLine(size / 2f, rank = 3),
        SnapLine(size / 3f, rank = 1), SnapLine(size * 2f / 3f, rank = 1),
        SnapLine(size / 4f, rank = 1), SnapLine(size * 3f / 4f, rank = 1),
    )

    /** Snaps a size to the nearest magnet within [threshold], keeping a [held] magnet longer. */
    fun snapSize(value: Float, magnets: List<SizeMagnet>, threshold: Float, held: String? = null): SizeSnap {
        var best: SizeMagnet? = null
        var bestScore = Float.MAX_VALUE
        for (m in magnets) {
            val isHeld = held != null && m.label == held
            val d = abs(m.value - value)
            if (d > if (isHeld) threshold * HOLD else threshold) continue
            val score = if (isHeld) -1f else d
            if (score < bestScore) { best = m; bestScore = score }
        }
        return best?.let { SizeSnap(it.value, it.label) } ?: SizeSnap(value, null)
    }

    /** The proportion magnet that [w] ÷ [h] falls within [tolerance] (relative) of, if any. */
    fun snapAspect(w: Float, h: Float, magnets: List<AspectMagnet>, tolerance: Float = .035f, held: String? = null): AspectMagnet? {
        if (w <= 0f || h <= 0f) return null
        val ratio = w / h
        return magnets
            .filter { abs(ratio / it.ratio - 1f) <= if (it.label == held) tolerance * HOLD else tolerance }
            .minByOrNull { if (it.label == held) -1f else abs(ratio / it.ratio - 1f) }
    }

    /** Fractions of the board worth sizing to. */
    fun fractionMagnets(size: Float, prefix: String = ""): List<SizeMagnet> = listOf(
        SizeMagnet(size / 4f, "$prefix¼"), SizeMagnet(size / 3f, "$prefix⅓"), SizeMagnet(size / 2f, "$prefix½"),
        SizeMagnet(size * 2f / 3f, "$prefix⅔"), SizeMagnet(size * 3f / 4f, "$prefix¾"), SizeMagnet(size, "${prefix}1"),
    )

    fun boardTargets(size: Float, grid: Int = 8): List<Float> = (0..grid).map { size * it / grid }

    fun rotation(value: Float, enabled: Boolean, step: Float = 15f, window: Float = 3.5f): Snap {
        if (!enabled) return Snap(value, null)
        val snapped = Math.round(value / step) * step
        return if (abs(snapped - value) <= window) Snap(snapped, snapped) else Snap(value, null)
    }
}
