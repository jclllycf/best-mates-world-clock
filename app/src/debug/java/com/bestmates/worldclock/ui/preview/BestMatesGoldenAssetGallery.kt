@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.bestmates.worldclock.ui.preview

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesColors
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import com.bestmates.worldclock.ui.theme.BestMatesDimensionTokens

/** Isolated Phase 2D-0.5 gallery. It has no production navigation or model path. */
@Composable
internal fun BestMatesGoldenAssetGallery() {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val report = remember { BestMatesGoldenAssets.validate() }

    GoldenGalleryHeader(report, colors, dimensions)
    GoldenOrbitShelf(colors, dimensions)
    GoldenCelestialShelf(colors, dimensions)
    GoldenShapeShelf(colors, dimensions)
    GoldenTypeShelf(colors, dimensions)
}

@Composable
private fun GoldenGalleryHeader(
    report: GoldenAssetIntegrityReport,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceBase,
        radius = dimensions.radii.radius2xl,
        padding = dimensions.spacing.space4,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(dimensions.shared.minimumTouchTarget)
                    .semantics {
                        contentDescription = if (report.isValid) "Golden asset integrity passed" else "Golden asset integrity failed"
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = if (report.isValid) OrbitIcons.Confirm else OrbitIcons.Close,
                    contentDescription = null,
                    tint = if (report.isValid) colors.success else colors.destructive,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1),
            ) {
                Text("Golden masters", style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                Text(
                    if (report.isValid) {
                        "${report.assetCount} candidates · unique IDs · local vectors · paths valid"
                    } else {
                        report.failures.joinToString(" · ")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (report.isValid) colors.textSecondary else colors.destructive,
                )
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        ) {
            GoldenFilterLabel("2D-0.5", true, colors, dimensions)
            GoldenFilterLabel("Candidate only", false, colors, dimensions)
            GoldenFilterLabel("Offline", false, colors, dimensions)
        }
    }
}

