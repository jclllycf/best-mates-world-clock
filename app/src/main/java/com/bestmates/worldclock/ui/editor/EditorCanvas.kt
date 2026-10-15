package com.bestmates.worldclock.ui.editor

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.scene.AspectMagnet
import com.bestmates.worldclock.scene.BoardFit
import com.bestmates.worldclock.scene.DrawOptions
import com.bestmates.worldclock.scene.Frame
import com.bestmates.worldclock.scene.LayerItem
import com.bestmates.worldclock.scene.Scene
import com.bestmates.worldclock.scene.SceneGeometry
import com.bestmates.worldclock.scene.SceneItem
import com.bestmates.worldclock.scene.SizeMagnet
import com.bestmates.worldclock.scene.SnapLine
import com.bestmates.worldclock.scene.SnapMath
import com.bestmates.worldclock.ui.common.Backdrop
import com.bestmates.worldclock.ui.common.SceneServices
import com.bestmates.worldclock.ui.common.WallpaperTones
import com.bestmates.worldclock.ui.common.backdropBrush
import com.bestmates.worldclock.ui.common.rememberMinuteTick
import com.bestmates.worldclock.ui.design.Bm
import com.bestmates.worldclock.ui.i18n.uiText
import java.time.Instant
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class Handle(val sx: Int, val sy: Int) {
    Move(0, 0), Corner(1, 1), Left(-1, 0), Right(1, 0), Top(0, -1), Bottom(0, 1), Rotate(0, 0);
    val resizes get() = this != Move && this != Rotate
}

/** Guide lines and readout for the gesture in progress, in board dp. */
private data class Guides(
    val xs: List<Float> = emptyList(),
    val ys: List<Float> = emptyList(),
    val angle: Float? = null,
    val label: String? = null,
    /** Other elements the moving one is aligned or sized to. */
    val peers: List<Frame> = emptyList(),
    /** A size or proportion magnet in effect (it has no guide line). */
    val magnet: String? = null,
) {
    /** Identity of the magnets in effect; a change fires one haptic tick. */
    val key get() = "${xs.map { it.roundToInt() }}|${ys.map { it.roundToInt() }}|$angle|$magnet"
    val snapped get() = xs.isNotEmpty() || ys.isNotEmpty() || angle != null || magnet != null
}

/** Localised words for the magnet readout. */
private data class SnapWords(val ratioWidget: String, val ratioShape: String, val ratioKept: String)

/** Magnet memory across the events of one gesture, so a caught magnet holds (hysteresis). */
private class Held {
    var x: Float? = null; var y: Float? = null
    var w: String? = null; var h: String? = null
    var aspect: String? = null; var scale: String? = null
}

/**
 * The interactive design surface. Draws the same [Scene] the launcher bitmap is drawn
 * from, then overlays selection chrome. One gesture detector hit-tests rotated frames in
 * board coordinates, so drags never need per-element rotated-pointer correction.
 *
 * Touch rules: handles of the selection first; otherwise the front-most element under the
 * finger ([EditorHitTest]). Two fingers pinch and turn the touched element, or the
 * selection when the gesture starts on empty canvas.
 */
