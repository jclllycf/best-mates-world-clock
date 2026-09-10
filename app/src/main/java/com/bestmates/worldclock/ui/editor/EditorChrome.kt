package com.bestmates.worldclock.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.ui.components.CrystalLevel
import com.bestmates.worldclock.ui.components.OrbitButton
import com.bestmates.worldclock.ui.components.OrbitButtonStyle
import com.bestmates.worldclock.ui.components.OrbitChoiceChip
import com.bestmates.worldclock.ui.components.OrbitIconButton
import com.bestmates.worldclock.ui.components.crystalSurface
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign

data class EditorChromeStrings(
    val title: String,
    val back: String,
    val undo: String,
    val redo: String,
    val autoSave: String,
    val addComponent: String,
    val addCity: String,
    val addText: String,
    val snap: String,
    val guides: String,
    val addToHome: String,
    val save: String,
    val selected: String,
)

@Composable
fun EditorFrame(
    strings: EditorChromeStrings,
    canUndo: Boolean,
    canRedo: Boolean,
    autoSave: Boolean,
    onBack: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onAutoSave: () -> Unit,
    onSave: () -> Unit,
    onAddToHome: (() -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = BestMatesDesign.colors
    Box(
        modifier
            .fillMaxSize()
            .background(colors.background)
            .background(
                Brush.verticalGradient(
                    listOf(
                        colors.atmosphereA.copy(alpha = if (colors.isDark) .10f else .18f),
                        Color.Transparent,
                        colors.atmosphereB.copy(alpha = if (colors.isDark) .08f else .12f),
                    ),
                ),
            ),
    ) {
        Column(Modifier.fillMaxSize()) {
            EditorTopBar(
                strings = strings,
                canUndo = canUndo,
                canRedo = canRedo,
                autoSave = autoSave,
                onBack = onBack,
                onUndo = onUndo,
                onRedo = onRedo,
                onAutoSave = onAutoSave,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = BestMatesDesign.dimensions.shared.compactScreenGutter),
                content = content,
            )
            EditorBottomBar(
                strings = strings,
                onSave = onSave,
                onAddToHome = onAddToHome,
            )
        }
    }
}

@Composable
private fun EditorTopBar(
    strings: EditorChromeStrings,
    canUndo: Boolean,
    canRedo: Boolean,
    autoSave: Boolean,
    onBack: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onAutoSave: () -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceBase.copy(alpha = .72f))
            .statusBarsPadding()
            .padding(
                start = dimensions.shared.compactScreenGutter,
                end = dimensions.shared.compactScreenGutter,
                top = dimensions.spacing.space2,
                bottom = dimensions.spacing.space3,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        OrbitIconButton(OrbitIcons.Back, strings.back, onBack)
        Text(
            strings.title,
            modifier = Modifier.weight(1f).padding(start = dimensions.spacing.space2),
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        OrbitIconButton(OrbitIcons.Undo, strings.undo, onUndo, enabled = canUndo)
        OrbitIconButton(OrbitIcons.Redo, strings.redo, onRedo, enabled = canRedo)
        OrbitIconButton(
            OrbitIcons.Confirm,
            strings.autoSave,
            onAutoSave,
            selected = autoSave,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditorControlShelf(
    strings: EditorChromeStrings,
    snapEnabled: Boolean,
    guidesVisible: Boolean,
    canAddComponent: Boolean,
    canAddCity: Boolean,
    canAddText: Boolean,
    onSnap: () -> Unit,
    onGuides: () -> Unit,
    onAddComponent: () -> Unit,
    onAddCity: () -> Unit,
    onAddText: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val shape = MaterialTheme.shapes.large
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .crystalSurface(
                baseColor = colors.surfaceRaised,
                shape = shape,
                level = CrystalLevel.PearlGlass,
                borderColor = colors.borderSubtle,
                intensity = .58f,
            )
            .padding(dimensions.spacing.space3),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
    ) {
        OrbitChoiceChip(
            label = strings.snap,
            selected = snapEnabled,
            selectedDescription = strings.selected,
            onClick = onSnap,
        )
        OrbitChoiceChip(
            label = strings.guides,
            selected = guidesVisible,
            selectedDescription = strings.selected,
            onClick = onGuides,
        )
        Spacer(Modifier.weight(1f))
        EditorAddAction(OrbitIcons.AddComponent, strings.addComponent, canAddComponent, onAddComponent)
        EditorAddAction(OrbitIcons.AddCity, strings.addCity, canAddCity, onAddCity)
        EditorAddAction(OrbitIcons.AddText, strings.addText, canAddText, onAddText)
    }
}

@Composable
private fun EditorAddAction(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    OrbitIconButton(
        icon = icon,
        contentDescription = description,
        onClick = onClick,
        enabled = enabled,
    )
}

@Composable
private fun EditorBottomBar(
    strings: EditorChromeStrings,
    onSave: () -> Unit,
    onAddToHome: (() -> Unit)?,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surfaceBase.copy(alpha = .78f))
            .navigationBarsPadding()
            .padding(
                horizontal = dimensions.shared.compactScreenGutter,
                vertical = dimensions.spacing.space3,
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onAddToHome != null) {
            OrbitButton(
                label = strings.addToHome,
                onClick = onAddToHome,
                modifier = Modifier.weight(1f),
                icon = OrbitIcons.CreateWidget,
                style = OrbitButtonStyle.Secondary,
                compact = true,
            )
        } else {
            Spacer(Modifier.weight(1f))
        }
        OrbitButton(
            label = strings.save,
            onClick = onSave,
            modifier = Modifier.weight(1f),
            style = OrbitButtonStyle.Primary,
            compact = true,
        )
    }
}
