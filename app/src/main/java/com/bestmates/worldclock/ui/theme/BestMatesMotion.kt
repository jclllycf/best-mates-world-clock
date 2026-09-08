package com.bestmates.worldclock.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.Immutable
import kotlin.math.min

@Immutable
data class BestMatesMotionDurations(
    val motionInstant: Int,
    val motionPress: Int,
    val motionRelease: Int,
    val motionState: Int,
    val motionExit: Int,
    val motionEnter: Int,
    val motionNavigate: Int,
    val motionTheme: Int,
)

@Immutable
data class BestMatesMotionEasings(
    val easeStandard: Easing,
    val easeEnter: Easing,
    val easeExit: Easing,
    val easeLinear: Easing,
)

@Immutable
data class BestMatesMotionTokens(
    val durations: BestMatesMotionDurations,
    val easings: BestMatesMotionEasings,
    val spatialMotionEnabled: Boolean,
    val haloMotionEnabled: Boolean,
    val maximumFadeDurationMillis: Int,
)

private val StandardBestMatesMotionDurations = BestMatesMotionDurations(
    motionInstant = 0,
    motionPress = 80,
    motionRelease = 120,
    motionState = 160,
    motionExit = 180,
    motionEnter = 220,
    motionNavigate = 260,
    motionTheme = 300,
)

private val DefaultBestMatesMotionEasings = BestMatesMotionEasings(
    easeStandard = CubicBezierEasing(0.2f, 0f, 0f, 1f),
    easeEnter = CubicBezierEasing(0f, 0f, 0.2f, 1f),
    easeExit = CubicBezierEasing(0.4f, 0f, 1f, 1f),
    easeLinear = LinearEasing,
)

fun resolveBestMatesMotion(
    reduceMotion: Boolean,
    systemAnimationsEnabled: Boolean,
): BestMatesMotionTokens {
    if (!systemAnimationsEnabled) {
        return BestMatesMotionTokens(
            durations = StandardBestMatesMotionDurations.withMaximum(0),
            easings = DefaultBestMatesMotionEasings,
            spatialMotionEnabled = false,
            haloMotionEnabled = false,
            maximumFadeDurationMillis = 0,
        )
    }

    if (reduceMotion) {
        return BestMatesMotionTokens(
            durations = StandardBestMatesMotionDurations.withMaximum(100),
            easings = DefaultBestMatesMotionEasings,
            spatialMotionEnabled = false,
            haloMotionEnabled = false,
            maximumFadeDurationMillis = 100,
        )
    }

    return BestMatesMotionTokens(
        durations = StandardBestMatesMotionDurations,
        easings = DefaultBestMatesMotionEasings,
        spatialMotionEnabled = true,
        haloMotionEnabled = true,
        maximumFadeDurationMillis = StandardBestMatesMotionDurations.motionTheme,
    )
}

private fun BestMatesMotionDurations.withMaximum(maximumMillis: Int) = copy(
    motionInstant = 0,
    motionPress = min(motionPress, maximumMillis),
    motionRelease = min(motionRelease, maximumMillis),
    motionState = min(motionState, maximumMillis),
    motionExit = min(motionExit, maximumMillis),
    motionEnter = min(motionEnter, maximumMillis),
    motionNavigate = min(motionNavigate, maximumMillis),
    motionTheme = min(motionTheme, maximumMillis),
)