@Composable
fun EditorCanvas(
    state: EditorState,
    night: Boolean,
    backdrop: Backdrop,
    wallpaper: WallpaperTones?,
    snap: Boolean,
    showGrid: Boolean,
    emptyHint: String,
    modifier: Modifier = Modifier,
    bottomInset: Dp = 0.dp,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    val colors = Bm.colors
    val minute = rememberMinuteTick()
    val builder = remember { SceneServices.builder(context) }
    val renderer = remember { SceneServices.renderer(context) }
    val config = state.config
    val scene = remember(config, minute) { builder.build(config, Instant.ofEpochMilli(minute * 60_000L)) }
    var guides by remember { mutableStateOf(Guides()) }
    var interacting by remember { mutableStateOf(false) }
    val words by rememberUpdatedState(SnapWords(uiText("snap_ratio_widget"), uiText("snap_ratio_shape"), uiText("snap_ratio_kept")))

    BoxWithConstraints(modifier.background(colors.desk)) {
        val side = with(density) { 22.dp.toPx() }
        val top = with(density) { 14.dp.toPx() }
        val bottom = with(density) { (bottomInset + 12.dp).toPx() }
        val wPx = with(density) { maxWidth.toPx() }
        val hPx = with(density) { maxHeight.toPx() }
        val fit = SceneGeometry.fit(scene.boardW, scene.boardH, side, top, (wPx - side * 2).coerceAtLeast(10f), (hPx - top - bottom).coerceAtLeast(10f))
        val latestScene by rememberUpdatedState(scene)
        val latestFit by rememberUpdatedState(fit)
        val latestSnap by rememberUpdatedState(snap)

        Canvas(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    val handleRadius = 24.dp.toPx()
                    val rotateOffset = ROTATE_OFFSET.toPx()
                    val knob = ROTATE_KNOB.toPx()
                    val snapPx = 9.dp.toPx()
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val sc = latestScene
                        val ft = latestFit
                        val cfg0 = state.config
                        val selected = state.selection
                        val bounds = Size(size.width.toFloat(), size.height.toFloat())

                        // 1. Handles of the current selection win over everything else.
                        var handleHit: Pair<EditorSelection, Handle>? = null
                        val selItem = selected?.let { itemFor(sc, it) }
                        if (selItem != null && !state.isLocked(selected)) {
                            handlePositions(selItem, ft, bounds, rotateOffset, knob)
                                .filter { (it.second - down.position).getDistance() <= handleRadius }
                                .minByOrNull { (it.second - down.position).getDistance() }
                                ?.let { handleHit = selected to it.first }
                        }
                        // 2. The front-most element under the finger.
                        val hit = if (handleHit != null) HitResult(selected, selected) else EditorHitTest.pick(
                            candidates(sc, cfg0), ft.boardX(down.position.x), ft.boardY(down.position.y), selected,
                            slop = 6.dp.toPx() / ft.scale, minTarget = 44.dp.toPx() / ft.scale,
                        )
                        // Select at once when tap and drag agree (nearly always), so the
                        // inspector follows the finger.
                        if (handleHit == null && hit.tap != null && hit.tap == hit.drag && state.selection != hit.tap) state.selection = hit.tap
                        val moveSel = handleHit?.first ?: hit.drag
                        val handle = handleHit?.second ?: Handle.Move
                        // Pinching on empty canvas acts on the selection.
                        val pinchSel = moveSel ?: selected?.takeIf { !state.isLocked(it) }

                        var dragging = false
                        var pinching = false
                        var zoomTotal = 1f; var rotTotal = 0f; var panTotal = Offset.Zero
                        var angleTotal = 0f
                        var lastAngle = 0f
                        var lastKey = ""
                        val held = Held()

                        fun publish(next: Guides) {
                            if (next.snapped && next.key != lastKey) haptic(view)
                            lastKey = if (next.snapped) next.key else ""
                            guides = next
                        }

                        while (true) {
                            val event = awaitPointerEvent()
                            val pressed = event.changes.filter { it.pressed }
                            if (pressed.isEmpty()) break
                            if (pressed.size >= 2) {
                                val target = pinchSel?.takeIf { !state.isLocked(it) }
                                val item0 = target?.let { itemFor(sc, it) }
                                if (target == null || item0 == null) { event.changes.forEach { it.consume() }; continue }
                                if (!pinching) {
                                    pinching = true
                                    if (state.selection != target) state.selection = target
                                    if (!dragging) { dragging = true; state.startGesture(); interacting = true }
                                }
                                zoomTotal *= event.calculateZoom()
                                rotTotal += event.calculateRotation()
                                panTotal += event.calculatePan()
                                val zoom = if (latestSnap && abs(zoomTotal - 1f) < .04f) 1f else zoomTotal
                                val rot = SnapMath.rotation(item0.frame.rotation + rotTotal, latestSnap)
                                state.live(applyPinch(cfg0, sc, target, item0, item0.frame, zoom, rot.value, panTotal / ft.scale))
                                publish(Guides(angle = rot.guide, label = "${(zoom * 100).roundToInt()}%  ·  ${normalizeAngle(rot.value).roundToInt()}°", magnet = if (zoom == 1f && zoomTotal != 1f) "100%" else null))
                                event.changes.forEach { it.consume() }
                                continue
                            }
                            if (pinching) { event.changes.forEach { it.consume() }; continue }
                            val sel = moveSel
                            val item0 = sel?.let { itemFor(sc, it) }
                            if (sel == null || item0 == null || state.isLocked(sel)) {
                                event.changes.forEach { if (it.positionChanged()) it.consume() }
                                continue
                            }
                            val frame0 = item0.frame
                            val change = pressed.first()
                            val pan = change.position - down.position
                            if (!dragging && pan.getDistance() > viewConfiguration.touchSlop) {
                                dragging = true; state.startGesture(); interacting = true
                                if (state.selection != sel) state.selection = sel
                                lastAngle = angleOf(Offset(ft.x(frame0.cx), ft.y(frame0.cy)), change.position)
                            }
                            if (!dragging) continue
                            change.consume()
                            val snapOn = latestSnap
                            val threshold = snapPx / ft.scale
                            val next: Pair<WidgetConfig, Guides> = when {
                                handle == Handle.Move -> applyMove(cfg0, sc, sel, item0, frame0, pan / ft.scale, snapOn, threshold, held)
                                handle.resizes -> applyResize(cfg0, sc, sel, item0, frame0, handle, down.position, change.position, ft, snapOn, threshold, held, words)
                                else -> {
                                    val c = Offset(ft.x(frame0.cx), ft.y(frame0.cy))
                                    val a = angleOf(c, change.position)
                                    var d = a - lastAngle
                                    while (d > 180f) d -= 360f
                                    while (d < -180f) d += 360f
                                    angleTotal += d; lastAngle = a
                                    val snapped = SnapMath.rotation(frame0.rotation + angleTotal, snapOn)
                                    rotateItem(cfg0, sel, snapped.value) to Guides(angle = snapped.guide, label = "${normalizeAngle(snapped.value).roundToInt()}°")
                                }
                            }
                            state.live(next.first)
                            publish(next.second)
                        }
                        if (dragging) state.endGesture()
                        else if (handleHit == null && state.selection != hit.tap) state.selection = hit.tap
                        guides = Guides()
                        interacting = false
                    }
                },
        ) {
            drawDesk(colors.deskDot)
            val tile = 9.dp.toPx()
            drawRoundRect(
                backdropBrush(backdrop, wallpaper),
                topLeft = Offset(fit.originX - tile, fit.originY - tile),
                size = Size(scene.boardW * fit.scale + tile * 2, scene.boardH * fit.scale + tile * 2),
                cornerRadius = CornerRadius(20.dp.toPx()),
            )
            val boardSize = Size(scene.boardW * fit.scale, scene.boardH * fit.scale)
            if (interacting || showGrid) {
                val grid = Color.White.copy(alpha = .16f)
                for (i in 1 until config.columns) { val x = fit.originX + boardSize.width * i / config.columns; drawLine(grid, Offset(x, fit.originY), Offset(x, fit.originY + boardSize.height), 1f) }
                for (i in 1 until config.rows) { val y = fit.originY + boardSize.height * i / config.rows; drawLine(grid, Offset(fit.originX, y), Offset(fit.originX + boardSize.width, y), 1f) }
            }
            drawRoundRect(
                Color.White.copy(alpha = .5f), topLeft = Offset(fit.originX, fit.originY), size = boardSize, cornerRadius = CornerRadius(12.dp.toPx()),
                style = Stroke(1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx()))),
            )
            drawIntoCanvas { renderer.draw(it.nativeCanvas, scene, fit, DrawOptions(night = night)) }

            // Guides: the magnet lines, and a soft outline on the elements they come from.
            val accent = colors.accent
            guides.peers.forEach { drawFrameOutline(it, fit, accent.copy(alpha = .55f), dashed = true) }
            guides.xs.forEach { gx -> val x = fit.x(gx); drawGuide(accent, Offset(x, fit.originY - tile), Offset(x, fit.originY + boardSize.height + tile)) }
            guides.ys.forEach { gy -> val y = fit.y(gy); drawGuide(accent, Offset(fit.originX - tile, y), Offset(fit.originX + boardSize.width + tile, y)) }

            // Selection chrome
            state.selection?.let { sel -> itemFor(scene, sel) }?.let { item ->
                drawSelection(item, fit, size, accent, state.isLocked(state.selection), guides.angle != null)
            }
        }

        if (config.layers.isEmpty() && config.cities.isEmpty() && config.texts.none { it.content.isNotBlank() }) {
            Text(emptyHint, style = Bm.type.bodySmall, color = Color.White.copy(alpha = .85f), modifier = Modifier.align(Alignment.Center).padding(horizontal = 48.dp))
        }
        guides.label?.let {
            Text(
                it, style = Bm.type.numeric, color = colors.paper,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 6.dp).clip(RoundedCornerShape(50)).background(colors.ink.copy(alpha = .86f)).padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
    }
}

