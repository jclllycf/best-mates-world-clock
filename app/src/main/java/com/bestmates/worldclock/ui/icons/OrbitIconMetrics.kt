package com.bestmates.worldclock.ui.icons

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class OrbitIconMetrics(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val defaultArtworkSize: Dp,
    val prominentArtworkSize: Dp,
    val inlineArtworkSize: Dp,
    val defaultStrokeViewportUnits: Float,
    val defaultRenderedStroke: Dp,
    val inlineMinimumRenderedStroke: Dp,
    val minimumInteractiveTarget: Dp,
    val liveAreaStart: Float,
    val liveAreaEnd: Float,
    val minimumDetailClearance: Float,
    val maximumOpticalOvershoot: Float,
    val maximumOpticalCenterOffset: Float,
)

val DefaultOrbitIconMetrics = OrbitIconMetrics(
    viewportWidth = 24f,
    viewportHeight = 24f,
    defaultArtworkSize = 20.dp,
    prominentArtworkSize = 24.dp,
    inlineArtworkSize = 16.dp,
    defaultStrokeViewportUnits = 1.9f,
    defaultRenderedStroke = 1.6.dp,
    inlineMinimumRenderedStroke = 1.4.dp,
    minimumInteractiveTarget = 48.dp,
    liveAreaStart = 3f,
    liveAreaEnd = 21f,
    minimumDetailClearance = 2f,
    maximumOpticalOvershoot = 0.5f,
    maximumOpticalCenterOffset = 0.5f,
)
