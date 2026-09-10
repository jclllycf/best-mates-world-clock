package com.bestmates.worldclock.ui.settings

import com.bestmates.worldclock.ui.theme.BestMatesBrightnessMode
import com.bestmates.worldclock.ui.theme.BestMatesVisualIdentity

/** Values intentionally keep the names used by the existing app_settings store. */
enum class AppThemeStyle(
    val label: String,
    val identity: BestMatesVisualIdentity,
) {
    CLOUD("Cloud", BestMatesVisualIdentity.CLOUD),
    DREAM("Dream", BestMatesVisualIdentity.DREAM),
    MIDNIGHT("Midnight", BestMatesVisualIdentity.MIDNIGHT),
    MONO("Mono", BestMatesVisualIdentity.MONO),
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

enum class AppAppearanceMode(val stored: String, val brightness: BestMatesBrightnessMode) {
    FOLLOW_SYSTEM("SYSTEM", BestMatesBrightnessMode.SYSTEM),
    LIGHT("LIGHT", BestMatesBrightnessMode.LIGHT),
    DARK("DARK", BestMatesBrightnessMode.DARK);

    companion object {
        fun from(value: String?): AppAppearanceMode? = entries.firstOrNull { it.stored == value }
    }
}

/** Missing appearance data keeps the exact pre-v0.3 identity-dependent behavior. */
fun legacyAppearanceFor(theme: AppThemeStyle): AppAppearanceMode =
    if (theme == AppThemeStyle.MIDNIGHT) AppAppearanceMode.DARK else AppAppearanceMode.LIGHT