private val ROTATE_OFFSET = 30.dp
private val ROTATE_KNOB = 11.dp

private fun haptic(view: android.view.View) {
    val type = if (Build.VERSION.SDK_INT >= 34) HapticFeedbackConstants.SEGMENT_TICK else HapticFeedbackConstants.CLOCK_TICK
    view.performHapticFeedback(type)
}

/* ------------------------------------------------------------------ geometry helpers */

private fun itemFor(scene: Scene, sel: EditorSelection): SceneItem? = when (sel) {
    is EditorSelection.Layer -> scene.layers.getOrNull(sel.index)
    is EditorSelection.CityItem -> scene.cities.firstOrNull { it.index == sel.index }
    is EditorSelection.TextItem -> scene.texts.firstOrNull { it.index == sel.index }
}

/** Every selectable element, front-most first, matching the renderer's drawing order. */
private fun candidates(scene: Scene, config: WidgetConfig): List<HitCandidate> =
    scene.texts.reversed().map { HitCandidate(EditorSelection.TextItem(it.index), it.frame, config.texts.getOrNull(it.index)?.locked == true) } +
        scene.cities.reversed().map { HitCandidate(EditorSelection.CityItem(it.index), it.frame, config.cities.getOrNull(it.index)?.locked == true) } +
        scene.layers.reversed().map { HitCandidate(EditorSelection.Layer(it.index), it.frame, config.layers.getOrNull(it.index)?.locked == true) }

