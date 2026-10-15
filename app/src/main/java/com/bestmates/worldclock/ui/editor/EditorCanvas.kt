package com.bestmates.worldclock.ui.editor

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.scene.BoardFit
import com.bestmates.worldclock.scene.CityItem
import com.bestmates.worldclock.scene.DrawOptions
import com.bestmates.worldclock.scene.Frame
import com.bestmates.worldclock.scene.LayerItem
import com.bestmates.worldclock.scene.Scene
import com.bestmates.worldclock.scene.SceneGeometry
import com.bestmates.worldclock.scene.SceneItem
import com.bestmates.worldclock.scene.SnapMath
import com.bestmates.worldclock.scene.TextItem
import com.bestmates.worldclock.ui.common.Backdrop
import com.bestmates.worldclock.ui.common.SceneServices
import com.bestmates.worldclock.ui.common.WallpaperTones
import com.bestmates.worldclock.ui.common.backdropBrush
import com.bestmates.worldclock.ui.common.rememberMinuteTick
import com.bestmates.worldclock.ui.design.Bm
import java.time.Instant
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.roundToInt
import kotlin.math.sin

private enum class Handle { Move, Corner, Right, Bottom, Rotate }

private data class Guides(val x: Float? = null, val y: Float? = null, val angle: Float? = null, val label: String? = null)

