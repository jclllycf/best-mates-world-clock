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
data class BestMatesElevationTokens(
    val depth0: BestMatesDepthRecipe,
    val depth1: BestMatesDepthRecipe,
    val depth2: BestMatesDepthRecipe,
    val depth3: BestMatesDepthRecipe,
    val modalScrimAlpha: Float,
    val atmospherePeakAlpha: Float,
    val softHalo: BestMatesHaloRecipe,
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
    automaticTonalElevationEnabled = false,
)
