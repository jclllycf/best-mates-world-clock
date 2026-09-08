package com.bestmates.worldclock.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BestMatesDepthRecipe(
    val elevation: Dp,
    val offsetX: Dp,
    val offsetY: Dp,
    val blur: Dp,
    val spread: Dp,
    val shadowAlpha: Float,
)

@Immutable
data class BestMatesHaloRecipe(
    val alpha: Float,
    val offsetX: Dp,
    val offsetY: Dp,
    val blur: Dp,
    val maximumSpread: Dp,
)

@Immutable
data class BestMatesCrystalSurfaceRecipe(
    val fillAlpha: Float,
    val elevation: Dp,
    val shadowAlpha: Float,
    val topToneMix: Float,
    val bottomToneMix: Float,
    val edgeAlpha: Float,
    val upperHighlightAlpha: Float,
    val lowerRefractionAlpha: Float,
    val warmReflectionAlpha: Float,
    val highlightLength: Float,
)

@Immutable
data class BestMatesCrystalDepthTokens(
    val pearlGlass: BestMatesCrystalSurfaceRecipe,
    val milkCrystal: BestMatesCrystalSurfaceRecipe,
    val jellyCrystal: BestMatesCrystalSurfaceRecipe,
    val pressedElevation: Dp,
    val pressedTranslationY: Dp,
    val pressedScale: Float,
    val pressedDarkenMix: Float,
    val pressedShadowMultiplier: Float,
    val pressedHighlightMultiplier: Float,
    val pressedRefractionMultiplier: Float,
    val pressedHighlightLength: Float,
    val pressedThumbExpansion: Dp,
)

@Immutable
data class BestMatesElevationTokens(
    val depth0: BestMatesDepthRecipe,
    val depth1: BestMatesDepthRecipe,
    val depth2: BestMatesDepthRecipe,
    val depth3: BestMatesDepthRecipe,
    val modalScrimAlpha: Float,
    val atmospherePeakAlpha: Float,
    val softHalo: BestMatesHaloRecipe,
    val crystalDepth: BestMatesCrystalDepthTokens,
    val automaticTonalElevationEnabled: Boolean,
)

internal fun resolveBestMatesElevation(isDark: Boolean) = BestMatesElevationTokens(
    depth0 = BestMatesDepthRecipe(0.dp, 0.dp, 0.dp, 0.dp, 0.dp, 0f),
    depth1 = BestMatesDepthRecipe(
        elevation = 2.dp,
        offsetX = 0.dp,
        offsetY = 2.dp,
        blur = 10.dp,
        spread = 0.dp,
        shadowAlpha = if (isDark) 0.18f else 0.08f,
    ),
    depth2 = BestMatesDepthRecipe(
        elevation = 4.dp,
        offsetX = 0.dp,
        offsetY = 4.dp,
        blur = 18.dp,
        spread = 0.dp,
        shadowAlpha = if (isDark) 0.22f else 0.10f,
    ),
    depth3 = BestMatesDepthRecipe(
        elevation = 8.dp,
        offsetX = 0.dp,
        offsetY = 10.dp,
        blur = 30.dp,
        spread = 0.dp,
        shadowAlpha = if (isDark) 0.20f else 0.12f,
    ),
    modalScrimAlpha = if (isDark) 0.52f else 0.32f,
    atmospherePeakAlpha = if (isDark) 0.10f else 0.28f,
    softHalo = BestMatesHaloRecipe(
        alpha = if (isDark) 0.24f else 0.20f,
        offsetX = 0.dp,
        offsetY = 0.dp,
        blur = 12.dp,
        maximumSpread = 4.dp,
    ),
    crystalDepth = BestMatesCrystalDepthTokens(
        pearlGlass = BestMatesCrystalSurfaceRecipe(
            fillAlpha = if (isDark) 0.82f else 0.90f,
            elevation = if (isDark) 1.dp else 1.5.dp,
            shadowAlpha = if (isDark) 0.022f else 0.07f,
            topToneMix = if (isDark) 0.07f else 0.045f,
            bottomToneMix = if (isDark) 0.09f else 0.055f,
            edgeAlpha = if (isDark) 0.56f else 0.42f,
            upperHighlightAlpha = if (isDark) 0.20f else 0.28f,
            lowerRefractionAlpha = if (isDark) 0.17f else 0.10f,
            warmReflectionAlpha = if (isDark) 0.04f else 0.035f,
            highlightLength = 0.68f,
        ),
        milkCrystal = BestMatesCrystalSurfaceRecipe(
            fillAlpha = if (isDark) 0.88f else 0.95f,
            elevation = if (isDark) 2.dp else 3.dp,
            shadowAlpha = if (isDark) 0.03f else 0.09f,
            topToneMix = if (isDark) 0.06f else 0.04f,
            bottomToneMix = if (isDark) 0.085f else 0.055f,
            edgeAlpha = if (isDark) 0.50f else 0.36f,
            upperHighlightAlpha = if (isDark) 0.18f else 0.24f,
            lowerRefractionAlpha = if (isDark) 0.18f else 0.12f,
            warmReflectionAlpha = if (isDark) 0.045f else 0.045f,
            highlightLength = 0.72f,
        ),
        jellyCrystal = BestMatesCrystalSurfaceRecipe(
            // The action body stays opaque so the approved minimum label contrast
            // cannot vary with the surface behind it. Translucency comes from the rim.
            fillAlpha = 1f,
            elevation = if (isDark) 2.5.dp else 4.dp,
            shadowAlpha = if (isDark) 0.035f else 0.12f,
            topToneMix = if (isDark) 0.04f else 0.02f,
            bottomToneMix = if (isDark) 0.11f else 0.075f,
            edgeAlpha = if (isDark) 0.58f else 0.42f,
            upperHighlightAlpha = if (isDark) 0.18f else 0.25f,
            lowerRefractionAlpha = if (isDark) 0.21f else 0.16f,
            warmReflectionAlpha = if (isDark) 0.05f else 0.055f,
            highlightLength = 0.62f,
        ),
        pressedElevation = 0.5.dp,
        pressedTranslationY = 0.75.dp,
        pressedScale = 0.987f,
        pressedDarkenMix = if (isDark) 0.045f else 0.035f,
        pressedShadowMultiplier = 0.22f,
        pressedHighlightMultiplier = 0.48f,
        pressedRefractionMultiplier = 0.72f,
        pressedHighlightLength = 0.46f,
        pressedThumbExpansion = 1.25.dp,
    ),
    automaticTonalElevationEnabled = false,
)
