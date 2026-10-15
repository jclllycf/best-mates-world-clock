package com.bestmates.worldclock.ui.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.R

/**
 * Best Mates design language. Four complete styles, each with a light and a dark form:
 *
 * * **Daybook** — paper and ink with a marigold accent; Fraunces display. The default.
 * * **Rosewater** — blush paper, plum ink and rose; Playfair Display; the softest corners.
 * * **Nocturne** — cool mist by day, deep navy by night, periwinkle accent; Sora display.
 * * **Studio** — neutral white and graphite, near-square corners; Bricolage Grotesque display.
 *
 * A style changes the app's chrome only: widget designs keep their own colours, and
 * light/dark appearance is chosen separately.
 */
enum class BmStyle { DAYBOOK, ROSEWATER, NOCTURNE, STUDIO }

/** Kept for source compatibility with 0.4 call sites: the old accents name the styles. */
enum class BmAccent(val style: BmStyle) { MARIGOLD(BmStyle.DAYBOOK), ROSE(BmStyle.ROSEWATER), IRIS(BmStyle.NOCTURNE), GRAPHITE(BmStyle.STUDIO) }

@Immutable
data class BmColors(
    val isDark: Boolean,
    val paper: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val surfaceSunken: Color,
    val desk: Color,
    val deskDot: Color,
    val ink: Color,
    val inkSoft: Color,
    val inkFaint: Color,
    val hairline: Color,
    val hairlineStrong: Color,
    val primary: Color,
    val onPrimary: Color,
    val accent: Color,
    val onAccent: Color,
    val accentSoft: Color,
    val accentInk: Color,
    val sun: Color,
    val moon: Color,
    val danger: Color,
    val dangerSoft: Color,
    val success: Color,
    val scrim: Color,
    val shadow: Color,
    /** The brand tile behind the mark (empty state, about): a night sky in this style. */
    val markTileTop: Color,
    val markTileBottom: Color,
)

fun bmColors(dark: Boolean, accent: BmAccent): BmColors = bmColors(dark, accent.style)