/**
 * The interactive design surface. Draws the same [Scene] the launcher bitmap is drawn
 * from, then overlays selection chrome. One gesture detector hit-tests rotated frames in
 * board coordinates, so drags never need per-element rotated-pointer correction.
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
    bottomInset: androidx.compose.ui.unit.Dp = 0.dp,
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

    BoxWithConstraints(modifier.background(colors.desk)) {
        val margin = with(density) { 34.dp.toPx() }
        val bottom = with(density) { bottomInset.toPx() }
        val wPx = with(density) { maxWidth.toPx() }
        val hPx = with(density) { maxHeight.toPx() }
        val fit = SceneGeometry.fit(scene.boardW, scene.boardH, margin, margin, (wPx - margin * 2).coerceAtLeast(10f), (hPx - margin * 2 - bottom).coerceAtLeast(10f))
        val latestScene by rememberUpdatedState(scene)
        val latestFit by rememberUpdatedState(fit)
        val latestSnap by rememberUpdatedState(snap)

        Canvas(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    val handleRadius = 24.dp.toPx()
                    val rotateOffset = 30.dp.toPx()
                    val snapPx = 7.dp.toPx()
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val sc = latestScene
                        val ft = latestFit
                        val cfg0 = state.config
                        val selected = state.selection
                        val locked = state.isLocked(selected)

                        // 1. Handles of the current selection win over everything else.
                        var target: Pair<EditorSelection, Handle>? = null
                        val selItem = selected?.let { itemFor(sc, it) }
                        if (selItem != null && !locked) {
                            handlePositions(selItem, ft, rotateOffset).forEach { (handle, pos) ->
                                if (target == null && (pos - down.position).getDistance() <= handleRadius) target = selected to handle
                            }
                        }
                        // 2. Topmost element under the finger.
                        if (target == null) {
                            val bx = ft.boardX(down.position.x); val by = ft.boardY(down.position.y)
                            val slop = 6.dp.toPx() / ft.scale
                            val hit = (sc.texts.reversed().map { EditorSelection.TextItem(it.index) to it.frame } +
                                sc.cities.reversed().map { EditorSelection.CityItem(it.index) to it.frame } +
                                sc.layers.reversed().map { EditorSelection.Layer(it.index) to it.frame })
                                .firstOrNull { it.second.contains(bx, by, slop) }
                            // Keep dragging the current selection if it is under the finger.
                            val preferSelected = selItem != null && selItem.frame.contains(bx, by, slop)
                            target = if (preferSelected) selected!! to Handle.Move else hit?.let { it.first to Handle.Move }
                        }
                        target?.let { if (state.selection != it.first) state.selection = it.first }
                        val (sel, handle) = target ?: (null to Handle.Move)
                        val item0 = sel?.let { itemFor(sc, it) }
                        val frame0 = item0?.frame
                        val canEdit = sel != null && !state.isLocked(sel)

                        var dragging = false
                        var pinching = false
                        var zoomTotal = 1f; var rotTotal = 0f; var panTotal = Offset.Zero
                        var lastAngle = frame0?.let { angleOf(Offset(ft.x(it.cx), ft.y(it.cy)), down.position) } ?: 0f
                        var angleTotal = 0f
                        var lastGuideKey = ""

                        fun haptic(next: Guides) {
                            val key = "${next.x}|${next.y}|${next.angle}"
                            if ((next.x != null || next.y != null || next.angle != null) && key != lastGuideKey) {
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            }
                            lastGuideKey = key
                        }

                        while (true) {
                            val event = awaitPointerEvent()
                            val pressed = event.changes.filter { it.pressed }
                            if (pressed.isEmpty()) break
                            if (!canEdit || item0 == null || frame0 == null) {
                                event.changes.forEach { if (it.positionChanged()) it.consume() }
                                continue
                            }
                            if (pressed.size >= 2) {
                                if (!pinching) { pinching = true; if (!dragging) { dragging = true; state.startGesture(); interacting = true } }
                                zoomTotal *= event.calculateZoom()
                                rotTotal += event.calculateRotation()
                                panTotal += event.calculatePan()
                                state.live(applyPinch(cfg0, sc, sel!!, item0, frame0, zoomTotal, rotTotal, panTotal / ft.scale))
                                guides = Guides(label = "${(zoomTotal * 100).roundToInt()}%  ·  ${normalizeAngle(frame0.rotation + rotTotal).roundToInt()}°")
                                event.changes.forEach { it.consume() }
                                continue
                            }
                            if (pinching) { event.changes.forEach { it.consume() }; continue }
                            val change = pressed.first()
                            val pan = change.position - down.position
                            if (!dragging && pan.getDistance() > viewConfiguration.touchSlop) {
                                dragging = true; state.startGesture(); interacting = true
                            }
                            if (!dragging) continue
                            change.consume()
                            val next: Pair<WidgetConfig, Guides> = when (handle) {
                                Handle.Move -> applyMove(cfg0, sc, sel!!, item0, frame0, pan / ft.scale, latestSnap, snapPx / ft.scale)
                                Handle.Corner, Handle.Right, Handle.Bottom -> applyResize(cfg0, sc, sel!!, item0, frame0, handle, down.position, change.position, ft, latestSnap, snapPx / ft.scale)
                                Handle.Rotate -> {
                                    val c = Offset(ft.x(frame0.cx), ft.y(frame0.cy))
                                    val a = angleOf(c, change.position)
                                    var d = a - lastAngle
                                    while (d > 180f) d -= 360f
                                    while (d < -180f) d += 360f
                                    angleTotal += d; lastAngle = a
                                    val snapped = SnapMath.rotation(frame0.rotation + angleTotal, latestSnap)
                                    rotateItem(cfg0, sel!!, snapped.value) to Guides(angle = snapped.guide, label = "${normalizeAngle(snapped.value).roundToInt()}°")
                                }
                            }
                            state.live(next.first)
                            haptic(next.second)
                            guides = next.second
                        }
                        if (dragging) state.endGesture()
                        else if (target == null) state.selection = null
                        guides = Guides()
                        interacting = false
                    }
                },
        ) {
            drawDesk(colors.deskDot)
            val tile = 14.dp.toPx()
            drawRoundRect(
                backdropBrush(backdrop, wallpaper),
                topLeft = Offset(fit.originX - tile, fit.originY - tile),
                size = Size(scene.boardW * fit.scale + tile * 2, scene.boardH * fit.scale + tile * 2),
                cornerRadius = CornerRadius(26.dp.toPx()),
            )
            val boardTL = Offset(fit.originX, fit.originY)
            val boardSize = Size(scene.boardW * fit.scale, scene.boardH * fit.scale)
            if (interacting || showGrid) {
                val grid = Color.White.copy(alpha = .16f)
                for (i in 1 until config.columns) { val x = fit.originX + boardSize.width * i / config.columns; drawLine(grid, Offset(x, fit.originY), Offset(x, fit.originY + boardSize.height), 1f) }
                for (i in 1 until config.rows) { val y = fit.originY + boardSize.height * i / config.rows; drawLine(grid, Offset(fit.originX, y), Offset(fit.originX + boardSize.width, y), 1f) }
            }
            drawRoundRect(
                Color.White.copy(alpha = .5f), topLeft = boardTL, size = boardSize, cornerRadius = CornerRadius(14.dp.toPx()),
                style = Stroke(1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(5.dp.toPx(), 4.dp.toPx()))),
            )
            drawIntoCanvas { renderer.draw(it.nativeCanvas, scene, fit, DrawOptions(night = night)) }

            // Guides
            val accent = colors.accent
            guides.x?.let { gx -> val x = fit.x(gx); drawLine(accent, Offset(x, fit.originY - tile), Offset(x, fit.originY + boardSize.height + tile), 1.5.dp.toPx()) }
            guides.y?.let { gy -> val y = fit.y(gy); drawLine(accent, Offset(fit.originX - tile, y), Offset(fit.originX + boardSize.width + tile, y), 1.5.dp.toPx()) }

            // Selection chrome
            state.selection?.let { sel -> itemFor(scene, sel) }?.let { item ->
                drawSelection(item, fit, accent, state.isLocked(state.selection), guides.angle != null)
            }
        }

        if (config.layers.isEmpty() && config.cities.isEmpty() && config.texts.none { it.content.isNotBlank() }) {
            Text(emptyHint, style = Bm.type.bodySmall, color = Color.White.copy(alpha = .85f), modifier = Modifier.align(Alignment.Center).padding(horizontal = 48.dp))
        }
        guides.label?.let {
            Text(
                it, style = Bm.type.numeric, color = colors.paper,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp).clip(RoundedCornerShape(50)).background(colors.ink.copy(alpha = .86f)).padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
    }
}

/* ------------------------------------------------------------------ geometry helpers */

