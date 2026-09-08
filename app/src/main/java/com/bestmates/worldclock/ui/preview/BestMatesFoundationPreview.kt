@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.bestmates.worldclock.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.ui.icons.OrbitIconMetrics
import com.bestmates.worldclock.ui.theme.BestMatesBrightnessMode
import com.bestmates.worldclock.ui.theme.BestMatesColors
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import com.bestmates.worldclock.ui.theme.BestMatesDesignTheme
import com.bestmates.worldclock.ui.theme.BestMatesDimensionTokens
import com.bestmates.worldclock.ui.theme.BestMatesElevationTokens
import com.bestmates.worldclock.ui.theme.BestMatesStateTokens
import com.bestmates.worldclock.ui.theme.BestMatesVisualIdentity
import com.bestmates.worldclock.ui.theme.BestMatesVisualState

/**
 * Phase 2B only: an isolated token-review surface. Android Studio's @Preview and the
 * debug-only device harness are its only access paths; production navigation cannot reach it.
 */
@Preview(
    name = "Best Mates 2.0 — Design Lab",
    showBackground = true,
    backgroundColor = 0xFFF8F5F2,
    widthDp = 393,
    heightDp = 852,
)
@Composable
private fun BestMatesFoundationPreview() {
    BestMatesFoundationDesignLab()
}

@Composable
internal fun BestMatesFoundationDesignLab() {
    var identityIndex by rememberSaveable { mutableIntStateOf(0) }
    var isDark by rememberSaveable { mutableStateOf(false) }
    val identities = BestMatesVisualIdentity.entries
    val identity = identities[identityIndex]

    BestMatesDesignTheme(
        systemIsDark = isDark,
        identity = identity,
        brightnessMode = if (isDark) BestMatesBrightnessMode.DARK else BestMatesBrightnessMode.LIGHT,
    ) {
        FoundationDesignLabPage(
            identity = identity,
            isDark = isDark,
            onIdentitySelected = { selected -> identityIndex = identities.indexOf(selected) },
            onBrightnessSelected = { selectedIsDark -> isDark = selectedIsDark },
        )
    }
}

@Composable
private fun FoundationDesignLabPage(
    identity: BestMatesVisualIdentity,
    isDark: Boolean,
    onIdentitySelected: (BestMatesVisualIdentity) -> Unit,
    onBrightnessSelected: (Boolean) -> Unit,
) {
    val tokens = BestMatesDesign.current
    val colors = tokens.colors
    val dimensions = tokens.dimensions
    val spacing = dimensions.spacing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = dimensions.shared.compactScreenGutter,
                vertical = spacing.space7,
            ),
        verticalArrangement = Arrangement.spacedBy(dimensions.shared.majorSectionSpacing),
    ) {
        LabHeader(
            identity = identity,
            isDark = isDark,
            onIdentitySelected = onIdentitySelected,
            onBrightnessSelected = onBrightnessSelected,
        )

        LabSection(
            title = "Atmosphere & surfaces",
            description = "Opaque surfaces sit above the Warm Pearl + Lavender atmosphere.",
        ) {
            AtmosphereSurface(colors = colors, elevation = tokens.elevation, dimensions = dimensions)
            SurfaceGrid(colors = colors, dimensions = dimensions)
        }

        LabSection(
            title = "Color tokens",
            description = "Resolved semantic colors for ${identity.displayName} ${if (isDark) "Dark" else "Light"}.",
        ) {
            TokenSwatchGrid(colors)
        }

        LabSection(
            title = "Typography",
            description = "Rounded hierarchy for display and headlines; neutral UI text for controls.",
        ) {
            TypographyReference()
        }

        LabSection(
            title = "Spacing",
            description = "The compact ruler confirms the approved 2dp → 64dp progression.",
        ) {
            SpacingRuler(dimensions)
        }

        LabSection(
            title = "Corner radii",
            description = "Use rounded corners for containment and circles only where the token is full.",
        ) {
            RadiusReference(dimensions)
        }

        LabSection(
            title = "Button hierarchy",
            description = "Static examples only; no production action is attached.",
        ) {
            ButtonReference(colors, dimensions)
        }

        LabSection(
            title = "Icon actions",
            description = "Orbit Softline keeps artwork light inside a 48dp interactive target.",
        ) {
            IconActionReference(tokens.icons, colors, dimensions)
        }

        LabSection(
            title = "Chips",
            description = "Selection includes a check indicator, so it is never color-only.",
        ) {
            ChipReference(dimensions)
        }

        LabSection(
            title = "Switches",
            description = "Visual setting references. They do not change a setting in this lab.",
        ) {
            SwitchReference(dimensions)
        }

        LabSection(
            title = "Slider",
            description = "The active track is the accent token; this reference is fixed at 40%.",
        ) {
            SliderReference(colors)
        }

        LabSection(
            title = "Text fields",
            description = "Resting, focused, and error references use the Material bridge and state tokens.",
        ) {
            TextFieldReference(tokens.states, dimensions)
        }

        LabSection(
            title = "Color swatches",
            description = "Every 32dp visual swatch has a 48dp target; translucency keeps its checkerboard.",
        ) {
            ColorSwatchReference(colors, dimensions)
        }

        LabSection(
            title = "State system",
            description = "Static references make precedence and non-color state indicators inspectable.",
        ) {
            StateReference(colors, tokens.states, dimensions)
        }

        LabSection(
            title = "Soft Halo",
            description = "Selected fill + 2dp outline + check are required. The halo remains optional.",
        ) {
            SoftHaloReference(colors, tokens.states, tokens.elevation, dimensions)
        }

        LabSection(
            title = "Overlay & scrim",
            description = "Overlay remains opaque. Modal scrim values are a reference, not a modal implementation.",
        ) {
            OverlayReference(colors, tokens.elevation, dimensions)
        }

        LabSection(
            title = "Orbit icon metrics",
            description = "The preview artwork is private to this file; it is not an icon-library migration.",
        ) {
            OrbitMetricReference(tokens.icons, colors, dimensions)
        }

        LabSection(
            title = "Quiet Motion",
            description = "Token reference only. The lab intentionally does not add production animation.",
        ) {
            MotionReference()
        }

        Spacer(Modifier.height(spacing.space3))
    }
}

