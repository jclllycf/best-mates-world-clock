package com.bestmates.worldclock.ui.editor

import androidx.compose.ui.text.font.FontFamily

internal data class FontChoice(val id: String, val label: String, val family: String, val style: Int = android.graphics.Typeface.NORMAL)

internal val FONT_CHOICES: List<FontChoice> = buildList {
    addAll(listOf(
        FontChoice("SANS_ROUNDED", "Rounded Sans", "sans-serif-rounded"), FontChoice("SANS", "Clean Sans", "sans-serif"),
        FontChoice("SANS_LIGHT", "Airy Light", "sans-serif-light"), FontChoice("SANS_THIN", "Silk Thin", "sans-serif-thin"),
        FontChoice("SANS_MEDIUM", "Modern Medium", "sans-serif-medium"), FontChoice("SANS_BLACK", "Bold Display", "sans-serif-black"),
        FontChoice("SANS_CONDENSED", "Narrow Sans", "sans-serif-condensed"), FontChoice("CONDENSED_LIGHT", "Narrow Light", "sans-serif-condensed-light"),
        FontChoice("CONDENSED_MEDIUM", "Narrow Medium", "sans-serif-condensed-medium"), FontChoice("SERIF", "Classic Serif", "serif"),
        FontChoice("SERIF_MONO", "Editorial Mono", "serif-monospace"), FontChoice("MONO", "Modern Mono", "monospace"),
        FontChoice("CURSIVE", "Cursive Script", "cursive"), FontChoice("CASUAL", "Cute Casual", "casual"),
        FontChoice("SMALL_CAPS", "Elegant Small Caps", "sans-serif-smallcaps")
    ))
    listOf(
        "sans-serif" to "System", "sans-serif-condensed" to "System Narrow", "serif" to "System Serif", "monospace" to "System Mono"
    ).forEachIndexed { index, (family, label) ->
        add(FontChoice("BM|$family|${android.graphics.Typeface.NORMAL}|$index", label, family))
        add(FontChoice("BM|$family|${android.graphics.Typeface.BOLD}|${index}b", "$label Bold", family, android.graphics.Typeface.BOLD))
    }
}

internal fun fontFamilyName(value: String): String = FONT_CHOICES.firstOrNull { it.id == value }?.family ?: value.split('|').getOrNull(1) ?: "sans-serif"
internal fun fontStyle(value: String): Int = FONT_CHOICES.firstOrNull { it.id == value }?.style ?: value.split('|').getOrNull(2)?.toIntOrNull() ?: android.graphics.Typeface.NORMAL
internal fun textFont(value: String): FontFamily = FontFamily(android.graphics.Typeface.create(fontFamilyName(value), fontStyle(value)))
