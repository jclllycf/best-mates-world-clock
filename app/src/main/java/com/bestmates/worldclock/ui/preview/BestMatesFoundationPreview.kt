@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.bestmates.worldclock.ui.preview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.ui.icons.OrbitIconMetrics
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesBrightnessMode
import com.bestmates.worldclock.ui.theme.BestMatesColors
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import com.bestmates.worldclock.ui.theme.BestMatesDesignTheme
import com.bestmates.worldclock.ui.theme.BestMatesDimensionTokens
import com.bestmates.worldclock.ui.theme.BestMatesElevationTokens
import com.bestmates.worldclock.ui.theme.BestMatesStateTokens
import com.bestmates.worldclock.ui.theme.BestMatesVisualIdentity
import com.bestmates.worldclock.ui.theme.BestMatesVisualState

@Preview(
    name = "Best Mates — Cute Atelier",
    showBackground = true,
    backgroundColor = 0xFFFFF8FB,
    widthDp = 393,
    heightDp = 852,
)
@Composable
private fun BestMatesFoundationPreview() {
    BestMatesFoundationDesignLab()
}

/**
 * Phase 2C visual-study host. Android Studio preview and the debug-only device
 * Activity are its only callers; production navigation remains disconnected.
 */
@Composable
internal fun BestMatesFoundationDesignLab(
    onAppearanceChanged: ((isDark: Boolean, background: Color) -> Unit)? = null,
) {
    var identityIndex by rememberSaveable { mutableIntStateOf(0) }
    var isDark by rememberSaveable { mutableStateOf(false) }
    val identities = BestMatesVisualIdentity.entries
    val identity = identities[identityIndex]

    BestMatesDesignTheme(
        systemIsDark = isDark,
        identity = identity,
        brightnessMode = if (isDark) BestMatesBrightnessMode.DARK else BestMatesBrightnessMode.LIGHT,
    ) {
        val background = BestMatesDesign.colors.background
        SideEffect { onAppearanceChanged?.invoke(isDark, background) }
        CuteAtelierLabPage(
            identity = identity,
            isDark = isDark,
            onIdentitySelected = { identityIndex = identities.indexOf(it) },
            onBrightnessSelected = { isDark = it },
        )
    }
}

@Composable
private fun CuteAtelierLabPage(
    identity: BestMatesVisualIdentity,
    isDark: Boolean,
    onIdentitySelected: (BestMatesVisualIdentity) -> Unit,
    onBrightnessSelected: (Boolean) -> Unit,
) {
    val tokens = BestMatesDesign.current
    val colors = tokens.colors
    val dimensions = tokens.dimensions

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
    ) {
        AtelierAtmosphere(colors, tokens.elevation)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = dimensions.shared.compactScreenGutter,
                    vertical = dimensions.spacing.space6,
                ),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space7),
        ) {
            AtelierHero(identity, isDark, colors, dimensions)
            ThemeMatrixSelector(
                identity = identity,
                isDark = isDark,
                onIdentitySelected = onIdentitySelected,
                onBrightnessSelected = onBrightnessSelected,
            )

            ChapterLabel("EXPERIENCE SHOWCASE", "Cute atmosphere · professional skeleton")
            ShowcaseSection("A warm welcome", "Personal, polished, and ready to make something.") {
                WelcomeSample(colors, dimensions)
            }
            ShowcaseSection("Workspace", "Floating creation space") {
                WorkspaceSample(colors, dimensions)
            }
            ShowcaseSection("Theme gallery", "A visual choice, presented as a collectible card.") {
                ThemeCardSample(identity, colors, dimensions)
            }
            ShowcaseSection("Editor tools", "Quiet actions with a recognisable Orbit Charm rhythm.") {
                EditorToolbarSample(colors, dimensions)
            }
            ShowcaseSection("Soft inspector", "A clear editing surface with stationery-like controls.") {
                InspectorSample(colors, dimensions)
            }
            ShowcaseSection("Picker tray", "A floating surface with stronger separation and calm choices.") {
                PickerSample(colors, dimensions)
            }
            ShowcaseSection("Orbit Charm", "The full reusable foundation family at 20dp artwork.") {
                IconFamilySample(colors, dimensions, tokens.icons)
            }
            ShowcaseSection("Soft Halo 2.0", "Selection stays clear with or without decorative light.") {
                HaloComparison(colors, tokens.states, tokens.elevation, dimensions)
            }
            ShowcaseSection("Future canvas language", "A neutral working mat with precise, quiet guidance.") {
                CanvasLanguageSample(colors, dimensions)
            }
            ShowcaseSection("Color atelier", "Continuous HSV + opacity, with a separate recommended palette.") {
                ColorPickerSample(colors, dimensions)
            }
            ShowcaseSection("Atelier controls", "Custom recipes replace the stock Material sample appearance.") {
                ControlShowcase(colors, dimensions, tokens.states)
            }

            ChapterLabel("TOKEN DIAGNOSTICS", "Compact reference · resolved ${identity.displayName} ${if (isDark) "Dark" else "Light"}")
            DiagnosticSection("Surface ladder") {
                SurfaceDiagnostic(colors, dimensions)
            }
            DiagnosticSection("Semantic color") {
                ColorDiagnostic(colors, dimensions)
            }
            DiagnosticSection("Typography") {
                TypographyDiagnostic(colors, dimensions)
            }
            DiagnosticSection("Spacing & radii") {
                SpacingAndRadiusDiagnostic(colors, dimensions)
            }
            DiagnosticSection("State system") {
                StateDiagnostic(colors, tokens.states, dimensions)
            }
            DiagnosticSection("Quiet Motion") {
                MotionDiagnostic(colors, dimensions)
            }
            Spacer(Modifier.height(dimensions.spacing.space6))
        }
    }
}

@Composable
private fun AtelierAtmosphere(colors: BestMatesColors, elevation: BestMatesElevationTokens) {
    Canvas(Modifier.fillMaxSize()) {
        val peak = elevation.atmospherePeakAlpha
        val radius = size.width * 0.95f
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(colors.atmosphereA.copy(alpha = peak), Color.Transparent),
                center = Offset(size.width * 0.08f, size.height * 0.08f),
                radius = radius,
            ),
        )
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(colors.atmosphereB.copy(alpha = peak * 0.78f), Color.Transparent),
                center = Offset(size.width * 0.94f, size.height * 0.58f),
                radius = radius,
            ),
        )
        drawArc(
            color = colors.accent.copy(alpha = if (colors.isDark) 0.08f else 0.06f),
            startAngle = 210f,
            sweepAngle = 95f,
            useCenter = false,
            topLeft = Offset(size.width * 0.58f, -size.width * 0.34f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.7f, size.width * 0.7f),
            style = Stroke(width = 1.dp.toPx()),
        )
    }
}

@Composable
private fun AtelierHero(
    identity: BestMatesVisualIdentity,
    isDark: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radius3xl)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = BestMatesDesign.elevation.depth2.elevation,
                shape = shape,
                ambientColor = colors.shadowColor.copy(alpha = BestMatesDesign.elevation.depth2.shadowAlpha),
                spotColor = colors.shadowColor.copy(alpha = BestMatesDesign.elevation.depth2.shadowAlpha),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(colors.surfaceRaised, colors.accentContainer.copy(alpha = if (colors.isDark) 0.62f else 0.72f)),
                ),
            )
            .border(1.dp, colors.borderSubtle, shape)
            .padding(dimensions.spacing.space6),
    ) {
        Column(
            modifier = Modifier.padding(end = dimensions.spacing.space8),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        ) {
            Text("BEST MATES", style = MaterialTheme.typography.labelMedium, color = colors.secondary)
            Text("Cute Atelier", style = MaterialTheme.typography.displaySmall, color = colors.textPrimary)
            Text(
                "A cozy orbit for creating the worlds you keep close.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textSecondary,
            )
            Spacer(Modifier.height(dimensions.spacing.space2))
            MiniTag("${identity.displayName} · ${if (isDark) "Night study" else "Day study"}", colors, dimensions)
        }
        OrbitSparkle(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(72.dp),
            colors = colors,
        )
    }
}