@Composable
private fun LabHeader(
    identity: BestMatesVisualIdentity,
    isDark: Boolean,
    onIdentitySelected: (BestMatesVisualIdentity) -> Unit,
    onBrightnessSelected: (Boolean) -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val spacing = dimensions.spacing

    Surface(
        color = colors.surfaceBase,
        contentColor = colors.textPrimary,
        shape = RoundedCornerShape(dimensions.radii.radius2xl),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(spacing.space5),
            verticalArrangement = Arrangement.spacedBy(spacing.space4),
        ) {
            Text("Mates Orbit", style = MaterialTheme.typography.labelLarge, color = colors.accent)
            Text("Best Mates Design Lab", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Phase 2B · Foundation visual validation · 8 resolved variants",
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
            )
            Text("Identity", style = MaterialTheme.typography.labelLarge)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing.space3),
                verticalArrangement = Arrangement.spacedBy(spacing.space3),
            ) {
                BestMatesVisualIdentity.entries.forEach { option ->
                    FilterChip(
                        selected = identity == option,
                        onClick = { onIdentitySelected(option) },
                        label = { Text(option.displayName) },
                        leadingIcon = if (identity == option) {
                            { Text("✓", style = MaterialTheme.typography.labelLarge) }
                        } else {
                            null
                        },
                        modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
                    )
                }
            }
            Text("Brightness", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.space3)) {
                FilterChip(
                    selected = !isDark,
                    onClick = { onBrightnessSelected(false) },
                    label = { Text("Light") },
                    leadingIcon = if (!isDark) {
                        { Text("✓", style = MaterialTheme.typography.labelLarge) }
                    } else {
                        null
                    },
                    modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
                )
                FilterChip(
                    selected = isDark,
                    onClick = { onBrightnessSelected(true) },
                    label = { Text("Dark") },
                    leadingIcon = if (isDark) {
                        { Text("✓", style = MaterialTheme.typography.labelLarge) }
                    } else {
                        null
                    },
                    modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
                )
            }
        }
    }
}

@Composable
private fun LabSection(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = BestMatesDesign.colors
    val spacing = BestMatesDesign.dimensions.spacing
    Column(verticalArrangement = Arrangement.spacedBy(spacing.space3)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, color = colors.textPrimary)
        Text(description, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
        content()
    }
}

