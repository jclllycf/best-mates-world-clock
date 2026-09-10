package com.bestmates.worldclock.ui.workspace

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bestmates.worldclock.data.ProjectStore
import com.bestmates.worldclock.model.ThemeMode
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.ui.components.CrystalLevel
import com.bestmates.worldclock.ui.components.OrbitButton
import com.bestmates.worldclock.ui.components.OrbitButtonStyle
import com.bestmates.worldclock.ui.components.OrbitIconButton
import com.bestmates.worldclock.ui.components.crystalSurface
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import com.bestmates.worldclock.widget.BackgroundRenderer
import java.util.Locale

@Immutable
data class WorkspaceStrings(
    val brand: String,
    val collectionTitle: String,
    val collectionCountTemplate: String,
    val selectedCountTemplate: String,
    val settings: String,
    val createWidget: String,
    val widgetName: String,
    val unnamed: String,
    val emptyTitle: String,
    val emptyBody: String,
    val backgroundsCitiesTemplate: String,
    val autoSaveOn: String,
    val editedLocally: String,
    val selected: String,
    val rename: String,
    val copy: String,
    val cut: String,
    val paste: String,
    val delete: String,
    val cancel: String,
    val apply: String,
    val close: String,
    val deleteTitle: String,
    val deleteMessageTemplate: String,
    val version: String,
)

private data class WorkspaceProject(val id: String, val config: WidgetConfig)

/** Owns the existing project commands while the app root retains navigation. */
@Composable
fun WorkspaceRoute(
    strings: WorkspaceStrings,
    onOpenProject: (String) -> Unit,
    onSettings: () -> Unit,
) {
    val context = LocalContext.current
    val store = remember { ProjectStore(context) }
    var revision by remember { mutableIntStateOf(0) }
    var selectedIds by remember { mutableStateOf(emptySet<String>()) }
    var clipboard by remember { mutableStateOf<WidgetConfig?>(null) }
    var namingProjectId by remember { mutableStateOf<String?>(null) }
    var nameDraft by remember { mutableStateOf("") }
    var confirmingDelete by remember { mutableStateOf(false) }
    val projects = remember(revision) {
        store.ids().map { WorkspaceProject(it, store.load(it)) }
    }

    BackHandler(enabled = selectedIds.isNotEmpty()) { selectedIds = emptySet() }

    WorkspaceScreen(
        projects = projects,
        selectedIds = selectedIds,
        hasClipboard = clipboard != null,
        strings = strings,
        onSettings = onSettings,
        onCreate = {
            nameDraft = ""
            namingProjectId = CREATE_PROJECT
        },
        onOpen = onOpenProject,
        onToggleSelection = { id -> selectedIds = selectedIds.toggle(id) },
        onClearSelection = { selectedIds = emptySet() },
        onPaste = {
            clipboard?.let { saved ->
                store.create(saved.copy(title = saved.title.ifBlank { strings.unnamed }))
                revision++
            }
        },
        onRename = {
            selectedIds.singleOrNull()?.let { id ->
                nameDraft = store.load(id).title
                namingProjectId = id
            }
        },
        onCopy = {
            selectedIds.singleOrNull()?.let { id -> clipboard = store.load(id) }
            selectedIds = emptySet()
        },
        onCut = {
            selectedIds.singleOrNull()?.let { id ->
                clipboard = store.load(id)
                store.delete(id)
                revision++
            }
            selectedIds = emptySet()
        },
        onDelete = { confirmingDelete = true },
    )

    namingProjectId?.let { targetId ->
        val creating = targetId == CREATE_PROJECT
        WorkspaceNameDialog(
            title = if (creating) strings.createWidget else strings.rename,
            draft = nameDraft,
            fieldLabel = strings.widgetName,
            confirmLabel = if (creating) strings.createWidget else strings.apply,
            cancelLabel = strings.cancel,
            onDraftChange = { nameDraft = it.take(MAX_PROJECT_NAME_LENGTH) },
            onDismiss = { namingProjectId = null },
            onConfirm = {
                val title = nameDraft.trim().ifBlank { strings.unnamed }
                if (creating) {
                    onOpenProject(store.create(WidgetConfig(title = title)))
                } else {
                    store.save(targetId, store.load(targetId).copy(title = title))
                    revision++
                    selectedIds = emptySet()
                }
                namingProjectId = null
            },
        )
    }

    if (confirmingDelete) {
        WorkspaceConfirmationDialog(
            title = strings.deleteTitle,
            message = format(strings.deleteMessageTemplate, selectedIds.size),
            confirmLabel = strings.delete,
            cancelLabel = strings.cancel,
            onDismiss = { confirmingDelete = false },
            onConfirm = {
                selectedIds.forEach(store::delete)
                selectedIds = emptySet()
                confirmingDelete = false
                revision++
            },
        )
    }
}