private fun angleOf(center: Offset, p: Offset) = Math.toDegrees(atan2((p.y - center.y).toDouble(), (p.x - center.x).toDouble())).toFloat()

private fun normalizeAngle(a: Float): Float { var v = a % 360f; if (v > 180f) v -= 360f; if (v < -180f) v += 360f; return v }

private fun rotate(v: Offset, degrees: Float): Offset {
    val r = Math.toRadians(degrees.toDouble()); val c = cos(r).toFloat(); val s = sin(r).toFloat()
    return Offset(v.x * c - v.y * s, v.x * s + v.y * c)
}

private fun Frame.px(fit: BoardFit, lx: Float, ly: Float): Offset { val (x, y) = toBoard(lx, ly); return Offset(fit.x(x), fit.y(y)) }

/**
 * The rotate knob sits above the element; when that is off the canvas (a shape filling a
 * board that fills the canvas) it moves below, and failing that just inside the top edge,
 * so it is always reachable.
 */
private fun rotateKnob(f: Frame, fit: BoardFit, bounds: Size, offsetPx: Float, knobPx: Float): Pair<Offset, Offset> {
    val off = offsetPx / fit.scale
    val options = listOf(
        f.px(fit, 0f, -f.h / 2f - off) to f.px(fit, 0f, -f.h / 2f),
        f.px(fit, 0f, f.h / 2f + off) to f.px(fit, 0f, f.h / 2f),
        f.px(fit, 0f, -f.h / 2f + off * .9f) to f.px(fit, 0f, -f.h / 2f),
    )
    val m = knobPx + 2f
    return options.firstOrNull { (p, _) -> p.x in m..(bounds.width - m) && p.y in m..(bounds.height - m) } ?: options.last()
}

