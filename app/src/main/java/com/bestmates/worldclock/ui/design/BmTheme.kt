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
 * Best Mates "Daybook" design language.
 *
 * Paper and ink with one warm accent. Day (sun) and night (moon) are the only
 * decorative motifs and they always carry meaning. Surfaces are flat paper separated
 * by hairlines; depth is reserved for things that float (dock, sheets, dragged objects).
 */
enum class BmAccent { MARIGOLD, ROSE, IRIS, GRAPHITE }

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
)

private data class AccentSet(val accent: Color, val onAccent: Color, val soft: Color, val ink: Color)

private fun accentSet(accent: BmAccent, dark: Boolean): AccentSet = when (accent) {
    BmAccent.MARIGOLD -> if (dark) AccentSet(Color(0xFFF0B45E), Color(0xFF241705), Color(0xFF3A2E1E), Color(0xFFF0B45E))
    else AccentSet(Color(0xFFE9A23B), Color(0xFF2A1C08), Color(0xFFF8E7CC), Color(0xFF9A5A0E))
    BmAccent.ROSE -> if (dark) AccentSet(Color(0xFFE8939A), Color(0xFF2B1013), Color(0xFF3A2226), Color(0xFFE8939A))
    else AccentSet(Color(0xFFD06A6E), Color(0xFFFFF7F5), Color(0xFFF6DEDC), Color(0xFFA3474D))
    BmAccent.IRIS -> if (dark) AccentSet(Color(0xFFA99CF0), Color(0xFF17123A), Color(0xFF2A2640), Color(0xFFB9AEF4))
    else AccentSet(Color(0xFF7B6BD0), Color(0xFFFBFAFF), Color(0xFFE8E4F8), Color(0xFF5A4BB0))
    BmAccent.GRAPHITE -> if (dark) AccentSet(Color(0xFFD9D3CC), Color(0xFF17161C), Color(0xFF2C2A32), Color(0xFFE3DDD5))
    else AccentSet(Color(0xFF3A3640), Color(0xFFFBF8F2), Color(0xFFE6E1DA), Color(0xFF3A3640))
}

fun bmColors(dark: Boolean, accent: BmAccent): BmColors {
    val a = accentSet(accent, dark)
    return if (dark) BmColors(
        isDark = true,
        paper = Color(0xFF131218), surface = Color(0xFF1B1A21), surfaceRaised = Color(0xFF24232B),
        surfaceSunken = Color(0xFF0E0D12), desk = Color(0xFF17161C), deskDot = Color(0xFF2C2A34),
        ink = Color(0xFFF2ECE3), inkSoft = Color(0xFFB8AFB0), inkFaint = Color(0xFF7F7780),
        hairline = Color(0xFF2B2932), hairlineStrong = Color(0xFF3D3A45),
        primary = Color(0xFFF2ECE3), onPrimary = Color(0xFF17161C),
        accent = a.accent, onAccent = a.onAccent, accentSoft = a.soft, accentInk = a.ink,
        sun = Color(0xFFF4B65A), moon = Color(0xFFAEB4EE),
        danger = Color(0xFFE5806F), dangerSoft = Color(0xFF3A2220), success = Color(0xFF8DBE98),
        scrim = Color(0xFF000000).copy(alpha = .55f), shadow = Color(0xFF000000),
    ) else BmColors(
        isDark = false,
        paper = Color(0xFFF4EFE6), surface = Color(0xFFFBF8F2), surfaceRaised = Color(0xFFFFFDF9),
        surfaceSunken = Color(0xFFEBE4D8), desk = Color(0xFFEDE7DC), deskDot = Color(0xFFD8CEBE),
        ink = Color(0xFF211C22), inkSoft = Color(0xFF5D5459), inkFaint = Color(0xFF958B8C),
        hairline = Color(0xFFE2D9CB), hairlineStrong = Color(0xFFCDBFAD),
        primary = Color(0xFF211C22), onPrimary = Color(0xFFFBF8F2),
        accent = a.accent, onAccent = a.onAccent, accentSoft = a.soft, accentInk = a.ink,
        sun = Color(0xFFE99A2C), moon = Color(0xFF4A4E8C),
        danger = Color(0xFFB9473A), dangerSoft = Color(0xFFF5DCD5), success = Color(0xFF4D7A5B),
        scrim = Color(0xFF211C22).copy(alpha = .42f), shadow = Color(0xFF3B2A1A),
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

    private fun figtree(weight: Int) = Font(
        R.font.figtree_vf,
        weight = FontWeight(weight),
        variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
    )

    /** Figtree: a clear, friendly grotesque for everything functional. */
    val text: FontFamily = FontFamily(figtree(400), figtree(500), figtree(600), figtree(700))
    val displayLarge: FontFamily = display(560, 144f, 100f)
    val displayMedium: FontFamily = display(560, 72f, 100f)
    val displaySmall: FontFamily = display(600, 36f, 60f)
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

private fun bmTypography(): BmTypography {
    val text = BmFonts.text
    return BmTypography(
        hero = TextStyle(fontFamily = BmFonts.displayLarge, fontWeight = FontWeight(560), fontSize = 40.sp, lineHeight = 44.sp, letterSpacing = (-0.02).em),
        display = TextStyle(fontFamily = BmFonts.displayLarge, fontWeight = FontWeight(560), fontSize = 32.sp, lineHeight = 36.sp, letterSpacing = (-0.015).em),
        headline = TextStyle(fontFamily = BmFonts.displayMedium, fontWeight = FontWeight(560), fontSize = 24.sp, lineHeight = 28.sp, letterSpacing = (-0.01).em),
        title = TextStyle(fontFamily = BmFonts.displaySmall, fontWeight = FontWeight(600), fontSize = 19.sp, lineHeight = 24.sp),
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
)

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
data class BmTokens(val colors: BmColors, val type: BmTypography, val shapes: BmShapes)

private val LocalBm = staticCompositionLocalOf { BmTokens(bmColors(false, BmAccent.MARIGOLD), bmTypography(), BmShapes()) }

object Bm {
    val colors: BmColors @Composable @ReadOnlyComposable get() = LocalBm.current.colors
    val type: BmTypography @Composable @ReadOnlyComposable get() = LocalBm.current.type
    val shapes: BmShapes @Composable @ReadOnlyComposable get() = LocalBm.current.shapes
}

@Composable
fun BmTheme(dark: Boolean, accent: BmAccent, content: @Composable () -> Unit) {
    val tokens = remember(dark, accent) { BmTokens(bmColors(dark, accent), bmTypography(), BmShapes()) }
    val c = tokens.colors
    val scheme = if (dark) darkColorScheme(
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