fun bmColors(dark: Boolean, style: BmStyle): BmColors = when (style) {
    BmStyle.DAYBOOK -> if (dark) BmColors(
        isDark = true,
        paper = Color(0xFF131218), surface = Color(0xFF1B1A21), surfaceRaised = Color(0xFF24232B),
        surfaceSunken = Color(0xFF0E0D12), desk = Color(0xFF17161C), deskDot = Color(0xFF2C2A34),
        ink = Color(0xFFF2ECE3), inkSoft = Color(0xFFB8AFB0), inkFaint = Color(0xFF7F7780),
        hairline = Color(0xFF2B2932), hairlineStrong = Color(0xFF3D3A45),
        primary = Color(0xFFF2ECE3), onPrimary = Color(0xFF17161C),
        accent = Color(0xFFF0B45E), onAccent = Color(0xFF241705), accentSoft = Color(0xFF3A2E1E), accentInk = Color(0xFFF0B45E),
        sun = Color(0xFFF4B65A), moon = Color(0xFFAEB4EE),
        danger = Color(0xFFE5806F), dangerSoft = Color(0xFF3A2220), success = Color(0xFF8DBE98),
        scrim = Color(0xFF000000).copy(alpha = .55f), shadow = Color(0xFF000000),
        markTileTop = Color(0xFF3A3260), markTileBottom = Color(0xFF1E1B2E),
    ) else BmColors(
        isDark = false,
        paper = Color(0xFFF4EFE6), surface = Color(0xFFFBF8F2), surfaceRaised = Color(0xFFFFFDF9),
        surfaceSunken = Color(0xFFEBE4D8), desk = Color(0xFFEDE7DC), deskDot = Color(0xFFD8CEBE),
        ink = Color(0xFF211C22), inkSoft = Color(0xFF5D5459), inkFaint = Color(0xFF958B8C),
        hairline = Color(0xFFE2D9CB), hairlineStrong = Color(0xFFCDBFAD),
        primary = Color(0xFF211C22), onPrimary = Color(0xFFFBF8F2),
        accent = Color(0xFFE9A23B), onAccent = Color(0xFF2A1C08), accentSoft = Color(0xFFF8E7CC), accentInk = Color(0xFF9A5A0E),
        sun = Color(0xFFE99A2C), moon = Color(0xFF4A4E8C),
        danger = Color(0xFFB9473A), dangerSoft = Color(0xFFF5DCD5), success = Color(0xFF4D7A5B),
        scrim = Color(0xFF211C22).copy(alpha = .42f), shadow = Color(0xFF3B2A1A),
        markTileTop = Color(0xFF3A3260), markTileBottom = Color(0xFF1E1B2E),
    )
    BmStyle.ROSEWATER -> if (dark) BmColors(
        isDark = true,
        paper = Color(0xFF1A1217), surface = Color(0xFF22181E), surfaceRaised = Color(0xFF2C2027),
        surfaceSunken = Color(0xFF130C10), desk = Color(0xFF1D1419), deskDot = Color(0xFF3A2832),
        ink = Color(0xFFF7E9EC), inkSoft = Color(0xFFC9AAB5), inkFaint = Color(0xFF8C7180),
        hairline = Color(0xFF35252D), hairlineStrong = Color(0xFF4A3440),
        primary = Color(0xFFF7E9EC), onPrimary = Color(0xFF22181E),
        accent = Color(0xFFEE93A8), onAccent = Color(0xFF2E0F18), accentSoft = Color(0xFF3F2530), accentInk = Color(0xFFF4A9BA),
        sun = Color(0xFFF2A07A), moon = Color(0xFFC9AEEA),
        danger = Color(0xFFEA8676), dangerSoft = Color(0xFF3D2222), success = Color(0xFF9CC4A5),
        scrim = Color(0xFF000000).copy(alpha = .55f), shadow = Color(0xFF000000),
        markTileTop = Color(0xFF54304A), markTileBottom = Color(0xFF241422),
    ) else BmColors(
        isDark = false,
        paper = Color(0xFFF8EDEB), surface = Color(0xFFFEF7F5), surfaceRaised = Color(0xFFFFFCFB),
        surfaceSunken = Color(0xFFF1E0DD), desk = Color(0xFFF3E5E2), deskDot = Color(0xFFE3C9C5),
        ink = Color(0xFF2C1B26), inkSoft = Color(0xFF6E5664), inkFaint = Color(0xFFA48D99),
        hairline = Color(0xFFEDD9D5), hairlineStrong = Color(0xFFDDBFBA),
        primary = Color(0xFF3A2234), onPrimary = Color(0xFFFEF7F5),
        accent = Color(0xFFC95A72), onAccent = Color(0xFFFFF6F6), accentSoft = Color(0xFFF8DDE3), accentInk = Color(0xFF9C3550),
        sun = Color(0xFFE5865A), moon = Color(0xFF7E5A9E),
        danger = Color(0xFFB8463B), dangerSoft = Color(0xFFF7DDD7), success = Color(0xFF55806A),
        scrim = Color(0xFF2C1B26).copy(alpha = .42f), shadow = Color(0xFF5A2A3A),
        markTileTop = Color(0xFF6A3A5C), markTileBottom = Color(0xFF2E1A2C),
    )
    BmStyle.NOCTURNE -> if (dark) BmColors(
        isDark = true,
        paper = Color(0xFF0B1020), surface = Color(0xFF111730), surfaceRaised = Color(0xFF19203E),
        surfaceSunken = Color(0xFF070B17), desk = Color(0xFF0E1428), deskDot = Color(0xFF222B4A),
        ink = Color(0xFFE7EBFA), inkSoft = Color(0xFFA2ABC9), inkFaint = Color(0xFF6C7596),
        hairline = Color(0xFF1E2645), hairlineStrong = Color(0xFF2D3760),
        primary = Color(0xFFE7EBFA), onPrimary = Color(0xFF0E1428),
        accent = Color(0xFF8EA0FF), onAccent = Color(0xFF0B1030), accentSoft = Color(0xFF232E5C), accentInk = Color(0xFFAEBBFF),
        sun = Color(0xFFF5C26B), moon = Color(0xFFB9C4FF),
        danger = Color(0xFFF08B7E), dangerSoft = Color(0xFF3A2026), success = Color(0xFF86C9A6),
        scrim = Color(0xFF000000).copy(alpha = .6f), shadow = Color(0xFF000000),
        markTileTop = Color(0xFF26346E), markTileBottom = Color(0xFF0A0F24),
    ) else BmColors(
        isDark = false,
        paper = Color(0xFFECEFF7), surface = Color(0xFFF6F8FC), surfaceRaised = Color(0xFFFFFFFF),
        surfaceSunken = Color(0xFFE0E5F0), desk = Color(0xFFE4E8F2), deskDot = Color(0xFFC9D0E2),
        ink = Color(0xFF151A2E), inkSoft = Color(0xFF4F5873), inkFaint = Color(0xFF8A92AA),
        hairline = Color(0xFFD5DBE8), hairlineStrong = Color(0xFFBCC5DA),
        primary = Color(0xFF1A2142), onPrimary = Color(0xFFF6F8FC),
        accent = Color(0xFF4E63D8), onAccent = Color(0xFFF7F8FF), accentSoft = Color(0xFFDDE3FA), accentInk = Color(0xFF2F43B0),
        sun = Color(0xFFE39A2F), moon = Color(0xFF3F4FB0),
        danger = Color(0xFFC0463D), dangerSoft = Color(0xFFF6DEDC), success = Color(0xFF3F7C62),
        scrim = Color(0xFF151A2E).copy(alpha = .45f), shadow = Color(0xFF1A2142),
        markTileTop = Color(0xFF2A3A7A), markTileBottom = Color(0xFF111833),
    )
    BmStyle.STUDIO -> if (dark) BmColors(
        isDark = true,
        paper = Color(0xFF0E0E0E), surface = Color(0xFF161616), surfaceRaised = Color(0xFF1F1F1F),
        surfaceSunken = Color(0xFF090909), desk = Color(0xFF121212), deskDot = Color(0xFF2A2A2A),
        ink = Color(0xFFF2F1EE), inkSoft = Color(0xFFB0ADA7), inkFaint = Color(0xFF77746F),
        hairline = Color(0xFF262626), hairlineStrong = Color(0xFF3A3A3A),
        primary = Color(0xFFF2F1EE), onPrimary = Color(0xFF121212),
        accent = Color(0xFFFF7A4D), onAccent = Color(0xFF1A0A04), accentSoft = Color(0xFF2E2622), accentInk = Color(0xFFFF9670),
        sun = Color(0xFFFF9A5C), moon = Color(0xFFD8D6E6),
        danger = Color(0xFFFF8070), dangerSoft = Color(0xFF331C1A), success = Color(0xFF8CC79A),
        scrim = Color(0xFF000000).copy(alpha = .6f), shadow = Color(0xFF000000),
        markTileTop = Color(0xFF2A2A2A), markTileBottom = Color(0xFF0C0C0C),
    ) else BmColors(
        isDark = false,
        paper = Color(0xFFF3F2EF), surface = Color(0xFFFAFAF8), surfaceRaised = Color(0xFFFFFFFF),
        surfaceSunken = Color(0xFFE7E5E0), desk = Color(0xFFEAE8E3), deskDot = Color(0xFFCFCCC4),
        ink = Color(0xFF121212), inkSoft = Color(0xFF55534F), inkFaint = Color(0xFF8E8B85),
        hairline = Color(0xFFDEDBD5), hairlineStrong = Color(0xFFC4C0B8),
        primary = Color(0xFF121212), onPrimary = Color(0xFFFAFAF8),
        accent = Color(0xFFE2582E), onAccent = Color(0xFFFFFAF7), accentSoft = Color(0xFFF6DED4), accentInk = Color(0xFFB23E19),
        sun = Color(0xFFE2702F), moon = Color(0xFF3A3A46),
        danger = Color(0xFFB8402F), dangerSoft = Color(0xFFF5DCD5), success = Color(0xFF3F7A55),
        scrim = Color(0xFF121212).copy(alpha = .45f), shadow = Color(0xFF000000),
        markTileTop = Color(0xFF2B2B2B), markTileBottom = Color(0xFF111111),
    )
}
/* ---------------------------------------------------------------- Typography */

