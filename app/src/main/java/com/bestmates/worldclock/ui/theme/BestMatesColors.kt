package com.bestmates.worldclock.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

enum class BestMatesVisualIdentity {
    CLOUD,
    DREAM,
    MIDNIGHT,
    MONO,
}

enum class BestMatesBrightnessMode {
    SYSTEM,
    LIGHT,
    DARK,
}

enum class BestMatesResolvedBrightness {
    LIGHT,
    DARK,
}

@Immutable
data class BestMatesColors(
    val background: Color,
    val surfaceBase: Color,
    val surfaceRaised: Color,
    val surfaceInset: Color,
    val surfaceOverlay: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val borderSubtle: Color,
    val borderControl: Color,
    val accent: Color,
    val onAccent: Color,
    val accentContainer: Color,
    val onAccentContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val destructive: Color,
    val onDestructive: Color,
    val destructiveContainer: Color,
    val onDestructiveContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val shadowColor: Color,
    val canvasMat: Color,
    val onCanvasMat: Color,
    val checkerLight: Color,
    val checkerDark: Color,
    val atmosphereA: Color,
    val atmosphereB: Color,
    val selectionOutline: Color,
    val selectionFill: Color,
    val selectionContent: Color,
    val focusRing: Color,
    val guide: Color,
    val isDark: Boolean,
)

fun resolveBestMatesBrightness(
    mode: BestMatesBrightnessMode,
    systemIsDark: Boolean,
): BestMatesResolvedBrightness = when (mode) {
    BestMatesBrightnessMode.SYSTEM -> if (systemIsDark) {
        BestMatesResolvedBrightness.DARK
    } else {
        BestMatesResolvedBrightness.LIGHT
    }

    BestMatesBrightnessMode.LIGHT -> BestMatesResolvedBrightness.LIGHT
    BestMatesBrightnessMode.DARK -> BestMatesResolvedBrightness.DARK
}

fun resolveBestMatesColors(
    identity: BestMatesVisualIdentity,
    isDark: Boolean,
): BestMatesColors {
    val neutral = when (identity) {
        BestMatesVisualIdentity.CLOUD -> if (isDark) CloudDarkNeutralReferences else CloudLightNeutralReferences
        BestMatesVisualIdentity.DREAM -> if (isDark) DreamDarkNeutralReferences else DreamLightNeutralReferences
        BestMatesVisualIdentity.MIDNIGHT -> if (isDark) MidnightDarkNeutralReferences else MidnightLightNeutralReferences
        BestMatesVisualIdentity.MONO -> if (isDark) MonoDarkNeutralReferences else MonoLightNeutralReferences
    }
    val supporting = if (isDark) {
        CuteAtelierDarkSupportingReferences
    } else {
        CuteAtelierLightSupportingReferences
    }
    val identityReferences = when (identity) {
        BestMatesVisualIdentity.CLOUD -> if (isDark) {
            CloudDarkIdentityReferences
        } else {
            CloudLightIdentityReferences
        }

        BestMatesVisualIdentity.DREAM -> if (isDark) {
            DreamDarkIdentityReferences
        } else {
            DreamLightIdentityReferences
        }

        BestMatesVisualIdentity.MIDNIGHT -> if (isDark) {
            MidnightDarkIdentityReferences
        } else {
            MidnightLightIdentityReferences
        }

        BestMatesVisualIdentity.MONO -> if (isDark) {
            MonoDarkIdentityReferences
        } else {
            MonoLightIdentityReferences
        }
    }

    return BestMatesColors(
        background = neutral.background,
        surfaceBase = neutral.surfaceBase,
        surfaceRaised = neutral.surfaceRaised,
        surfaceInset = neutral.surfaceInset,
        surfaceOverlay = neutral.surfaceOverlay,
        surfaceElevated = neutral.surfaceElevated,
        textPrimary = neutral.textPrimary,
        textSecondary = neutral.textSecondary,
        textMuted = neutral.textMuted,
        borderSubtle = neutral.borderSubtle,
        borderControl = neutral.borderControl,
        accent = identityReferences.accent,
        onAccent = identityReferences.onAccent,
        accentContainer = identityReferences.accentContainer,
        onAccentContainer = identityReferences.onAccentContainer,
        secondary = supporting.secondary,
        onSecondary = supporting.onSecondary,
        secondaryContainer = supporting.secondaryContainer,
        onSecondaryContainer = supporting.onSecondaryContainer,
        tertiary = supporting.tertiary,
        onTertiary = supporting.onTertiary,
        tertiaryContainer = supporting.tertiaryContainer,
        onTertiaryContainer = supporting.onTertiaryContainer,
        destructive = supporting.destructive,
        onDestructive = supporting.onDestructive,
        destructiveContainer = supporting.destructiveContainer,
        onDestructiveContainer = supporting.onDestructiveContainer,
        success = supporting.success,
        onSuccess = supporting.onSuccess,
        successContainer = supporting.successContainer,
        onSuccessContainer = supporting.onSuccessContainer,
        warning = supporting.warning,
        onWarning = supporting.onWarning,
        warningContainer = supporting.warningContainer,
        onWarningContainer = supporting.onWarningContainer,
        shadowColor = neutral.shadowColor,
        canvasMat = neutral.canvasMat,
        onCanvasMat = neutral.onCanvasMat,
        checkerLight = neutral.checkerLight,
        checkerDark = neutral.checkerDark,
        atmosphereA = identityReferences.atmosphereA,
        atmosphereB = identityReferences.atmosphereB,
        selectionOutline = identityReferences.accent,
        selectionFill = identityReferences.accentContainer,
        selectionContent = identityReferences.onAccentContainer,
        focusRing = identityReferences.accent,
        guide = identityReferences.accent,
        isDark = isDark,
    )
}

/**
 * Explicit Material 3 bridge for material3 1.3.1.
 *
 * All 36 roles used here exist in the repository's current BOM. No stock Material
 * palette defaults are allowed to leak into a resolved Best Mates theme.
 */
fun BestMatesColors.toMaterialColorScheme(
    inverseColors: BestMatesColors,
): ColorScheme {
    return ColorScheme(
        primary = accent,
        onPrimary = onAccent,
        primaryContainer = accentContainer,
        onPrimaryContainer = onAccentContainer,
        inversePrimary = inverseColors.accent,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = textPrimary,
        surface = surfaceBase,
        onSurface = textPrimary,
        surfaceVariant = surfaceInset,
        onSurfaceVariant = textSecondary,
        surfaceTint = accent,
        inverseSurface = inverseColors.surfaceRaised,
        inverseOnSurface = inverseColors.textPrimary,
        error = destructive,
        onError = onDestructive,
        errorContainer = destructiveContainer,
        onErrorContainer = onDestructiveContainer,
        outline = borderControl,
        outlineVariant = borderSubtle,
        scrim = Color.Black,
        surfaceBright = if (isDark) surfaceElevated else surfaceRaised,
        surfaceDim = surfaceInset,
        surfaceContainer = if (isDark) surfaceRaised else surfaceInset,
        surfaceContainerHigh = surfaceOverlay,
        surfaceContainerHighest = surfaceElevated,
        surfaceContainerLow = surfaceBase,
        surfaceContainerLowest = if (isDark) surfaceInset else surfaceRaised,
    )
}
