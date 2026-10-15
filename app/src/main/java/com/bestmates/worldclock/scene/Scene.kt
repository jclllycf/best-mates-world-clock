package com.bestmates.worldclock.scene

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.solar.SolarCalculator
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class LineKind { NAME, TIME, DATE, TEXT }

/**
 * One measured line of text, in design-board dp, relative to its element's top-left.
 * [box] is the font's full line box (descent − ascent); text is vertically centred in it,
 * which is exactly how a launcher TextClock with includeFontPadding=false draws.
 */
data class SceneLine(
    val kind: LineKind,
    val text: String,
    val fontId: String,
    val size: Float,
    val color: Int,
    val width: Float,
    val box: Float,
    val ascent: Float,
    val cy: Float,
    /** Width reserved for live clock text so the minute changing never clips it. */
    val slotWidth: Float,
)

data class SceneIcon(val size: Float, val cy: Float, val day: Boolean, val color: Int, val city: City)

sealed interface SceneItem { val index: Int; val frame: Frame }
data class LayerItem(override val index: Int, val layer: WidgetLayer, override val frame: Frame) : SceneItem
data class CityItem(override val index: Int, val city: City, override val frame: Frame, val icon: SceneIcon?, val lines: List<SceneLine>) : SceneItem
data class TextItem(override val index: Int, val text: CustomText, override val frame: Frame, val lines: List<SceneLine>) : SceneItem

data class Scene(
    val boardW: Float,
    val boardH: Float,
    val layers: List<LayerItem>,
    val cities: List<CityItem>,
    val texts: List<TextItem>,
)

/** Shared base proportions of a city block at scale 1, in board dp. */
object CityMetrics {
    const val ICON = 18f
    const val NAME = 11f
    const val TIME = 22f
    const val DATE = 9f
    const val PAD_X = 6f
    const val PAD_Y = 4f
    const val ICON_GAP = 2f
    /** Optical stacking: lines overlap their font boxes slightly so the block reads as one unit. */
    fun advance(kind: LineKind) = when (kind) { LineKind.NAME -> .96f; LineKind.TIME -> .86f; LineKind.DATE -> .98f; LineKind.TEXT -> 1.04f }
}