@Composable
private fun WorkspaceScreen(
    projects: List<WorkspaceProject>,
    selectedIds: Set<String>,
    hasClipboard: Boolean,
    strings: WorkspaceStrings,
    onSettings: () -> Unit,
    onCreate: () -> Unit,
    onOpen: (String) -> Unit,
    onToggleSelection: (String) -> Unit,
    onClearSelection: () -> Unit,
    onPaste: () -> Unit,
    onRename: () -> Unit,
    onCopy: () -> Unit,
    onCut: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val motion = BestMatesDesign.motion
    Box(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .workspaceAtmosphere()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Column(Modifier.fillMaxSize()) {
            WorkspaceTopBar(
                brand = strings.brand,
                subtitle = if (selectedIds.isEmpty()) strings.collectionTitle else format(strings.selectedCountTemplate, selectedIds.size),
                selected = selectedIds.isNotEmpty(),
                hasClipboard = hasClipboard,
                settingsLabel = strings.settings,
                pasteLabel = strings.paste,
                closeLabel = strings.close,
                onSettings = onSettings,
                onPaste = onPaste,
                onClearSelection = onClearSelection,
            )
            AnimatedVisibility(
                visible = selectedIds.isNotEmpty(),
                enter = fadeIn(tween(motion.durations.motionState)) + expandVertically(tween(motion.durations.motionState)),
                exit = fadeOut(tween(motion.durations.motionRelease)) + shrinkVertically(tween(motion.durations.motionRelease)),
            ) {
                WorkspaceSelectionBar(
                    selectedCount = selectedIds.size,
                    canUseSingleActions = selectedIds.size == 1,
                    hasClipboard = hasClipboard,
                    strings = strings,
                    onPaste = onPaste,
                    onRename = onRename,
                    onCopy = onCopy,
                    onCut = onCut,
                    onDelete = onDelete,
                )
            }

            if (projects.isEmpty()) {
                WorkspaceEmptyState(
                    strings = strings,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = dimensions.shared.standardScreenGutter),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = dimensions.shared.compactScreenGutter,
                        top = dimensions.spacing.space4,
                        end = dimensions.shared.compactScreenGutter,
                        bottom = dimensions.spacing.space6,
                    ),
                    verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space4),
                ) {
                    item(key = "collection-heading") {
                        Column(Modifier.padding(horizontal = dimensions.spacing.space2)) {
                            Text(strings.collectionTitle, style = MaterialTheme.typography.headlineSmall, color = colors.textPrimary)
                            Text(
                                format(strings.collectionCountTemplate, projects.size),
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.textSecondary,
                            )
                        }
                    }
                    items(projects, key = { it.id }) { project ->
                        WorkspaceProjectCard(
                            project = project,
                            isSelected = project.id in selectedIds,
                            selectionMode = selectedIds.isNotEmpty(),
                            strings = strings,
                            onOpen = { onOpen(project.id) },
                            onToggleSelection = { onToggleSelection(project.id) },
                        )
                    }
                }
            }

            WorkspaceCreateBar(
                version = strings.version,
                createLabel = strings.createWidget,
                onCreate = onCreate,
            )
        }
    }
}

@Composable
private fun WorkspaceTopBar(
    brand: String,
    subtitle: String,
    selected: Boolean,
    hasClipboard: Boolean,
    settingsLabel: String,
    pasteLabel: String,
    closeLabel: String,
    onSettings: () -> Unit,
    onPaste: () -> Unit,
    onClearSelection: () -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                start = dimensions.shared.standardScreenGutter,
                top = dimensions.spacing.space5,
                end = dimensions.shared.standardScreenGutter,
                bottom = dimensions.spacing.space3,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(48.dp)
                .crystalSurface(
                    baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                    shape = CircleShape,
                    level = CrystalLevel.PearlGlass,
                    borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                    borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                    intensity = 0.7f,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text("BM", style = MaterialTheme.typography.labelMedium, color = if (selected) colors.selectionContent else colors.accent)
        }
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = dimensions.spacing.space4),
        ) {
            Text(brand, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary, maxLines = 1)
        }
        if (!selected && hasClipboard) {
            OrbitIconButton(
                icon = OrbitIcons.Paste,
                contentDescription = pasteLabel,
                onClick = onPaste,
                modifier = Modifier.padding(end = dimensions.spacing.space3),
            )
        }
        OrbitIconButton(
            icon = if (selected) OrbitIcons.Close else OrbitIcons.Settings,
            contentDescription = if (selected) closeLabel else settingsLabel,
            onClick = if (selected) onClearSelection else onSettings,
            selected = selected,
        )
    }
}

