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
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
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
            ShowcaseSection("Color atelier", "A compact future picker study; production remains unchanged.") {
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
    Box(
        modifier = Modifier
            .heightIn(min = dimensions.shared.minimumTouchTarget)
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(34.dp)
                .clip(shape)
                .background(if (selected) colors.accentContainer else colors.surfaceInset)
                .border(1.dp, if (selected) colors.selectionOutline else colors.borderSubtle, shape)
                .padding(horizontal = dimensions.spacing.space4),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selected) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(colors.accent))
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
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(radius)
    Column(
        modifier = modifier
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) colors.selectionFill else colors.surfaceRaised)
            .border(if (selected) 1.5.dp else 1.dp, if (selected) colors.selectionOutline else colors.borderSubtle, shape)
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
        if (selected) Icon(OrbitIcons.Confirm, null, tint = colors.accent, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun ThemeCardSample(
    identity: BestMatesVisualIdentity,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val shape = RoundedCornerShape(dimensions.radii.radius2xl)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, shape, false, colors.accent.copy(alpha = 0.18f), colors.accent.copy(alpha = 0.18f))
            .clip(shape)
            .background(colors.surfaceRaised)
            .border(1.5.dp, colors.selectionOutline, shape)
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
                    Icon(OrbitIcons.Confirm, null, tint = colors.accent, modifier = Modifier.size(15.dp))
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
    AtelierSurface(colors, dimensions, color = colors.surfaceRaised, radius = dimensions.radii.radiusXl, padding = dimensions.spacing.space3) {
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
) {
    Box(
        modifier = Modifier.requiredSize(dimensions.shared.minimumTouchTarget),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(if (prominent) 38.dp else 34.dp)
                .clip(RoundedCornerShape(if (prominent) dimensions.radii.radiusMd else dimensions.radii.radiusSm))
                .background(if (prominent) colors.accentContainer else colors.surfaceInset)
                .border(1.dp, if (prominent) colors.accent.copy(alpha = 0.45f) else colors.borderSubtle, RoundedCornerShape(if (prominent) dimensions.radii.radiusMd else dimensions.radii.radiusSm)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, description, tint = if (prominent) colors.accent else colors.textSecondary, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun InspectorSample(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    AtelierSurface(colors, dimensions, color = colors.surfaceRaised, radius = dimensions.radii.radiusXl) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(colors.secondary))
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) colors.selectionFill else colors.surfaceRaised)
            .border(1.dp, if (selected) colors.selectionOutline else colors.borderSubtle, shape)
            .padding(dimensions.spacing.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(34.dp).clip(RoundedCornerShape(dimensions.radii.radiusSm)).background(swatch))
        Text(label, modifier = Modifier.weight(1f).padding(start = dimensions.spacing.space3), style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary)
        if (selected) Icon(OrbitIcons.Confirm, null, tint = colors.accent, modifier = Modifier.size(17.dp))
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
    Column(
        modifier = Modifier.width(74.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1),
    ) {
        Box(
            modifier = Modifier
                .requiredSize(dimensions.shared.minimumTouchTarget)
                .clip(RoundedCornerShape(dimensions.radii.radiusMd))
                .background(colors.surfaceRaised)
                .border(1.dp, colors.borderSubtle, RoundedCornerShape(dimensions.radii.radiusMd)),
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
                .clip(shape)
                .background(states.colors.selectionFill)
                .border(states.selectedOutlineWidth, states.colors.selectionOutline, shape),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(9.dp).clip(CircleShape).background(colors.secondary))
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
                .shadow(8.dp, RoundedCornerShape(dimensions.radii.radiusLg), false, colors.accent.copy(alpha = 0.2f), colors.accent.copy(alpha = 0.2f))
                .clip(RoundedCornerShape(dimensions.radii.radiusLg))
                .background(colors.surfaceRaised)
                .border(1.5.dp, colors.selectionOutline, RoundedCornerShape(dimensions.radii.radiusLg))
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
                .clip(RoundedCornerShape(dimensions.radii.radiusSm))
                .background(colors.surfaceOverlay)
                .border(1.dp, colors.borderSubtle, RoundedCornerShape(dimensions.radii.radiusSm))
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
                    .clip(CircleShape)
                    .background(colors.surfaceOverlay)
                    .border(1.dp, colors.borderControl, CircleShape),
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
    ) {
        Text("Color atelier", style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
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
                Text("Current", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                Row(horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2)) {
                    ColorSwatch(colors.accent, true, colors, dimensions)
                    ColorSwatch(colors.secondary, false, colors, dimensions)
                }
                Text("Plum veil", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                Text("#8F60AD", style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
            }
        }
        AtelierSlider("Opacity", "72%", 0.72f, colors, dimensions)
        AtelierButton("Apply color", AtelierButtonKind.Primary, OrbitIcons.Confirm, colors, dimensions)
    }
}

@Composable
private fun ColorWheel(colors: BestMatesColors) {
    val palette = listOf(colors.accent, colors.secondary, colors.tertiary, colors.warning, colors.success, colors.destructive)
    Canvas(Modifier.size(126.dp)) {
        val stroke = 18.dp.toPx()
        palette.forEachIndexed { index, color ->
            drawArc(
                color = color,
                startAngle = index * 60f - 90f,
                sweepAngle = 61f,
                useCenter = false,
                style = Stroke(width = stroke),
            )
        }
        drawCircle(colors.surfaceOverlay, radius = size.minDimension * 0.28f)
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
    Box(Modifier.requiredSize(dimensions.shared.minimumTouchTarget), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(dimensions.radii.radiusSm))
                .background(color)
                .border(if (selected) 2.dp else 1.dp, if (selected) colors.selectionOutline else colors.borderControl, RoundedCornerShape(dimensions.radii.radiusSm)),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) Icon(OrbitIcons.Confirm, null, tint = colors.onAccent, modifier = Modifier.size(15.dp))
        }
    }
}

