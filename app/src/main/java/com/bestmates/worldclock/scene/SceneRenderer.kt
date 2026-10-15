package com.bestmates.worldclock.scene

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.text.TextPaint
import android.util.LruCache
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.model.MATERIAL_MIST
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.widget.SolarIconRenderer
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/** What to leave out while drawing; the editor hides an element it is dragging live. */
data class DrawOptions(
    val night: Boolean,
    /** False on the launcher widget: TextClocks draw time and date there. */
    val liveText: Boolean = true,
    val hiddenLayers: Set<Int> = emptySet(),
    /**
     * Nothing outside the board is part of the widget: a launcher shows exactly the board,
     * so the editor and previews clip to it too (shadows of full-bleed shapes included).
     */
    val clipToBoard: Boolean = true,
)

/**
 * Draws a [Scene] onto any Android canvas through a [BoardFit] (board dp → canvas px).
 * The launcher widget's bitmap, the Workspace preview and the editor canvas all call this,
 * so they cannot drift apart.
 */
class SceneRenderer(context: Context) {
    private val app = context.applicationContext
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG or Paint.LINEAR_TEXT_FLAG)
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    private val layerCache = object : LruCache<String, Bitmap>(24 * 1024 * 1024) {
        override fun sizeOf(key: String, value: Bitmap) = value.byteCount
    }
    private val iconCache = object : LruCache<String, Bitmap>(6 * 1024 * 1024) {
        override fun sizeOf(key: String, value: Bitmap) = value.byteCount
    }

    /** One renderer per thread of use: the paints are reused between calls. */
    @Synchronized
    fun draw(canvas: Canvas, scene: Scene, fit: BoardFit, options: DrawOptions) {
        val saved = canvas.save()
        if (options.clipToBoard) canvas.clipRect(fit.x(0f), fit.y(0f), fit.x(scene.boardW), fit.y(scene.boardH))
        scene.layers.forEach { if (it.index !in options.hiddenLayers) drawLayer(canvas, it, fit, options.night) }
        scene.cities.forEach { drawCity(canvas, it, fit, options.liveText) }
        scene.texts.forEach { drawText(canvas, it, fit) }
        canvas.restoreToCount(saved)
    }

    /* ------------------------------------------------------------ layers */

    private fun bleed(layer: WidgetLayer): Float =
        max(if (layer.shadowOpacity > 0f) layer.shadow + abs(layer.shadowYOffset) else 0f, if (layer.glowOpacity > 0f) layer.softness else 0f) + layer.borderWidth + 1f

    fun drawLayer(canvas: Canvas, item: LayerItem, fit: BoardFit, night: Boolean) {
        val f = item.frame
        val k = fit.scale
        val wPx = (f.w * k).roundToInt().coerceAtLeast(1)
        val hPx = (f.h * k).roundToInt().coerceAtLeast(1)
        val bleedPx = ceil(bleed(item.layer) * k).toInt()
        val l = item.layer
        val key = "${l.shape}|$wPx|$hPx|$bleedPx|${l.dayColor}|${l.nightColor}|${l.textColor}|${l.borderWidth}|${l.borderOpacity}|" +
            "${l.softness}|${l.glowOpacity}|${l.shadow}|${l.shadowOpacity}|${l.shadowYOffset}|${l.material}|$night|$k"
        val bitmap = layerCache.get(key) ?: renderLayerBitmap(l, wPx, hPx, bleedPx, k, night).also { layerCache.put(key, it) }
        canvas.save()
        canvas.rotate(f.rotation, fit.x(f.cx), fit.y(f.cy))
        canvas.drawBitmap(bitmap, fit.x(f.left) - bleedPx, fit.y(f.top) - bleedPx, bitmapPaint)
        canvas.restore()
    }

    /** Software-rendered so blur, glow and shadow look identical on every canvas type. */
    fun renderLayerBitmap(layer: WidgetLayer, wPx: Int, hPx: Int, bleedPx: Int, k: Float, night: Boolean): Bitmap {
        val bitmap = Bitmap.createBitmap(wPx + bleedPx * 2, hPx + bleedPx * 2, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val stroke = layer.borderWidth * k
        val inset = if (layer.borderWidth > 0f && layer.borderOpacity > 0f) stroke / 2f else 0f
        val rect = RectF(bleedPx + inset, bleedPx + inset, bleedPx + wPx - inset, bleedPx + hPx - inset)
        val path = ShapePaths.path(layer.shape, rect)
        if (layer.shadow > 0f && layer.shadowOpacity > 0f) {
            canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0x01000000
                setShadowLayer(layer.shadow * k, 0f, layer.shadowYOffset * k, withAlpha(0xFF16131C.toInt(), (layer.shadowOpacity * 255).toInt()))
            })
            // The shadow belongs outside the shape. Under a translucent fill (Mist, glass) it
            // would otherwise show through as a grey smudge, so the shape's own area is cleared.
            canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) })
        }
        if (layer.softness > 0f && layer.glowOpacity > 0f) {
            canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = withAlpha(layer.textColor.toInt(), (layer.glowOpacity * 255).toInt())
                maskFilter = BlurMaskFilter((layer.softness * k).coerceAtLeast(.5f), BlurMaskFilter.Blur.OUTER)
            })
        }
        canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = (if (night) layer.nightColor else layer.dayColor).toInt() })
        if (layer.material == MATERIAL_MIST) drawMist(canvas, layer, rect, k, night)
        if (layer.borderWidth > 0f && layer.borderOpacity > 0f) canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            color = withAlpha(layer.textColor.toInt(), (layer.borderOpacity * 255).toInt())
        })
        return bitmap
    }

    /**
     * Mist: light falls on a frosted veil from above. A soft sheen over the top half and a
     * hairline edge, brighter at the top, give the fill depth and a readable outline on any
     * wallpaper without needing a blur the launcher cannot provide.
     */
    private fun drawMist(canvas: Canvas, layer: WidgetLayer, rect: RectF, k: Float, night: Boolean) {
        val path = ShapePaths.path(layer.shape, rect)
        val sheenTop = if (night) 0x26FFFFFF else 0x38FFFFFF
        canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(0f, rect.top, 0f, rect.bottom, intArrayOf(sheenTop, 0x0AFFFFFF, 0x00FFFFFF), floatArrayOf(0f, .5f, 1f), Shader.TileMode.CLAMP)
        })
        val stroke = (k * .9f).coerceAtLeast(1f)
        val inner = RectF(rect).apply { inset(stroke / 2f, stroke / 2f) }
        canvas.drawPath(ShapePaths.path(layer.shape, inner), Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = stroke
            shader = LinearGradient(0f, rect.top, 0f, rect.bottom, intArrayOf(if (night) 0x52FFFFFF else 0x73FFFFFF, 0x1AFFFFFF), null, Shader.TileMode.CLAMP)
        })
    }

    /* ------------------------------------------------------------ cities and text */

    private fun drawCity(canvas: Canvas, item: CityItem, fit: BoardFit, liveText: Boolean) {
        val f = item.frame
        val k = fit.scale
        canvas.save()
        canvas.rotate(f.rotation, fit.x(f.cx), fit.y(f.cy))
        item.icon?.let { icon ->
            val sizePx = (icon.size * k).roundToInt().coerceAtLeast(8)
            val city = icon.city
            val key = "${city.sunStyle}|${city.moonStyle}|${icon.day}|${icon.color}|$sizePx"
            val bitmap = iconCache.get(key)
                ?: SolarIconRenderer.render(app, sizePx, icon.day, city.sunStyle, city.moonStyle, icon.color).also { iconCache.put(key, it) }
            canvas.drawBitmap(bitmap, fit.x(f.cx) - sizePx / 2f, fit.y(f.top + icon.cy) - sizePx / 2f, bitmapPaint)
        }
        item.lines.forEach { line ->
            if (!liveText && (line.kind == LineKind.TIME || line.kind == LineKind.DATE)) return@forEach
            drawLine(canvas, line, fit.x(f.cx), fit.y(f.top), k)
        }
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, item: TextItem, fit: BoardFit) {
        val f = item.frame
        canvas.save()
        canvas.rotate(f.rotation, fit.x(f.cx), fit.y(f.cy))
        item.lines.forEach { drawLine(canvas, it, fit.x(f.cx), fit.y(f.top), fit.scale) }
        canvas.restore()
    }

    /** Baseline = box top − ascent, with the box centred on the line's centre. */
    private fun drawLine(canvas: Canvas, line: SceneLine, centerXPx: Float, topPx: Float, k: Float) {
        if (line.text.isEmpty()) return
        textPaint.typeface = WidgetFonts.typeface(app, line.fontId)
        textPaint.textSize = line.size * k
        textPaint.color = line.color
        textPaint.textAlign = Paint.Align.CENTER
        val baseline = topPx + (line.cy - line.box / 2f - line.ascent) * k
        canvas.drawText(line.text, centerXPx, baseline, textPaint)
    }

    private fun withAlpha(color: Int, alpha: Int) = (color and 0x00FFFFFF) or (alpha.coerceIn(0, 255) shl 24)
}