private fun itemFor(scene: Scene, sel: EditorSelection): SceneItem? = when (sel) {
    is EditorSelection.Layer -> scene.layers.getOrNull(sel.index)
    is EditorSelection.CityItem -> scene.cities.firstOrNull { it.index == sel.index }
    is EditorSelection.TextItem -> scene.texts.firstOrNull { it.index == sel.index }
}

private fun angleOf(center: Offset, p: Offset) = Math.toDegrees(atan2((p.y - center.y).toDouble(), (p.x - center.x).toDouble())).toFloat()

private fun normalizeAngle(a: Float): Float { var v = a % 360f; if (v > 180f) v -= 360f; if (v < -180f) v += 360f; return v }

private fun rotate(v: Offset, degrees: Float): Offset {
    val r = Math.toRadians(degrees.toDouble()); val c = cos(r).toFloat(); val s = sin(r).toFloat()
    return Offset(v.x * c - v.y * s, v.x * s + v.y * c)
}

private fun Frame.px(fit: BoardFit, lx: Float, ly: Float): Offset { val (x, y) = toBoard(lx, ly); return Offset(fit.x(x), fit.y(y)) }

private fun handlePositions(item: SceneItem, fit: BoardFit, rotateOffsetPx: Float): List<Pair<Handle, Offset>> {
    val f = item.frame
    val list = mutableListOf(
        Handle.Rotate to f.px(fit, 0f, -f.h / 2f - rotateOffsetPx / fit.scale),
        Handle.Corner to f.px(fit, f.w / 2f, f.h / 2f),
    )
    if (item is LayerItem) {
        list += Handle.Right to f.px(fit, f.w / 2f, 0f)
        list += Handle.Bottom to f.px(fit, 0f, f.h / 2f)
    }
    return list
}

/** Half extents of a rotated frame's axis-aligned bounding box. */
private fun aabbHalf(f: Frame): Pair<Float, Float> {
    val r = Math.toRadians(f.rotation.toDouble()); val c = abs(cos(r)).toFloat(); val s = abs(sin(r)).toFloat()
    return (f.w / 2f * c + f.h / 2f * s) to (f.w / 2f * s + f.h / 2f * c)
}

