package com.bestmates.worldclock.fonts

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import androidx.core.content.res.ResourcesCompat
import com.bestmates.worldclock.R
import java.util.concurrent.ConcurrentHashMap

enum class FontCategory { LIVE, SANS, DISPLAY, SERIF, ROUNDED, MONO, HAND }

/**
 * A typeface style usable on the widget.
 *
 * Two kinds exist because of a platform rule: launchers inflate widget layouts in a
 * restricted context where app-bundled font resources are ignored. Therefore:
 *  - bundled fonts ([familyRes]) style everything Best Mates draws itself (city names,
 *    custom text) — identical in the editor, previews and the launcher bitmap;
 *  - system styles ([systemFamily] + [weight]) style the live time and date, which the
 *    launcher keeps ticking through TextClock ([clockLayout]). The editor draws those same
 *    system typefaces, so the preview still matches the home screen exactly.
 */
data class WidgetFont(
    val id: String,
    val label: String,
    val category: FontCategory,
    val familyRes: Int = 0,
    val systemFamily: String? = null,
    val weight: Int = 400,
    val clockLayout: Int = 0,
) {
    val isLive: Boolean get() = systemFamily != null
}

object WidgetFonts {
    /** Live (TextClock-capable) styles: system families at precise weights. */
    val live: List<WidgetFont> = listOf(
        WidgetFont("sys.sans_thin", "Thin", FontCategory.LIVE, systemFamily = "sans-serif", weight = 200, clockLayout = R.layout.widget_clock_sys_sans_thin),
        WidgetFont("sys.sans_light", "Light", FontCategory.LIVE, systemFamily = "sans-serif", weight = 300, clockLayout = R.layout.widget_clock_sys_sans_light),
        WidgetFont("sys.sans", "Regular", FontCategory.LIVE, systemFamily = "sans-serif", weight = 400, clockLayout = R.layout.widget_clock_sys_sans),
        WidgetFont("sys.sans_medium", "Medium", FontCategory.LIVE, systemFamily = "sans-serif", weight = 500, clockLayout = R.layout.widget_clock_sys_sans_medium),
        WidgetFont("sys.sans_bold", "Bold", FontCategory.LIVE, systemFamily = "sans-serif", weight = 700, clockLayout = R.layout.widget_clock_sys_sans_bold),
        WidgetFont("sys.sans_black", "Black", FontCategory.LIVE, systemFamily = "sans-serif", weight = 900, clockLayout = R.layout.widget_clock_sys_sans_black),
        WidgetFont("sys.condensed_light", "Narrow Light", FontCategory.LIVE, systemFamily = "sans-serif-condensed", weight = 300, clockLayout = R.layout.widget_clock_sys_condensed_light),
        WidgetFont("sys.condensed", "Narrow", FontCategory.LIVE, systemFamily = "sans-serif-condensed", weight = 400, clockLayout = R.layout.widget_clock_sys_condensed),
        WidgetFont("sys.condensed_bold", "Narrow Bold", FontCategory.LIVE, systemFamily = "sans-serif-condensed", weight = 700, clockLayout = R.layout.widget_clock_sys_condensed_bold),
        WidgetFont("sys.serif", "Serif", FontCategory.LIVE, systemFamily = "serif", weight = 400, clockLayout = R.layout.widget_clock_sys_serif),
        WidgetFont("sys.serif_bold", "Serif Bold", FontCategory.LIVE, systemFamily = "serif", weight = 700, clockLayout = R.layout.widget_clock_sys_serif_bold),
        WidgetFont("sys.mono", "Mono", FontCategory.LIVE, systemFamily = "monospace", weight = 400, clockLayout = R.layout.widget_clock_sys_mono),
        WidgetFont("sys.script", "Script", FontCategory.LIVE, systemFamily = "cursive", weight = 700, clockLayout = R.layout.widget_clock_sys_script),
        WidgetFont("sys.casual", "Casual", FontCategory.LIVE, systemFamily = "casual", weight = 400, clockLayout = R.layout.widget_clock_sys_casual),
    )

    /** Bundled, SIL OFL 1.1 typefaces, in curated picker order. */
    val bundled: List<WidgetFont> = listOf(
        WidgetFont("bm.figtree", "Figtree", FontCategory.SANS, R.font.bmf_figtree),
        WidgetFont("bm.figtree_light", "Figtree Light", FontCategory.SANS, R.font.bmf_figtree_light),
        WidgetFont("bm.figtree_bold", "Figtree Bold", FontCategory.SANS, R.font.bmf_figtree_bold),
        WidgetFont("bm.sora", "Sora", FontCategory.SANS, R.font.bmf_sora),
        WidgetFont("bm.space_grotesk", "Space Grotesk", FontCategory.SANS, R.font.bmf_space_grotesk),
        WidgetFont("bm.fraunces", "Fraunces", FontCategory.SERIF, R.font.bmf_fraunces),
        WidgetFont("bm.fraunces_fine", "Fraunces Fine", FontCategory.SERIF, R.font.bmf_fraunces_fine),
        WidgetFont("bm.instrument_serif", "Instrument Serif", FontCategory.SERIF, R.font.bmf_instrument_serif),
        WidgetFont("bm.dm_serif", "DM Serif", FontCategory.SERIF, R.font.bmf_dm_serif),
        WidgetFont("bm.young_serif", "Young Serif", FontCategory.SERIF, R.font.bmf_young_serif),
        WidgetFont("bm.playfair", "Playfair", FontCategory.SERIF, R.font.bmf_playfair),
        WidgetFont("bm.bricolage", "Bricolage", FontCategory.DISPLAY, R.font.bmf_bricolage),
        WidgetFont("bm.syne", "Syne", FontCategory.DISPLAY, R.font.bmf_syne),
        WidgetFont("bm.unbounded", "Unbounded", FontCategory.DISPLAY, R.font.bmf_unbounded),
        WidgetFont("bm.oswald", "Oswald", FontCategory.DISPLAY, R.font.bmf_oswald),
        WidgetFont("bm.nunito", "Nunito", FontCategory.ROUNDED, R.font.bmf_nunito),
        WidgetFont("bm.baloo", "Baloo", FontCategory.ROUNDED, R.font.bmf_baloo),
        WidgetFont("bm.jetbrains_mono", "JetBrains Mono", FontCategory.MONO, R.font.bmf_jetbrains_mono),
        WidgetFont("bm.silkscreen", "Silkscreen", FontCategory.MONO, R.font.bmf_silkscreen),
        WidgetFont("bm.caveat", "Caveat", FontCategory.HAND, R.font.bmf_caveat),
        WidgetFont("bm.pacifico", "Pacifico", FontCategory.HAND, R.font.bmf_pacifico),
    )