@Composable
private fun WorkspaceSelectionBar(
    selectedCount: Int,
    canUseSingleActions: Boolean,
    hasClipboard: Boolean,
    strings: WorkspaceStrings,
    onPaste: () -> Unit,
    onRename: () -> Unit,
    onCopy: () -> Unit,
    onCut: () -> Unit,
    onDelete: () -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.shared.compactScreenGutter, vertical = dimensions.spacing.space2)
            .crystalSurface(
                baseColor = colors.surfaceRaised,
                shape = RoundedCornerShape(dimensions.radii.radiusXl),
                level = CrystalLevel.MilkCrystal,
                borderColor = colors.borderSubtle,
                intensity = 0.76f,
            )
            .padding(vertical = dimensions.spacing.space3),
    ) {
        Text(
            format(strings.selectedCountTemplate, selectedCount),
            modifier = Modifier.padding(horizontal = dimensions.spacing.space4, vertical = dimensions.spacing.space1),
            style = MaterialTheme.typography.labelMedium,
            color = colors.textSecondary,
        )
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = dimensions.spacing.space3),
            horizontalArrangement = Arrangement.spacedBy(dimensions.shared.distinctTouchTargetGap),
        ) {
            OrbitButton(strings.paste, onPaste, icon = OrbitIcons.Paste, style = OrbitButtonStyle.Quiet, enabled = hasClipboard, compact = true)
            if (canUseSingleActions) {
                OrbitButton(strings.rename, onRename, icon = OrbitIcons.Rename, style = OrbitButtonStyle.Quiet, compact = true)
                OrbitButton(strings.copy, onCopy, icon = OrbitIcons.Copy, style = OrbitButtonStyle.Quiet, compact = true)
                OrbitButton(strings.cut, onCut, icon = OrbitIcons.Cut, style = OrbitButtonStyle.Quiet, compact = true)
            }
            OrbitButton(strings.delete, onDelete, icon = OrbitIcons.Delete, style = OrbitButtonStyle.Destructive, compact = true)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WorkspaceProjectCard(
    project: WorkspaceProject,
    isSelected: Boolean,
    selectionMode: Boolean,
    strings: WorkspaceStrings,
    onOpen: () -> Unit,
    onToggleSelection: () -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val shape = RoundedCornerShape(dimensions.radii.radius2xl)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (selectionMode) Modifier.semantics { selected = isSelected } else Modifier)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                role = if (selectionMode) Role.Checkbox else Role.Button,
                onClick = if (selectionMode) onToggleSelection else onOpen,
                onLongClick = onToggleSelection,
            )
            .crystalSurface(
                baseColor = if (isSelected) colors.selectionFill else colors.surfaceRaised,
                shape = shape,
                level = CrystalLevel.MilkCrystal,
                pressed = pressed,
                borderColor = if (isSelected) colors.selectionOutline else colors.borderSubtle,
                borderWidth = if (isSelected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (isSelected) 0.94f else 0.76f,
            )
            .padding(dimensions.spacing.space4),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WorkspaceThumbnail(project.config)
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = dimensions.spacing.space4),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space2),
        ) {
            Text(
                project.config.title.ifBlank { strings.unnamed },
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) colors.selectionContent else colors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                format(strings.backgroundsCitiesTemplate, project.config.layers.size, project.config.cities.size),
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) colors.selectionContent.copy(alpha = 0.78f) else colors.textSecondary,
            )
            Text(
                if (project.config.autoSave) strings.autoSaveOn else strings.editedLocally,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) colors.selectionContent.copy(alpha = 0.68f) else colors.textMuted,
            )
        }
        if (isSelected) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceRaised.copy(alpha = 0.54f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    OrbitIcons.Confirm,
                    strings.selected,
                    tint = colors.selectionContent,
                    modifier = Modifier.size(BestMatesDesign.states.selectionCheckIconSize),
                )
            }
        }
    }
}

@Composable
private fun WorkspaceThumbnail(config: WidgetConfig) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val density = LocalDensity.current.density
    val night = config.theme == ThemeMode.DARK
    val bitmap = remember(config.layers, night, density) {
        BackgroundRenderer.renderLayers(config, 96, 88, density, night).asImageBitmap()
    }
    Box(
        Modifier
            .size(width = 88.dp, height = 78.dp)
            .clip(RoundedCornerShape(dimensions.radii.radiusLg))
            .background(colors.canvasMat)
            .drawBehind {
                drawLine(colors.guide.copy(alpha = 0.11f), Offset(size.width / 2f, 0f), Offset(size.width / 2f, size.height), 1.dp.toPx())
                drawLine(colors.guide.copy(alpha = 0.11f), Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), 1.dp.toPx())
            },
        contentAlignment = Alignment.Center,
    ) {
        if (config.layers.isEmpty()) {
            Icon(OrbitIcons.CreateWidget, null, tint = colors.accent.copy(alpha = 0.72f), modifier = Modifier.size(24.dp))
        } else {
            Image(bitmap, null, Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
        }
    }
}

