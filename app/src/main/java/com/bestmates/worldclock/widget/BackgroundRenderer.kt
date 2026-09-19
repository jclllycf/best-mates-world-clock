package com.bestmates.worldclock.widget

import android.graphics.*
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape

object BackgroundRenderer {
    /** The editor is always a 4×4 design board.  Keep the same centred board on a launcher. */
    fun designBounds(widthDp: Float, heightDp: Float, insetDp: Float = 0f): RectF {
        val side = (minOf(widthDp, heightDp) - insetDp * 2f).coerceAtLeast(1f)
        return RectF(
            (widthDp - side) / 2f,
            (heightDp - side) / 2f,
            (widthDp + side) / 2f,
            (heightDp + side) / 2f
        )
    }

    fun layerBounds(layer: WidgetLayer, widthDp: Float, heightDp: Float, insetDp: Float = 0f): RectF {
        val board = designBounds(widthDp, heightDp, insetDp)
        val width = board.width() * layer.width.coerceIn(.08f, 1f)
        val height = board.height() * layer.height.coerceIn(.08f, 1f)
        val wantedX = board.centerX() + layer.x * board.width() * .5f
        val wantedY = board.centerY() + layer.y * board.height() * .5f
        val cx = wantedX.coerceIn(board.left + width / 2f, board.right - width / 2f)
        val cy = wantedY.coerceIn(board.top + height / 2f, board.bottom - height / 2f)
        return RectF(cx - width / 2f, cy - height / 2f, cx + width / 2f, cy + height / 2f)
    }

    fun renderLayers(config: WidgetConfig, widthDp: Int, heightDp: Int, density: Float, night: Boolean): Bitmap {
        val widthPx = (widthDp * density).toInt().coerceIn(32, 1600)
        val heightPx = (heightDp * density).toInt().coerceIn(32, 2000)
        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        config.layers.forEach { layer ->
            val bounds = layerBounds(layer, widthDp.toFloat(), heightDp.toFloat(), 1f)
            val layerConfig = config.copy(
                shape = layer.shape,
                dayColor = layer.dayColor,
                nightColor = layer.nightColor,
                textColor = layer.textColor,
                borderWidth = layer.borderWidth,
                borderOpacity = layer.borderOpacity,
                softness = layer.softness,
                glowOpacity = layer.glowOpacity,
                shadow = layer.shadow,
                shadowOpacity = layer.shadowOpacity,
                shadowYOffset = layer.shadowYOffset
            )
            val rendered = render(layerConfig, bounds.width().toInt().coerceAtLeast(1), bounds.height().toInt().coerceAtLeast(1), density, night)
            canvas.save()
            canvas.rotate(layer.rotation, bounds.centerX() * density, bounds.centerY() * density)
            canvas.drawBitmap(rendered, bounds.left * density, bounds.top * density, null)
            canvas.restore()
        }
        return bitmap
    }