@Composable
private fun AtmosphereSurface(
    colors: BestMatesColors,
    elevation: BestMatesElevationTokens,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radius2xl)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
            .clip(shape)
            .background(colors.canvasMat),
    ) {
        Canvas(Modifier.matchParentSize()) {
            val radius = maxOf(size.width, size.height) * 0.9f
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        colors.atmosphereA.copy(alpha = elevation.atmospherePeakAlpha),
                        Color.Transparent,
                    ),
                    center = Offset(size.width * 0.18f, size.height * 0.18f),
                    radius = radius,
                ),
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        colors.atmosphereB.copy(alpha = elevation.atmospherePeakAlpha),
                        Color.Transparent,
                    ),
                    center = Offset(size.width * 0.83f, size.height * 0.78f),
                    radius = radius,
                ),
            )
        }
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(dimensions.spacing.space5),
            color = colors.surfaceOverlay,
            contentColor = colors.textPrimary,
            shape = RoundedCornerShape(dimensions.radii.radiusLg),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
        ) {
            Column(
                modifier = Modifier.padding(dimensions.spacing.space4),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1),
            ) {
                Text("Opaque surface", style = MaterialTheme.typography.titleSmall)
                Text("Atmosphere stays behind content.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun SurfaceGrid(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val samples = listOf(
        "background" to colors.background,
        "surfaceBase" to colors.surfaceBase,
        "surfaceRaised" to colors.surfaceRaised,
        "surfaceInset" to colors.surfaceInset,
        "surfaceOverlay" to colors.surfaceOverlay,
        "surfaceElevated" to colors.surfaceElevated,
        "canvasMat" to colors.canvasMat,
    )
    val spacing = dimensions.spacing.space3
    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        samples.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) {
                row.forEach { (name, color) ->
                    SurfaceToken(name, color, colors, dimensions)
                }
                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
    }
}

@Composable
private fun RowScope.SurfaceToken(
    name: String,
    color: Color,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Surface(
        color = color,
        contentColor = colors.textPrimary,
        shape = RoundedCornerShape(dimensions.radii.radiusMd),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
        modifier = Modifier
            .weight(1f)
            .height(72.dp),
    ) {
        Text(
            text = name,
            modifier = Modifier.padding(dimensions.spacing.space3),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textPrimary,
        )
    }
}

@Composable
private fun TokenSwatchGrid(colors: BestMatesColors) {
    val tokens = listOf(
        LabColorToken("accent", colors.accent),
        LabColorToken("accentContainer", colors.accentContainer),
        LabColorToken("secondary", colors.secondary),
        LabColorToken("secondaryContainer", colors.secondaryContainer),
        LabColorToken("tertiary", colors.tertiary),
        LabColorToken("tertiaryContainer", colors.tertiaryContainer),
        LabColorToken("destructive", colors.destructive),
        LabColorToken("destructiveContainer", colors.destructiveContainer),
        LabColorToken("success", colors.success),
        LabColorToken("successContainer", colors.successContainer),
        LabColorToken("warning", colors.warning),
        LabColorToken("warningContainer", colors.warningContainer),
        LabColorToken("textPrimary", colors.textPrimary),
        LabColorToken("textSecondary", colors.textSecondary),
        LabColorToken("textMuted", colors.textMuted),
        LabColorToken("borderSubtle", colors.borderSubtle),
        LabColorToken("borderControl", colors.borderControl),
        LabColorToken("canvasMat", colors.canvasMat),
        LabColorToken("onCanvasMat", colors.onCanvasMat),
        LabColorToken("checkerLight", colors.checkerLight),
        LabColorToken("checkerDark", colors.checkerDark),
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(BestMatesDesign.dimensions.spacing.space3),
        verticalArrangement = Arrangement.spacedBy(BestMatesDesign.dimensions.spacing.space3),
    ) {
        tokens.forEach { token -> TokenSwatch(token, colors) }
    }
}

private data class LabColorToken(val name: String, val color: Color)

@Composable
private fun TokenSwatch(token: LabColorToken, colors: BestMatesColors) {
    Column(
        modifier = Modifier.width(108.dp),
        verticalArrangement = Arrangement.spacedBy(BestMatesDesign.dimensions.spacing.space1),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(RoundedCornerShape(BestMatesDesign.dimensions.radii.radiusSm))
                .background(token.color)
                .border(1.dp, colors.borderControl, RoundedCornerShape(BestMatesDesign.dimensions.radii.radiusSm)),
        )
        Text(
            token.name,
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary,
            maxLines = 1,
        )
    }
}