private fun handlePositions(item: SceneItem, fit: BoardFit, bounds: Size, rotateOffsetPx: Float, knobPx: Float): List<Pair<Handle, Offset>> {
    val f = item.frame
    val list = mutableListOf(
        Handle.Corner to f.px(fit, f.w / 2f, f.h / 2f),
        Handle.Rotate to rotateKnob(f, fit, bounds, rotateOffsetPx, knobPx).first,
    )
    if (item is LayerItem) {
        list += Handle.Right to f.px(fit, f.w / 2f, 0f)
        list += Handle.Bottom to f.px(fit, 0f, f.h / 2f)
        list += Handle.Left to f.px(fit, -f.w / 2f, 0f)
        list += Handle.Top to f.px(fit, 0f, -f.h / 2f)
    }
    return list
}

/** Half extents of a rotated frame's axis-aligned bounding box. */
private fun aabbHalf(f: Frame): Pair<Float, Float> {
    val r = Math.toRadians(f.rotation.toDouble()); val c = abs(cos(r)).toFloat(); val s = abs(sin(r)).toFloat()
    return (f.w / 2f * c + f.h / 2f * s) to (f.w / 2f * s + f.h / 2f * c)
}

private fun others(scene: Scene, exclude: SceneItem): List<SceneItem> =
    (scene.layers + scene.cities + scene.texts).filter { it !== exclude }

/** Board magnets plus the edges and centres of the other elements, on one axis. */
private fun snapLines(scene: Scene, exclude: SceneItem, horizontal: Boolean): List<SnapLine> {
    val board = SnapMath.boardLines(if (horizontal) scene.boardW else scene.boardH)
    val peers = others(scene, exclude).flatMap {
        val (hw, hh) = aabbHalf(it.frame)
        val c = if (horizontal) it.frame.cx else it.frame.cy
        val h = if (horizontal) hw else hh
        listOf(SnapLine(c - h, centre = false, rank = 2), SnapLine(c + h, centre = false, rank = 2), SnapLine(c, edges = false, rank = 2))
    }
    return board + peers
}

/** Elements whose AABB edge or centre lies on a guide, for highlighting. */
private fun peersOn(scene: Scene, exclude: SceneItem, xs: List<Float>, ys: List<Float>): List<Frame> =
    others(scene, exclude).filter { o ->
        val (hw, hh) = aabbHalf(o.frame)
        xs.any { g -> listOf(o.frame.cx - hw, o.frame.cx, o.frame.cx + hw).any { abs(it - g) < .05f } } ||
            ys.any { g -> listOf(o.frame.cy - hh, o.frame.cy, o.frame.cy + hh).any { abs(it - g) < .05f } }
    }.map { it.frame }

private fun applyMove(
    cfg: WidgetConfig, scene: Scene, sel: EditorSelection, item: SceneItem, f: Frame, deltaBoard: Offset,
    snap: Boolean, threshold: Float, held: Held,
): Pair<WidgetConfig, Guides> {
    var cx = f.cx + deltaBoard.x
    var cy = f.cy + deltaBoard.y
    val (hw, hh) = aabbHalf(f)
    var xs = emptyList<Float>(); var ys = emptyList<Float>()
    if (snap) {
        val sx = SnapMath.snapLines(cx - hw, cx, cx + hw, snapLines(scene, item, true), threshold, held.x)
        val sy = SnapMath.snapLines(cy - hh, cy, cy + hh, snapLines(scene, item, false), threshold, held.y)
        cx += sx.value; cy += sy.value
        held.x = sx.guide; held.y = sy.guide
        xs = listOfNotNull(sx.guide); ys = listOfNotNull(sy.guide)
    }
    val bw = scene.boardW; val bh = scene.boardH
    val next = when (sel) {
        is EditorSelection.Layer -> {
            val l = cfg.layers[sel.index]
            cfg.copy(layers = cfg.layers.mapIndexed { i, x -> if (i == sel.index) SceneGeometry.layerFromFrame(l, f.copy(cx = cx, cy = cy), bw, bh) else x })
        }
        is EditorSelection.CityItem -> {
            val ccx = cx.coerceIn(minOf(f.w, bw) / 2f, bw - minOf(f.w, bw) / 2f); val ccy = cy.coerceIn(minOf(f.h, bh) / 2f, bh - minOf(f.h, bh) / 2f)
            cfg.copy(cities = cfg.cities.mapIndexed { i, c -> if (i == sel.index) c.copy(x = SceneGeometry.normX(ccx, bw), y = SceneGeometry.normY(ccy, bh)) else c })
        }
        is EditorSelection.TextItem -> {
            val ccx = cx.coerceIn(minOf(f.w, bw) / 2f, bw - minOf(f.w, bw) / 2f); val ccy = cy.coerceIn(minOf(f.h, bh) / 2f, bh - minOf(f.h, bh) / 2f)
            cfg.copy(texts = cfg.texts.mapIndexed { i, t -> if (i == sel.index) t.copy(x = SceneGeometry.normX(ccx, bw), y = SceneGeometry.normY(ccy, bh)) else t })
        }
    }
    return next to Guides(xs, ys, peers = peersOn(scene, item, xs, ys))
}

