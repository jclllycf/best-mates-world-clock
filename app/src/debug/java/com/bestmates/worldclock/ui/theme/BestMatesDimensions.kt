package com.bestmates.worldclock.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BestMatesSpacing(
    val space0: Dp,
    val space1: Dp,
    val space2: Dp,
    val space3: Dp,
    val space4: Dp,
    val space5: Dp,
    val space6: Dp,
    val space7: Dp,
    val space8: Dp,
    val space9: Dp,
    val space10: Dp,
    val space11: Dp,
)

@Immutable
data class BestMatesRadii(
    val radiusNone: Dp,
    val radiusXs: Dp,
    val radiusSm: Dp,
    val radiusMd: Dp,
    val radiusLg: Dp,
    val radiusXl: Dp,
    val radius2xl: Dp,
    val radius3xl: Dp,
    val radiusFull: CornerBasedShape,
)

@Immutable
data class BestMatesSharedDimensions(
    val minimumTouchTarget: Dp,
    val minimumSettingsRowHeight: Dp,
    val minimumInputHeight: Dp,
    val compactScreenGutter: Dp,
    val standardScreenGutter: Dp,
    val inspectorContentPadding: Dp,
    val inspectorRowSpacing: Dp,
    val majorSectionSpacing: Dp,
    val iconLabelGap: Dp,
    val distinctTouchTargetGap: Dp,
)

@Immutable
data class BestMatesDimensionTokens(
    val spacing: BestMatesSpacing,
    val radii: BestMatesRadii,
    val shared: BestMatesSharedDimensions,
    val materialShapes: Shapes,
)

private val DefaultBestMatesSpacing = BestMatesSpacing(
    space0 = 0.dp,
    space1 = 2.dp,
    space2 = 4.dp,
    space3 = 8.dp,
    space4 = 12.dp,
    space5 = 16.dp,
    space6 = 20.dp,
    space7 = 24.dp,
    space8 = 32.dp,
    space9 = 40.dp,
    space10 = 48.dp,
    space11 = 64.dp,
)

private val DefaultBestMatesRadii = BestMatesRadii(
    radiusNone = 0.dp,
    radiusXs = 6.dp,
    radiusSm = 10.dp,
    radiusMd = 14.dp,
    radiusLg = 18.dp,
    radiusXl = 20.dp,
    radius2xl = 24.dp,
    radius3xl = 28.dp,
    radiusFull = CircleShape,
)

private val DefaultBestMatesSharedDimensions = BestMatesSharedDimensions(
    minimumTouchTarget = 48.dp,
    minimumSettingsRowHeight = 56.dp,
    minimumInputHeight = 56.dp,
    compactScreenGutter = 16.dp,
    standardScreenGutter = 24.dp,
    inspectorContentPadding = 16.dp,
    inspectorRowSpacing = 12.dp,
    majorSectionSpacing = 24.dp,
    iconLabelGap = 8.dp,
    distinctTouchTargetGap = 8.dp,
)

private val DefaultBestMatesMaterialShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

internal val DefaultBestMatesDimensions = BestMatesDimensionTokens(
    spacing = DefaultBestMatesSpacing,
    radii = DefaultBestMatesRadii,
    shared = DefaultBestMatesSharedDimensions,
    materialShapes = DefaultBestMatesMaterialShapes,
)
