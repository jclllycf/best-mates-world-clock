package com.bestmates.worldclock.ui.i18n

import com.bestmates.worldclock.fonts.FontCategory
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.ui.settings.AppLanguage
import com.bestmates.worldclock.ui.settings.AppThemeStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * BM-REAL-10 build gate: every language offered in Settings is complete.
 *
 * Keys are collected from the source itself (uiText / uiFormat / uiPlural / translated
 * calls) plus the families built at run time (shapes, palettes, backdrops…), so a new
 * string cannot ship in English only.
 */
class StringCoverageTest {
    private val sourceRoot = listOf(File("src/main/java"), File("app/src/main/java")).first { it.isDirectory }
    private val sources = sourceRoot.walkTopDown().filter { it.extension == "kt" }.map { it.readText() }.toList()

    private val literalKeys: Set<String> = sources.flatMap { text ->
        Regex("""\b(uiText|uiFormat|translated\(\s*\w+\s*,)\s*\(?\s*"([a-z0-9_]+)"(?!\s*\+)""").findAll(text).map { it.groupValues[2] }.toList()
    }.toSet()

    private val pluralBases: Set<String> = sources.flatMap { text ->
        Regex("""\buiPlural\(\s*"([a-z0-9_]+)"""").findAll(text).map { it.groupValues[1] }.toList()
    }.toSet()

    /** Families whose keys are assembled at run time. Keep in step with their call sites. */
    private val dynamicKeys: Set<String> = buildSet {
        WidgetShape.entries.forEach { add("shape_" + it.name.lowercase()) }
        FontCategory.entries.forEach { add("font_cat_" + it.name.lowercase()) }
        AppThemeStyle.entries.forEach { add("style_" + it.key) }
        listOf("wallpaper", "dusk", "night", "meadow", "paper", "ink").forEach { add("backdrop_$it") }
        listOf("mist", "frost", "paper", "marigold", "apricot", "rose", "lilac", "dusk", "sage", "ink", "lavender").forEach { add("palette_$it") }
        listOf("as", "eu", "af", "na", "sa", "oc", "an").forEach { add("continent_$it") }
    }

    @Test
    fun sourceScan_findsTheKeys() {
        assertTrue("scan found too few keys: ${literalKeys.size}", literalKeys.size > 100)
        assertTrue(pluralBases.containsAll(listOf("cities_count", "selected_count", "delete_projects_message")))
    }

    @Test
    fun everyUsedKey_existsInEveryLanguage() {
        val missing = AppLanguage.entries.flatMap { language ->
            (literalKeys + dynamicKeys).filter { Strings.lookup(language, it) == null }.map { "${language.stored}: $it" }
        }
        assertEquals("Missing translations:\n" + missing.joinToString("\n"), emptyList<String>(), missing)
    }

    @Test
    fun everyPlural_hasEveryCategoryTheLanguageUses() {
        val missing = AppLanguage.entries.flatMap { language ->
            pluralBases.flatMap { base ->
                Plurals.categories(language).filter { Strings.lookup(language, "${base}_$it") == null }.map { "${language.stored}: ${base}_$it" }
            }
        }
        assertEquals("Missing plural forms:\n" + missing.joinToString("\n"), emptyList<String>(), missing)
    }

    @Test
    fun everyEnglishKey_isTranslatedEverywhere() {
        val pluralKeys = Strings.en.keys.filter { key -> pluralBases.any { key.startsWith(it + "_") } }.toSet()
        val missing = AppLanguage.entries.flatMap { language ->
            (Strings.en.keys - pluralKeys).filter { Strings.lookup(language, it) == null }.map { "${language.stored}: $it" }
        }
        assertEquals("Untranslated English keys:\n" + missing.joinToString("\n"), emptyList<String>(), missing)
    }

    @Test
    fun placeholders_matchEnglish() {
        fun specs(s: String) = Regex("""%(\d+\$)?[ds]""").findAll(s).map { it.value }.sorted().toList()
        val problems = AppLanguage.entries.flatMap { language ->
            Strings.table(language).mapNotNull { (key, value) ->
                val base = pluralBases.firstOrNull { key.startsWith(it + "_") }
                val english = if (base != null) Strings.en["${base}_other"] else Strings.en[key]
                when {
                    english == null -> "${language.stored}: $key is not an English key"
                    // A plural form may drop the number ("one city"), never invent another.
                    base != null -> if (!specs(english).containsAll(specs(value))) "${language.stored}: $key placeholders ${specs(value)}" else null
                    specs(english) != specs(value) -> "${language.stored}: $key placeholders ${specs(value)} vs ${specs(english)}"
                    else -> null
                }
            }
        }
        assertEquals("Placeholder problems:\n" + problems.joinToString("\n"), emptyList<String>(), problems)
    }

    @Test
    fun noHardCodedEnglishInComposables() {
        // Literal UI text outside the string tables. Brand name, sample glyphs and numbers are allowed.
        val allowed = setOf("Best Mates", "Aa", "AndroidSVG", "Apache License 2.0")
        val offenders = sourceRoot.walkTopDown().filter { it.extension == "kt" && "/ui/" in it.invariantSeparatorsPath && "/i18n/" !in it.invariantSeparatorsPath }
            .flatMap { file ->
                Regex("""(?<![A-Za-z])Text\(\s*"([^"$]*)"|contentDescription\s*=\s*"([^"]*)"""").findAll(file.readText())
                    .map { m -> m.groupValues[1].ifEmpty { m.groupValues[2] } }
                    .filter { it.any(Char::isLetter) && it !in allowed }
                    .map { "${file.name}: \"$it\"" }
            }.toList()
        assertEquals("Hard-coded UI text:\n" + offenders.joinToString("\n"), emptyList<String>(), offenders)
    }

    @Test
    fun pluralRules_followCldr() {
        assertEquals("one", Plurals.category(AppLanguage.RUSSIAN, 21))
        assertEquals("few", Plurals.category(AppLanguage.RUSSIAN, 3))
        assertEquals("many", Plurals.category(AppLanguage.RUSSIAN, 11))
        assertEquals("many", Plurals.category(AppLanguage.RUSSIAN, 25))
        assertEquals("two", Plurals.category(AppLanguage.ARABIC, 2))
        assertEquals("few", Plurals.category(AppLanguage.ARABIC, 7))
        assertEquals("many", Plurals.category(AppLanguage.ARABIC, 15))
        assertEquals("other", Plurals.category(AppLanguage.ARABIC, 100))
        assertEquals("one", Plurals.category(AppLanguage.FRENCH, 0))
        assertEquals("%d cities".format(3), pluralText(AppLanguage.ENGLISH, "cities_count", 3))
        assertEquals("21 город", pluralText(AppLanguage.RUSSIAN, "cities_count", 21))
        assertEquals("5 городов", pluralText(AppLanguage.RUSSIAN, "cities_count", 5))
    }
}
