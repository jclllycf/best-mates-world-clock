package com.bestmates.worldclock.debug

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import android.widget.ScrollView
import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import com.bestmates.worldclock.scene.CityMetrics
import com.bestmates.worldclock.solar.SolarCatalog
import com.bestmates.worldclock.widget.SolarIconRenderer
import java.io.File
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * BM-REAL-01 contact sheet, drawn with the production [SolarIconRenderer] at production
 * sizes: the widget glyph (city icon at scale 1), the picker tile, and a large widget glyph,
 * on the night tile and on a bright wallpaper tone. Every sun and moon style is listed with
 * its status. The sheet is shown and also saved as PNG to
 * `Android/data/<package>/files/review/` for `adb pull`.
 *
 * Debug builds only: `adb shell am start -n com.bestmates.worldclock/.debug.CelestialContactSheetActivity`
 * (add `--es only daybook` for the Daybook set alone).
 */
class CelestialContactSheetActivity : Activity() {
    private data class Entry(val name: String, val day: Boolean, val sun: SunIconStyle, val moon: MoonIconStyle, val status: String, val group: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onlyDaybook = intent.getStringExtra("only") == "daybook"
        val entries = buildList {
            SunIconStyle.entries.forEach { s ->
                val st = SolarCatalog.status(s)
                if (onlyDaybook && st != SolarCatalog.Status.DAYBOOK) return@forEach
                add(Entry(s.name, true, s, MoonIconStyle.DB_MOON_CRESCENT, label(st, SolarCatalog.daybookSun(s)?.name), st.ordinal))
            }
            MoonIconStyle.entries.forEach { m ->
                val st = SolarCatalog.status(m)
                if (onlyDaybook && st != SolarCatalog.Status.DAYBOOK) return@forEach
                add(Entry(m.name, false, SunIconStyle.DB_SUN_DISC, m, label(st, SolarCatalog.daybookMoon(m)?.name), st.ordinal))
            }
        }.sortedWith(compareBy({ it.group }, { !it.day }))
        val bitmap = draw(entries, if (onlyDaybook) "Daybook celestial set" else "Sun & moon audit — every style")
        runCatching {
            val dir = File(getExternalFilesDir(null), "review").apply { mkdirs() }
            File(dir, if (onlyDaybook) "celestial_daybook.png" else "celestial_audit.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
        setContentView(ScrollView(this).apply {
            setBackgroundColor(Color.rgb(24, 22, 32))
            addView(ImageView(this@CelestialContactSheetActivity).apply { setImageBitmap(bitmap); adjustViewBounds = true })
        })
    }

    private fun label(status: SolarCatalog.Status, target: String?) = when (status) {
        SolarCatalog.Status.DAYBOOK -> "KEEP · Daybook (picker)"
        SolarCatalog.Status.REDRAWN -> "REDRAW → ${target?.removePrefix("DB_")?.lowercase()} · legacy only"
        SolarCatalog.Status.RETIRED -> "REMOVED from picker · legacy only"
    }

    private fun draw(entries: List<Entry>, title: String): Bitmap {
        val d = resources.displayMetrics.density
        fun dp(v: Float) = v * d
        val glyph = (CityMetrics.ICON * d).roundToInt()
        val tile = (36 * d).roundToInt()
        val large = (64 * d).roundToInt()
        val cols = 3
        val cellW = dp(120f); val cellH = dp(132f)
        val width = (cellW * cols + dp(24f)).roundToInt()
        val rows = ceil(entries.size / cols.toFloat()).toInt()
        val height = (dp(56f) + rows * cellH + dp(16f)).roundToInt()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.rgb(24, 22, 32))
        val text = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(239, 231, 218); textSize = dp(15f); typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText(title, dp(12f), dp(26f), text)
        text.typeface = Typeface.DEFAULT; text.textSize = dp(10f); text.color = Color.rgb(170, 164, 190)
        canvas.drawText("glyph ${CityMetrics.ICON.toInt()}dp · tile 36dp · large 64dp · bright wallpaper", dp(12f), dp(44f), text)
        val night = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(46, 42, 69) }
        val bright = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(226, 214, 236) }
        entries.forEachIndexed { i, e ->
            val x = dp(12f) + (i % cols) * cellW
            val y = dp(56f) + (i / cols) * cellH
            val card = RectF(x, y, x + cellW - dp(8f), y + cellH - dp(8f))
            canvas.drawRoundRect(card, dp(12f), dp(12f), night)
            val tint = if (e.day) 0xFFFFD58A.toInt() else 0xFFE4E2FF.toInt()
            fun icon(size: Int, cx: Float, cy: Float, color: Int = tint) {
                val b = SolarIconRenderer.render(this, size, e.day, e.sun, e.moon, color, raw = true)
                canvas.drawBitmap(b, cx - size / 2f, cy - size / 2f, null)
            }
            icon(large, card.left + dp(40f), card.top + dp(42f))
            icon(tile, card.left + dp(92f), card.top + dp(26f))
            icon(glyph, card.left + dp(92f), card.top + dp(58f))
            val swatch = RectF(card.left + dp(80f), card.top + dp(70f), card.left + dp(104f), card.top + dp(94f))
            canvas.drawRoundRect(swatch, dp(6f), dp(6f), bright)
            icon(glyph, swatch.centerX(), swatch.centerY(), 0xFF3A3346.toInt())
            text.color = Color.rgb(239, 231, 218); text.textSize = dp(8.5f)
            canvas.drawText(e.name.lowercase(), card.left + dp(6f), card.bottom - dp(18f), text)
            text.color = when (e.group) { 0 -> Color.rgb(160, 220, 170); 1 -> Color.rgb(240, 200, 120); else -> Color.rgb(200, 150, 160) }
            text.textSize = dp(7.5f)
            canvas.drawText(e.status, card.left + dp(6f), card.bottom - dp(7f), text)
        }
        return bitmap
    }
}
