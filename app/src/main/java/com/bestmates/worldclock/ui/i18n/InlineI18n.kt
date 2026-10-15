package com.bestmates.worldclock.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.bestmates.worldclock.ui.settings.AppLanguage
import java.util.Locale

internal val LocalBestMatesLanguage = staticCompositionLocalOf { AppLanguage.ENGLISH }

@Composable internal fun uiText(key: String): String = translated(LocalBestMatesLanguage.current, key)
@Composable internal fun uiFormat(key: String, vararg values: Any): String = String.format(uiText(key), *values)

/** A counted phrase: `<base>_<category>` with the language's plural category for [count]. */
@Composable internal fun uiPlural(base: String, count: Int): String = pluralText(LocalBestMatesLanguage.current, base, count)

private val translationCache = java.util.concurrent.ConcurrentHashMap<String, String>()

/**
 * Every offered language has a complete table ([Strings]); a unit test fails the build when
 * a key used by the app is missing in any of them. English is only a safety net here.
 */
internal fun translated(language: AppLanguage, key: String): String =
    translationCache.getOrPut(language.stored + '|' + key) { Strings.lookup(language, key) ?: Strings.en[key] ?: key }

internal fun pluralText(language: AppLanguage, base: String, count: Int): String {
    val key = "${base}_${Plurals.category(language, count)}"
    val pattern = Strings.lookup(language, key) ?: Strings.lookup(language, "${base}_other") ?: Strings.en["${base}_other"] ?: base
    return String.format(pattern, count)
}

/** CLDR cardinal plural categories for the offered languages (integers only). */
internal object Plurals {
    fun category(language: AppLanguage, n: Int): String = when (language) {
        AppLanguage.RUSSIAN -> {
            val m10 = n % 10; val m100 = n % 100
            when {
                m10 == 1 && m100 != 11 -> "one"
                m10 in 2..4 && m100 !in 12..14 -> "few"
                else -> "many"
            }
        }
        AppLanguage.ARABIC -> {
            val m100 = n % 100
            when {
                n == 0 -> "zero"
                n == 1 -> "one"
                n == 2 -> "two"
                m100 in 3..10 -> "few"
                m100 in 11..99 -> "many"
                else -> "other"
            }
        }
        AppLanguage.FRENCH -> if (n == 0 || n == 1) "one" else "other"
        AppLanguage.CHINESE -> "other"
        AppLanguage.ENGLISH, AppLanguage.GERMAN, AppLanguage.TURKISH -> if (n == 1) "one" else "other"
    }

    /** Every category a language can produce, so the coverage test can require all of them. */
    fun categories(language: AppLanguage): List<String> = when (language) {
        AppLanguage.RUSSIAN -> listOf("one", "few", "many")
        AppLanguage.ARABIC -> listOf("zero", "one", "two", "few", "many", "other")
        AppLanguage.CHINESE -> listOf("other")
        else -> listOf("one", "other")
    }
}

internal fun AppLanguage.dateLocale(): Locale = when (this) {
    AppLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
    AppLanguage.GERMAN -> Locale.GERMAN
    AppLanguage.RUSSIAN -> Locale("ru")
    AppLanguage.TURKISH -> Locale("tr")
    AppLanguage.ARABIC -> Locale("ar")
    AppLanguage.FRENCH -> Locale.FRENCH
    AppLanguage.ENGLISH -> Locale.ENGLISH
}

/** A country's name in the app's language, from its ISO code (city data stores English names). */
internal fun localizedCountry(code: String, language: AppLanguage, fallback: String): String =
    if (code.length == 2) Locale("", code).getDisplayCountry(language.dateLocale()).ifBlank { fallback } else fallback

private val englishCountryCodes: Map<String, String> by lazy {
    Locale.getISOCountries().associateBy { Locale("", it).getDisplayCountry(Locale.ENGLISH).lowercase() }
}

/** Same as [localizedCountry] when only the English name is known. */
internal fun localizedCountryName(englishName: String, language: AppLanguage): String =
    if (language == AppLanguage.ENGLISH) englishName
    else englishCountryCodes[englishName.lowercase()]?.let { localizedCountry(it, language, englishName) } ?: englishName
