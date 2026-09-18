package com.bestmates.worldclock.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.bestmates.worldclock.R
import com.bestmates.worldclock.data.ConfigStore
import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.ThemeMode
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.solar.SolarCalculator
import java.time.ZoneId
import kotlin.math.roundToInt

open class WorldClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) = ids.forEach { render(context, manager, it) }
    override fun onAppWidgetOptionsChanged(context: Context, manager: AppWidgetManager, appWidgetId: Int, newOptions: android.os.Bundle) {
        render(context, manager, appWidgetId)
    }
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action in setOf(Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_CHANGED, Intent.ACTION_DATE_CHANGED)) {
            AppWidgetManager.getInstance(context).getAppWidgetIds(android.content.ComponentName(context, WorldClockWidgetProvider::class.java)).forEach { render(context, AppWidgetManager.getInstance(context), it) }
        }
    }
    override fun onDeleted(context: Context, ids: IntArray) = ids.forEach { ConfigStore(context).delete(it) }

    companion object {
        fun render(context: Context, manager: AppWidgetManager, id: Int) {
            val store = ConfigStore(context)
            val projectId = manager.getAppWidgetOptions(id).getString("projectId")
            val config = projectId?.let { store.loadProject(it) } ?: store.load(id)
            store.save(id, config)
            val views = RemoteViews(context.packageName, R.layout.widget_world)
            val night = config.theme == ThemeMode.DARK || (config.theme == ThemeMode.SYSTEM && (context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES)
            val bounds = manager.getAppWidgetOptions(id)
            val exactSize = if (android.os.Build.VERSION.SDK_INT >= 31) bounds.getParcelableArrayList<android.util.SizeF>(AppWidgetManager.OPTION_APPWIDGET_SIZES)?.firstOrNull() else null
            val width = (exactSize?.width?.roundToInt() ?: bounds.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 300)).coerceAtLeast(40)
            val height = (exactSize?.height?.roundToInt() ?: bounds.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 250)).coerceAtLeast(40)
            val layers = config.layers
            val designBounds = BackgroundRenderer.designBounds(width.toFloat(), height.toFloat(), 1f)
            views.setImageViewBitmap(R.id.widget_background, BackgroundRenderer.renderLayers(config, width, height, context.resources.displayMetrics.density, night))
            views.setViewPadding(R.id.widget_content, 0, 0, 0, 0)
            views.setTextViewText(R.id.widget_title, config.title)
            views.setTextViewText(R.id.widget_subtitle, config.subtitle)
            views.setViewVisibility(R.id.widget_title, if (config.title.isBlank()) View.GONE else View.VISIBLE)
            views.setViewVisibility(R.id.widget_subtitle, if (config.subtitle.isBlank()) View.GONE else View.VISIBLE)
            if (android.os.Build.VERSION.SDK_INT >= 31) {
                val titleLeft = width * .08f
                val titleTop = height * when (config.shape) { WidgetShape.CLOUD -> .16f; WidgetShape.PILL -> .10f; else -> .06f }
                views.setViewLayoutMargin(R.id.widget_title, RemoteViews.MARGIN_LEFT, titleLeft, android.util.TypedValue.COMPLEX_UNIT_DIP)
                views.setViewLayoutMargin(R.id.widget_title, RemoteViews.MARGIN_TOP, titleTop, android.util.TypedValue.COMPLEX_UNIT_DIP)
                views.setViewLayoutMargin(R.id.widget_subtitle, RemoteViews.MARGIN_LEFT, titleLeft, android.util.TypedValue.COMPLEX_UNIT_DIP)
                views.setViewLayoutMargin(R.id.widget_subtitle, RemoteViews.MARGIN_TOP, titleTop + 23f, android.util.TypedValue.COMPLEX_UNIT_DIP)
            }
            (1..10).forEach { index ->
                val slot = context.resources.getIdentifier("city_slot_$index", "id", context.packageName)
                val city = config.cities.getOrNull(index - 1)
                views.setViewVisibility(slot, if (city == null) View.GONE else View.VISIBLE)
                city?.let {
                    val contentBounds = designBounds
                    bindCity(context, views, index, it, config, contentBounds, WidgetShape.SQUIRCLE)
                }
            }
            (1..5).forEach { index ->
                val viewId = context.resources.getIdentifier("custom_text_$index", "id", context.packageName)
                val text = config.texts.getOrNull(index - 1)
                views.setViewVisibility(viewId, if (text == null || text.content.isBlank()) View.GONE else View.VISIBLE)
                text?.let {
                    val contentBounds = designBounds
                    val rendered = renderCustomText(context, it)
                    views.setImageViewBitmap(viewId, rendered.bitmap)
                    if (android.os.Build.VERSION.SDK_INT >= 31) {
                        val requestedX = contentBounds.centerX() + .5f * it.x * contentBounds.width()
                        val requestedY = contentBounds.centerY() + .5f * it.y * contentBounds.height()
                        val centre = android.graphics.PointF(
                            requestedX.coerceIn(contentBounds.left + rendered.widthDp / 2f, contentBounds.right - rendered.widthDp / 2f),
                            requestedY.coerceIn(contentBounds.top + rendered.heightDp / 2f, contentBounds.bottom - rendered.heightDp / 2f)
                        )
                        val left = centre.x - rendered.widthDp / 2f
                        val top = centre.y - rendered.heightDp / 2f
                        views.setViewLayoutWidth(viewId, rendered.widthDp, android.util.TypedValue.COMPLEX_UNIT_DIP)
                        views.setViewLayoutHeight(viewId, rendered.heightDp, android.util.TypedValue.COMPLEX_UNIT_DIP)
                        views.setViewLayoutMargin(viewId, RemoteViews.MARGIN_LEFT, left, android.util.TypedValue.COMPLEX_UNIT_DIP)
                        views.setViewLayoutMargin(viewId, RemoteViews.MARGIN_TOP, top, android.util.TypedValue.COMPLEX_UNIT_DIP)
                    }
                }
            }
            manager.updateAppWidget(id, views)
        }

        private data class RenderedText(val bitmap: android.graphics.Bitmap, val widthDp: Float, val heightDp: Float)

        private fun renderCustomText(context: Context, item: com.bestmates.worldclock.model.CustomText): RenderedText {
            val density = context.resources.displayMetrics.density
            val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                textSize = item.size * density
                typeface = android.graphics.Typeface.create(fontFamilyName(item.font), fontStyle(item.font))
                color = item.color.toInt()
                alpha = (((item.color ushr 24) and 0xFF) * item.alpha.coerceIn(0f, 1f)).toInt()
            }
            val lines = item.content.lines().ifEmpty { listOf("") }
            val padding = 5f * density
            // Line height includes the font's natural ascent-to-descent range plus a
            // small breathing multiplier for comfortable reading. The previous 1.36
            // value caused excessive gaps in multiline text.
            val lineHeight = (paint.fontMetrics.descent - paint.fontMetrics.ascent) * 1.18f
            val widthPx = (lines.maxOfOrNull { paint.measureText(it) } ?: 1f) + padding * 2f
            val heightPx = lineHeight * lines.size + padding * 2f
            val bitmap = android.graphics.Bitmap.createBitmap(widthPx.toInt().coerceAtLeast(1), heightPx.toInt().coerceAtLeast(1), android.graphics.Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            lines.forEachIndexed { index, line ->
                val x = (widthPx - paint.measureText(line)) / 2f
                val y = padding - paint.fontMetrics.ascent + lineHeight * index
                canvas.drawText(line, x, y, paint)
            }
            bitmap.density = context.resources.displayMetrics.densityDpi
            return RenderedText(bitmap, widthPx / density, heightPx / density)
        }
        private fun bindCity(context: Context, views: RemoteViews, index: Int, city: City, config: com.bestmates.worldclock.model.WidgetConfig, contentBounds: android.graphics.RectF, shape: WidgetShape) {
            fun id(name: String) = context.resources.getIdentifier("${name}_$index", "id", context.packageName)
            val cityLabel = city.nickname.ifBlank { city.name } + if (config.showCountry && city.showCountry && city.country.isNotBlank()) " · ${city.country}" else ""
            views.setTextViewText(id("city_name"), styled(cityLabel, city.nameFont))
            val day = SolarCalculator.isDay(city)
            val iconSizeDp = 18f * city.scale
            val iconColor = if (day) city.sunColor.toInt() else city.moonColor.toInt()
            views.setImageViewBitmap(id("city_icon"), SolarIconRenderer.render(context, (iconSizeDp * context.resources.displayMetrics.density).roundToInt(), day, city.sunStyle, city.moonStyle, iconColor))
            views.setViewVisibility(id("city_icon"), if (config.showSolarIcon && city.showSolar) View.VISIBLE else View.GONE)
            views.setViewVisibility(id("city_time"), if (config.showTime && city.showTime) View.VISIBLE else View.GONE)
            views.setViewVisibility(id("city_date"), if (config.showDate && city.showDate) View.VISIBLE else View.GONE)
            views.setTextColor(id("city_name"), city.nameColor.toInt()); views.setTextColor(id("city_time"), city.timeColor.toInt()); views.setTextColor(id("city_date"), city.dateColor.toInt())
            views.setTextViewTextSize(id("city_name"), android.util.TypedValue.COMPLEX_UNIT_SP, 11f * city.scale)
            views.setTextViewTextSize(id("city_time"), android.util.TypedValue.COMPLEX_UNIT_SP, 22f * city.scale)
            views.setTextViewTextSize(id("city_date"), android.util.TypedValue.COMPLEX_UNIT_SP, 9f * city.scale)
            if (android.os.Build.VERSION.SDK_INT >= 31) {
                views.setViewLayoutWidth(id("city_icon"), iconSizeDp, android.util.TypedValue.COMPLEX_UNIT_DIP)
                views.setViewLayoutHeight(id("city_icon"), iconSizeDp, android.util.TypedValue.COMPLEX_UNIT_DIP)
                val blockWidth = (130f * city.scale).coerceAtMost(contentBounds.width() * .94f)
                val blockHeight = (86f * city.scale).coerceAtMost(contentBounds.height() * .94f)
                val requestedX = contentBounds.centerX() + .5f * city.x * contentBounds.width()
                val requestedY = contentBounds.centerY() + .5f * city.y * contentBounds.height()
                val centre = android.graphics.PointF(
                    requestedX.coerceIn(contentBounds.left + blockWidth / 2f, contentBounds.right - blockWidth / 2f),
                    requestedY.coerceIn(contentBounds.top + blockHeight / 2f, contentBounds.bottom - blockHeight / 2f)
                )
                val left = centre.x - blockWidth / 2f
                val top = centre.y - blockHeight / 2f
                views.setViewLayoutWidth(id("city_slot"), blockWidth, android.util.TypedValue.COMPLEX_UNIT_DIP)
                views.setViewLayoutMargin(id("city_slot"), RemoteViews.MARGIN_LEFT, left, android.util.TypedValue.COMPLEX_UNIT_DIP)
                views.setViewLayoutMargin(id("city_slot"), RemoteViews.MARGIN_TOP, top, android.util.TypedValue.COMPLEX_UNIT_DIP)
            }
            views.setString(id("city_time"), "setTimeZone", ZoneId.of(city.zoneId).id)
            views.setString(id("city_date"), "setTimeZone", ZoneId.of(city.zoneId).id)
            views.setCharSequence(id("city_time"), "setFormat24Hour", styled(if (config.use24Hour) "HH:mm" else "h:mm a", city.timeFont))
            views.setCharSequence(id("city_time"), "setFormat12Hour", styled(if (config.use24Hour) "HH:mm" else "h:mm a", city.timeFont))
            views.setCharSequence(id("city_date"), "setFormat24Hour", styled("EEE, MMM d", city.dateFont))
            views.setCharSequence(id("city_date"), "setFormat12Hour", styled("EEE, MMM d", city.dateFont))
        }

        private fun styled(text: String, font: String): CharSequence = android.text.SpannableString(text).apply {
            setSpan(android.text.style.TypefaceSpan(fontFamilyName(font)), 0, length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(android.text.style.StyleSpan(fontStyle(font)), 0, length, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        private fun fontFamilyName(font: String): String = if (font.startsWith("BM|")) font.split('|').getOrNull(1) ?: "sans-serif" else when (font) {
            "SANS_LIGHT" -> "sans-serif-light"
            "SANS_THIN" -> "sans-serif-thin"
            "SANS_MEDIUM" -> "sans-serif-medium"
            "SANS_CONDENSED" -> "sans-serif-condensed"
            "CONDENSED_LIGHT" -> "sans-serif-condensed-light"
            "CONDENSED_MEDIUM" -> "sans-serif-condensed-medium"
            "SANS_BLACK" -> "sans-serif-black"
            "SERIF" -> "serif"
            "SERIF_MONO" -> "serif-monospace"
            "MONO" -> "monospace"
            "CURSIVE" -> "cursive"
            "CASUAL" -> "casual"
            "SMALL_CAPS" -> "sans-serif-smallcaps"
            "SANS_ROUNDED" -> "sans-serif-rounded"
            else -> "sans-serif"
        }
        private fun fontStyle(font: String): Int = if (font.startsWith("BM|")) font.split('|').getOrNull(2)?.toIntOrNull() ?: android.graphics.Typeface.NORMAL else android.graphics.Typeface.NORMAL
    }
}