private fun snapTargets(scene: Scene, exclude: SceneItem, horizontal: Boolean): List<Float> {
    val size = if (horizontal) scene.boardW else scene.boardH
    val base = listOf(0f, size / 4f, size / 2f, size * 3f / 4f, size)
    val others = (scene.layers + scene.cities + scene.texts).filter { it !== exclude }.flatMap {
        val (hw, hh) = aabbHalf(it.frame)
        if (horizontal) listOf(it.frame.cx - hw, it.frame.cx, it.frame.cx + hw) else listOf(it.frame.cy - hh, it.frame.cy, it.frame.cy + hh)
    }
    return base + others
}

private fun applyMove(cfg: WidgetConfig, scene: Scene, sel: EditorSelection, item: SceneItem, f: Frame, deltaBoard: Offset, snap: Boolean, threshold: Float): Pair<WidgetConfig, Guides> {
    var cx = f.cx + deltaBoard.x
    var cy = f.cy + deltaBoard.y
    val (hw, hh) = aabbHalf(f)
    var gx: Float? = null; var gy: Float? = null
    if (snap) {
        val sx = SnapMath.snapAxis(cx - hw, cx, cx + hw, snapTargets(scene, item, true), threshold)
        val sy = SnapMath.snapAxis(cy - hh, cy, cy + hh, snapTargets(scene, item, false), threshold)
        cx += sx.value; cy += sy.value; gx = sx.guide; gy = sy.guide
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
    return next to Guides(gx, gy)
}

private fun applyResize(
    cfg: WidgetConfig, scene: Scene, sel: EditorSelection, item: SceneItem, f: Frame, handle: Handle,
    downPx: Offset, nowPx: Offset, fit: BoardFit, snap: Boolean, threshold: Float,
): Pair<WidgetConfig, Guides> {
    val bw = scene.boardW; val bh = scene.boardH
    when (sel) {
        is EditorSelection.Layer -> {
            val local = rotate((nowPx - downPx) / fit.scale, -f.rotation)
            var w = f.w + if (handle != Handle.Bottom) local.x else 0f
            var h = f.h + if (handle != Handle.Right) local.y else 0f
            val minW = bw * SceneGeometry.MIN_LAYER_FRACTION; val minH = bh * SceneGeometry.MIN_LAYER_FRACTION
            w = w.coerceIn(minW, bw); h = h.coerceIn(minH, bh)
            var gx: Float? = null; var gy: Float? = null
            if (snap && abs(normalizeAngle(f.rotation)) < .01f) {
                if (handle != Handle.Bottom) {
                    val s = SnapMath.snapAxis(f.left + w, f.left + w, f.left + w, snapTargets(scene, item, true), threshold)
                    w = (w + s.value).coerceIn(minW, bw); gx = s.guide
                }
                if (handle != Handle.Right) {
                    val s = SnapMath.snapAxis(f.top + h, f.top + h, f.top + h, snapTargets(scene, item, false), threshold)
                    h = (h + s.value).coerceIn(minH, bh); gy = s.guide
                }
            }
            val shift = rotate(Offset((w - f.w) / 2f, (h - f.h) / 2f), f.rotation)
            val nf = Frame(f.cx + shift.x, f.cy + shift.y, w, h, f.rotation)
            val l = cfg.layers[sel.index]
            val next = cfg.copy(layers = cfg.layers.mapIndexed { i, x -> if (i == sel.index) SceneGeometry.layerFromFrame(l, nf, bw, bh) else x })
            return next to Guides(gx, gy, label = "${w.roundToInt()} × ${h.roundToInt()}")
        }
        is EditorSelection.CityItem -> {
            val c = Offset(fit.x(f.cx), fit.y(f.cy))
            val factor = ((nowPx - c).getDistance() / (downPx - c).getDistance().coerceAtLeast(1f)).coerceIn(.1f, 10f)
            val city0 = cfg.cities[sel.index]
            var scale = (city0.scale * factor).coerceIn(.35f, 3f)
            var label = "${(scale * 100).roundToInt()}%"
            if (snap) cfg.cities.filterIndexed { i, _ -> i != sel.index }.minByOrNull { abs(it.scale - scale) }?.let { other ->
                if (abs(other.scale - scale) / other.scale < .04f) { scale = other.scale; label = "= ${(scale * 100).roundToInt()}%" }
            }
            return cfg.copy(cities = cfg.cities.mapIndexed { i, x -> if (i == sel.index) x.copy(scale = scale) else x }) to Guides(label = label)
        }
        is EditorSelection.TextItem -> {
            val c = Offset(fit.x(f.cx), fit.y(f.cy))
            val factor = ((nowPx - c).getDistance() / (downPx - c).getDistance().coerceAtLeast(1f)).coerceIn(.1f, 10f)
            val t0 = cfg.texts[sel.index]
            val size = (t0.size * factor).coerceIn(6f, 120f)
            return cfg.copy(texts = cfg.texts.mapIndexed { i, x -> if (i == sel.index) x.copy(size = size) else x }) to Guides(label = "${size.roundToInt()} pt")
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
    val moved = applyMove(cfg, scene, sel, item, f, pan, false, 0f).first
    val rot = SnapMath.rotation(f.rotation + rotation, true).value
    return when (sel) {
        is EditorSelection.Layer -> {
            val l = moved.layers[sel.index]
            val nf = SceneGeometry.layerFrame(l, bw, bh).copy(w = (f.w * zoom).coerceIn(bw * SceneGeometry.MIN_LAYER_FRACTION, bw), h = (f.h * zoom).coerceIn(bh * SceneGeometry.MIN_LAYER_FRACTION, bh), rotation = rot)
            moved.copy(layers = moved.layers.mapIndexed { i, x -> if (i == sel.index) SceneGeometry.layerFromFrame(l, nf, bw, bh) else x })
        }
        is EditorSelection.CityItem -> moved.copy(cities = moved.cities.mapIndexed { i, x -> if (i == sel.index) x.copy(scale = (cfg.cities[i].scale * zoom).coerceIn(.35f, 3f), rotation = rot) else x })
        is EditorSelection.TextItem -> moved.copy(texts = moved.texts.mapIndexed { i, x -> if (i == sel.index) x.copy(size = (cfg.texts[i].size * zoom).coerceIn(6f, 120f), rotation = rot) else x })
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

private fun DrawScope.drawSelection(item: SceneItem, fit: BoardFit, accent: Color, locked: Boolean, angleSnapped: Boolean) {
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
    val rotatePos = f.px(fit, 0f, -f.h / 2f - 30.dp.toPx() / fit.scale)
    val topMid = f.px(fit, 0f, -f.h / 2f)
    drawLine(accent, topMid, rotatePos, 1.2.dp.toPx())
    fun knob(p: Offset, r: Float) {
        drawCircle(Color.Black.copy(alpha = .16f), r + 1.5.dp.toPx(), p + Offset(0f, 1.dp.toPx()))
        drawCircle(Color.White, r, p)
        drawCircle(accent, r, p, style = Stroke(2.dp.toPx()))
    }
    knob(rotatePos, 10.dp.toPx())
    // Rotation glyph inside the knob.
    val arcR = 4.5.dp.toPx()
    drawArc(if (angleSnapped) accent else Color(0xFF3A3640), 200f, 270f, false, rotatePos - Offset(arcR, arcR), Size(arcR * 2, arcR * 2), style = Stroke(1.4.dp.toPx()))
    knob(f.px(fit, f.w / 2f, f.h / 2f), 7.dp.toPx())
    if (item is LayerItem) {
        // Edge handles as short pills along the edge direction.
        listOf(f.px(fit, f.w / 2f, 0f) to f.rotation + 90f, f.px(fit, 0f, f.h / 2f) to f.rotation).forEach { (p, angle) ->
            val half = rotate(Offset(7.dp.toPx(), 0f), angle)
            drawLine(Color.Black.copy(alpha = .16f), p - half + Offset(0f, 1.dp.toPx()), p + half + Offset(0f, 1.dp.toPx()), 7.dp.toPx(), androidx.compose.ui.graphics.StrokeCap.Round)
            drawLine(accent, p - half, p + half, 6.dp.toPx(), androidx.compose.ui.graphics.StrokeCap.Round)
            drawLine(Color.White, p - half * .6f, p + half * .6f, 2.dp.toPx(), androidx.compose.ui.graphics.StrokeCap.Round)
        }
    }
}