@Composable
private fun OrbitSparkle(modifier: Modifier, colors: BestMatesColors) {
    Canvas(modifier) {
        drawArc(
            color = colors.accent.copy(alpha = 0.55f),
            startAngle = 200f,
            sweepAngle = 230f,
            useCenter = false,
            topLeft = Offset(size.width * 0.08f, size.height * 0.13f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.78f, size.height * 0.62f),
            style = Stroke(width = 1.5.dp.toPx()),
        )
        drawCircle(colors.secondary, radius = 3.dp.toPx(), center = Offset(size.width * 0.82f, size.height * 0.37f))
        drawLine(colors.tertiary, Offset(size.width * 0.24f, size.height * 0.08f), Offset(size.width * 0.24f, size.height * 0.25f), 1.4.dp.toPx())
        drawLine(colors.tertiary, Offset(size.width * 0.16f, size.height * 0.165f), Offset(size.width * 0.32f, size.height * 0.165f), 1.4.dp.toPx())
    }
}

@Composable
private fun ThemeMatrixSelector(
    identity: BestMatesVisualIdentity,
    isDark: Boolean,
    onIdentitySelected: (BestMatesVisualIdentity) -> Unit,
    onBrightnessSelected: (Boolean) -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceBase,
        radius = dimensions.radii.radius2xl,
        padding = dimensions.spacing.space4,
    ) {
        Text("Foundation study", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        Text("Choose one of eight resolved atmospheres", style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        ) {
            BestMatesVisualIdentity.entries.forEach { option ->
                ChoiceChip(
                    label = option.displayName,
                    selected = option == identity,
                    onClick = { onIdentitySelected(option) },
                    colors = colors,
                    dimensions = dimensions,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
            ChoiceChip("Light", !isDark, { onBrightnessSelected(false) }, colors, dimensions)
            ChoiceChip("Dark", isDark, { onBrightnessSelected(true) }, colors, dimensions)
        }
    }
}

@Composable
private fun ChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = dimensions.radii.radiusFull
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .heightIn(min = dimensions.shared.minimumTouchTarget)
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(34.dp)
                .atelierCrystalSurface(
                    baseColor = if (selected) colors.accentContainer else colors.surfaceInset,
                    shape = shape,
                    level = AtelierCrystalLevel.PearlGlass,
                    pressed = pressed,
                    borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                    borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                    intensity = if (selected) 0.92f else 0.58f,
                )
                .padding(horizontal = dimensions.spacing.space4),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selected) {
                Box(
                    Modifier
                        .size(BestMatesDesign.states.selectionIndicatorDotSize)
                        .clip(CircleShape)
                        .background(colors.accent),
                )
            }
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) colors.onAccentContainer else colors.textSecondary,
            )
        }
    }
}

@Composable
private fun ChapterLabel(title: String, subtitle: String) {
    val colors = BestMatesDesign.colors
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = colors.accent)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
    }
}

@Composable
private fun ShowcaseSection(
    title: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
        Text(title, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
        Text(description, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
        content()
    }
}

@Composable
private fun DiagnosticSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceBase,
        radius = dimensions.radii.radiusXl,
        padding = dimensions.spacing.space4,
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
        content()
    }
}

@Composable
private fun AtelierSurface(
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    modifier: Modifier = Modifier,
    color: Color = colors.surfaceRaised,
    radius: Dp = dimensions.radii.radiusXl,
    elevation: Dp = 0.dp,
    padding: Dp = dimensions.spacing.space5,
    borderColor: Color = colors.borderSubtle,
    crystalLevel: AtelierCrystalLevel? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(radius)
    val surfaceTreatment = if (crystalLevel != null) {
        Modifier.atelierCrystalSurface(
            baseColor = color,
            shape = shape,
            level = crystalLevel,
            borderColor = borderColor,
            intensity = 0.72f,
        )
    } else {
        Modifier
            .then(
                if (elevation > 0.dp) {
                    Modifier.shadow(
                        elevation = elevation,
                        shape = shape,
                        clip = false,
                        ambientColor = colors.shadowColor.copy(alpha = 0.14f),
                        spotColor = colors.shadowColor.copy(alpha = 0.14f),
                    )
                } else {
                    Modifier
                },
            )
            .clip(shape)
            .background(color)
            .border(1.dp, borderColor, shape)
    }
    Column(
        modifier = modifier
            .then(surfaceTreatment)
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
        content = content,
    )
}

@Composable
private fun WelcomeSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceRaised,
        radius = dimensions.radii.radius2xl,
        elevation = BestMatesDesign.elevation.depth1.elevation,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(dimensions.radii.radiusLg))
                    .background(colors.accentContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(OrbitIcons.Sun, null, tint = colors.accent, modifier = Modifier.size(24.dp))
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensions.spacing.space4),
            ) {
                Text("Good evening", style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                Text("Your favorite places are close.", style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            }
            Icon(OrbitIcons.Settings, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun WorkspaceSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(colors, dimensions, color = colors.surfaceBase, radius = dimensions.radii.radius2xl) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Your worlds", style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
                Text("2 little time companions", style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
            }
            Icon(OrbitIcons.Search, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
        }
        MiniProjectCard("Tokyo & Home", "Tokyo · Shanghai", true, colors, dimensions)
        MiniProjectCard("Slow Sunday", "Paris · Seoul", false, colors, dimensions)
        AtelierButton("Create widget", AtelierButtonKind.Primary, OrbitIcons.CreateWidget, colors, dimensions)
    }
}

@Composable
private fun MiniProjectCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusLg)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = {},
            )
            .atelierCrystalSurface(
                baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                shape = shape,
                level = AtelierCrystalLevel.MilkCrystal,
                pressed = pressed,
                borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (selected) 0.92f else 0.68f,
            )
            .padding(dimensions.spacing.space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(dimensions.radii.radiusMd))
                .background(colors.canvasMat),
            contentAlignment = Alignment.Center,
        ) {
            Text("18:40", style = MaterialTheme.typography.labelSmall, color = colors.onCanvasMat)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = dimensions.spacing.space3),
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
        }
        if (selected) {
            Icon(
                OrbitIcons.Confirm,
                null,
                tint = colors.accent,
                modifier = Modifier.size(BestMatesDesign.states.selectionCheckIconSize),
            )
        }
    }
}

@Composable
private fun ThemeCardSample(
    identity: BestMatesVisualIdentity,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radius2xl)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = true,
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = {},
            )
            .atelierCrystalSurface(
                baseColor = colors.selectionFill,
                shape = shape,
                level = AtelierCrystalLevel.MilkCrystal,
                pressed = pressed,
                borderColor = colors.selectionOutline,
                borderWidth = BestMatesDesign.states.selectedOutlineWidth,
                intensity = 0.96f,
            )
            .padding(dimensions.spacing.space5),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(identity.displayName, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
                    Text(identity.moodLine, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                }
                Box(
                    Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(colors.accentContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        OrbitIcons.Confirm,
                        null,
                        tint = colors.accent,
                        modifier = Modifier.size(BestMatesDesign.states.selectionCheckCompactIconSize),
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
                listOf(colors.background, colors.surfaceInset, colors.accentContainer, colors.secondaryContainer, colors.accent).forEach {
                    Box(Modifier.weight(1f).height(34.dp).clip(RoundedCornerShape(dimensions.radii.radiusSm)).background(it))
                }
            }
        }
    }
}

