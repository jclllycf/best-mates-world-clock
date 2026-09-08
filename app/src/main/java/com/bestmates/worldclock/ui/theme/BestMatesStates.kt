package com.bestmates.worldclock.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Precedence describes color/state resolution only. Focus remains an independent
 * ring and may coexist with any non-disabled persistent state.
 */
enum class BestMatesVisualState(
    val colorPrecedence: Int?,
    val suppressesInteractionFeedback: Boolean,
    val isPersistent: Boolean,
) {
    RESTING(colorPrecedence = 0, suppressesInteractionFeedback = false, isPersistent = false),
    HOVERED(colorPrecedence = 10, suppressesInteractionFeedback = false, isPersistent = false),
    PRESSED(colorPrecedence = 20, suppressesInteractionFeedback = false, isPersistent = false),
    SELECTED(colorPrecedence = 30, suppressesInteractionFeedback = false, isPersistent = true),
    LOCKED(colorPrecedence = 30, suppressesInteractionFeedback = false, isPersistent = true),
    DESTRUCTIVE(colorPrecedence = 40, suppressesInteractionFeedback = false, isPersistent = true),
    ERROR(colorPrecedence = 50, suppressesInteractionFeedback = false, isPersistent = true),
    LOADING(colorPrecedence = 60, suppressesInteractionFeedback = true, isPersistent = true),
    DISABLED(colorPrecedence = 70, suppressesInteractionFeedback = true, isPersistent = true),
    FOCUSED(colorPrecedence = null, suppressesInteractionFeedback = false, isPersistent = true),
}

@Immutable
data class BestMatesStateColorAliases(
    val selectionOutline: Color,
    val selectionFill: Color,
    val selectionContent: Color,
    val focusRing: Color,
    val guide: Color,
    val destructive: Color,
    val onDestructive: Color,
    val destructiveContainer: Color,
    val onDestructiveContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val loading: Color,
    val locked: Color,
    val disabledContentBasis: Color,
    val disabledContainerBasis: Color,
)

@Immutable
data class BestMatesStateTokens(
    val hoverAlpha: Float,
    val pressedAlpha: Float,
    val disabledContentAlpha: Float,
    val disabledContainerAlpha: Float,
    val selectedOutlineWidth: Dp,
    val focusRingWidth: Dp,
    val focusRingGap: Dp,
    val errorOutlineWidth: Dp,
    val colors: BestMatesStateColorAliases,
)

internal fun resolveBestMatesStates(colors: BestMatesColors) = BestMatesStateTokens(
    hoverAlpha = 0.06f,
    pressedAlpha = 0.08f,
    disabledContentAlpha = 0.38f,
    disabledContainerAlpha = 0.08f,
    selectedOutlineWidth = 1.5.dp,
    focusRingWidth = 1.5.dp,
    focusRingGap = 2.dp,
    errorOutlineWidth = 1.dp,
    colors = BestMatesStateColorAliases(
        selectionOutline = colors.selectionOutline,
        selectionFill = colors.selectionFill,
        selectionContent = colors.selectionContent,
        focusRing = colors.focusRing,
        guide = colors.guide,
        destructive = colors.destructive,
        onDestructive = colors.onDestructive,
        destructiveContainer = colors.destructiveContainer,
        onDestructiveContainer = colors.onDestructiveContainer,
        error = colors.destructive,
        onError = colors.onDestructive,
        errorContainer = colors.destructiveContainer,
        onErrorContainer = colors.onDestructiveContainer,
        loading = colors.accent,
        locked = colors.accent,
        disabledContentBasis = colors.textPrimary,
        disabledContainerBasis = colors.textPrimary,
    ),
)
