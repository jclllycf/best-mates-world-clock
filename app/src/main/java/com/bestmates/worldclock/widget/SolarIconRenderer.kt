package com.bestmates.worldclock.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import com.bestmates.worldclock.solar.SolarCatalog
import com.caverock.androidsvg.SVG

/** Renders the actual SVG artwork bundled in assets/solar_icons. */
object SolarIconRenderer {
    fun render(
        context: Context,
        sizePx: Int,
        isDay: Boolean,
        sun: SunIconStyle,
        moon: MoonIconStyle,
        color: Int,
        raw: Boolean = false,
    ): Bitmap {
        // `sizePx` is the final, physical target size. Keeping the returned
        // bitmap at exactly that size avoids a second density-dependent scale
        // in Compose/RemoteViews (the source of the former mosaic effect).
        val size = sizePx.coerceAtLeast(20)
        // Older Best Mates styles with a Daybook redraw are drawn with the redraw (several
        // had malformed paths, BM-REAL-01). The saved identifier is untouched, so this is a
        // rendering choice only; [raw] shows the original artwork for the audit sheet.
        val shownSun = if (raw) sun else SolarCatalog.daybookSun(sun) ?: sun
        val shownMoon = if (raw) moon else SolarCatalog.daybookMoon(moon) ?: moon
        val asset = if (isDay) shownSun.asset else shownMoon.asset
        return runCatching { renderSvg(context, asset, size, color) }
            .getOrElse { fallback(size, isDay, color) }
    }

    private fun renderSvg(context: Context, asset: String, size: Int, color: Int): Bitmap {
        // Icon packs use different view-box padding. Render large first, crop to the
        // pixels that are actually visible, then fit that visual shape into one
        // shared optical box. This keeps every picker tile and widget icon centred
        // and perceptually the same size.
        // Render SVG paths at 4x and perform one high-quality downsample into
        // the exact final box. SVGs therefore remain crisp at every density.
        val sourceSize = (size * 4).coerceAtLeast(160)
        val rawMask = Bitmap.createBitmap(sourceSize, sourceSize, Bitmap.Config.ARGB_8888)
        val svg = SVG.getFromAsset(context.assets, "solar_icons/$asset").apply {
            // Most web icon sets declare width/height as `1em`. AndroidSVG
            // otherwise resolves that unit to a tiny default font size and the
            // result is enlarged afterwards, which is exactly what caused the
            // visible blocky/pixelated edges. Give the document an explicit
            // raster target before drawing so every path is rendered natively
            // at the supersampled pixel resolution.
            setDocumentWidth(sourceSize.toFloat())
            setDocumentHeight(sourceSize.toFloat())
        }
        svg.renderToCanvas(
            Canvas(rawMask),
            RectF(0f, 0f, sourceSize.toFloat(), sourceSize.toFloat())
        )

        // The Daybook set is designed on one grid with its padding built in, so its whole view
        // box is shown: every glyph keeps the optical size it was drawn at. Legacy packs vary
        // in padding and are still cropped to their visible pixels and fitted.
        val designed = asset.startsWith("db_")
        val visible = if (designed) Rect(0, 0, sourceSize, sourceSize) else alphaBounds(rawMask)
        val mask = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val occupancy = if (designed) 1f else .80f
        val maxSide = size * occupancy
        val scale = minOf(maxSide / visible.width(), maxSide / visible.height())
        val drawWidth = visible.width() * scale
        val drawHeight = visible.height() * scale
        val left = (size - drawWidth) / 2f
        val top = (size - drawHeight) / 2f
        Canvas(mask).drawBitmap(
            rawMask,
            visible,
            RectF(left, top, left + drawWidth, top + drawHeight),
            Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        )
        rawMask.recycle()

        // All packs remain recolourable, regardless of whether their SVG uses
        // currentColor, black, or white internally.
        val result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
        Canvas(result).drawBitmap(mask, 0f, 0f, paint)
        mask.recycle()
        return result
    }

    private fun alphaBounds(bitmap: Bitmap): Rect {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var left = width
        var top = height
        var right = -1
        var bottom = -1
        for (y in 0 until height) {
            val row = y * width
            for (x in 0 until width) {
                if ((pixels[row + x] ushr 24) > 8) {
                    if (x < left) left = x
                    if (x > right) right = x
                    if (y < top) top = y
                    if (y > bottom) bottom = y
                }
            }
        }
        return if (right >= left && bottom >= top) {
            Rect(left, top, right + 1, bottom + 1)
        } else {
            Rect(0, 0, width, height)
        }
    }

    private fun fallback(size: Int, isDay: Boolean, color: Int): Bitmap =
        Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).also { bitmap ->
            val canvas = Canvas(bitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = color
                style = Paint.Style.STROKE
                strokeWidth = size * .07f
                strokeCap = Paint.Cap.ROUND
            }
            val center = size / 2f
            if (isDay) {
                canvas.drawCircle(center, center, size * .18f, paint)
                repeat(8) { index ->
                    val angle = Math.toRadians(index * 45.0)
                    val x1 = center + kotlin.math.cos(angle).toFloat() * size * .30f
                    val y1 = center + kotlin.math.sin(angle).toFloat() * size * .30f
                    val x2 = center + kotlin.math.cos(angle).toFloat() * size * .43f
                    val y2 = center + kotlin.math.sin(angle).toFloat() * size * .43f
                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
            } else {
                paint.style = Paint.Style.FILL
                canvas.drawCircle(center * .90f, center, size * .31f, paint)
                paint.color = Color.TRANSPARENT
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                canvas.drawCircle(center * 1.16f, center * .86f, size * .29f, paint)
            }
        }
}