@Composable
private fun EditorToolbarSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(colors, dimensions, color = colors.surfaceRaised, radius = dimensions.radii.radiusXl, padding = dimensions.spacing.space3, crystalLevel = AtelierCrystalLevel.MilkCrystal) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OrbitAction(OrbitIcons.Back, "Back", colors, dimensions)
            Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
                OrbitAction(OrbitIcons.Undo, "Undo", colors, dimensions)
                OrbitAction(OrbitIcons.Redo, "Redo", colors, dimensions)
                OrbitAction(OrbitIcons.AddComponent, "Add", colors, dimensions, prominent = true)
                OrbitAction(OrbitIcons.Settings, "Settings", colors, dimensions)
            }
        }
    }
}

@Composable
private fun OrbitAction(
    icon: ImageVector,
    description: String,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    prominent: Boolean = false,
    pressedReference: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressed = pressedReference || isPressed
    val shape = RoundedCornerShape(if (prominent) dimensions.radii.radiusMd else dimensions.radii.radiusSm)
    val container = if (prominent) colors.accentContainer else colors.surfaceInset
    val border = if (prominent) colors.accent else colors.borderSubtle
    Box(
        modifier = Modifier
            .requiredSize(dimensions.shared.minimumTouchTarget)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(if (prominent) 38.dp else 34.dp)
                .atelierCrystalSurface(
                    baseColor = container,
                    shape = shape,
                    level = AtelierCrystalLevel.PearlGlass,
                    pressed = pressed,
                    borderColor = border,
                    intensity = if (prominent) 0.88f else 0.62f,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, description, tint = if (prominent) colors.accent else colors.textSecondary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun InspectorSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(colors, dimensions, color = colors.surfaceRaised, radius = dimensions.radii.radiusXl, crystalLevel = AtelierCrystalLevel.MilkCrystal) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(BestMatesDesign.states.selectionIndicatorDotSize)
                    .clip(CircleShape)
                    .background(colors.secondary),
            )
            Text("Selected city", modifier = Modifier.padding(start = dimensions.spacing.space2), style = MaterialTheme.typography.labelMedium, color = colors.textMuted)
            Spacer(Modifier.weight(1f))
            Icon(OrbitIcons.Rename, null, tint = colors.textSecondary, modifier = Modifier.size(18.dp))
        }
        Text("Tokyo", style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
        AtelierField("City label", "Tokyo", AtelierFieldState.Resting, colors, dimensions)
        Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
            AtelierChip("Time", true, colors, dimensions)
            AtelierChip("Date", true, colors, dimensions)
            AtelierChip("Country", false, colors, dimensions)
        }
        AtelierSlider("Scale", "72%", 0.72f, colors, dimensions)
    }
}

@Composable
private fun PickerSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceOverlay,
        radius = dimensions.radii.radius3xl,
        elevation = BestMatesDesign.elevation.depth3.elevation,
        padding = dimensions.spacing.space6,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Choose a mood", style = MaterialTheme.typography.headlineSmall, color = colors.textPrimary)
                Text("A tiny tray of quiet palettes", style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
            }
            OrbitAction(OrbitIcons.Close, "Close", colors, dimensions)
        }
        PickerRow("Pearl morning", colors.accentContainer, true, colors, dimensions)
        PickerRow("Rose postcard", colors.secondaryContainer, false, colors, dimensions)
        PickerRow("Peach hour", colors.tertiaryContainer, false, colors, dimensions)
        AtelierButton("Apply", AtelierButtonKind.Primary, OrbitIcons.Confirm, colors, dimensions)
    }
}

@Composable
private fun PickerRow(
    label: String,
    swatch: Color,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusMd)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimensions.shared.minimumTouchTarget)
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = {},
            )
            .atelierCrystalSurface(
                baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                shape = shape,
                level = AtelierCrystalLevel.MilkCrystal,
                pressed = pressed,
                borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (selected) 0.88f else 0.60f,
            )
            .padding(dimensions.spacing.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(34.dp).clip(RoundedCornerShape(dimensions.radii.radiusSm)).background(swatch))
        Text(label, modifier = Modifier.weight(1f).padding(start = dimensions.spacing.space3), style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
        if (selected) {
            Icon(
                OrbitIcons.Confirm,
                null,
                tint = colors.accent,
                modifier = Modifier.size(BestMatesDesign.states.selectionCheckIconSize),
            )
        }
    }
}

@Composable
private fun IconFamilySample(
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    metrics: OrbitIconMetrics,
) {
    AtelierSurface(colors, dimensions, color = colors.surfaceBase, radius = dimensions.radii.radius2xl, padding = dimensions.spacing.space4) {
        Text(
            "24×24 viewport · ${metrics.defaultStrokeViewportUnits}u stroke · ${metrics.minimumInteractiveTarget.value.toInt()}dp target",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
        ) {
            orbitIconSpecs.forEach { spec -> IconFamilyCell(spec, colors, dimensions) }
        }
    }
}