@Composable
private fun TypographyReference() {
    val colors = BestMatesDesign.colors
    val type = MaterialTheme.typography
    val spacing = BestMatesDesign.dimensions.spacing
    val rows = listOf(
        "displayLarge" to ("Best Mates" to type.displayLarge),
        "displayMedium" to ("Your worlds" to type.displayMedium),
        "displaySmall" to ("Create widget" to type.displaySmall),
        "headlineLarge" to ("Tokyo & Home" to type.headlineLarge),
        "headlineMedium" to ("Tuesday, September 8" to type.headlineMedium),
        "headlineSmall" to ("Widget details" to type.headlineSmall),
        "titleLarge" to ("Your worlds" to type.titleLarge),
        "titleMedium" to ("Create widget" to type.titleMedium),
        "titleSmall" to ("Visual theme" to type.titleSmall),
        "bodyLarge" to ("Time follows every city you choose." to type.bodyLarge),
        "bodyMedium" to ("Keep the canvas clear and focused." to type.bodyMedium),
        "bodySmall" to ("A quiet detail stays easy to read." to type.bodySmall),
        "labelLarge" to ("WIDGET NAME" to type.labelLarge),
        "labelMedium" to ("TIME FORMAT" to type.labelMedium),
        "labelSmall" to ("LOCAL PREVIEW" to type.labelSmall),
    )
    Column(verticalArrangement = Arrangement.spacedBy(spacing.space3)) {
        rows.forEach { (name, example) ->
            Column(verticalArrangement = Arrangement.spacedBy(spacing.space1)) {
                Text(name, style = type.labelSmall, color = colors.textMuted)
                Text(example.first, style = example.second, color = colors.textPrimary)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(spacing.space1)) {
            Text("valueTime", style = type.labelSmall, color = colors.textMuted)
            Text("18:40", style = BestMatesDesign.typography.valueTime, color = colors.accent)
        }
    }
}

@Composable
private fun SpacingRuler(dimensions: BestMatesDimensionTokens) {
    val colors = BestMatesDesign.colors
    val spacing = dimensions.spacing
    val steps = listOf(
        "space1" to spacing.space1,
        "space2" to spacing.space2,
        "space3" to spacing.space3,
        "space4" to spacing.space4,
        "space5" to spacing.space5,
        "space6" to spacing.space6,
        "space7" to spacing.space7,
        "space8" to spacing.space8,
        "space9" to spacing.space9,
        "space10" to spacing.space10,
        "space11" to spacing.space11,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing.space3),
        verticalArrangement = Arrangement.spacedBy(spacing.space3),
    ) {
        steps.forEach { (name, value) ->
            Column(
                modifier = Modifier.width(68.dp),
                verticalArrangement = Arrangement.spacedBy(spacing.space1),
            ) {
                Box(
                    modifier = Modifier
                        .height(28.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Box(
                        modifier = Modifier
                            .width(value)
                            .height(8.dp)
                            .clip(dimensions.radii.radiusFull)
                            .background(colors.accent),
                    )
                }
                Text(
                    "$name · ${value.value.toInt()}dp",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun RadiusReference(dimensions: BestMatesDimensionTokens) {
    val colors = BestMatesDesign.colors
    val radii = dimensions.radii
    val samples = listOf(
        LabRadius("xs", RoundedCornerShape(radii.radiusXs)),
        LabRadius("sm", RoundedCornerShape(radii.radiusSm)),
        LabRadius("md", RoundedCornerShape(radii.radiusMd)),
        LabRadius("lg", RoundedCornerShape(radii.radiusLg)),
        LabRadius("xl", RoundedCornerShape(radii.radiusXl)),
        LabRadius("2xl", RoundedCornerShape(radii.radius2xl)),
        LabRadius("3xl", RoundedCornerShape(radii.radius3xl)),
        LabRadius("full", CircleShape),
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
    ) {
        samples.forEach { sample ->
            Column(
                modifier = Modifier.width(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(sample.shape)
                        .background(colors.accentContainer)
                        .border(1.dp, colors.accent, sample.shape),
                )
                Text(sample.name, style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
            }
        }
    }
}

private data class LabRadius(val name: String, val shape: Shape)

@Composable
private fun ButtonReference(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .heightIn(min = dimensions.shared.minimumTouchTarget)
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                contentColor = colors.onAccent,
            ),
            modifier = buttonModifier,
        ) { Text("Create widget") }
        Button(
            onClick = {},
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.secondaryContainer,
                contentColor = colors.onSecondaryContainer,
            ),
            modifier = buttonModifier,
        ) { Text("Add to Home") }
        OutlinedButton(
            onClick = {},
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderControl),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.textPrimary),
            modifier = buttonModifier,
        ) { Text("Import font") }
        TextButton(
            onClick = {},
            colors = ButtonDefaults.textButtonColors(contentColor = colors.accent),
            modifier = buttonModifier,
        ) { Text("Cancel") }
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.destructive,
                contentColor = colors.onDestructive,
            ),
            modifier = buttonModifier,
        ) { Text("Delete") }
    }
}

