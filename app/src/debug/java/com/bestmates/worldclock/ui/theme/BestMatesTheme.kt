package com.bestmates.worldclock.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTonalElevationEnabled
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.bestmates.worldclock.ui.icons.DefaultOrbitIconMetrics
import com.bestmates.worldclock.ui.icons.OrbitIconMetrics

@Immutable
data class BestMatesDesignTokens(
    val identity: BestMatesVisualIdentity,
    val brightness: BestMatesResolvedBrightness,
    val colors: BestMatesColors,
    val materialColorScheme: ColorScheme,
    val typography: BestMatesTypographyTokens,
    val dimensions: BestMatesDimensionTokens,
    val elevation: BestMatesElevationTokens,
    val states: BestMatesStateTokens,
    val motion: BestMatesMotionTokens,
    val icons: OrbitIconMetrics,
)

fun resolveBestMatesDesign(
    identity: BestMatesVisualIdentity,
    brightnessMode: BestMatesBrightnessMode,
    systemIsDark: Boolean,
    reduceMotion: Boolean = false,
    systemAnimationsEnabled: Boolean = true,
): BestMatesDesignTokens {
    val brightness = resolveBestMatesBrightness(brightnessMode, systemIsDark)
    val isDark = brightness == BestMatesResolvedBrightness.DARK
    val colors = resolveBestMatesColors(identity, isDark)
    val inverseColors = resolveBestMatesColors(identity, !isDark)

    return BestMatesDesignTokens(
        identity = identity,
        brightness = brightness,
        colors = colors,
        materialColorScheme = colors.toMaterialColorScheme(inverseColors),
        typography = DefaultBestMatesTypography,
        dimensions = DefaultBestMatesDimensions,
        elevation = resolveBestMatesElevation(isDark),
        states = resolveBestMatesStates(colors),
        motion = resolveBestMatesMotion(reduceMotion, systemAnimationsEnabled),
        icons = DefaultOrbitIconMetrics,
    )
}

private val DefaultBestMatesDesign = resolveBestMatesDesign(
    identity = BestMatesVisualIdentity.CLOUD,
    brightnessMode = BestMatesBrightnessMode.LIGHT,
    systemIsDark = false,
)

private val LocalBestMatesDesign = staticCompositionLocalOf { DefaultBestMatesDesign }

/** Read-only access to the active opt-in Best Mates 2.0 design foundation. */
object BestMatesDesign {
    val current: BestMatesDesignTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalBestMatesDesign.current

    val colors: BestMatesColors
        @Composable
        @ReadOnlyComposable
        get() = current.colors

    val typography: BestMatesTypographyTokens
        @Composable
        @ReadOnlyComposable
        get() = current.typography

    val dimensions: BestMatesDimensionTokens
        @Composable
        @ReadOnlyComposable
        get() = current.dimensions

    val elevation: BestMatesElevationTokens
        @Composable
        @ReadOnlyComposable
        get() = current.elevation

    val states: BestMatesStateTokens
        @Composable
        @ReadOnlyComposable
        get() = current.states

    val motion: BestMatesMotionTokens
        @Composable
        @ReadOnlyComposable
        get() = current.motion

    val icons: OrbitIconMetrics
        @Composable
        @ReadOnlyComposable
        get() = current.icons
}

/**
 * Opt-in Phase 2A host. Production screens intentionally do not call this yet.
 * System brightness and animation availability are supplied by the caller so the
 * resolver remains deterministic and independent from Android settings or stores.
 */
@Composable
fun BestMatesDesignTheme(
    systemIsDark: Boolean,
    identity: BestMatesVisualIdentity = BestMatesVisualIdentity.CLOUD,
    brightnessMode: BestMatesBrightnessMode = BestMatesBrightnessMode.SYSTEM,
    reduceMotion: Boolean = false,
    systemAnimationsEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val design = remember(
        identity,
        brightnessMode,
        systemIsDark,
        reduceMotion,
        systemAnimationsEnabled,
    ) {
        resolveBestMatesDesign(
            identity = identity,
            brightnessMode = brightnessMode,
            systemIsDark = systemIsDark,
            reduceMotion = reduceMotion,
            systemAnimationsEnabled = systemAnimationsEnabled,
        )
    }

    CompositionLocalProvider(
        LocalBestMatesDesign provides design,
        LocalTonalElevationEnabled provides design.elevation.automaticTonalElevationEnabled,
    ) {
        MaterialTheme(
            colorScheme = design.materialColorScheme,
            typography = design.typography.material,
            shapes = design.dimensions.materialShapes,
            content = content,
        )
    }
}