@Composable
private fun IconFamilyCell(spec: OrbitIconSpec, colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val iconShape = RoundedCornerShape(dimensions.radii.radiusMd)
    Column(
        modifier = Modifier.width(74.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1),
    ) {
        Box(
            modifier = Modifier
                .requiredSize(dimensions.shared.minimumTouchTarget)
                .atelierCrystalSurface(
                    baseColor = colors.surfaceRaised,
                    shape = iconShape,
                    level = AtelierCrystalLevel.PearlGlass,
                    borderColor = colors.borderSubtle,
                    intensity = 0.42f,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(spec.icon, spec.label, tint = colors.textPrimary, modifier = Modifier.size(20.dp))
        }
        Text(spec.label, style = MaterialTheme.typography.labelSmall, color = colors.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun HaloComparison(
    colors: BestMatesColors,
    states: BestMatesStateTokens,
    elevation: BestMatesElevationTokens,
    dimensions: BestMatesDimensionTokens,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space4),
    ) {
        HaloCard("With halo", true, colors, states, elevation, dimensions, Modifier.weight(1f))
        HaloCard("Without", false, colors, states, elevation, dimensions, Modifier.weight(1f))
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
    val shape = RoundedCornerShape(dimensions.radii.radiusXl)
    val halo = elevation.softHalo
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
    ) {
        Box(
            modifier = Modifier
                .padding(halo.maximumSpread + 4.dp)
                .then(
                    if (showHalo) {
                        Modifier
                            .drawBehind {
                                drawRoundRect(
                                    color = colors.accent.copy(alpha = halo.alpha * 0.52f),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(dimensions.radii.radiusXl.toPx()),
                                )
                            }
                            .shadow(
                                elevation = halo.blur,
                                shape = shape,
                                clip = false,
                                ambientColor = colors.accent.copy(alpha = halo.alpha),
                                spotColor = colors.secondary.copy(alpha = halo.alpha),
                            )
                    } else {
                        Modifier
                    },
                )
                .fillMaxWidth()
                .height(106.dp)
                .atelierCrystalSurface(
                    baseColor = states.colors.selectionFill,
                    shape = shape,
                    level = AtelierCrystalLevel.MilkCrystal,
                    borderColor = states.colors.selectionOutline,
                    borderWidth = states.selectedOutlineWidth,
                    intensity = 0.86f,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(states.selectionIndicatorDotSize)
                        .clip(CircleShape)
                        .background(colors.secondary),
                )
                Spacer(Modifier.height(dimensions.spacing.space2))
                Text("Selected", style = MaterialTheme.typography.labelLarge, color = states.colors.selectionContent)
            }
        }
        Text(label, style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
    }
}

@Composable
private fun CanvasLanguageSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val shape = RoundedCornerShape(dimensions.radii.radius2xl)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(244.dp)
            .clip(shape)
            .background(colors.canvasMat)
            .border(1.dp, colors.borderSubtle, shape),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val divisions = 8
            repeat(divisions + 1) { index ->
                val x = size.width * index / divisions
                val y = size.height * index / divisions
                drawLine(colors.guide.copy(alpha = 0.09f), Offset(x, 0f), Offset(x, size.height), 0.7.dp.toPx())
                drawLine(colors.guide.copy(alpha = 0.09f), Offset(0f, y), Offset(size.width, y), 0.7.dp.toPx())
            }
            drawLine(colors.guide.copy(alpha = 0.62f), Offset(size.width / 2f, 18.dp.toPx()), Offset(size.width / 2f, size.height - 18.dp.toPx()), 1.dp.toPx())
            drawCircle(colors.guide.copy(alpha = 0.78f), 2.5.dp.toPx(), Offset(size.width / 2f, size.height / 2f))
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(206.dp)
                .height(104.dp)
                .atelierCrystalSurface(
                    baseColor = colors.surfaceRaised,
                    shape = RoundedCornerShape(dimensions.radii.radiusLg),
                    level = AtelierCrystalLevel.MilkCrystal,
                    borderColor = colors.selectionOutline,
                    borderWidth = BestMatesDesign.states.selectedOutlineWidth,
                    intensity = 0.90f,
                )
                .padding(dimensions.spacing.space4),
        ) {
            Column(Modifier.align(Alignment.CenterStart)) {
                Text("TOKYO", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                Text("18:40", style = BestMatesDesign.typography.valueTime, color = colors.textPrimary)
            }
        }
        CanvasHandle(OrbitIcons.Rotate, Alignment.TopEnd, colors, dimensions, Modifier.padding(top = 38.dp, end = 48.dp))
        CanvasHandle(OrbitIcons.Resize, Alignment.BottomEnd, colors, dimensions, Modifier.padding(bottom = 42.dp, end = 48.dp))
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = dimensions.spacing.space4, bottom = dimensions.spacing.space4)
                .atelierCrystalSurface(
                    baseColor = colors.surfaceOverlay,
                    shape = RoundedCornerShape(dimensions.radii.radiusSm),
                    level = AtelierCrystalLevel.PearlGlass,
                    borderColor = colors.borderSubtle,
                    intensity = 0.66f,
                )
                .padding(horizontal = dimensions.spacing.space3, vertical = dimensions.spacing.space2),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(OrbitIcons.Guides, null, tint = colors.accent, modifier = Modifier.size(14.dp))
            Text("≡ Same size", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
        }
    }
}

@Composable
private fun CanvasHandle(
    icon: ImageVector,
    alignment: Alignment,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    modifier: Modifier,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = alignment) {
        Box(modifier.requiredSize(dimensions.shared.minimumTouchTarget), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .size(30.dp)
                    .atelierCrystalSurface(
                        baseColor = colors.surfaceOverlay,
                        shape = CircleShape,
                        level = AtelierCrystalLevel.PearlGlass,
                        borderColor = colors.borderControl,
                        intensity = 0.86f,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, null, tint = colors.accent, modifier = Modifier.size(17.dp))
            }
        }
    }
}

@Composable
private fun ColorPickerSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceOverlay,
        radius = dimensions.radii.radius3xl,
        elevation = BestMatesDesign.elevation.depth2.elevation,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Text("Color atelier", style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
        Text("Full spectrum HSV · separate brightness · independent opacity", style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space5),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorWheel(colors)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
            ) {
                Text("Recommended", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
                    ColorSwatch(colors.accent, true, colors, dimensions)
                    ColorSwatch(colors.secondary, false, colors, dimensions)
                }
                Text("Plum veil", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                Text("Palette shortcut", style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            }
        }
        AtelierSlider("Brightness", "86%", 0.86f, colors, dimensions)
        AtelierSlider("Opacity", "72%", 0.72f, colors, dimensions)
        AtelierButton("Apply color", AtelierButtonKind.Primary, OrbitIcons.Confirm, colors, dimensions)
    }
}

@Composable
private fun ColorWheel(colors: BestMatesColors) {
    val hueSweep = remember {
        listOf(Color.Red, Color.Magenta, Color.Blue, Color.Cyan, Color.Green, Color.Yellow, Color.Red)
    }
    Canvas(Modifier.size(126.dp)) {
        val radius = size.minDimension / 2f
        drawCircle(brush = Brush.sweepGradient(hueSweep), radius = radius)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, Color.White.copy(alpha = 0.72f), Color.Transparent),
                center = center,
                radius = radius,
            ),
            radius = radius,
        )
        drawCircle(colors.borderControl.copy(alpha = 0.72f), radius = radius, style = Stroke(1.dp.toPx()))
        drawCircle(colors.accent, radius = 5.dp.toPx(), center = Offset(size.width * 0.76f, size.height * 0.21f))
        drawCircle(colors.surfaceOverlay, radius = 2.2.dp.toPx(), center = Offset(size.width * 0.76f, size.height * 0.21f))
    }
}

