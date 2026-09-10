package com.bestmates.worldclock.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign

enum class CrystalLevel { PearlGlass, MilkCrystal, JellyCrystal }

enum class OrbitButtonStyle { Primary, Secondary, Quiet, Destructive }

/** Shared production implementation of the frozen Pearl/Milk/Jelly material recipe. */
@Composable
fun Modifier.crystalSurface(
    baseColor: Color,
    shape: Shape,
    level: CrystalLevel,
    pressed: Boolean = false,
    borderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.dp,
    intensity: Float = 1f,
): Modifier {
    val colors = BestMatesDesign.colors
    val motion = BestMatesDesign.motion
    val crystal = BestMatesDesign.elevation.crystalDepth
    val recipe = when (level) {
        CrystalLevel.PearlGlass -> crystal.pearlGlass
        CrystalLevel.MilkCrystal -> crystal.milkCrystal
        CrystalLevel.JellyCrystal -> crystal.jellyCrystal
    }
    val duration = if (pressed) motion.durations.motionPress else motion.durations.motionRelease
    val easing = motion.easings.easeStandard
    val elevation by animateDpAsState(
        if (pressed) crystal.pressedElevation else recipe.elevation * intensity,
        tween(durationMillis = duration, easing = easing),
        label = "crystal elevation",
    )
    val translationY by animateDpAsState(
        if (pressed && motion.spatialMotionEnabled) crystal.pressedTranslationY else 0.dp,
        tween(durationMillis = duration, easing = easing),
        label = "crystal translation",
    )
    val scale by animateFloatAsState(
        if (pressed && motion.spatialMotionEnabled) crystal.pressedScale else 1f,
        tween(durationMillis = duration, easing = easing),
        label = "crystal scale",
    )
    val pressedDarken by animateFloatAsState(
        if (pressed) crystal.pressedDarkenMix else 0f,
        tween(durationMillis = duration, easing = easing),
        label = "crystal pressed tone",
    )
    val shadowAlpha by animateFloatAsState(
        recipe.shadowAlpha * intensity * if (pressed) crystal.pressedShadowMultiplier else 1f,
        tween(durationMillis = duration, easing = easing),
        label = "crystal shadow",
    )
    val highlightAlpha by animateFloatAsState(
        recipe.upperHighlightAlpha * intensity * if (pressed) crystal.pressedHighlightMultiplier else 1f,
        tween(durationMillis = duration, easing = easing),
        label = "crystal highlight",
    )
    val highlightLength by animateFloatAsState(
        if (pressed) crystal.pressedHighlightLength else recipe.highlightLength,
        tween(durationMillis = duration, easing = easing),
        label = "crystal highlight length",
    )
    val refractionAlpha by animateFloatAsState(
        recipe.lowerRefractionAlpha * intensity * if (pressed) crystal.pressedRefractionMultiplier else 1f,
        tween(durationMillis = duration, easing = easing),
        label = "crystal refraction",
    )
    val topColor = lerp(baseColor, Color.White, recipe.topToneMix * intensity * if (pressed) 0.55f else 1f)
        .copy(alpha = recipe.fillAlpha)
    val middleColor = lerp(baseColor, Color.Black, pressedDarken).copy(alpha = recipe.fillAlpha)
    val bottomColor = lerp(
        baseColor,
        Color.Black,
        recipe.bottomToneMix * intensity + pressedDarken,
    ).copy(alpha = recipe.fillAlpha)
    val resolvedBorder = if (borderColor == Color.Unspecified) colors.borderSubtle else borderColor
    val borderAlpha = (recipe.edgeAlpha * (0.72f + intensity * 0.28f)).coerceIn(0f, 1f)
    val themeContaminationAlpha = recipe.themeContaminationAlpha * intensity

    return this
        .graphicsLayer {
            this.translationY = translationY.toPx()
            scaleX = scale
            scaleY = scale
        }
        .shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = colors.shadowColor.copy(alpha = shadowAlpha),
            spotColor = colors.shadowColor.copy(alpha = shadowAlpha),
        )
        .clip(shape)
        .drawWithCache {
            val body = Brush.verticalGradient(listOf(topColor, middleColor, bottomColor))
            val contamination = Brush.linearGradient(
                listOf(
                    colors.atmosphereA.copy(alpha = themeContaminationAlpha),
                    Color.Transparent,
                    colors.atmosphereB.copy(alpha = themeContaminationAlpha * 0.82f),
                ),
                start = Offset.Zero,
                end = Offset(size.width, size.height),
            )
            val pearlVeil = Brush.verticalGradient(
                listOf(Color.White.copy(alpha = highlightAlpha * 0.16f), Color.Transparent),
                endY = size.height * 0.46f,
            )
            val lineInset = 10.dp.toPx().coerceAtMost(size.width * 0.20f)
            val highlightEnd = (size.width * highlightLength).coerceAtLeast(lineInset)
            val lineWidth = 1.dp.toPx()
            onDrawWithContent {
                drawRect(body)
                drawRect(contamination)
                drawRect(pearlVeil)
                drawContent()
                drawLine(
                    Color.White.copy(alpha = highlightAlpha),
                    Offset(lineInset, lineWidth),
                    Offset(highlightEnd, lineWidth),
                    lineWidth,
                    StrokeCap.Round,
                )
                drawLine(
                    lerp(baseColor, colors.secondary, 0.36f).copy(alpha = refractionAlpha),
                    Offset(lineInset, size.height - lineWidth),
                    Offset(size.width - lineInset, size.height - lineWidth),
                    lineWidth,
                    StrokeCap.Round,
                )
                drawLine(
                    colors.tertiary.copy(alpha = recipe.warmReflectionAlpha * intensity),
                    Offset(size.width * 0.62f, size.height - lineWidth * 2f),
                    Offset(size.width * 0.82f, size.height - lineWidth * 2f),
                    lineWidth,
                    StrokeCap.Round,
                )
            }
        }
        .border(borderWidth, resolvedBorder.copy(alpha = borderAlpha), shape)
}