@OptIn(ExperimentalTextApi::class)
object BmFonts {
    /** Fraunces: a soft, warm optical-size serif for display and editorial moments. */
    fun display(weight: Int = 560, opsz: Float = 72f, soft: Float = 100f): FontFamily = FontFamily(
        Font(
            R.font.fraunces_vf,
            weight = FontWeight(weight),
            variationSettings = FontVariation.Settings(
                FontVariation.weight(weight),
                FontVariation.Setting("opsz", opsz),
                FontVariation.Setting("SOFT", soft),
            ),
        ),
    )

    private fun variable(res: Int, weight: Int) = FontFamily(
        Font(res, weight = FontWeight(weight), variationSettings = FontVariation.Settings(FontVariation.weight(weight))),
    )

    private fun figtree(weight: Int) = Font(
        R.font.figtree_vf,
        weight = FontWeight(weight),
        variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
    )

    /** Figtree: a clear, friendly grotesque for everything functional, in every style. */
    val text: FontFamily = FontFamily(figtree(400), figtree(500), figtree(600), figtree(700))
    val displayLarge: FontFamily = display(560, 144f, 100f)
    val displayMedium: FontFamily = display(560, 72f, 100f)
    val displaySmall: FontFamily = display(600, 36f, 60f)

    /** Display faces per style: large (wordmark, headlines) and small (titles). */
    fun displayFor(style: BmStyle): Pair<FontFamily, FontFamily> = when (style) {
        BmStyle.DAYBOOK -> displayLarge to displaySmall
        BmStyle.ROSEWATER -> variable(R.font.playfair_vf, 600) to variable(R.font.playfair_vf, 640)
        BmStyle.NOCTURNE -> variable(R.font.sora_vf, 560) to variable(R.font.sora_vf, 600)
        BmStyle.STUDIO -> variable(R.font.bricolage_vf, 720) to variable(R.font.bricolage_vf, 680)
    }
}