private fun applyResize(
    cfg: WidgetConfig, scene: Scene, sel: EditorSelection, item: SceneItem, f: Frame, handle: Handle,
    downPx: Offset, nowPx: Offset, fit: BoardFit, snap: Boolean, threshold: Float, held: Held, words: SnapWords,
): Pair<WidgetConfig, Guides> {
    val bw = scene.boardW; val bh = scene.boardH
    when (sel) {
        is EditorSelection.Layer -> {
            val result = LayerResize.resize(
                frame = f, sx = handle.sx, sy = handle.sy,
                localDelta = rotate((nowPx - downPx) / fit.scale, -f.rotation).let { it.x to it.y },
                boardW = bw, boardH = bh,
                snap = if (!snap) null else LayerResize.Magnets(
                    xLines = snapLines(scene, item, true), yLines = snapLines(scene, item, false),
                    widths = SnapMath.fractionMagnets(bw, "↔ ") + others(scene, item).filterIsInstance<LayerItem>().map { SizeMagnet(it.frame.w, "↔ = ${it.frame.w.roundToInt()}") },
                    heights = SnapMath.fractionMagnets(bh, "↕ ") + others(scene, item).filterIsInstance<LayerItem>().map { SizeMagnet(it.frame.h, "↕ = ${it.frame.h.roundToInt()}") },
                    aspects = buildList {
                        add(AspectMagnet(1f, "1:1"))
                        add(AspectMagnet(bw / bh, words.ratioWidget))
                        add(AspectMagnet(cfg.layers[sel.index].shape.designAspect, words.ratioShape))
                        add(AspectMagnet(f.w / f.h, words.ratioKept))
                    }.distinctBy { (it.ratio * 50).roundToInt() },
                    threshold = threshold,
                ),
                held = LayerResize.Held(held.x, held.y, held.w, held.h, held.aspect),
            )
            held.x = result.held.x; held.y = result.held.y; held.w = result.held.w; held.h = result.held.h; held.aspect = result.held.aspect
            val l = cfg.layers[sel.index]
            val next = cfg.copy(layers = cfg.layers.mapIndexed { i, x -> if (i == sel.index) SceneGeometry.layerFromFrame(l, result.frame, bw, bh) else x })
            val size = "${result.frame.w.roundToInt()} × ${result.frame.h.roundToInt()}"
            val label = listOfNotNull(size, result.label).joinToString("  ·  ")
            return next to Guides(result.xs, result.ys, label = label, peers = peersOn(scene, item, result.xs, result.ys), magnet = result.label)
        }
        is EditorSelection.CityItem -> {
            val c = Offset(fit.x(f.cx), fit.y(f.cy))
            val factor = ((nowPx - c).getDistance() / (downPx - c).getDistance().coerceAtLeast(1f)).coerceIn(.1f, 10f)
            val city0 = cfg.cities[sel.index]
            var scale = (city0.scale * factor).coerceIn(.35f, 3f)
            var label = "${(scale * 100).roundToInt()}%"
            if (snap) {
                val magnets = cfg.cities.filterIndexed { i, _ -> i != sel.index }.map { SizeMagnet(it.scale, "= ${(it.scale * 100).roundToInt()}%") } +
                    SizeMagnet(1f, "· 100%")
                val s = SnapMath.snapSize(scale, magnets, scale * .045f, held.scale)
                held.scale = s.label
                if (s.label != null) { scale = s.value; label = s.label.removePrefix("· ") }
            }
            return cfg.copy(cities = cfg.cities.mapIndexed { i, x -> if (i == sel.index) x.copy(scale = scale) else x }) to Guides(label = label, magnet = held.scale)
        }
        is EditorSelection.TextItem -> {
            val c = Offset(fit.x(f.cx), fit.y(f.cy))
            val factor = ((nowPx - c).getDistance() / (downPx - c).getDistance().coerceAtLeast(1f)).coerceIn(.1f, 10f)
            val t0 = cfg.texts[sel.index]
            var size = (t0.size * factor).coerceIn(6f, 120f)
            var label = "${size.roundToInt()} pt"
            if (snap) {
                val magnets = cfg.texts.filterIndexed { i, t -> i != sel.index && t.content.isNotBlank() }.map { SizeMagnet(it.size, "= ${it.size.roundToInt()} pt") }
                val s = SnapMath.snapSize(size, magnets, size * .045f, held.scale)
                held.scale = s.label
                if (s.label != null) { size = s.value; label = s.label }
            }
            return cfg.copy(texts = cfg.texts.mapIndexed { i, x -> if (i == sel.index) x.copy(size = size) else x }) to Guides(label = label, magnet = held.scale)
        }
    }
}