@Composable
private fun GoldenFilterLabel(
    label: String,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        modifier = Modifier.width(if (label == "Candidate only") 112.dp else 84.dp),
        color = if (selected) colors.accentContainer else colors.surfaceRaised,
        radius = 999.dp,
        padding = dimensions.spacing.space2,
        borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
        crystalLevel = AtelierCrystalLevel.PearlGlass,
    ) {
        Text(
            label,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) colors.selectionContent else colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun GoldenOrbitShelf(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    GoldenShelf(
        title = "Orbit Charm · six masters",
        note = "Original 24×24 vectors · 1.8u stroke · shown at 16 / 20 / 24dp",
        colors = colors,
        dimensions = dimensions,
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        ) {
            BestMatesGoldenAssets.orbit.forEach { asset ->
                AtelierSurface(
                    colors = colors,
                    dimensions = dimensions,
                    modifier = Modifier.width(103.dp),
                    color = colors.surfaceRaised,
                    radius = dimensions.radii.radiusLg,
                    padding = dimensions.spacing.space3,
                    crystalLevel = AtelierCrystalLevel.MilkCrystal,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(asset.vector, "${asset.name}, 16dp", tint = colors.textPrimary, modifier = Modifier.size(16.dp))
                        Icon(asset.vector, "${asset.name}, 20dp", tint = colors.textPrimary, modifier = Modifier.size(20.dp))
                        Icon(asset.vector, "${asset.name}, 24dp", tint = colors.textPrimary, modifier = Modifier.size(24.dp))
                    }
                    Text(
                        asset.name,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        Text(
            "Artwork-size diagnostics; future controls retain a separate 48dp target.",
            style = MaterialTheme.typography.bodySmall,
            color = colors.textMuted,
        )
    }
}

@Composable
private fun GoldenCelestialShelf(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    GoldenShelf(
        title = "Celestial Atelier · Golden Set 2",
        note = "Four paired families + one dedicated symbolic Moon · recolourable",
        colors = colors,
        dimensions = dimensions,
    ) {
        BestMatesGoldenAssets.celestial.forEachIndexed { index, family ->
            GoldenCelestialPairCard(family, selected = index == 1, colors, dimensions)
        }
        BestMatesGoldenAssets.celestialSymbols.forEach { symbol ->
            GoldenCelestialSymbolCard(symbol, colors, dimensions)
        }
    }
}

@Composable
private fun GoldenCelestialSymbolCard(
    symbol: GoldenCelestialSymbol,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceRaised,
        radius = dimensions.radii.radiusXl,
        padding = dimensions.spacing.space4,
        borderColor = colors.borderSubtle,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Text(symbol.name, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        Text(symbol.note, style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(symbol.vector, "${symbol.name}, 20dp", tint = colors.secondary, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(dimensions.spacing.space3))
            Icon(symbol.vector, "${symbol.name}, 28dp", tint = colors.secondary, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(dimensions.spacing.space3))
            Icon(symbol.vector, "${symbol.name}, 40dp", tint = colors.secondary, modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
private fun GoldenCelestialPairCard(
    family: GoldenCelestialFamily,
    selected: Boolean,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
) {
    val halo = BestMatesDesign.elevation.softHalo
    val haloModifier = if (selected) {
        Modifier
            .padding(halo.maximumSpread)
            .drawBehind {
                drawRoundRect(
                    color = colors.accent.copy(alpha = halo.alpha * 0.55f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(dimensions.radii.radiusXl.toPx()),
                )
            }
    } else {
        Modifier
    }
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        modifier = haloModifier.fillMaxWidth(),
        color = if (selected) colors.selectionFill else colors.surfaceRaised,
        radius = dimensions.radii.radiusXl,
        padding = dimensions.spacing.space4,
        borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space1),
            ) {
                Text(family.name, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                Text(family.note, style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
            }
            if (selected) {
                Icon(
                    OrbitIcons.Confirm,
                    "Selected family",
                    tint = colors.selectionContent,
                    modifier = Modifier.size(BestMatesDesign.states.selectionCheckIconSize),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GoldenCelestialFigure("Sun", family.sun, colors.accent, dimensions)
            GoldenCelestialFigure("Moon", family.moon, colors.secondary, dimensions)
        }
    }
}

@Composable
private fun GoldenCelestialFigure(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    dimensions: BestMatesDimensionTokens,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, "$label, 20dp", tint = tint, modifier = Modifier.size(20.dp))
            Icon(icon, "$label, 28dp", tint = tint, modifier = Modifier.size(28.dp))
            Icon(icon, "$label, 40dp", tint = tint, modifier = Modifier.size(40.dp))
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = BestMatesDesign.colors.textSecondary)
    }
}

@Composable
private fun GoldenShapeShelf(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    GoldenShelf(
        title = "Shape Atelier · canonical + variants",
        note = "Nine Golden Set 2 silhouettes · intrinsic proportion retained",
        colors = colors,
        dimensions = dimensions,
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        ) {
            BestMatesGoldenAssets.shapes.forEach { shape ->
                AtelierSurface(
                    colors = colors,
                    dimensions = dimensions,
                    modifier = Modifier.width(158.dp),
                    color = colors.surfaceRaised,
                    radius = dimensions.radii.radiusXl,
                    padding = dimensions.spacing.space3,
                    crystalLevel = AtelierCrystalLevel.MilkCrystal,
                ) {
                    GoldenShapeFigure(shape, Modifier.fillMaxWidth().height(42.dp), colors)
                    GoldenShapeFigure(shape, Modifier.fillMaxWidth().height(88.dp), colors)
                    Text(
                        shape.name,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun GoldenShapeFigure(shape: GoldenShapeAsset, modifier: Modifier, colors: BestMatesColors) {
    Canvas(modifier.semantics { contentDescription = "${shape.name} shape preview" }) {
        val availableWidth = size.width * 0.82f
        val availableHeight = size.height * 0.76f
        val drawWidth: Float
        val drawHeight: Float
        if (availableWidth / availableHeight > shape.aspectRatio) {
            drawHeight = availableHeight
            drawWidth = drawHeight * shape.aspectRatio
        } else {
            drawWidth = availableWidth
            drawHeight = drawWidth / shape.aspectRatio
        }
        val left = (size.width - drawWidth) / 2f
        val top = (size.height - drawHeight) / 2f
        translate(left, top) {
            val path = shape.path(Size(drawWidth, drawHeight))
            drawPath(path, colors.accentContainer.copy(alpha = if (colors.isDark) 0.88f else 0.76f))
            drawPath(path, colors.accent.copy(alpha = 0.82f), style = Stroke(width = 1.25.dp.toPx()))
        }
    }
}

@Composable
private fun GoldenTypeShelf(colors: BestMatesColors, dimensions: BestMatesDimensionTokens) {
    GoldenShelf(
        title = "Type Wardrobe · presentation master",
        note = "Existing device fonts only · specimen first · UI label remains neutral",
        colors = colors,
        dimensions = dimensions,
    ) {
        BestMatesGoldenAssets.typeWardrobe.forEachIndexed { index, type ->
            AtelierSurface(
                colors = colors,
                dimensions = dimensions,
                modifier = Modifier.fillMaxWidth(),
                color = if (index == 0) colors.selectionFill else colors.surfaceRaised,
                radius = dimensions.radii.radiusXl,
                padding = dimensions.spacing.space4,
                borderColor = if (index == 0) colors.selectionOutline else colors.borderSubtle,
                crystalLevel = AtelierCrystalLevel.MilkCrystal,
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                    ) {
                        Text("Tokyo", fontFamily = type.family, fontSize = 18.sp, color = colors.textPrimary)
                        Text("18:40", fontFamily = type.family, fontWeight = type.timeWeight, fontSize = 30.sp, lineHeight = 34.sp, color = colors.textPrimary)
                        Text("Tue, Sep 8", fontFamily = type.family, fontSize = 13.sp, color = colors.textSecondary)
                        Text("AM  PM", fontFamily = type.family, fontSize = 12.sp, color = colors.textMuted)
                    }
                    if (index == 0) {
                        Icon(
                            OrbitIcons.Confirm,
                            "Selected type sample",
                            tint = colors.selectionContent,
                            modifier = Modifier.size(BestMatesDesign.states.selectionCheckIconSize),
                        )
                    }
                }
                Text(type.name, style = MaterialTheme.typography.labelMedium, color = colors.textMuted)
            }
        }
    }
}

@Composable
private fun GoldenShelf(
    title: String,
    note: String,
    colors: BestMatesColors,
    dimensions: BestMatesDimensionTokens,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit,
) {
    AtelierSurface(
        colors = colors,
        dimensions = dimensions,
        color = colors.surfaceBase,
        radius = dimensions.radii.radius2xl,
        padding = dimensions.spacing.space4,
        crystalLevel = AtelierCrystalLevel.MilkCrystal,
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
        Text(note, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
        Spacer(Modifier.height(dimensions.spacing.space1))
        content()
    }
}