@Immutable
data class BmTypography(
    val hero: TextStyle,
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val titleSans: TextStyle,
    val body: TextStyle,
    val bodyStrong: TextStyle,
    val bodySmall: TextStyle,
    val label: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle,
    val numeric: TextStyle,
)

private fun bmTypography(style: BmStyle): BmTypography {
    val text = BmFonts.text
    val (large, small) = BmFonts.displayFor(style)
    // Geometric and grotesque faces read larger than Fraunces; tracking keeps headlines tight.
    val (scale, tracking) = when (style) {
        BmStyle.DAYBOOK -> 1f to -0.015f
        BmStyle.ROSEWATER -> 1f to -0.01f
        BmStyle.NOCTURNE -> .92f to -0.03f
        BmStyle.STUDIO -> .94f to -0.035f
    }
    fun sp(v: Float) = (v * scale).sp
    return BmTypography(
        hero = TextStyle(fontFamily = large, fontSize = sp(40f), lineHeight = sp(44f), letterSpacing = (tracking - .005f).em),
        display = TextStyle(fontFamily = large, fontSize = sp(32f), lineHeight = sp(36f), letterSpacing = tracking.em),
        headline = TextStyle(fontFamily = large, fontSize = sp(24f), lineHeight = sp(28f), letterSpacing = (tracking + .005f).em),
        title = TextStyle(fontFamily = small, fontSize = sp(19f), lineHeight = sp(24f)),
        titleSans = TextStyle(fontFamily = text, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp),
        body = TextStyle(fontFamily = text, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 21.sp),
        bodyStrong = TextStyle(fontFamily = text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 21.sp),
        bodySmall = TextStyle(fontFamily = text, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
        label = TextStyle(fontFamily = text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 18.sp),
        caption = TextStyle(fontFamily = text, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
        overline = TextStyle(fontFamily = text, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 14.sp, letterSpacing = 0.09.em),
        numeric = TextStyle(fontFamily = text, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 16.sp, fontFeatureSettings = "tnum"),
    )
}

/* ---------------------------------------------------------------- Shape and space */

@Immutable
data class BmShapes(
    val control: RoundedCornerShape = RoundedCornerShape(14.dp),
    val card: RoundedCornerShape = RoundedCornerShape(24.dp),
    val sheet: RoundedCornerShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    val tile: RoundedCornerShape = RoundedCornerShape(18.dp),
    val pill: RoundedCornerShape = RoundedCornerShape(percent = 50),
    /** Buttons, chips and segmented controls. */
    val button: RoundedCornerShape = RoundedCornerShape(percent = 50),
    val dialog: RoundedCornerShape = RoundedCornerShape(28.dp),
)

private fun bmShapes(style: BmStyle): BmShapes = when (style) {
    BmStyle.DAYBOOK -> BmShapes()
    BmStyle.ROSEWATER -> BmShapes(RoundedCornerShape(18.dp), RoundedCornerShape(30.dp), RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp), RoundedCornerShape(24.dp), dialog = RoundedCornerShape(32.dp))
    BmStyle.NOCTURNE -> BmShapes(RoundedCornerShape(12.dp), RoundedCornerShape(20.dp), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), RoundedCornerShape(16.dp), button = RoundedCornerShape(14.dp), dialog = RoundedCornerShape(22.dp))
    BmStyle.STUDIO -> BmShapes(RoundedCornerShape(8.dp), RoundedCornerShape(12.dp), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), RoundedCornerShape(10.dp), button = RoundedCornerShape(8.dp), dialog = RoundedCornerShape(14.dp))
}