private fun rotateItem(cfg: WidgetConfig, sel: EditorSelection, degrees: Float): WidgetConfig = when (sel) {
    is EditorSelection.Layer -> cfg.copy(layers = cfg.layers.mapIndexed { i, x -> if (i == sel.index) x.copy(rotation = degrees) else x })
    is EditorSelection.CityItem -> cfg.copy(cities = cfg.cities.mapIndexed { i, x -> if (i == sel.index) x.copy(rotation = degrees) else x })
    is EditorSelection.TextItem -> cfg.copy(texts = cfg.texts.mapIndexed { i, x -> if (i == sel.index) x.copy(rotation = degrees) else x })
}

private fun applyPinch(cfg: WidgetConfig, scene: Scene, sel: EditorSelection, item: SceneItem, f: Frame, zoom: Float, rotation: Float, pan: Offset): WidgetConfig {
    val bw = scene.boardW; val bh = scene.boardH
    val moved = applyMove(cfg, scene, sel, item, f, pan, false, 0f, Held()).first
    return when (sel) {
        is EditorSelection.Layer -> {
            val l = moved.layers[sel.index]
            val nf = SceneGeometry.layerFrame(l, bw, bh).copy(w = (f.w * zoom).coerceIn(bw * SceneGeometry.MIN_LAYER_FRACTION, bw), h = (f.h * zoom).coerceIn(bh * SceneGeometry.MIN_LAYER_FRACTION, bh), rotation = rotation)
            moved.copy(layers = moved.layers.mapIndexed { i, x -> if (i == sel.index) SceneGeometry.layerFromFrame(l, nf, bw, bh) else x })
        }
        is EditorSelection.CityItem -> moved.copy(cities = moved.cities.mapIndexed { i, x -> if (i == sel.index) x.copy(scale = (cfg.cities[i].scale * zoom).coerceIn(.35f, 3f), rotation = rotation) else x })
        is EditorSelection.TextItem -> moved.copy(texts = moved.texts.mapIndexed { i, x -> if (i == sel.index) x.copy(size = (cfg.texts[i].size * zoom).coerceIn(6f, 120f), rotation = rotation) else x })
    }
}

/* ------------------------------------------------------------------ drawing */

private fun DrawScope.drawDesk(dot: Color) {
    val step = 18.dp.toPx()
    val r = 1.1.dp.toPx()
    var y = step / 2f
    while (y < size.height) {
        var x = step / 2f
        while (x < size.width) { drawCircle(dot, r, Offset(x, y)); x += step }
        y += step
    }
}

private fun DrawScope.drawGuide(color: Color, a: Offset, b: Offset) {
    drawLine(Color.Black.copy(alpha = .22f), a, b, 3.dp.toPx())
    drawLine(color, a, b, 1.5.dp.toPx())
}