@Composable
private fun ControlShowcase(colors: BestMatesColors, dimensions: BestMatesDimensionTokens, states: BestMatesStateTokens) {
    AtelierSurface(colors, dimensions, color = colors.surfaceBase, radius = dimensions.radii.radius2xl) {
        Text("Buttons", style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        AtelierButton("Create widget", AtelierButtonKind.Primary, OrbitIcons.CreateWidget, colors, dimensions)
        AtelierButton("Add to Home", AtelierButtonKind.Secondary, OrbitIcons.AddComponent, colors, dimensions)
        AtelierButton("Import font", AtelierButtonKind.Outlined, OrbitIcons.Font, colors, dimensions)
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

private enum class AtelierButtonKind { Primary, Secondary, Outlined, Text, Destructive }

@Composable
private fun AtelierButton(
    label: String,
    kind: AtelierButtonKind,
    icon: ImageVector?,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    modifier: Modifier = Modifier,
) {
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
    val border = when (kind) {
        AtelierButtonKind.Primary -> colors.accent
        AtelierButtonKind.Secondary -> colors.secondary.copy(alpha = 0.28f)
        AtelierButtonKind.Outlined -> colors.borderControl
        AtelierButtonKind.Text -> Color.Transparent
        AtelierButtonKind.Destructive -> colors.destructive.copy(alpha = 0.34f)
    }
    val shape = RoundedCornerShape(if (kind == AtelierButtonKind.Text) dimensions.radii.radiusMd else dimensions.radii.radiusXl)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (kind == AtelierButtonKind.Text) dimensions.shared.minimumTouchTarget else 54.dp)
            .then(
                if (kind == AtelierButtonKind.Primary) {
                    Modifier.shadow(4.dp, shape, false, colors.shadowColor.copy(alpha = 0.16f), colors.shadowColor.copy(alpha = 0.16f))
                } else {
                    Modifier
                },
            )
            .clip(shape)
            .background(container)
            .border(if (kind == AtelierButtonKind.Text) 0.dp else 1.dp, border, shape)
            .padding(horizontal = dimensions.spacing.space5),
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
    Box(Modifier.requiredSize(dimensions.shared.minimumTouchTarget), contentAlignment = Alignment.Center) {
        val track = when {
            !enabled -> colors.surfaceInset.copy(alpha = 0.58f)
            checked -> colors.accentContainer
            else -> colors.surfaceInset
        }
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(24.dp)
                .clip(dimensions.radii.radiusFull)
                .background(track)
                .border(1.dp, if (checked) colors.accent.copy(alpha = 0.55f) else colors.borderControl.copy(alpha = 0.68f), dimensions.radii.radiusFull)
                .padding(3.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                    .then(
                        if (checked && enabled) {
                            Modifier.shadow(4.dp, CircleShape, false, colors.accent.copy(alpha = 0.16f), colors.accent.copy(alpha = 0.16f))
                        } else {
                            Modifier
                        },
                    )
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            !enabled -> colors.textMuted.copy(alpha = 0.48f)
                            checked -> colors.accent
                            else -> colors.surfaceRaised
                        },
                    )
                    .border(1.dp, if (checked) colors.accent else colors.borderControl, CircleShape),
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
            drawLine(colors.borderSubtle, Offset(start, y), Offset(end, y), 3.dp.toPx())
            drawLine(colors.secondary, Offset(start, y), Offset(activeEnd, y), 3.dp.toPx())
            drawCircle(colors.accent.copy(alpha = 0.12f), 11.dp.toPx(), Offset(activeEnd, y))
            drawCircle(colors.surfaceRaised, 7.dp.toPx(), Offset(activeEnd, y))
            drawCircle(colors.accent, 7.dp.toPx(), Offset(activeEnd, y), style = Stroke(1.5.dp.toPx()))
        }
    }
}

@Composable
private fun MiniTag(text: String, colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(dimensions.radii.radiusSm))
            .background(colors.surfaceInset)
            .border(1.dp, colors.borderSubtle, RoundedCornerShape(dimensions.radii.radiusSm))
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
    Box(
        modifier = Modifier.heightIn(min = dimensions.shared.minimumTouchTarget),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(34.dp)
                .clip(dimensions.radii.radiusFull)
                .background(if (selected) colors.selectionFill else colors.surfaceRaised)
                .border(1.dp, if (selected) colors.selectionOutline else colors.borderSubtle, dimensions.radii.radiusFull)
                .padding(horizontal = dimensions.spacing.space3),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selected) Box(Modifier.size(6.dp).clip(CircleShape).background(colors.secondary))
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
                .then(
                    if (state == AtelierFieldState.Focused) {
                        Modifier.shadow(5.dp, shape, false, colors.accent.copy(alpha = 0.12f), colors.accent.copy(alpha = 0.12f))
                    } else {
                        Modifier
                    },
                )
                .clip(shape)
                .background(container)
                .border(
                    when (state) {
                        AtelierFieldState.Resting -> 1.dp
                        AtelierFieldState.Focused -> BestMatesDesign.states.focusRingWidth
                        AtelierFieldState.Error -> BestMatesDesign.states.errorOutlineWidth
                    },
                    outline,
                    shape,
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
        Box(Modifier.size(44.dp).clip(shape).background(colors.accentContainer).border(1.dp, colors.accent, shape))
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
            .border(if (state == BestMatesVisualState.SELECTED || state == BestMatesVisualState.FOCUSED) 1.5.dp else 1.dp, outline, shape),
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