@Composable
private fun ColorSwatch(
    color: Color,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Box(
        Modifier
            .requiredSize(dimensions.shared.minimumTouchTarget)
            .selectable(
                selected = selected,
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        val shape = RoundedCornerShape(dimensions.radii.radiusSm)
        Box(
            modifier = Modifier
                .size(36.dp)
                .atelierCrystalSurface(
                    baseColor = colors.surfaceRaised,
                    shape = shape,
                    level = AtelierCrystalLevel.PearlGlass,
                    pressed = pressed,
                    borderColor = if (selected) colors.selectionOutline else colors.borderControl,
                    borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                    intensity = if (selected) 0.92f else 0.56f,
                ),
        ) {
            Box(
                Modifier
                    .padding(3.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(dimensions.radii.radiusXs))
                    .background(color),
            )
            if (selected) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(colors.accentContainer)
                        .border(1.dp, colors.selectionOutline, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        OrbitIcons.Confirm,
                        null,
                        tint = colors.onAccentContainer,
                        modifier = Modifier.size(BestMatesDesign.states.selectionCheckCompactIconSize),
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlShowcase(colors: BestMatesColors, dimensions: BestMatesDimensionTokens, states: BestMatesStateTokens) {
    AtelierSurface(colors, dimensions, color = colors.surfaceBase, radius = dimensions.radii.radius2xl) {
        Text("Soft Crystal action depth", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        Text("Resting / Pressed reference", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
        ButtonDepthPair("Primary", "Create", AtelierButtonKind.Primary, OrbitIcons.CreateWidget, colors, dimensions)
        ButtonDepthPair("Secondary", "Add", AtelierButtonKind.Secondary, OrbitIcons.AddComponent, colors, dimensions)
        ButtonDepthPair("Outlined", "Import", AtelierButtonKind.Outlined, OrbitIcons.Font, colors, dimensions)
        IconDepthPair(colors, dimensions)
        Text("Quiet actions", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        AtelierButton("Cancel", AtelierButtonKind.Text, null, colors, dimensions)
        AtelierButton("Delete", AtelierButtonKind.Destructive, OrbitIcons.Delete, colors, dimensions)
        Text("Accessory toggles", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        ToggleRow("Reduce motion", false, true, colors, dimensions)
        ToggleRow("Gentle glow", true, true, colors, dimensions)
        ToggleRow("Disabled option", false, false, colors, dimensions)
        AtelierSlider("Glow strength", "40%", 0.4f, colors, dimensions)
        Text("Chips", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
            AtelierChip("Cloud", true, colors, dimensions)
            AtelierChip("Dream", false, colors, dimensions)
            AtelierChip("Mono", false, colors, dimensions, enabled = false)
        }
        Text("Stationery fields", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        AtelierField("Widget name", "Tokyo & Home", AtelierFieldState.Resting, colors, dimensions)
        AtelierField("Widget name", "Tokyo & Home", AtelierFieldState.Focused, colors, dimensions)
        AtelierField("Widget name", "", AtelierFieldState.Error, colors, dimensions, "A widget name is required.")
        Text(
            "Disabled content ${(states.disabledContentAlpha * 100).toInt()}% · container ${(states.disabledContainerAlpha * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
        )
    }
}

@Composable
private fun ButtonDepthPair(
    title: String,
    label: String,
    kind: AtelierButtonKind,
    icon: ImageVector,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Text(title, style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        DepthStateColumn("Resting", colors, Modifier.weight(1f)) {
            AtelierButton(label, kind, icon, colors, dimensions, compact = true)
        }
        DepthStateColumn("Pressed", colors, Modifier.weight(1f)) {
            AtelierButton(label, kind, icon, colors, dimensions, pressedReference = true, compact = true)
        }
    }
}

@Composable
private fun IconDepthPair(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    Text("Icon action", style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        DepthStateColumn("Resting", colors, Modifier.weight(1f)) {
            OrbitAction(OrbitIcons.AddComponent, "Resting icon action", colors, dimensions, prominent = true)
        }
        DepthStateColumn("Pressed", colors, Modifier.weight(1f)) {
            OrbitAction(
                OrbitIcons.AddComponent,
                "Pressed icon action",
                colors,
                dimensions,
                prominent = true,
                pressedReference = true,
            )
        }
    }
}

@Composable
private fun DepthStateColumn(
    label: String,
    colors: BestMatesColors,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
        content()
    }
}

private enum class AtelierButtonKind { Primary, Secondary, Outlined, Text, Destructive }
private enum class AtelierCrystalLevel { PearlGlass, MilkCrystal, JellyCrystal }

@Composable
private fun AtelierButton(
    label: String,
    kind: AtelierButtonKind,
    icon: ImageVector?,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    modifier: Modifier = Modifier,
    pressedReference: Boolean = false,
    compact: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressed = pressedReference || isPressed
    val container = when (kind) {
        AtelierButtonKind.Primary -> colors.accent
        AtelierButtonKind.Secondary -> colors.secondaryContainer
        AtelierButtonKind.Outlined -> colors.surfaceRaised
        AtelierButtonKind.Text -> Color.Transparent
        AtelierButtonKind.Destructive -> colors.destructiveContainer
    }
    val content = when (kind) {
        AtelierButtonKind.Primary -> colors.onAccent
        AtelierButtonKind.Secondary -> colors.onSecondaryContainer
        AtelierButtonKind.Outlined -> colors.textPrimary
        AtelierButtonKind.Text -> colors.accent
        AtelierButtonKind.Destructive -> colors.onDestructiveContainer
    }
    val borderColor = when (kind) {
        AtelierButtonKind.Primary -> colors.secondary
        AtelierButtonKind.Secondary -> colors.secondary
        AtelierButtonKind.Outlined -> colors.borderControl
        AtelierButtonKind.Text -> Color.Transparent
        AtelierButtonKind.Destructive -> colors.destructive
    }
    val shape = RoundedCornerShape(if (kind == AtelierButtonKind.Text) dimensions.radii.radiusMd else dimensions.radii.radiusXl)
    val textPressedAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.42f else 0f,
        animationSpec = tween(
            durationMillis = if (pressed) BestMatesDesign.motion.durations.motionPress else BestMatesDesign.motion.durations.motionRelease,
            easing = BestMatesDesign.motion.easings.easeStandard,
        ),
        label = "text button pressed fill",
    )
    val crystalLevel = when (kind) {
        AtelierButtonKind.Primary -> AtelierCrystalLevel.JellyCrystal
        AtelierButtonKind.Secondary -> AtelierCrystalLevel.MilkCrystal
        AtelierButtonKind.Outlined -> AtelierCrystalLevel.PearlGlass
        AtelierButtonKind.Text -> null
        AtelierButtonKind.Destructive -> AtelierCrystalLevel.MilkCrystal
    }
    val surfaceModifier = if (crystalLevel == null) {
        Modifier
            .clip(shape)
            .background(colors.accentContainer.copy(alpha = textPressedAlpha))
    } else {
        Modifier.atelierCrystalSurface(
            baseColor = container,
            shape = shape,
            level = crystalLevel,
            pressed = pressed,
            borderColor = borderColor,
            intensity = when (kind) {
                AtelierButtonKind.Primary -> 1f
                AtelierButtonKind.Secondary, AtelierButtonKind.Destructive -> 0.82f
                AtelierButtonKind.Outlined -> 0.62f
                AtelierButtonKind.Text -> 0f
            },
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (kind == AtelierButtonKind.Text) dimensions.shared.minimumTouchTarget else 54.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = {},
            )
            .then(surfaceModifier)
            .padding(horizontal = if (compact) dimensions.spacing.space3 else dimensions.spacing.space5),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(icon, null, tint = content, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(dimensions.spacing.space2))
        }
        Text(label, style = MaterialTheme.typography.labelLarge, color = content)
    }
}

@Composable
private fun Modifier.atelierCrystalSurface(
    baseColor: Color,
    shape: Shape,
    level: AtelierCrystalLevel,
    pressed: Boolean = false,
    borderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.dp,
    intensity: Float = 1f,
): Modifier {
    val colors = BestMatesDesign.colors
    val motion = BestMatesDesign.motion
    val crystal = BestMatesDesign.elevation.crystalDepth
    val recipe = when (level) {
        AtelierCrystalLevel.PearlGlass -> crystal.pearlGlass
        AtelierCrystalLevel.MilkCrystal -> crystal.milkCrystal
        AtelierCrystalLevel.JellyCrystal -> crystal.jellyCrystal
    }
    val duration = if (pressed) motion.durations.motionPress else motion.durations.motionRelease
    val easing = motion.easings.easeStandard
    val elevation by animateDpAsState(
        targetValue = if (pressed) crystal.pressedElevation else recipe.elevation * intensity,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button elevation",
    )
    val translationY by animateDpAsState(
        targetValue = if (pressed && motion.spatialMotionEnabled) crystal.pressedTranslationY else 0.dp,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button translation",
    )
    val scale by animateFloatAsState(
        targetValue = if (pressed && motion.spatialMotionEnabled) crystal.pressedScale else 1f,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button scale",
    )
    val pressedDarken by animateFloatAsState(
        targetValue = if (pressed) crystal.pressedDarkenMix else 0f,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button pressed tone",
    )
    val shadowAlpha by animateFloatAsState(
        targetValue = recipe.shadowAlpha * intensity * if (pressed) crystal.pressedShadowMultiplier else 1f,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button shadow",
    )
    val highlightAlpha by animateFloatAsState(
        targetValue = recipe.upperHighlightAlpha * intensity * if (pressed) crystal.pressedHighlightMultiplier else 1f,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button highlight",
    )
    val highlightLength by animateFloatAsState(
        targetValue = if (pressed) crystal.pressedHighlightLength else recipe.highlightLength,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button highlight length",
    )
    val refractionAlpha by animateFloatAsState(
        targetValue = recipe.lowerRefractionAlpha * intensity * if (pressed) crystal.pressedRefractionMultiplier else 1f,
        animationSpec = tween(durationMillis = duration, easing = easing),
        label = "button lower refraction",
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
    val warmReflectionAlpha = recipe.warmReflectionAlpha * intensity
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
            val themeContamination = Brush.linearGradient(
                colors = listOf(
                    colors.atmosphereA.copy(alpha = themeContaminationAlpha),
                    Color.Transparent,
                    colors.atmosphereB.copy(alpha = themeContaminationAlpha * 0.82f),
                ),
                start = Offset.Zero,
                end = Offset(size.width, size.height),
            )
            val pearlVeil = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = highlightAlpha * 0.16f),
                    Color.Transparent,
                ),
                endY = size.height * 0.46f,
            )
            val lineInset = 10.dp.toPx().coerceAtMost(size.width * 0.20f)
            val highlightEnd = (size.width * highlightLength).coerceAtLeast(lineInset)
            val lineWidth = 1.dp.toPx()
            onDrawWithContent {
                drawRect(body)
                drawRect(themeContamination)
                drawRect(pearlVeil)
                drawContent()
                drawLine(
                    color = Color.White.copy(alpha = highlightAlpha),
                    start = Offset(lineInset, lineWidth),
                    end = Offset(highlightEnd, lineWidth),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = lerp(baseColor, colors.secondary, 0.36f).copy(alpha = refractionAlpha),
                    start = Offset(lineInset, size.height - lineWidth),
                    end = Offset(size.width - lineInset, size.height - lineWidth),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = colors.tertiary.copy(alpha = warmReflectionAlpha),
                    start = Offset(size.width * 0.62f, size.height - lineWidth * 2f),
                    end = Offset(size.width * 0.82f, size.height - lineWidth * 2f),
                    strokeWidth = lineWidth,
                    cap = StrokeCap.Round,
                )
            }
        }
        .border(borderWidth, resolvedBorder.copy(alpha = borderAlpha), shape)
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    enabled: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = dimensions.shared.minimumSettingsRowHeight),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textPrimary.copy(alpha = if (enabled) 1f else BestMatesDesign.states.disabledContentAlpha),
        )
        AtelierToggle(checked, enabled, colors, dimensions)
    }
}

@Composable
private fun AtelierToggle(
    checked: Boolean,
    enabled: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val thumbExpansion by animateDpAsState(
        targetValue = if (pressed && enabled) BestMatesDesign.elevation.crystalDepth.pressedThumbExpansion else 0.dp,
        animationSpec = tween(
            durationMillis = if (pressed) BestMatesDesign.motion.durations.motionPress else BestMatesDesign.motion.durations.motionRelease,
            easing = BestMatesDesign.motion.easings.easeStandard,
        ),
        label = "crystal toggle thumb press",
    )
    Box(
        Modifier
            .requiredSize(dimensions.shared.minimumTouchTarget)
            .toggleable(
                value = checked,
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Switch,
                onValueChange = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        val track = when {
            !enabled -> colors.surfaceInset.copy(alpha = 0.58f)
            checked -> colors.accentContainer
            else -> colors.surfaceInset
        }
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(26.dp)
                .atelierCrystalSurface(
                    baseColor = track,
                    shape = dimensions.radii.radiusFull,
                    level = AtelierCrystalLevel.PearlGlass,
                    pressed = pressed,
                    borderColor = if (checked) colors.accent else colors.borderControl,
                    intensity = if (enabled) 0.82f else 0.25f,
                )
                .padding(3.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                    .size(17.dp + thumbExpansion)
                    .atelierCrystalSurface(
                        baseColor = when {
                            !enabled -> colors.textMuted.copy(alpha = 0.48f)
                            colors.isDark && checked -> lerp(colors.textPrimary, colors.accentContainer, 0.10f)
                            colors.isDark -> lerp(colors.textPrimary, colors.surfaceRaised, 0.34f)
                            else -> colors.surfaceRaised
                        },
                        shape = CircleShape,
                        level = AtelierCrystalLevel.PearlGlass,
                        pressed = pressed,
                        borderColor = if (checked) colors.accent else colors.borderControl,
                        intensity = if (enabled) 0.78f else 0.20f,
                    )
            )
        }
    }
}

@Composable
private fun AtelierSlider(
    label: String,
    valueLabel: String,
    value: Float,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
            MiniTag(valueLabel, colors, dimensions)
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.shared.minimumTouchTarget),
        ) {
            val start = 8.dp.toPx()
            val end = size.width - start
            val y = size.height / 2f
            val activeEnd = start + (end - start) * value
            val thumbLight = lerp(colors.surfaceRaised, Color.White, if (colors.isDark) 0.14f else 0.62f)
            drawLine(colors.borderSubtle.copy(alpha = 0.78f), Offset(start, y), Offset(end, y), 5.dp.toPx(), StrokeCap.Round)
            drawLine(
                brush = Brush.horizontalGradient(listOf(colors.secondary, colors.accent)),
                start = Offset(start, y),
                end = Offset(activeEnd, y),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawCircle(colors.shadowColor.copy(alpha = 0.08f), 11.dp.toPx(), Offset(activeEnd, y + 1.dp.toPx()))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(thumbLight, colors.surfaceRaised, colors.accentContainer),
                    center = Offset(activeEnd - 2.dp.toPx(), y - 3.dp.toPx()),
                    radius = 10.dp.toPx(),
                ),
                radius = 9.dp.toPx(),
                center = Offset(activeEnd, y),
            )
            drawCircle(colors.accent.copy(alpha = 0.58f), 9.dp.toPx(), Offset(activeEnd, y), style = Stroke(1.dp.toPx()))
            drawArc(
                color = Color.White.copy(alpha = if (colors.isDark) 0.18f else 0.42f),
                startAngle = 205f,
                sweepAngle = 105f,
                useCenter = false,
                topLeft = Offset(activeEnd - 6.dp.toPx(), y - 6.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(1.dp.toPx(), cap = StrokeCap.Round),
            )
        }
    }
}

