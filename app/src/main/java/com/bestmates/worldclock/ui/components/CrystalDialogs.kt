package com.bestmates.worldclock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign

/** Shared production shell for picker, import, confirmation and editing dialogs. */
@Composable
fun CrystalDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val shape = RoundedCornerShape(dimensions.radii.radius2xl)
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .crystalSurface(
                    baseColor = colors.surfaceOverlay,
                    shape = shape,
                    level = CrystalLevel.MilkCrystal,
                    borderColor = colors.borderControl,
                    intensity = .92f,
                )
                .padding(dimensions.spacing.space6),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space5),
        ) {
            if (title != null) {
                Box(Modifier.fillMaxWidth()) { title() }
            }
            if (text != null) {
                Box(Modifier.fillMaxWidth()) { text() }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensions.spacing.space3,
                    Alignment.End,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                dismissButton?.invoke()
                confirmButton()
            }
        }
    }
}

@Composable
fun CrystalPickerRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(dimensions.radii.radiusLg)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = dimensions.shared.minimumSettingsRowHeight)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .crystalSurface(
                baseColor = if (enabled) colors.surfaceRaised else colors.surfaceInset,
                shape = shape,
                level = CrystalLevel.MilkCrystal,
                pressed = pressed && enabled,
                borderColor = colors.borderSubtle,
                intensity = if (enabled) .68f else .30f,
            )
            .padding(dimensions.spacing.space4),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
fun CrystalChoiceTile(
    selected: Boolean,
    selectedDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val shape = RoundedCornerShape(dimensions.radii.radiusLg)
    Box(
        modifier = modifier
            .semantics { this.selected = selected }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .crystalSurface(
                baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                shape = shape,
                level = CrystalLevel.MilkCrystal,
                pressed = pressed,
                borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (selected) .90f else .64f,
            ),
    ) {
        content()
        if (selected) {
            Icon(
                OrbitIcons.Confirm,
                contentDescription = selectedDescription,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensions.spacing.space3)
                    .size(16.dp),
                tint = colors.selectionContent,
            )
        }
    }
}