private fun DrawScope.drawFrameOutline(f: Frame, fit: BoardFit, color: Color, dashed: Boolean) {
    val corners = listOf(f.px(fit, -f.w / 2f, -f.h / 2f), f.px(fit, f.w / 2f, -f.h / 2f), f.px(fit, f.w / 2f, f.h / 2f), f.px(fit, -f.w / 2f, f.h / 2f))
    val outline = Path().apply { moveTo(corners[0].x, corners[0].y); corners.drop(1).forEach { lineTo(it.x, it.y) }; close() }
    drawPath(outline, color, style = Stroke(1.2.dp.toPx(), pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 3.dp.toPx())) else null))
}

private fun DrawScope.drawSelection(item: SceneItem, fit: BoardFit, bounds: Size, accent: Color, locked: Boolean, angleSnapped: Boolean) {
    val f = item.frame
    val corners = listOf(f.px(fit, -f.w / 2f, -f.h / 2f), f.px(fit, f.w / 2f, -f.h / 2f), f.px(fit, f.w / 2f, f.h / 2f), f.px(fit, -f.w / 2f, f.h / 2f))
    val outline = Path().apply { moveTo(corners[0].x, corners[0].y); corners.drop(1).forEach { lineTo(it.x, it.y) }; close() }
    val stroke = 1.5.dp.toPx()
    drawPath(outline, Color.Black.copy(alpha = .18f), style = Stroke(stroke + 2.dp.toPx()))
    drawPath(outline, accent, style = Stroke(stroke, pathEffect = if (locked) PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx())) else null))
    if (locked) {
        val p = corners[1]
        drawCircle(Color.White, 9.dp.toPx(), p)
        drawCircle(accent, 9.dp.toPx(), p, style = Stroke(1.5.dp.toPx()))
        // Tiny padlock glyph.
        drawRoundRect(accent, Offset(p.x - 3.5.dp.toPx(), p.y - .5.dp.toPx()), Size(7.dp.toPx(), 5.dp.toPx()), CornerRadius(1.dp.toPx()))
        drawArc(accent, 180f, 180f, false, Offset(p.x - 2.5.dp.toPx(), p.y - 4.dp.toPx()), Size(5.dp.toPx(), 6.dp.toPx()), style = Stroke(1.2.dp.toPx()))
        return
    }
    val (rotatePos, anchor) = rotateKnob(f, fit, bounds, ROTATE_OFFSET.toPx(), ROTATE_KNOB.toPx())
    drawLine(accent, anchor, rotatePos, 1.2.dp.toPx())
    fun knob(p: Offset, r: Float) {
        drawCircle(Color.Black.copy(alpha = .16f), r + 1.5.dp.toPx(), p + Offset(0f, 1.dp.toPx()))
        drawCircle(Color.White, r, p)
        drawCircle(accent, r, p, style = Stroke(2.dp.toPx()))
    }
    knob(rotatePos, ROTATE_KNOB.toPx() - 1.dp.toPx())
    // Rotation glyph inside the knob.
    val arcR = 4.5.dp.toPx()
    drawArc(if (angleSnapped) accent else Color(0xFF3A3640), 200f, 270f, false, rotatePos - Offset(arcR, arcR), Size(arcR * 2, arcR * 2), style = Stroke(1.4.dp.toPx(), cap = StrokeCap.Round))
    knob(f.px(fit, f.w / 2f, f.h / 2f), 7.dp.toPx())
    if (item is LayerItem) {
        // Edge handles as short pills along each edge.
        listOf(
            f.px(fit, f.w / 2f, 0f) to f.rotation + 90f, f.px(fit, 0f, f.h / 2f) to f.rotation,
            f.px(fit, -f.w / 2f, 0f) to f.rotation + 90f, f.px(fit, 0f, -f.h / 2f) to f.rotation,
        ).forEach { (p, angle) ->
            val half = rotate(Offset(7.dp.toPx(), 0f), angle)
            drawLine(Color.Black.copy(alpha = .16f), p - half + Offset(0f, 1.dp.toPx()), p + half + Offset(0f, 1.dp.toPx()), 7.dp.toPx(), StrokeCap.Round)
            drawLine(accent, p - half, p + half, 6.dp.toPx(), StrokeCap.Round)
            drawLine(Color.White, p - half * .6f, p + half * .6f, 2.dp.toPx(), StrokeCap.Round)
        }
    }
}