    fun render(config: WidgetConfig, widthDp: Int, heightDp: Int, density: Float, night: Boolean): Bitmap {
        val width = (widthDp * density).toInt().coerceIn(32, 1600)
        val height = (heightDp * density).toInt().coerceIn(32, 2000)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // Blur may extend outside the path. Reserve only a small optical margin so the
        // decorative shape still fills the launcher cell instead of floating in empty space.
        // The object bounding box is the shape bounding box.  Effects may softly clip at
        // its edge, but the geometry itself must fill it exactly (especially the heart).
        val insetDp = if (config.shape == WidgetShape.HEART) 0f else 1f + config.borderWidth * .25f
        val inset = (insetDp * density).coerceAtMost(minOf(width, height) * .025f)
        // Every built-in background is a normalized vector. It expands to the
        // selected widget bounds instead of floating as a small glyph inside it.
        val area = RectF(inset, inset, width - inset, height - inset)
        val path = shapePath(config.shape, area)
        if (config.shadow > 0f && config.shadowOpacity > 0f) {
            val shadow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = 0x01000000
                setShadowLayer(config.shadow * density, 0f, config.shadowYOffset * density, withAlpha(0xFF16131C.toInt(), (config.shadowOpacity * 255).toInt()))
            }
            canvas.drawPath(path, shadow)
        }
        if (config.softness > 0f && config.glowOpacity > 0f) {
            val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = withAlpha(config.textColor.toInt(), (config.glowOpacity * 255).toInt())
                maskFilter = BlurMaskFilter(config.softness * density, BlurMaskFilter.Blur.OUTER)
            }
            canvas.drawPath(path, glow)
        }
        canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = (if (night) config.nightColor else config.dayColor).toInt() })
        if (config.borderWidth > 0f && config.borderOpacity > 0f) canvas.drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = config.borderWidth * density
            color = withAlpha(config.textColor.toInt(), (config.borderOpacity * 255).toInt())
        })
        return bitmap
    }

    /** Shared coordinates for editor and RemoteViews. */
    fun contentBounds(shape: WidgetShape, width: Float, height: Float, inset: Float = 0f): RectF =
        RectF(inset, inset, width - inset, height - inset)

    /**
     * Keeps a rectangular element completely inside an irregular background.
     * Moving from the centre towards the requested point and binary searching
     * the last valid point gives stable dragging for hearts, clouds and petals.
     */
    fun clampCenter(shape: WidgetShape, desiredX: Float, desiredY: Float, halfWidth: Float, halfHeight: Float, bounds: RectF): PointF {
        val path = shapePath(shape, bounds)
        val clip = Region(bounds.left.toInt() - 2, bounds.top.toInt() - 2, bounds.right.toInt() + 2, bounds.bottom.toInt() + 2)
        val region = Region().apply { setPath(path, clip) }
        fun valid(x: Float, y: Float): Boolean {
            val points = arrayOf(
                x - halfWidth to y - halfHeight, x + halfWidth to y - halfHeight,
                x - halfWidth to y + halfHeight, x + halfWidth to y + halfHeight
            )
            return points.all { region.contains(it.first.toInt(), it.second.toInt()) }
        }
        val cx = desiredX.coerceIn(bounds.left + halfWidth, bounds.right - halfWidth)
        val cy = desiredY.coerceIn(bounds.top + halfHeight, bounds.bottom - halfHeight)
        if (valid(cx, cy)) return PointF(cx, cy)
        var low = 0f; var high = 1f
        repeat(18) {
            val t = (low + high) / 2f
            val x = bounds.centerX() + (cx - bounds.centerX()) * t
            val y = bounds.centerY() + (cy - bounds.centerY()) * t
            if (valid(x, y)) low = t else high = t
        }
        return PointF(bounds.centerX() + (cx - bounds.centerX()) * low, bounds.centerY() + (cy - bounds.centerY()) * low)
    }

    internal fun shapePath(shape: WidgetShape, r: RectF): Path = Path().apply {
        when (shape) {
            WidgetShape.ROUNDED -> {
                val x = r.width()*.18f; val y = r.height()*.18f
                moveTo(r.left+x,r.top);lineTo(r.right-x,r.top);cubicTo(r.right-x*.28f,r.top,r.right,r.top+y*.28f,r.right,r.top+y)
                lineTo(r.right,r.bottom-y);cubicTo(r.right,r.bottom-y*.28f,r.right-x*.28f,r.bottom,r.right-x,r.bottom)
                lineTo(r.left+x,r.bottom);cubicTo(r.left+x*.28f,r.bottom,r.left,r.bottom-y*.28f,r.left,r.bottom-y)
                lineTo(r.left,r.top+y);cubicTo(r.left,r.top+y*.28f,r.left+x*.28f,r.top,r.left+x,r.top);close()
            }
            WidgetShape.SQUIRCLE -> addRoundRect(r, minOf(r.width(), r.height()) * .24f, minOf(r.width(), r.height()) * .24f, Path.Direction.CW)
            WidgetShape.PILL -> {
                val corner = minOf(r.width(),r.height()) * .11f
                moveTo(r.left, r.top + r.height() * .34f)
                cubicTo(r.left, r.top + r.height() * .12f, r.left + r.width() * .25f, r.top, r.centerX(), r.top)
                cubicTo(r.right - r.width() * .25f, r.top, r.right, r.top + r.height() * .12f, r.right, r.top + r.height() * .34f)
                lineTo(r.right, r.bottom - corner); quadTo(r.right, r.bottom, r.right - corner, r.bottom)
                lineTo(r.left + corner, r.bottom); quadTo(r.left, r.bottom, r.left, r.bottom - corner); close()
            }
            WidgetShape.CAPSULE -> addRoundRect(r, minOf(r.width(), r.height()) * .50f, minOf(r.width(), r.height()) * .50f, Path.Direction.CW)
            WidgetShape.CIRCLE -> addOval(r, Path.Direction.CW)
            WidgetShape.DIAMOND -> {
                moveTo(r.centerX(), r.top)
                cubicTo(r.centerX()+r.width()*.16f,r.top,r.centerX()+r.width()*.25f,r.top+r.height()*.15f,r.centerX()+r.width()*.24f,r.top+r.height()*.27f)
                cubicTo(r.right-r.width()*.15f,r.top+r.height()*.25f,r.right,r.centerY()-r.height()*.16f,r.right,r.centerY())
                cubicTo(r.right,r.centerY()+r.height()*.16f,r.right-r.width()*.15f,r.bottom-r.height()*.25f,r.centerX()+r.width()*.24f,r.bottom-r.height()*.27f)
                cubicTo(r.centerX()+r.width()*.25f,r.bottom-r.height()*.15f,r.centerX()+r.width()*.16f,r.bottom,r.centerX(),r.bottom)
                cubicTo(r.centerX()-r.width()*.16f,r.bottom,r.centerX()-r.width()*.25f,r.bottom-r.height()*.15f,r.centerX()-r.width()*.24f,r.bottom-r.height()*.27f)
                cubicTo(r.left+r.width()*.15f,r.bottom-r.height()*.25f,r.left,r.centerY()+r.height()*.16f,r.left,r.centerY())
                cubicTo(r.left,r.centerY()-r.height()*.16f,r.left+r.width()*.15f,r.top+r.height()*.25f,r.centerX()-r.width()*.24f,r.top+r.height()*.27f)
                cubicTo(r.centerX()-r.width()*.25f,r.top+r.height()*.15f,r.centerX()-r.width()*.16f,r.top,r.centerX(),r.top); close()
            }
            WidgetShape.PETAL -> {
                moveTo(r.centerX(), r.top)
                cubicTo(r.right-r.width()*.08f,r.top+r.height()*.02f,r.right,r.top+r.height()*.30f,r.centerX(),r.bottom)
                cubicTo(r.left,r.top+r.height()*.30f,r.left+r.width()*.08f,r.top+r.height()*.02f,r.centerX(),r.top); close()
            }
            WidgetShape.CLOUD -> {
                moveTo(r.left, r.centerY()+r.height()*.04f)
                cubicTo(r.left,r.top+r.height()*.25f,r.left+r.width()*.18f,r.top+r.height()*.12f,r.left+r.width()*.36f,r.top+r.height()*.18f)
                cubicTo(r.left+r.width()*.48f,r.top-r.height()*.02f,r.left+r.width()*.72f,r.top+r.height()*.02f,r.left+r.width()*.77f,r.top+r.height()*.24f)
                cubicTo(r.right-r.width()*.06f,r.top+r.height()*.27f,r.right,r.top+r.height()*.43f,r.right,r.centerY()+r.height()*.02f)
                cubicTo(r.right,r.bottom-r.height()*.18f,r.right-r.width()*.18f,r.bottom-r.height()*.06f,r.right-r.width()*.38f,r.bottom-r.height()*.12f)
                cubicTo(r.centerX(),r.bottom+r.height()*.02f,r.left+r.width()*.18f,r.bottom-r.height()*.02f,r.left+r.width()*.14f,r.bottom-r.height()*.18f)
                cubicTo(r.left+r.width()*.05f,r.bottom-r.height()*.24f,r.left,r.bottom-r.height()*.38f,r.left,r.centerY()+r.height()*.04f);close()
            }
            WidgetShape.HEART -> {
                // Full-bleed, balanced heart. Every extreme touches its bounding box.
                moveTo(r.centerX(), r.bottom)
                cubicTo(r.centerX()-r.width()*.11f,r.bottom-r.height()*.10f,r.left,r.top+r.height()*.58f,r.left,r.top+r.height()*.30f)
                cubicTo(r.left,r.top+r.height()*.04f,r.left+r.width()*.34f,r.top-r.height()*.01f,r.centerX(),r.top+r.height()*.20f)
                cubicTo(r.left+r.width()*.66f,r.top-r.height()*.01f,r.right,r.top+r.height()*.04f,r.right,r.top+r.height()*.30f)
                cubicTo(r.right,r.top+r.height()*.58f,r.centerX()+r.width()*.11f,r.bottom-r.height()*.10f,r.centerX(),r.bottom)
                close()
            }
            WidgetShape.DROP -> {
                // A full, calm raindrop: round crown, tapered but never needle-like tip.
                moveTo(r.centerX(), r.top)
                cubicTo(r.right - r.width() * .13f, r.top + r.height() * .25f, r.right, r.top + r.height() * .51f, r.right, r.bottom - r.height() * .27f)
                cubicTo(r.right, r.bottom - r.height() * .08f, r.right - r.width() * .23f, r.bottom, r.centerX(), r.bottom)
                cubicTo(r.left + r.width() * .23f, r.bottom, r.left, r.bottom - r.height() * .08f, r.left, r.bottom - r.height() * .27f)
                cubicTo(r.left, r.top + r.height() * .51f, r.left + r.width() * .13f, r.top + r.height() * .25f, r.centerX(), r.top)
                close()
            }
            WidgetShape.TICKET -> {
                val corner = minOf(r.width(), r.height()) * .14f
                val notch = minOf(r.width(), r.height()) * .12f
                moveTo(r.left + corner, r.top); lineTo(r.right - corner, r.top); quadTo(r.right, r.top, r.right, r.top + corner)
                lineTo(r.right, r.centerY() - notch); cubicTo(r.right - notch, r.centerY() - notch, r.right - notch, r.centerY() + notch, r.right, r.centerY() + notch)
                lineTo(r.right, r.bottom - corner); quadTo(r.right, r.bottom, r.right - corner, r.bottom); lineTo(r.left + corner, r.bottom); quadTo(r.left, r.bottom, r.left, r.bottom - corner)
                lineTo(r.left, r.centerY() + notch); cubicTo(r.left + notch, r.centerY() + notch, r.left + notch, r.centerY() - notch, r.left, r.centerY() - notch)
                lineTo(r.left, r.top + corner); quadTo(r.left, r.top, r.left + corner, r.top); close()
            }
            WidgetShape.WAVE -> {
                // Soft flag silhouette with visible undulation, full-bleed in its bounds.
                val radius = minOf(r.width(), r.height()) * .10f
                val pole = r.left + r.width() * .10f
                moveTo(pole, r.top + radius)
                quadTo(pole, r.top, pole + radius, r.top)
                cubicTo(r.left + r.width() * .34f, r.top + r.height() * .04f, r.left + r.width() * .60f, r.top + r.height() * .20f, r.right - radius, r.top + r.height() * .12f)
                quadTo(r.right, r.top + r.height() * .12f, r.right, r.top + r.height() * .20f)
                cubicTo(r.left + r.width() * .64f, r.top + r.height() * .32f, r.left + r.width() * .38f, r.top + r.height() * .14f, pole, r.top + r.height() * .30f)
                lineTo(pole, r.bottom - radius)
                quadTo(pole, r.bottom, pole + radius, r.bottom)
                cubicTo(r.left + r.width() * .34f, r.bottom - r.height() * .04f, r.left + r.width() * .60f, r.bottom - r.height() * .20f, r.right - radius, r.bottom - r.height() * .12f)
                quadTo(r.right, r.bottom - r.height() * .12f, r.right, r.bottom - r.height() * .20f)
                cubicTo(r.left + r.width() * .64f, r.bottom - r.height() * .32f, r.left + r.width() * .38f, r.bottom - r.height() * .14f, pole, r.bottom - r.height() * .30f)
                close()
            }
        }
    }
    private fun withAlpha(color: Int, alpha: Int) = (color and 0x00FFFFFF) or (alpha.coerceIn(0,255) shl 24)
}
