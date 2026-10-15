package com.bestmates.worldclock.ui.settings

import com.bestmates.worldclock.ui.design.BmStyle

/**
 * Stored names are the historical `app_settings/app_theme` values, so every existing
 * preference still resolves. Since 0.5 each selects a complete style (palette, display
 * typeface and corner geometry) instead of only an accent; each style keeps the accent
 * family its name had in 0.4, so nobody lands somewhere unexpected.
 */
enum class AppThemeStyle(val style: BmStyle, val key: String) {
    CLOUD(BmStyle.DAYBOOK, "daybook"),
    DREAM(BmStyle.ROSEWATER, "rosewater"),
    MIDNIGHT(BmStyle.NOCTURNE, "nocturne"),
    MONO(BmStyle.STUDIO, "studio");

    companion object {
        fun from(value: String?) = entries.firstOrNull { it.name == value } ?: CLOUD
    }
}

/** Native language names remain stable because they are used inside the language picker. */
enum class AppLanguage(val stored: String, val menuLabel: String) {
    ENGLISH("en", "English"),
    CHINESE("zh", "中文"),
    GERMAN("de", "Deutsch"),
    RUSSIAN("ru", "Русский"),
    TURKISH("tr", "Türkçe"),
    ARABIC("ar", "العربية"),
    FRENCH("fr", "Français");

    companion object {
        fun from(value: String?) = entries.firstOrNull { it.stored == value } ?: ENGLISH
    }
}

enum class AppAppearanceMode(val stored: String) {
    FOLLOW_SYSTEM("SYSTEM"),
    LIGHT("LIGHT"),
    DARK("DARK");

    companion object {
        fun from(value: String?): AppAppearanceMode? = entries.firstOrNull { it.stored == value }
    }
}

/** Missing appearance data keeps the exact pre-v0.3 identity-dependent behaviour. */
fun legacyAppearanceFor(theme: AppThemeStyle): AppAppearanceMode =
    if (theme == AppThemeStyle.MIDNIGHT) AppAppearanceMode.DARK else AppAppearanceMode.LIGHT