@Composable
private fun WorkspaceEmptyState(strings: WorkspaceStrings, modifier: Modifier = Modifier) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Canvas(Modifier.size(116.dp)) {
            drawCircle(colors.atmosphereA.copy(alpha = if (colors.isDark) 0.12f else 0.24f), radius = size.minDimension * 0.46f)
            drawCircle(colors.borderSubtle.copy(alpha = 0.76f), radius = size.minDimension * 0.34f, style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()))
            drawCircle(colors.accent.copy(alpha = 0.58f), radius = 4.dp.toPx(), center = Offset(size.width * 0.75f, size.height * 0.31f))
            drawCircle(colors.secondary.copy(alpha = 0.48f), radius = 3.dp.toPx(), center = Offset(size.width * 0.28f, size.height * 0.70f))
        }
        Spacer(Modifier.height(dimensions.spacing.space3))
        Text(strings.emptyTitle, style = MaterialTheme.typography.headlineSmall, color = colors.textPrimary)
        Text(
            strings.emptyBody,
            modifier = Modifier.padding(top = dimensions.spacing.space2),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary,
        )
    }
}

@Composable
private fun WorkspaceCreateBar(version: String, createLabel: String, onCreate: () -> Unit) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensions.shared.compactScreenGutter)
            .padding(bottom = dimensions.spacing.space4),
    ) {
        Text(
            version,
            modifier = Modifier.padding(start = dimensions.spacing.space3, bottom = dimensions.spacing.space2),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textMuted,
        )
        OrbitButton(
            label = createLabel,
            onClick = onCreate,
            icon = OrbitIcons.CreateWidget,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun WorkspaceNameDialog(
    title: String,
    draft: String,
    fieldLabel: String,
    confirmLabel: String,
    cancelLabel: String,
    onDraftChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    WorkspaceDialogShell(onDismiss) {
        val colors = BestMatesDesign.colors
        val dimensions = BestMatesDesign.dimensions
        Text(title, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            label = { Text(fieldLabel) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = dimensions.shared.minimumInputHeight),
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
            OrbitButton(cancelLabel, onDismiss, Modifier.weight(1f), style = OrbitButtonStyle.Quiet, compact = true)
            OrbitButton(confirmLabel, onConfirm, Modifier.weight(1f), style = OrbitButtonStyle.Primary, compact = true)
        }
    }
}

@Composable
private fun WorkspaceConfirmationDialog(
    title: String,
    message: String,
    confirmLabel: String,
    cancelLabel: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    WorkspaceDialogShell(onDismiss) {
        val colors = BestMatesDesign.colors
        val dimensions = BestMatesDesign.dimensions
        Text(title, style = MaterialTheme.typography.titleLarge, color = colors.textPrimary)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3)) {
            OrbitButton(cancelLabel, onDismiss, Modifier.weight(1f), style = OrbitButtonStyle.Quiet, compact = true)
            OrbitButton(confirmLabel, onConfirm, Modifier.weight(1f), style = OrbitButtonStyle.Destructive, compact = true)
        }
    }
}

@Composable
private fun WorkspaceDialogShell(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensions.shared.standardScreenGutter)
                .crystalSurface(
                    baseColor = colors.surfaceElevated,
                    shape = RoundedCornerShape(dimensions.radii.radius2xl),
                    level = CrystalLevel.MilkCrystal,
                    borderColor = colors.borderControl,
                    intensity = 0.96f,
                )
                .padding(dimensions.spacing.space6),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space5),
            content = content,
        )
    }
}

@Composable
private fun Modifier.workspaceAtmosphere(): Modifier {
    val colors = BestMatesDesign.colors
    val peak = BestMatesDesign.elevation.atmospherePeakAlpha
    return drawBehind {
        drawRect(
            Brush.radialGradient(
                listOf(colors.atmosphereA.copy(alpha = peak), Color.Transparent),
                center = Offset.Zero,
                radius = size.maxDimension * 0.78f,
            ),
        )
        drawRect(
            Brush.radialGradient(
                listOf(colors.atmosphereB.copy(alpha = peak * 0.72f), Color.Transparent),
                center = Offset(size.width, size.height),
                radius = size.maxDimension * 0.70f,
            ),
        )
    }
}

private fun Set<String>.toggle(id: String) = if (contains(id)) this - id else this + id

private fun format(template: String, vararg values: Any): String = String.format(Locale.getDefault(), template, *values)

private const val CREATE_PROJECT = "__create__"
private const val MAX_PROJECT_NAME_LENGTH = 40