@Composable
private fun MiniTag(text: String, colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val shape = RoundedCornerShape(dimensions.radii.radiusSm)
    Box(
        modifier = Modifier
            .atelierCrystalSurface(
                baseColor = colors.surfaceInset,
                shape = shape,
                level = AtelierCrystalLevel.PearlGlass,
                borderColor = colors.borderSubtle,
                intensity = 0.42f,
            )
            .padding(horizontal = dimensions.spacing.space3, vertical = dimensions.spacing.space1),
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
    }
}

@Composable
private fun AtelierChip(
    label: String,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .heightIn(min = dimensions.shared.minimumTouchTarget)
            .selectable(
                selected = selected,
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(34.dp)
                .atelierCrystalSurface(
                    baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                    shape = dimensions.radii.radiusFull,
                    level = AtelierCrystalLevel.PearlGlass,
                    pressed = pressed,
                    borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                    borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                    intensity = when {
                        !enabled -> 0.20f
                        selected -> 0.92f
                        else -> 0.54f
                    },
                )
                .padding(horizontal = dimensions.spacing.space3),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selected) {
                Box(
                    Modifier
                        .size(BestMatesDesign.states.selectionIndicatorDotSize)
                        .clip(CircleShape)
                        .background(colors.secondary),
                )
            }
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = (if (selected) colors.selectionContent else colors.textSecondary).copy(alpha = if (enabled) 1f else BestMatesDesign.states.disabledContentAlpha),
            )
        }
    }
}