@Composable
private fun IconActionReference(
    icons: OrbitIconMetrics,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        IconActionSample(
            label = "Default",
            detail = "${icons.defaultArtworkSize.value.toInt()}dp art",
            artworkSize = icons.defaultArtworkSize,
            stroke = icons.defaultRenderedStroke,
            icons = icons,
            colors = colors,
            dimensions = dimensions,
        )
        IconActionSample(
            label = "Prominent",
            detail = "${icons.prominentArtworkSize.value.toInt()}dp art",
            artworkSize = icons.prominentArtworkSize,
            stroke = icons.defaultRenderedStroke,
            icons = icons,
            colors = colors,
            dimensions = dimensions,
        )
        IconActionSample(
            label = "Inline",
            detail = "${icons.inlineArtworkSize.value.toInt()}dp art",
            artworkSize = icons.inlineArtworkSize,
            stroke = icons.inlineMinimumRenderedStroke,
            icons = icons,
            colors = colors,
            dimensions = dimensions,
        )
    }
}

@Composable
private fun IconActionSample(
    label: String,
    detail: String,
    artworkSize: Dp,
    stroke: Dp,
    icons: OrbitIconMetrics,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Column(
        modifier = Modifier.widthIn(min = 88.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        Box(
            modifier = Modifier
                .requiredSize(icons.minimumInteractiveTarget)
                .clip(CircleShape)
                .background(colors.surfaceInset)
                .border(1.dp, colors.borderControl, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            OrbitPreviewMark(artworkSize = artworkSize, stroke = stroke, color = colors.accent)
        }
        Text(label, style = MaterialTheme.typography.labelMedium, color = colors.textPrimary)
        Text(detail, style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
        Text("48dp target", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
    }
}

@Composable
private fun OrbitPreviewMark(artworkSize: Dp, stroke: Dp, color: Color) {
    Canvas(Modifier.size(artworkSize)) {
        val radius = size.minDimension * 0.33f
        val center = Offset(size.width / 2f, size.height / 2f)
        drawCircle(
            color = color,
            radius = radius,
            center = center,
            style = Stroke(width = stroke.toPx()),
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.16f, size.height * 0.72f),
            end = Offset(size.width * 0.84f, size.height * 0.28f),
            strokeWidth = stroke.toPx(),
            cap = StrokeCap.Round,
        )
        drawCircle(color = color, radius = stroke.toPx() * 0.7f, center = Offset(size.width * 0.84f, size.height * 0.28f))
    }
}

@Composable
private fun ChipReference(dimensions: BestMatesDimensionTokens) {
    val spacing = dimensions.spacing.space3
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
    ) {
        FilterChip(
            selected = false,
            onClick = {},
            label = { Text("Cloud") },
            modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
        )
        FilterChip(
            selected = true,
            onClick = {},
            label = { Text("Dream") },
            leadingIcon = { Text("✓", style = MaterialTheme.typography.labelLarge) },
            modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
        )
        FilterChip(
            selected = false,
            onClick = {},
            enabled = false,
            label = { Text("Mono") },
            modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
        )
    }
}

@Composable
private fun SwitchReference(dimensions: BestMatesDimensionTokens) {
    val colors = BestMatesDesign.colors
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
        SwitchRow("Reduce motion · Off", checked = false, enabled = true, colors = colors, dimensions = dimensions)
        SwitchRow("Reduce motion · On", checked = true, enabled = true, colors = colors, dimensions = dimensions)
        SwitchRow("Reduce motion · Disabled", checked = false, enabled = false, colors = colors, dimensions = dimensions)
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    enabled: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimensions.shared.minimumSettingsRowHeight)
            .clip(RoundedCornerShape(dimensions.radii.radiusMd))
            .background(colors.surfaceRaised)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(dimensions.radii.radiusMd))
            .padding(horizontal = dimensions.spacing.space4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) colors.textPrimary else colors.textPrimary.copy(alpha = BestMatesDesign.states.disabledContentAlpha),
        )
        Switch(checked = checked, onCheckedChange = {}, enabled = enabled)
    }
}

@Composable
private fun SliderReference(colors: BestMatesColors) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BestMatesDesign.dimensions.spacing.space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Glow strength", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
            Slider(
                value = 0.4f,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = colors.accent,
                    activeTrackColor = colors.accent,
                    inactiveTrackColor = colors.surfaceInset,
                ),
            )
        }
        Text("40%", style = MaterialTheme.typography.labelLarge, color = colors.accent)
    }
}