class SceneBuilder(context: Context) {
    private val app = context.applicationContext
    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG or Paint.SUBPIXEL_TEXT_FLAG)

    private data class Measured(val width: Float, val box: Float, val ascent: Float)

    /** Measures in dp. Metrics are taken at 8× and divided back for sub-pixel precision. */
    private fun measure(text: String, fontId: String, size: Float): Measured {
        paint.typeface = WidgetFonts.typeface(app, fontId)
        paint.textSize = size * 8f
        val fm = paint.fontMetrics
        return Measured(paint.measureText(text) / 8f, (fm.descent - fm.ascent) / 8f, fm.ascent / 8f)
    }

    private fun widestDigitWidth(fontId: String, size: Float, text: String): Float {
        paint.typeface = WidgetFonts.typeface(app, fontId)
        paint.textSize = size * 8f
        val widest = ('0'..'9').maxBy { paint.measureText(it.toString()) }
        return paint.measureText(text.map { if (it.isDigit()) widest else it }.joinToString("")) / 8f
    }

    /** Thread-safe: the measuring paint is shared, so builds are serialised (they take well under a millisecond). */
    @Synchronized
    fun build(config: WidgetConfig, now: Instant = Instant.now(), locale: Locale = Locale.getDefault()): Scene {
        val bw = config.boardWidthDp
        val bh = config.boardHeightDp
        val layers = config.layers.mapIndexed { i, layer -> LayerItem(i, layer, SceneGeometry.layerFrame(layer, bw, bh)) }
        val cities = config.cities.mapIndexed { i, city -> buildCity(i, city, config, now, locale) }
        val texts = config.texts.mapIndexedNotNull { i, t -> if (t.content.isBlank()) null else buildText(i, t, bw, bh) }
        return Scene(bw, bh, layers, cities, texts)
    }

    fun timePattern(use24Hour: Boolean) = if (use24Hour) "HH:mm" else "h:mm a"
    val datePattern = "EEE, MMM d"

    private fun buildCity(index: Int, city: City, config: WidgetConfig, now: Instant, locale: Locale): CityItem {
        val s = city.scale.coerceIn(.3f, 3f)
        val zone = runCatching { ZoneId.of(city.zoneId) }.getOrDefault(ZoneId.systemDefault())
        val local = ZonedDateTime.ofInstant(now, zone)
        val day = runCatching { SolarCalculator.isDay(city, local) }.getOrDefault(true)

        val label = city.nickname.ifBlank { city.name } +
            if (config.showCountry && city.showCountry && city.country.isNotBlank()) " · ${city.country}" else ""
        val lines = mutableListOf<SceneLine>()
        var y = CityMetrics.PAD_Y * s
        var icon: SceneIcon? = null
        if (config.showSolarIcon && city.showSolar) {
            val size = CityMetrics.ICON * s
            icon = SceneIcon(size, y + size / 2f, day, (if (day) city.sunColor else city.moonColor).toInt(), city)
            y += size + CityMetrics.ICON_GAP * s
        }
        // [stable] keeps a live line's width constant as digits change; [slot] widens the
        // launcher TextClock view beyond the visible text so longer values never clip.
        fun add(kind: LineKind, text: String, font: String, size: Float, color: Long, stable: Float = 0f, slot: Float = 1f) {
            val m = measure(text, font, size)
            val advance = m.box * CityMetrics.advance(kind)
            val width = maxOf(m.width, stable)
            lines += SceneLine(kind, text, font, size, color.toInt(), width, m.box, m.ascent, y + advance / 2f, width * slot)
            y += advance
        }
        add(LineKind.NAME, label, city.nameFont, CityMetrics.NAME * s, city.nameColor)
        if (config.showTime && city.showTime) {
            val text = local.format(DateTimeFormatter.ofPattern(timePattern(config.use24Hour), locale))
            val font = WidgetFonts.live(city.timeFont).id
            add(LineKind.TIME, text, font, CityMetrics.TIME * s, city.timeColor, widestDigitWidth(font, CityMetrics.TIME * s, text), 1.08f)
        }
        if (config.showDate && city.showDate) {
            val text = local.format(DateTimeFormatter.ofPattern(datePattern, locale))
            add(LineKind.DATE, text, WidgetFonts.live(city.dateFont).id, CityMetrics.DATE * s, city.dateColor, slot = 1.4f)
        }
        val contentW = maxOf(lines.maxOfOrNull { it.width } ?: 0f, icon?.size ?: 0f)
        val w = contentW + CityMetrics.PAD_X * 2 * s
        val h = y + CityMetrics.PAD_Y * s
        val frame = SceneGeometry.contentFrame(city.x, city.y, w, h, city.rotation, config.boardWidthDp, config.boardHeightDp)
        return CityItem(index, city, frame, icon, lines)
    }

    private fun buildText(index: Int, item: CustomText, bw: Float, bh: Float): TextItem {
        val size = item.size.coerceIn(4f, 160f)
        val alpha = ((item.color ushr 24) and 0xFF) * item.alpha.coerceIn(0f, 1f)
        val color = ((alpha.toInt().coerceIn(0, 255)) shl 24) or (item.color and 0xFFFFFF).toInt()
        val padX = size * .3f
        val padY = size * .18f
        var y = padY
        val lines = item.content.split('\n').map { text ->
            val m = measure(text, item.font, size)
            val advance = m.box * CityMetrics.advance(LineKind.TEXT)
            SceneLine(LineKind.TEXT, text, item.font, size, color, m.width, m.box, m.ascent, y + advance / 2f, m.width).also { y += advance }
        }
        val w = (lines.maxOfOrNull { it.width } ?: 0f) + padX * 2
        val h = y + padY
        return TextItem(index, item, SceneGeometry.contentFrame(item.x, item.y, w, h, item.rotation, bw, bh), lines)
    }
}