private enum class AtelierFieldState { Resting, Focused, Error }

@Composable
private fun AtelierField(
    label: String,
    value: String,
    state: AtelierFieldState,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    helper: String? = null,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusLg)
    val container = if (state == AtelierFieldState.Error) colors.destructiveContainer else if (state == AtelierFieldState.Focused) colors.surfaceRaised else colors.surfaceInset
    val outline = when (state) {
        AtelierFieldState.Resting -> colors.borderSubtle
        AtelierFieldState.Focused -> colors.focusRing
        AtelierFieldState.Error -> colors.destructive
    }
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .atelierCrystalSurface(
                    baseColor = container,
                    shape = shape,
                    level = AtelierCrystalLevel.PearlGlass,
                    borderColor = outline,
                    borderWidth = when (state) {
                        AtelierFieldState.Resting -> 1.dp
                        AtelierFieldState.Focused -> BestMatesDesign.states.focusRingWidth
                        AtelierFieldState.Error -> BestMatesDesign.states.errorOutlineWidth
                    },
                    intensity = when (state) {
                        AtelierFieldState.Resting -> 0.46f
                        AtelierFieldState.Focused -> 0.94f
                        AtelierFieldState.Error -> 0.74f
                    },
                )
                .padding(horizontal = dimensions.spacing.space4, vertical = dimensions.spacing.space3),
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = if (state == AtelierFieldState.Error) colors.destructive else colors.textMuted)
            Text(if (value.isEmpty()) "Name your world" else value, style = MaterialTheme.typography.bodyLarge, color = if (value.isEmpty()) colors.textMuted else colors.textPrimary)
        }
        if (helper != null) Text(helper, style = MaterialTheme.typography.bodySmall, color = colors.destructive)
    }
}

@Composable
private fun SurfaceDiagnostic(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val surfaces = listOf(
        "background" to colors.background,
        "base" to colors.surfaceBase,
        "raised" to colors.surfaceRaised,
        "inset" to colors.surfaceInset,
        "overlay" to colors.surfaceOverlay,
        "elevated" to colors.surfaceElevated,
        "canvas" to colors.canvasMat,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        surfaces.forEach { (name, color) ->
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(58.dp)
                    .clip(RoundedCornerShape(dimensions.radii.radiusMd))
                    .background(color)
                    .border(1.dp, colors.borderSubtle, RoundedCornerShape(dimensions.radii.radiusMd))
                    .padding(dimensions.spacing.space2),
            ) {
                Text(name, style = MaterialTheme.typography.labelSmall, color = if (name == "canvas") colors.onCanvasMat else colors.textSecondary)
            }
        }
    }
}