@Composable
private fun TextFieldReference(states: BestMatesStateTokens, dimensions: BestMatesDimensionTokens) {
    val colors = BestMatesDesign.colors
    val spacing = dimensions.spacing.space3
    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        Text("Resting", style = MaterialTheme.typography.labelMedium, color = colors.textMuted)
        OutlinedTextField(
            value = "Widget name",
            onValueChange = {},
            readOnly = true,
            label = { Text("Widget name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Text("Focused reference", style = MaterialTheme.typography.labelMedium, color = colors.textMuted)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensions.radii.radiusLg))
                .border(states.focusRingWidth, states.colors.focusRing, RoundedCornerShape(dimensions.radii.radiusLg))
                .padding(states.focusRingGap),
        ) {
            OutlinedTextField(
                value = "Tokyo & Home",
                onValueChange = {},
                readOnly = true,
                label = { Text("Widget name") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Text("Error reference", style = MaterialTheme.typography.labelMedium, color = colors.textMuted)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            isError = true,
            label = { Text("Widget name") },
            supportingText = { Text("A widget name is required.") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ColorSwatchReference(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ColorSwatchSample(
            label = "Opaque",
            color = colors.accent,
            showChecker = false,
            selected = false,
            colors = colors,
            dimensions = dimensions,
        )
        ColorSwatchSample(
            label = "Semi",
            color = colors.accent.copy(alpha = 0.5f),
            showChecker = true,
            selected = false,
            colors = colors,
            dimensions = dimensions,
        )
        ColorSwatchSample(
            label = "Selected",
            color = colors.secondary,
            showChecker = false,
            selected = true,
            colors = colors,
            dimensions = dimensions,
        )
    }
}

@Composable
private fun ColorSwatchSample(
    label: String,
    color: Color,
    showChecker: Boolean,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusMd)
    Column(
        modifier = Modifier.width(88.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        Box(
            modifier = Modifier
                .requiredSize(dimensions.shared.minimumTouchTarget)
                .clip(shape)
                .background(colors.surfaceRaised)
                .border(
                    if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                    if (selected) BestMatesDesign.states.colors.selectionOutline else colors.borderControl,
                    shape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(dimensions.radii.radiusSm)),
            ) {
                if (showChecker) Checkerboard(colors.checkerLight, colors.checkerDark)
                Box(Modifier.matchParentSize().background(color))
                if (selected) {
                    Text(
                        "✓",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSecondary,
                    )
                }
            }
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
        Text("32 / 48dp", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
    }
}

@Composable
private fun Checkerboard(light: Color, dark: Color) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.weight(1f)) {
            Box(Modifier.weight(1f).background(light))
            Box(Modifier.weight(1f).background(dark))
        }
        Row(Modifier.weight(1f)) {
            Box(Modifier.weight(1f).background(dark))
            Box(Modifier.weight(1f).background(light))
        }
    }
}

@Composable
private fun StateReference(
    colors: BestMatesColors,
    states: BestMatesStateTokens,
    dimensions: BestMatesDimensionTokens,
) {
    val stateRows = listOf(
        BestMatesVisualState.RESTING,
        BestMatesVisualState.PRESSED,
        BestMatesVisualState.SELECTED,
        BestMatesVisualState.FOCUSED,
        BestMatesVisualState.DISABLED,
        BestMatesVisualState.DESTRUCTIVE,
        BestMatesVisualState.ERROR,
        BestMatesVisualState.LOADING,
        BestMatesVisualState.LOCKED,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
    ) {
        stateRows.forEach { state -> StateSample(state, colors, states, dimensions) }
    }
}

@Composable
private fun StateSample(
    state: BestMatesVisualState,
    colors: BestMatesColors,
    states: BestMatesStateTokens,
    dimensions: BestMatesDimensionTokens,
) {
    val appearance = stateAppearance(state, colors, states)
    val shape = RoundedCornerShape(dimensions.radii.radiusMd)
    Column(
        modifier = Modifier.width(108.dp),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.shared.minimumTouchTarget)
                .clip(shape)
                .background(appearance.container)
                .then(
                    if (appearance.outline != null) {
                        Modifier.border(appearance.outlineWidth, appearance.outline, shape)
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = dimensions.spacing.space3),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                appearance.indicator,
                style = MaterialTheme.typography.labelMedium,
                color = appearance.content,
                textAlign = TextAlign.Center,
            )
        }
        Text(state.displayName, style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
    }
}

private data class StateAppearance(
    val container: Color,
    val content: Color,
    val outline: Color?,
    val outlineWidth: Dp,
    val indicator: String,
)

private fun stateAppearance(
    state: BestMatesVisualState,
    colors: BestMatesColors,
    states: BestMatesStateTokens,
): StateAppearance = when (state) {
    BestMatesVisualState.RESTING -> StateAppearance(
        colors.surfaceRaised, colors.textPrimary, colors.borderSubtle, 1.dp, "Resting",
    )
    BestMatesVisualState.PRESSED -> StateAppearance(
        colors.textPrimary.copy(alpha = states.pressedAlpha), colors.textPrimary, null, 0.dp, "Pressed",
    )
    BestMatesVisualState.SELECTED -> StateAppearance(
        states.colors.selectionFill,
        states.colors.selectionContent,
        states.colors.selectionOutline,
        states.selectedOutlineWidth,
        "✓ Selected",
    )
    BestMatesVisualState.FOCUSED -> StateAppearance(
        colors.surfaceRaised, colors.textPrimary, states.colors.focusRing, states.focusRingWidth, "Focused",
    )
    BestMatesVisualState.DISABLED -> StateAppearance(
        states.colors.disabledContainerBasis.copy(alpha = states.disabledContainerAlpha),
        states.colors.disabledContentBasis.copy(alpha = states.disabledContentAlpha),
        null,
        0.dp,
        "Disabled",
    )
    BestMatesVisualState.DESTRUCTIVE -> StateAppearance(
        states.colors.destructiveContainer,
        states.colors.onDestructiveContainer,
        states.colors.destructive,
        1.dp,
        "Delete",
    )
    BestMatesVisualState.ERROR -> StateAppearance(
        states.colors.errorContainer,
        states.colors.onErrorContainer,
        states.colors.error,
        states.errorOutlineWidth,
        "Error",
    )
    BestMatesVisualState.LOADING -> StateAppearance(
        colors.accentContainer, states.colors.loading, colors.accent, 1.dp, "•••",
    )
    BestMatesVisualState.LOCKED -> StateAppearance(
        colors.surfaceInset, states.colors.locked, colors.borderControl, 1.dp, "• Locked",
    )
    BestMatesVisualState.HOVERED -> StateAppearance(
        colors.textPrimary.copy(alpha = states.hoverAlpha), colors.textPrimary, null, 0.dp, "Hovered",
    )
}

@Composable
private fun SoftHaloReference(
    colors: BestMatesColors,
    states: BestMatesStateTokens,
    elevation: BestMatesElevationTokens,
    dimensions: BestMatesDimensionTokens,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space4),
    ) {
        HaloCard(
            label = "With halo",
            showHalo = true,
            colors = colors,
            states = states,
            elevation = elevation,
            dimensions = dimensions,
            modifier = Modifier.weight(1f),
        )
        HaloCard(
            label = "Without halo",
            showHalo = false,
            colors = colors,
            states = states,
            elevation = elevation,
            dimensions = dimensions,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun HaloCard(
    label: String,
    showHalo: Boolean,
    colors: BestMatesColors,
    states: BestMatesStateTokens,
    elevation: BestMatesElevationTokens,
    dimensions: BestMatesDimensionTokens,
    modifier: Modifier,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusLg)
    val halo = elevation.softHalo
    val haloModifier = if (showHalo) {
        Modifier.shadow(
            elevation = halo.blur,
            shape = shape,
            clip = false,
            ambientColor = colors.accent.copy(alpha = halo.alpha),
            spotColor = colors.accent.copy(alpha = halo.alpha),
        )
    } else {
        Modifier
    }
    Column(
        modifier = modifier.padding(halo.maximumSpread),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
    ) {
        Box(
            modifier = haloModifier
                .fillMaxWidth()
                .height(96.dp)
                .clip(shape)
                .background(states.colors.selectionFill)
                .border(states.selectedOutlineWidth, states.colors.selectionOutline, shape),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("✓", style = MaterialTheme.typography.headlineSmall, color = states.colors.selectionContent)
                Text("Selected", style = MaterialTheme.typography.labelLarge, color = states.colors.selectionContent)
            }
        }
        Text(label, style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
    }
}

@Composable
private fun OverlayReference(
    colors: BestMatesColors,
    elevation: BestMatesElevationTokens,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusLg)
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .clip(shape)
                .background(colors.surfaceBase)
                .border(1.dp, colors.borderSubtle, shape),
        ) {
            Text(
                "Base surface",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(dimensions.spacing.space4),
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(72.dp),
                color = colors.surfaceOverlay,
                contentColor = colors.textPrimary,
                shape = RoundedCornerShape(topStart = dimensions.radii.radiusLg, topEnd = dimensions.radii.radiusLg),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
            ) {
                Text(
                    "surfaceOverlay · opaque",
                    modifier = Modifier.padding(dimensions.spacing.space4),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
        ) {
            ScrimReference("Light scrim", 0.32f, colors, dimensions, Modifier.weight(1f))
            ScrimReference("Dark scrim", 0.48f, colors, dimensions, Modifier.weight(1f))
        }
        Text(
            "Current resolved scrim: ${(elevation.modalScrimAlpha * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
        )
    }
}

@Composable
private fun ScrimReference(
    label: String,
    alpha: Float,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    modifier: Modifier,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusMd)
    Box(
        modifier = modifier
            .height(72.dp)
            .clip(shape)
            .background(colors.surfaceBase)
            .border(1.dp, colors.borderSubtle, shape),
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = alpha)))
        Text(
            "$label\n${(alpha * 100).toInt()}%",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun OrbitMetricReference(
    icons: OrbitIconMetrics,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Surface(
        color = colors.surfaceRaised,
        contentColor = colors.textPrimary,
        shape = RoundedCornerShape(dimensions.radii.radiusLg),
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderSubtle),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(dimensions.spacing.space4),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space4),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(icons.minimumInteractiveTarget)
                    .clip(CircleShape)
                    .background(colors.surfaceInset),
                contentAlignment = Alignment.Center,
            ) {
                OrbitPreviewMark(icons.defaultArtworkSize, icons.defaultRenderedStroke, colors.accent)
            }
            Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1)) {
                Text("${icons.viewportWidth.toInt()} × ${icons.viewportHeight.toInt()} viewport", style = MaterialTheme.typography.titleSmall)
                Text(
                    "${icons.defaultArtworkSize.value.toInt()}dp default · ${icons.defaultStrokeViewportUnits}u stroke",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                )
                Text(
                    "Live area ${icons.liveAreaStart.toInt()}–${icons.liveAreaEnd.toInt()} · target ${icons.minimumInteractiveTarget.value.toInt()}dp",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                )
                Text(
                    "Optical overshoot ≤ ${icons.maximumOpticalOvershoot}u · center offset ≤ ${icons.maximumOpticalCenterOffset}u",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMuted,
                )
            }
        }
    }
}

