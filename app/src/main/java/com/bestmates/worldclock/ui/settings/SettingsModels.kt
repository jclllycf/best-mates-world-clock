package com.bestmates.worldclock.ui.settings

import com.bestmates.worldclock.ui.design.BmAccent

/**
 * Stored names are the historical `app_settings/app_theme` values. Since 0.4 they select
 * the accent of the single Best Mates palette instead of four separate identities, so
 * every existing preference still resolves.
 */
enum class AppThemeStyle(val accent: BmAccent) {
    CLOUD(BmAccent.MARIGOLD),
    DREAM(BmAccent.ROSE),
    MIDNIGHT(BmAccent.IRIS),
    MONO(BmAccent.GRAPHITE);

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