    val all: List<WidgetFont> = bundled + live
    private val byId = all.associateBy { it.id }

    const val DEFAULT_NAME = "bm.figtree"
    const val DEFAULT_TIME = "sys.sans_light"
    const val DEFAULT_DATE = "sys.sans"
    const val DEFAULT_TEXT = "bm.fraunces"

    /**
     * Maps every historical font identifier (Android system aliases and the hidden
     * `BM|family|style|index` form) to a current style. Aliases that correspond to
     * system families stay system styles, so legacy time text keeps its old look.
     */
    fun canonicalId(stored: String?): String {
        if (stored == null) return DEFAULT_NAME
        if (byId.containsKey(stored)) return stored
        if (stored.startsWith("BM|")) {
            val parts = stored.split('|')
            val family = parts.getOrNull(1).orEmpty()
            val bold = parts.getOrNull(2)?.toIntOrNull() == Typeface.BOLD
            return when {
                family.startsWith("sans-serif-condensed") -> if (bold) "sys.condensed_bold" else "sys.condensed"
                family.startsWith("serif") -> if (bold) "sys.serif_bold" else "sys.serif"
                family.startsWith("monospace") -> "sys.mono"
                bold -> "sys.sans_bold"
                else -> "sys.sans"
            }
        }
        return when (stored) {
            "SANS" -> "sys.sans"
            "SANS_MEDIUM" -> "sys.sans_medium"
            "SANS_BLACK" -> "sys.sans_black"
            "SANS_LIGHT" -> "sys.sans_light"
            "SANS_THIN" -> "sys.sans_thin"
            // The historical default; on most phones it resolved to bold Roboto.
            "SANS_ROUNDED" -> "sys.sans_bold"
            "SANS_CONDENSED", "CONDENSED_MEDIUM" -> "sys.condensed"
            "CONDENSED_LIGHT" -> "sys.condensed_light"
            "SERIF" -> "sys.serif"
            "SERIF_MONO", "MONO" -> "sys.mono"
            "CASUAL" -> "sys.casual"
            "CURSIVE" -> "sys.script"
            "SMALL_CAPS" -> "sys.sans_medium"
            else -> DEFAULT_NAME
        }
    }

    fun resolve(stored: String?): WidgetFont = byId.getValue(canonicalId(stored))

    /** The live style used for time and date when [stored] is a bundled font. */
    fun live(stored: String?): WidgetFont {
        val font = resolve(stored)
        if (font.isLive) return font
        val id = when (font.id) {
            "bm.figtree_light", "bm.sora" -> "sys.sans_light"
            "bm.figtree", "bm.space_grotesk" -> "sys.sans_medium"
            "bm.figtree_bold", "bm.nunito" -> "sys.sans_bold"
            "bm.syne", "bm.unbounded", "bm.baloo" -> "sys.sans_black"
            "bm.fraunces", "bm.fraunces_fine", "bm.instrument_serif", "bm.young_serif" -> "sys.serif"
            "bm.dm_serif", "bm.playfair" -> "sys.serif_bold"
            "bm.bricolage" -> "sys.condensed_bold"
            "bm.oswald" -> "sys.condensed"
            "bm.jetbrains_mono", "bm.silkscreen" -> "sys.mono"
            "bm.caveat" -> "sys.casual"
            "bm.pacifico" -> "sys.script"
            else -> DEFAULT_TIME
        }
        return byId.getValue(id)
    }

    private val typefaces = ConcurrentHashMap<String, Typeface>()

    fun typeface(context: Context, stored: String?): Typeface {
        val font = resolve(stored)
        return typefaces.getOrPut(font.id) {
            val family = font.systemFamily
            if (family != null) {
                val base = Typeface.create(family, Typeface.NORMAL)
                if (Build.VERSION.SDK_INT >= 28) Typeface.create(base, font.weight, false)
                else Typeface.create(base, if (font.weight >= 600) Typeface.BOLD else Typeface.NORMAL)
            } else {
                runCatching { ResourcesCompat.getFont(context.applicationContext, font.familyRes) }.getOrNull() ?: Typeface.DEFAULT
            }
        }
    }
}