@Composable
fun OrbitButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: OrbitButtonStyle = OrbitButtonStyle.Primary,
    enabled: Boolean = true,
    compact: Boolean = false,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val container = when (style) {
        OrbitButtonStyle.Primary -> colors.accent
        OrbitButtonStyle.Secondary -> colors.secondaryContainer
        OrbitButtonStyle.Quiet -> colors.surfaceRaised
        OrbitButtonStyle.Destructive -> colors.destructiveContainer
    }
    val content = when (style) {
        OrbitButtonStyle.Primary -> colors.onAccent
        OrbitButtonStyle.Secondary -> colors.onSecondaryContainer
        OrbitButtonStyle.Quiet -> colors.textPrimary
        OrbitButtonStyle.Destructive -> colors.onDestructiveContainer
    }.copy(alpha = if (enabled) 1f else BestMatesDesign.states.disabledContentAlpha)
    val border = when (style) {
        OrbitButtonStyle.Primary -> colors.secondary
        OrbitButtonStyle.Secondary -> colors.secondary
        OrbitButtonStyle.Quiet -> colors.borderControl
        OrbitButtonStyle.Destructive -> colors.destructive
    }
    val level = when (style) {
        OrbitButtonStyle.Primary -> CrystalLevel.JellyCrystal
        OrbitButtonStyle.Secondary, OrbitButtonStyle.Destructive -> CrystalLevel.MilkCrystal
        OrbitButtonStyle.Quiet -> CrystalLevel.PearlGlass
    }
    val intensity = when (style) {
        OrbitButtonStyle.Primary -> 1f
        OrbitButtonStyle.Secondary, OrbitButtonStyle.Destructive -> 0.82f
        OrbitButtonStyle.Quiet -> 0.62f
    }
    val shape = RoundedCornerShape(dimensions.radii.radiusXl)
    Row(
        modifier = modifier
            .heightIn(
                min = if (compact) {
                    dimensions.shared.minimumTouchTarget
                } else {
                    dimensions.shared.minimumSettingsRowHeight
                },
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .crystalSurface(
                baseColor = if (enabled) container else colors.surfaceInset,
                shape = shape,
                level = level,
                pressed = pressed && enabled,
                borderColor = border,
                intensity = if (enabled) intensity else intensity * 0.42f,
            )
            .padding(horizontal = if (compact) dimensions.spacing.space4 else dimensions.spacing.space6),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(icon, null, tint = content, modifier = Modifier.size(if (compact) 18.dp else 20.dp))
            Spacer(Modifier.width(dimensions.shared.iconLabelGap))
        }
        Text(label, style = MaterialTheme.typography.labelLarge, color = content)
    }
}

@Composable
fun OrbitIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    destructive: Boolean = false,
    enabled: Boolean = true,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(dimensions.radii.radiusMd)
    val base = when {
        destructive -> colors.destructiveContainer
        selected -> colors.selectionFill
        else -> colors.surfaceRaised
    }
    val tint = when {
        destructive -> colors.onDestructiveContainer
        selected -> colors.selectionContent
        else -> colors.textSecondary
    }.copy(alpha = if (enabled) 1f else BestMatesDesign.states.disabledContentAlpha)
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .size(dimensions.shared.minimumTouchTarget)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .crystalSurface(
                baseColor = if (enabled) base else colors.surfaceInset,
                shape = shape,
                level = CrystalLevel.PearlGlass,
                pressed = pressed && enabled,
                borderColor = when {
                    destructive -> colors.destructive
                    selected -> colors.selectionOutline
                    else -> colors.borderSubtle
                },
                borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (enabled) 0.68f else 0.32f,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, contentDescription, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun CrystalPanel(
    modifier: Modifier = Modifier,
    level: CrystalLevel = CrystalLevel.MilkCrystal,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val shape = RoundedCornerShape(dimensions.radii.radiusXl)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .crystalSurface(
                baseColor = colors.surfaceRaised,
                shape = shape,
                level = level,
                borderColor = colors.borderSubtle,
                intensity = if (level == CrystalLevel.PearlGlass) .62f else .72f,
            )
            .padding(dimensions.shared.inspectorContentPadding),
        content = content,
    )
}

@Composable
fun OrbitChoiceChip(
    label: String,
    selected: Boolean,
    selectedDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = dimensions.radii.radiusFull
    val contentColor = when {
        !enabled -> colors.textMuted.copy(alpha = BestMatesDesign.states.disabledContentAlpha)
        selected -> colors.selectionContent
        else -> colors.textSecondary
    }
    Row(
        modifier = modifier
            .heightIn(min = dimensions.shared.minimumTouchTarget)
            .semantics { this.selected = selected }
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .crystalSurface(
                baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                shape = shape,
                level = CrystalLevel.PearlGlass,
                pressed = pressed && enabled,
                borderColor = if (selected) colors.selectionOutline else colors.borderControl,
                borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (enabled) .64f else .28f,
            )
            .padding(horizontal = dimensions.spacing.space4),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            Icon(
                OrbitIcons.Confirm,
                contentDescription = selectedDescription,
                modifier = Modifier.size(16.dp),
                tint = contentColor,
            )
            Spacer(Modifier.width(dimensions.spacing.space3))
        }
        Text(label, style = MaterialTheme.typography.labelLarge, color = contentColor)
    }
}