@Composable
private fun MotionReference() {
    val colors = BestMatesDesign.colors
    val motion = BestMatesDesign.motion
    val rows = listOf(
        MotionRow("Instant", motion.durations.motionInstant, "linear"),
        MotionRow("Press", motion.durations.motionPress, "standard · cubic(0.2, 0, 0, 1)"),
        MotionRow("Release", motion.durations.motionRelease, "standard · cubic(0.2, 0, 0, 1)"),
        MotionRow("State", motion.durations.motionState, "standard · cubic(0.2, 0, 0, 1)"),
        MotionRow("Exit", motion.durations.motionExit, "exit · cubic(0.4, 0, 1, 1)"),
        MotionRow("Enter", motion.durations.motionEnter, "enter · cubic(0, 0, 0.2, 1)"),
        MotionRow("Navigate", motion.durations.motionNavigate, "standard · cubic(0.2, 0, 0, 1)"),
        MotionRow("Theme", motion.durations.motionTheme, "standard · cubic(0.2, 0, 0, 1)"),
    )
    Column(verticalArrangement = Arrangement.spacedBy(BestMatesDesign.dimensions.spacing.space2)) {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(BestMatesDesign.dimensions.radii.radiusSm))
                    .background(colors.surfaceRaised)
                    .padding(
                        horizontal = BestMatesDesign.dimensions.spacing.space3,
                        vertical = BestMatesDesign.dimensions.spacing.space2,
                    ),
                horizontalArrangement = Arrangement.spacedBy(BestMatesDesign.dimensions.spacing.space3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(row.name, modifier = Modifier.width(64.dp), style = MaterialTheme.typography.labelLarge)
                Text("${row.durationMillis}ms", modifier = Modifier.width(48.dp), style = MaterialTheme.typography.labelMedium, color = colors.accent)
                Text(row.easing, modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
            }
        }
    }
}

private data class MotionRow(val name: String, val durationMillis: Int, val easing: String)

private val BestMatesVisualIdentity.displayName: String
    get() = when (this) {
        BestMatesVisualIdentity.CLOUD -> "Cloud"
        BestMatesVisualIdentity.DREAM -> "Dream"
        BestMatesVisualIdentity.MIDNIGHT -> "Midnight"
        BestMatesVisualIdentity.MONO -> "Mono"
    }

private val BestMatesVisualState.displayName: String
    get() = when (this) {
        BestMatesVisualState.RESTING -> "Resting"
        BestMatesVisualState.HOVERED -> "Hovered"
        BestMatesVisualState.PRESSED -> "Pressed ref"
        BestMatesVisualState.SELECTED -> "Selected"
        BestMatesVisualState.LOCKED -> "Locked"
        BestMatesVisualState.DESTRUCTIVE -> "Destructive"
        BestMatesVisualState.ERROR -> "Error"
        BestMatesVisualState.LOADING -> "Loading"
        BestMatesVisualState.DISABLED -> "Disabled"
        BestMatesVisualState.FOCUSED -> "Focused"
    }