object BmSpace {
    val xxs: Dp = 4.dp
    val xs: Dp = 8.dp
    val sm: Dp = 12.dp
    val md: Dp = 16.dp
    val lg: Dp = 20.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val gutter: Dp = 20.dp
    val touch: Dp = 48.dp
}

@Immutable
data class BmTokens(val style: BmStyle, val colors: BmColors, val type: BmTypography, val shapes: BmShapes)

fun bmTokens(dark: Boolean, style: BmStyle) = BmTokens(style, bmColors(dark, style), bmTypography(style), bmShapes(style))

private val LocalBm = staticCompositionLocalOf { bmTokens(false, BmStyle.DAYBOOK) }

object Bm {
    val style: BmStyle @Composable @ReadOnlyComposable get() = LocalBm.current.style
    val colors: BmColors @Composable @ReadOnlyComposable get() = LocalBm.current.colors
    val type: BmTypography @Composable @ReadOnlyComposable get() = LocalBm.current.type
    val shapes: BmShapes @Composable @ReadOnlyComposable get() = LocalBm.current.shapes
}

@Composable
fun BmTheme(dark: Boolean, style: BmStyle, content: @Composable () -> Unit) {
    val tokens = remember(dark, style) { bmTokens(dark, style) }
    BmProvide(tokens, content)
}

/** Provides an exact token set; Settings uses it to preview other styles in place. */
@Composable
fun BmProvide(tokens: BmTokens, content: @Composable () -> Unit) {
    val c = tokens.colors
    val scheme = if (c.isDark) darkColorScheme(
        primary = c.primary, onPrimary = c.onPrimary, secondary = c.accent, onSecondary = c.onAccent,
        background = c.paper, onBackground = c.ink, surface = c.surface, onSurface = c.ink,
        surfaceVariant = c.surfaceSunken, onSurfaceVariant = c.inkSoft, outline = c.hairlineStrong,
        outlineVariant = c.hairline, error = c.danger, surfaceContainer = c.surfaceRaised,
        surfaceContainerHigh = c.surfaceRaised, surfaceContainerLow = c.surface,
    ) else lightColorScheme(
        primary = c.primary, onPrimary = c.onPrimary, secondary = c.accent, onSecondary = c.onAccent,
        background = c.paper, onBackground = c.ink, surface = c.surface, onSurface = c.ink,
        surfaceVariant = c.surfaceSunken, onSurfaceVariant = c.inkSoft, outline = c.hairlineStrong,
        outlineVariant = c.hairline, error = c.danger, surfaceContainer = c.surfaceRaised,
        surfaceContainerHigh = c.surfaceRaised, surfaceContainerLow = c.surface,
    )
    val t = tokens.type
    val material = Typography(
        displayLarge = t.hero, displayMedium = t.display, displaySmall = t.headline,
        headlineLarge = t.display, headlineMedium = t.headline, headlineSmall = t.title,
        titleLarge = t.title, titleMedium = t.titleSans, titleSmall = t.label,
        bodyLarge = t.body, bodyMedium = t.body, bodySmall = t.bodySmall,
        labelLarge = t.label, labelMedium = t.caption, labelSmall = t.caption,
    )
    CompositionLocalProvider(LocalBm provides tokens) {
        MaterialTheme(colorScheme = scheme, typography = material, content = content)
    }
}