@Composable
private fun ColorDiagnostic(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val swatches = listOf(
        "accent" to colors.accent,
        "accent tint" to colors.accentContainer,
        "rose" to colors.secondary,
        "rose tint" to colors.secondaryContainer,
        "peach" to colors.tertiary,
        "peach tint" to colors.tertiaryContainer,
        "success" to colors.success,
        "success tint" to colors.successContainer,
        "warning" to colors.warning,
        "warning tint" to colors.warningContainer,
        "destructive" to colors.destructive,
        "error tint" to colors.destructiveContainer,
        "text primary" to colors.textPrimary,
        "text secondary" to colors.textSecondary,
        "text muted" to colors.textMuted,
        "hairline" to colors.borderSubtle,
        "control edge" to colors.borderControl,
        "checker light" to colors.checkerLight,
        "checker dark" to colors.checkerDark,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
    ) {
        swatches.forEach { (name, color) ->
            Column(Modifier.width(96.dp), verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1)) {
                Box(Modifier.fillMaxWidth().height(34.dp).clip(RoundedCornerShape(dimensions.radii.radiusSm)).background(color).border(1.dp, colors.borderSubtle, RoundedCornerShape(dimensions.radii.radiusSm)))
                Text(name, style = MaterialTheme.typography.labelSmall, color = colors.textMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun TypographyDiagnostic(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val type = MaterialTheme.typography
    val rows = listOf(
        TypeSpec("displayLarge", "Best Mates", type.displayLarge),
        TypeSpec("displayMedium", "Cute Atelier", type.displayMedium),
        TypeSpec("displaySmall", "Your worlds", type.displaySmall),
        TypeSpec("headlineLarge", "Create a world", type.headlineLarge),
        TypeSpec("headlineMedium", "Theme gallery", type.headlineMedium),
        TypeSpec("headlineSmall", "Tokyo & Home", type.headlineSmall),
        TypeSpec("titleLarge", "Widget details", type.titleLarge),
        TypeSpec("titleMedium", "Glow strength", type.titleMedium),
        TypeSpec("titleSmall", "Visual theme", type.titleSmall),
        TypeSpec("bodyLarge", "A warm creative workspace.", type.bodyLarge),
        TypeSpec("bodyMedium", "Clear enough for long editing sessions.", type.bodyMedium),
        TypeSpec("bodySmall", "Quiet supporting information.", type.bodySmall),
        TypeSpec("labelLarge", "CREATE WIDGET", type.labelLarge),
        TypeSpec("labelMedium", "FOUNDATION STUDY", type.labelMedium),
        TypeSpec("labelSmall", "LOCAL PREVIEW", type.labelSmall),
        TypeSpec("valueTime", "18:40", BestMatesDesign.typography.valueTime),
    )
    Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
        rows.forEach { row ->
            Column(verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1)) {
                Text(row.name, style = type.labelSmall, color = colors.textMuted)
                Text(row.sample, style = row.style, color = if (row.name == "valueTime") colors.accent else colors.textPrimary)
            }
        }
    }
}

private data class TypeSpec(val name: String, val sample: String, val style: TextStyle)

@Composable
private fun SpacingAndRadiusDiagnostic(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val spacing = dimensions.spacing
    val spaces = listOf(spacing.space1, spacing.space2, spacing.space3, spacing.space4, spacing.space5, spacing.space6, spacing.space7, spacing.space8, spacing.space9, spacing.space10, spacing.space11)
    Text("Spacing", style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(spacing.space2), verticalArrangement = Arrangement.spacedBy(spacing.space2)) {
        spaces.forEachIndexed { index, value ->
            Column(Modifier.width(58.dp)) {
                Box(Modifier.height(18.dp).width(value).clip(dimensions.radii.radiusFull).background(colors.accent))
                Text("s${index + 1} · ${value.value.toInt()}", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
            }
        }
    }
    Text("Radii", style = MaterialTheme.typography.labelMedium, color = colors.textSecondary)
    val radii = listOf(
        "xs" to RoundedCornerShape(dimensions.radii.radiusXs),
        "sm" to RoundedCornerShape(dimensions.radii.radiusSm),
        "md" to RoundedCornerShape(dimensions.radii.radiusMd),
        "lg" to RoundedCornerShape(dimensions.radii.radiusLg),
        "xl" to RoundedCornerShape(dimensions.radii.radiusXl),
        "2xl" to RoundedCornerShape(dimensions.radii.radius2xl),
        "3xl" to RoundedCornerShape(dimensions.radii.radius3xl),
        "full" to CircleShape,
    )
    FlowRow(horizontalArrangement = Arrangement.spacedBy(spacing.space3), verticalArrangement = Arrangement.spacedBy(spacing.space3)) {
        radii.forEach { (name, shape) -> RadiusCell(name, shape, colors, dimensions) }
    }
}

@Composable
private fun RadiusCell(name: String, shape: Shape, colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    Column(Modifier.width(56.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(44.dp)
                .atelierCrystalSurface(
                    baseColor = colors.accentContainer,
                    shape = shape,
                    level = AtelierCrystalLevel.MilkCrystal,
                    borderColor = colors.accent,
                    intensity = 0.62f,
                ),
        )
        Text(name, style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
    }
}

@Composable
private fun StateDiagnostic(colors: BestMatesColors, states: BestMatesStateTokens, dimensions: BestMatesDimensionTokens) {
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
    FlowRow(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2), verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
        stateRows.forEach { state -> StateCell(state, colors, states, dimensions) }
    }
}

@Composable
private fun StateCell(
    state: BestMatesVisualState,
    colors: BestMatesColors,
    states: BestMatesStateTokens,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radiusMd)
    val container = when (state) {
        BestMatesVisualState.SELECTED -> colors.selectionFill
        BestMatesVisualState.DESTRUCTIVE, BestMatesVisualState.ERROR -> colors.destructiveContainer
        BestMatesVisualState.DISABLED -> colors.textPrimary.copy(alpha = states.disabledContainerAlpha)
        BestMatesVisualState.PRESSED -> colors.accentContainer.copy(alpha = 0.7f)
        else -> colors.surfaceRaised
    }
    val outline = when (state) {
        BestMatesVisualState.SELECTED -> colors.selectionOutline
        BestMatesVisualState.FOCUSED -> colors.focusRing
        BestMatesVisualState.DESTRUCTIVE, BestMatesVisualState.ERROR -> colors.destructive
        else -> colors.borderSubtle
    }
    val outlineWidth = when (state) {
        BestMatesVisualState.SELECTED -> states.selectedOutlineWidth
        BestMatesVisualState.FOCUSED -> states.focusRingWidth
        else -> 1.dp
    }
    val label = when (state) {
        BestMatesVisualState.SELECTED -> "● Selected"
        BestMatesVisualState.LOADING -> "··· Loading"
        BestMatesVisualState.LOCKED -> "◇ Locked"
        else -> state.displayName
    }
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(dimensions.shared.minimumTouchTarget)
            .clip(shape)
            .background(container)
            .border(outlineWidth, outline, shape),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = when (state) {
                BestMatesVisualState.DISABLED -> colors.textPrimary.copy(alpha = states.disabledContentAlpha)
                BestMatesVisualState.DESTRUCTIVE, BestMatesVisualState.ERROR -> colors.onDestructiveContainer
                BestMatesVisualState.SELECTED -> colors.selectionContent
                else -> colors.textSecondary
            },
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun MotionDiagnostic(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    val duration = BestMatesDesign.motion.durations
    val rows = listOf(
        Triple("Instant", duration.motionInstant, "linear"),
        Triple("Press", duration.motionPress, "standard"),
        Triple("Release", duration.motionRelease, "standard"),
        Triple("State", duration.motionState, "standard"),
        Triple("Exit", duration.motionExit, "exit"),
        Triple("Enter", duration.motionEnter, "enter"),
        Triple("Navigate", duration.motionNavigate, "standard"),
        Triple("Theme", duration.motionTheme, "standard"),
    )
    rows.forEach { (name, millis, easing) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensions.spacing.space1),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(name, modifier = Modifier.width(70.dp), style = MaterialTheme.typography.labelMedium, color = colors.textPrimary)
            Text("${millis}ms", modifier = Modifier.width(54.dp), style = MaterialTheme.typography.labelSmall, color = colors.accent)
            Text(easing, style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
        }
    }
}

private data class OrbitIconSpec(val label: String, val icon: ImageVector)

private val orbitIconSpecs = listOf(
    OrbitIconSpec("Back", OrbitIcons.Back),
    OrbitIconSpec("Settings", OrbitIcons.Settings),
    OrbitIconSpec("Undo", OrbitIcons.Undo),
    OrbitIconSpec("Redo", OrbitIcons.Redo),
    OrbitIconSpec("Create", OrbitIcons.CreateWidget),
    OrbitIconSpec("Component", OrbitIcons.AddComponent),
    OrbitIconSpec("City", OrbitIcons.AddCity),
    OrbitIconSpec("Text", OrbitIcons.AddText),
    OrbitIconSpec("Rename", OrbitIcons.Rename),
    OrbitIconSpec("Copy", OrbitIcons.Copy),
    OrbitIconSpec("Paste", OrbitIcons.Paste),
    OrbitIconSpec("Cut", OrbitIcons.Cut),
    OrbitIconSpec("Pin", OrbitIcons.Pin),
    OrbitIconSpec("Lock", OrbitIcons.Lock),
    OrbitIconSpec("Delete", OrbitIcons.Delete),
    OrbitIconSpec("Rotate", OrbitIcons.Rotate),
    OrbitIconSpec("Resize", OrbitIcons.Resize),
    OrbitIconSpec("Snap", OrbitIcons.Snap),
    OrbitIconSpec("Guides", OrbitIcons.Guides),
    OrbitIconSpec("Theme", OrbitIcons.Theme),
    OrbitIconSpec("Font", OrbitIcons.Font),
    OrbitIconSpec("Color", OrbitIcons.Color),
    OrbitIconSpec("Sun", OrbitIcons.Sun),
    OrbitIconSpec("Moon", OrbitIcons.Moon),
    OrbitIconSpec("Search", OrbitIcons.Search),
    OrbitIconSpec("Close", OrbitIcons.Close),
    OrbitIconSpec("Confirm", OrbitIcons.Confirm),
)

private val BestMatesVisualIdentity.displayName: String
    get() = when (this) {
        BestMatesVisualIdentity.CLOUD -> "Cloud"
        BestMatesVisualIdentity.DREAM -> "Dream"
        BestMatesVisualIdentity.MIDNIGHT -> "Midnight"
        BestMatesVisualIdentity.MONO -> "Mono"
    }

private val BestMatesVisualIdentity.moodLine: String
    get() = when (this) {
        BestMatesVisualIdentity.CLOUD -> "Milk pearl · blush mist · lavender veil"
        BestMatesVisualIdentity.DREAM -> "Rose postcard · berry · muted lilac"
        BestMatesVisualIdentity.MIDNIGHT -> "Ink plum studio · soft lilac light"
        BestMatesVisualIdentity.MONO -> "Ivory editorial · dusty mauve detail"
    }

private val BestMatesVisualState.displayName: String
    get() = when (this) {
        BestMatesVisualState.RESTING -> "Resting"
        BestMatesVisualState.HOVERED -> "Hovered"
        BestMatesVisualState.PRESSED -> "Pressed"
        BestMatesVisualState.SELECTED -> "Selected"
        BestMatesVisualState.LOCKED -> "Locked"
        BestMatesVisualState.DESTRUCTIVE -> "Destructive"
        BestMatesVisualState.ERROR -> "Error"
        BestMatesVisualState.LOADING -> "Loading"
        BestMatesVisualState.DISABLED -> "Disabled"
        BestMatesVisualState.FOCUSED -> "Focused"
    }
