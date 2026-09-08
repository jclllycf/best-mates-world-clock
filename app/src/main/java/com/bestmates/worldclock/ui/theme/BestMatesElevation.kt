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
data class BestMatesPressableDepthRecipe(
    val primaryElevation: Dp,
    val secondaryElevation: Dp,
    val outlinedElevation: Dp,
    val iconElevation: Dp,
    val destructiveElevation: Dp,
    val pressedElevation: Dp,
    val pressedTranslationY: Dp,
    val pressedScale: Float,
    val topToneMix: Float,
    val bottomToneMix: Float,
    val pressedDarkenMix: Float,
    val outerShadowAlpha: Float,
    val innerHighlightAlpha: Float,
    val lowerEdgeAlpha: Float,
    val supportingShadowMultiplier: Float,
    val outlinedShadowMultiplier: Float,
    val iconShadowMultiplier: Float,
    val restingHighlightLength: Float,
    val pressedHighlightLength: Float,
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
    val pressableDepth: BestMatesPressableDepthRecipe,
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
    pressableDepth = BestMatesPressableDepthRecipe(
        primaryElevation = 3.dp,
        secondaryElevation = 2.dp,
        outlinedElevation = 1.dp,
        iconElevation = 1.5.dp,
        destructiveElevation = 1.5.dp,
        pressedElevation = 0.5.dp,
        pressedTranslationY = 1.dp,
        pressedScale = 0.988f,
        topToneMix = if (isDark) 0.04f else 0.025f,
        bottomToneMix = if (isDark) 0.07f else 0.04f,
        pressedDarkenMix = if (isDark) 0.04f else 0.03f,
        outerShadowAlpha = if (isDark) 0.055f else 0.10f,
        innerHighlightAlpha = if (isDark) 0.14f else 0.18f,
        lowerEdgeAlpha = if (isDark) 0.16f else 0.08f,
        supportingShadowMultiplier = 0.68f,
        outlinedShadowMultiplier = 0.32f,
        iconShadowMultiplier = 0.50f,
        restingHighlightLength = 0.74f,
        pressedHighlightLength = 0.52f,
    ),
    automaticTonalElevationEnabled = false,
)
