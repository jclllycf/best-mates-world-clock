package com.bestmates.worldclock.ui.workspace

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.data.LauncherMetrics
import com.bestmates.worldclock.data.ProjectEntry
import com.bestmates.worldclock.data.ProjectStore
import com.bestmates.worldclock.data.WidgetBindings
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.ui.common.Backdrop
import com.bestmates.worldclock.ui.common.ScenePreview
import com.bestmates.worldclock.ui.common.backdrop
import com.bestmates.worldclock.ui.common.rememberMinuteTick
import com.bestmates.worldclock.ui.common.rememberWallpaperTones
import com.bestmates.worldclock.ui.design.Bm
import com.bestmates.worldclock.ui.design.BmButton
import com.bestmates.worldclock.ui.design.BmButtonSize
import com.bestmates.worldclock.ui.design.BmButtonStyle
import com.bestmates.worldclock.ui.design.BmDialog
import com.bestmates.worldclock.ui.design.BmIconButton
import com.bestmates.worldclock.ui.design.BmIcons
import com.bestmates.worldclock.ui.design.BmOverline
import com.bestmates.worldclock.ui.design.BmSheet
import com.bestmates.worldclock.ui.design.BmTextField
import com.bestmates.worldclock.ui.design.pressable
import com.bestmates.worldclock.ui.i18n.LocalBestMatesLanguage
import com.bestmates.worldclock.ui.i18n.dateLocale
import com.bestmates.worldclock.ui.i18n.uiFormat
import com.bestmates.worldclock.ui.i18n.uiText
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WorkspaceRoute(
    onOpenProject: (String) -> Unit,
    onSettings: () -> Unit,
    onCreated: (String) -> Unit = onOpenProject,
    /** Launcher configure flow: tapping a design picks it for the new widget. */
    pickMode: Boolean = false,
) {
    val context = LocalContext.current
    val store = remember { ProjectStore(context) }
    val bindings = remember { WidgetBindings(context) }
    var revision by remember { mutableIntStateOf(0) }
    var selectedIds by remember { mutableStateOf(emptySet<String>()) }
    var creating by remember { mutableStateOf(false) }
    var renaming by remember { mutableStateOf<String?>(null) }
    var confirmingDelete by remember { mutableStateOf(false) }
    val projects = remember(revision) { store.all() }
    val placed = remember(revision) { projects.associate { it.id to bindings.widgetCount(it.id) } }
    val duplicateSuffix = uiText("copy_suffix")

    BackHandler(enabled = selectedIds.isNotEmpty()) { selectedIds = emptySet() }

    WorkspaceScreen(
        projects = projects,
        placed = placed,
        selectedIds = selectedIds,
        pickMode = pickMode,
        onSettings = onSettings,
        onCreate = { creating = true },
        onOpen = onOpenProject,
        onToggle = { id -> if (!pickMode) selectedIds = if (id in selectedIds) selectedIds - id else selectedIds + id },
        onClearSelection = { selectedIds = emptySet() },
        onRename = { selectedIds.singleOrNull()?.let { renaming = it } },
        onDuplicate = {
            selectedIds.forEach { store.duplicate(it, duplicateSuffix) }
            selectedIds = emptySet(); revision++
        },
        onDelete = { confirmingDelete = true },
    )

    if (creating) {
        CreateWidgetSheet(
            onDismiss = { creating = false },
            onCreate = { name, columns, rows ->
                val (w, h) = LauncherMetrics(context).boardFor(columns, rows)
                val id = store.create(WidgetConfig(title = name, columns = columns, rows = rows, boardWidthDp = w, boardHeightDp = h))
                creating = false
                onCreated(id)
            },
        )
    }

    renaming?.let { id ->
        var draft by remember(id) { mutableStateOf(store.load(id).title) }
        BmDialog(
            title = uiText("rename"),
            onDismiss = { renaming = null },
            confirmText = uiText("apply"),
            onConfirm = {
                store.save(id, store.load(id).copy(title = draft.trim().ifBlank { "" }), touch = false)
                renaming = null; selectedIds = emptySet(); revision++
            },
            dismissText = uiText("cancel"),
        ) {
            BmTextField(draft, { draft = it.take(40) }, Modifier.fillMaxWidth(), placeholder = uiText("widget_name"))
        }
    }

    if (confirmingDelete) {
        BmDialog(
            title = uiText("delete_projects_title"),
            message = uiFormat(if (selectedIds.size == 1) "delete_projects_message_one" else "delete_projects_message", selectedIds.size),
            onDismiss = { confirmingDelete = false },
            confirmText = uiText("delete"),
            onConfirm = {
                selectedIds.forEach(store::delete)
                selectedIds = emptySet(); confirmingDelete = false; revision++
            },
            dismissText = uiText("cancel"),
            destructive = true,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WorkspaceScreen(
    projects: List<ProjectEntry>,
    placed: Map<String, Int>,
    selectedIds: Set<String>,
    pickMode: Boolean,
    onSettings: () -> Unit,
    onCreate: () -> Unit,
    onOpen: (String) -> Unit,
    onToggle: (String) -> Unit,
    onClearSelection: () -> Unit,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    val c = Bm.colors
    val selecting = selectedIds.isNotEmpty()
    val wallpaper = rememberWallpaperTones()
    val night = isSystemInDarkTheme()
    Box(Modifier.fillMaxSize().background(c.paper)) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 18.dp,
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                WorkspaceHeader(selecting, pickMode, selectedIds.size, onSettings, onClearSelection, onRename, onDuplicate, onDelete)
            }
            if (projects.isEmpty()) {
                item(span = StaggeredGridItemSpan.FullLine) { EmptyWorkspace(onCreate) }
            } else {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Row(Modifier.fillMaxWidth().padding(start = 4.dp, top = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        BmOverline(uiText("your_widgets"))
                        Spacer(Modifier.width(8.dp))
                        Text("${projects.size}", style = Bm.type.numeric, color = c.inkFaint)
                    }
                }
                items(projects, key = { it.id }) { project ->
                    ProjectCard(
                        project = project,
                        placedCount = placed[project.id] ?: 0,
                        selected = project.id in selectedIds,
                        selecting = selecting,
                        night = night,
                        backdrop = Backdrop.from(project.config.backdrop),
                        wallpaper = wallpaper,
                        onOpen = { if (selecting) onToggle(project.id) else onOpen(project.id) },
                        onLongPress = { onToggle(project.id) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !selecting && projects.isNotEmpty(),
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 20.dp),
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
        ) {
            BmButton(
                text = uiText("new_widget"),
                onClick = onCreate,
                icon = BmIcons.Plus,
                size = BmButtonSize.Large,
                modifier = Modifier.shadow(18.dp, Bm.shapes.pill, ambientColor = c.shadow, spotColor = c.shadow),
            )
        }
    }
}

@Composable
private fun WorkspaceHeader(
    selecting: Boolean,
    pickMode: Boolean,
    count: Int,
    onSettings: () -> Unit,
    onClear: () -> Unit,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
) {
    val c = Bm.colors
    Column(Modifier.fillMaxWidth().statusBarsPadding().padding(top = 18.dp, bottom = 6.dp, start = 4.dp)) {
        AnimatedContent(
            targetState = selecting,
            transitionSpec = { fadeIn(tween(160)) togetherWith fadeOut(tween(120)) },
            label = "header",
        ) { inSelection ->
            if (inSelection) {
                Row(Modifier.fillMaxWidth().height(56.dp), verticalAlignment = Alignment.CenterVertically) {
                    BmIconButton(BmIcons.Close, uiText("close"), onClear)
                    Text(uiFormat("selected_count", count), style = Bm.type.title, color = c.ink, modifier = Modifier.weight(1f).padding(start = 8.dp))
                    BmIconButton(BmIcons.Edit, uiText("rename"), onRename, enabled = count == 1)
                    BmIconButton(BmIcons.Duplicate, uiText("duplicate"), onDuplicate)
                    BmIconButton(BmIcons.Trash, uiText("delete"), onDelete, tint = c.danger)
                }
            } else {
                Row(Modifier.fillMaxWidth().height(56.dp), verticalAlignment = Alignment.CenterVertically) {
                    BrandMark(Modifier.size(34.dp), moonOnPaper = !c.isDark)
                    Spacer(Modifier.width(12.dp))
                    Text(if (pickMode) uiText("choose_design") else "Best Mates", style = if (pickMode) Bm.type.headline else Bm.type.display, color = c.ink, modifier = Modifier.weight(1f))
                    if (!pickMode) BmIconButton(BmIcons.Settings, uiText("settings"), onSettings, tonal = true)
                }
            }
        }
        LocalNow(Modifier.padding(start = 46.dp, top = 0.dp))
    }
}

/** A quiet live line under the wordmark: the user's own day and time. */
@Composable
private fun LocalNow(modifier: Modifier = Modifier) {
    val minute = rememberMinuteTick()
    val language = LocalBestMatesLanguage.current
    val text = remember(minute, language) {
        val pattern = when (language) {
            com.bestmates.worldclock.ui.settings.AppLanguage.CHINESE -> "M月d日 EEEE · HH:mm"
            com.bestmates.worldclock.ui.settings.AppLanguage.GERMAN -> "EEEE, d. MMMM · HH:mm"
            else -> "EEEE, d MMMM · HH:mm"
        }
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern(pattern, language.dateLocale()))
    }
    Text(text, style = Bm.type.bodySmall, color = Bm.colors.inkSoft, modifier = modifier)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProjectCard(
    project: ProjectEntry,
    placedCount: Int,
    selected: Boolean,
    selecting: Boolean,
    night: Boolean,
    backdrop: Backdrop,
    wallpaper: com.bestmates.worldclock.ui.common.WallpaperTones?,
    onOpen: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = Bm.colors
    val haptics = LocalHapticFeedback.current
    val interaction = remember { MutableInteractionSource() }
    val config = project.config
    val aspect = (config.boardWidthDp / config.boardHeightDp).coerceIn(.62f, 2.2f)
    val tileScale by animateFloatAsState(if (selected) .94f else 1f, spring(dampingRatio = .7f, stiffness = 500f), label = "sel")
    Column(
        modifier
            .fillMaxWidth()
            .semantics { if (selecting) this.selected = selected }
            .combinedClickable(
                interactionSource = interaction,
                indication = null,
                role = Role.Button,
                onClick = onOpen,
                onLongClick = { haptics.performHapticFeedback(HapticFeedbackType.LongPress); onLongPress() },
            ),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .pressable(interaction)
                .aspectRatio(aspect)
                .clip(Bm.shapes.tile)
                .backdrop(backdrop, wallpaper)
                .border(if (selected) 2.5.dp else 0.dp, if (selected) c.ink else Color.Transparent, Bm.shapes.tile),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.fillMaxSize().scale(tileScale).padding(10.dp)) {
                if (config.layers.isEmpty() && config.cities.isEmpty() && config.texts.isEmpty()) {
                    Text(uiText("empty_design"), style = Bm.type.caption, color = Color.White.copy(alpha = .78f), modifier = Modifier.align(Alignment.Center))
                } else {
                    ScenePreview(config, night, Modifier.fillMaxSize())
                }
            }
            if (selecting) {
                Box(
                    Modifier.align(Alignment.TopEnd).padding(10.dp).size(24.dp).clip(CircleShape)
                        .background(if (selected) c.ink else Color.White.copy(alpha = .35f))
                        .border(1.5.dp, Color.White.copy(alpha = .9f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) Icon(BmIcons.Check, null, tint = c.paper, modifier = Modifier.size(15.dp))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            config.title.ifBlank { uiText("unnamed") },
            style = Bm.type.title,
            color = c.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 2.dp),
        )
        Row(Modifier.padding(horizontal = 2.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                uiFormat(if (config.cities.size == 1) "card_meta_one" else "card_meta", config.cities.size, config.columns, config.rows),
                style = Bm.type.caption,
                color = c.inkSoft,
            )
            if (placedCount > 0) {
                Spacer(Modifier.width(6.dp))
                Box(Modifier.size(5.dp).clip(CircleShape).background(c.success))
                Spacer(Modifier.width(4.dp))
                Text(uiText("on_home"), style = Bm.type.caption, color = c.success)
            }
        }
    }
}

/* ---------------------------------------------------------------- Empty state */

@Composable
private fun EmptyWorkspace(onCreate: () -> Unit) {
    val c = Bm.colors
    Column(
        Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 24.dp, start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(220.dp).clip(RoundedCornerShape(40.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF3A3260), Color(0xFF1E1B2E)))),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.fillMaxSize()) { drawStars() }
            BrandMark(Modifier.size(104.dp))
        }
        Spacer(Modifier.height(28.dp))
        Text(uiText("empty_title"), style = Bm.type.headline, color = c.ink)
        Spacer(Modifier.height(8.dp))
        Text(uiText("empty_body"), style = Bm.type.body, color = c.inkSoft, modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        BmButton(uiText("make_first_widget"), onCreate, icon = BmIcons.Plus, size = BmButtonSize.Large)
    }
}

private fun DrawScope.drawStars() {
    val stars = listOf(.16f to .22f, .82f to .18f, .72f to .78f, .24f to .74f, .5f to .12f, .9f to .52f, .1f to .5f)
    stars.forEachIndexed { i, (x, y) ->
        drawCircle(Color(0xFFF4EAD8).copy(alpha = if (i % 2 == 0) .55f else .3f), radius = if (i % 3 == 0) 2.4.dp.toPx() else 1.5.dp.toPx(), center = Offset(size.width * x, size.height * y))
    }
}

/** The Best Mates mark drawn natively so it scales crisply anywhere in the UI. */
@Composable
fun BrandMark(modifier: Modifier = Modifier, sun: Color = Color(0xFFF2A93B), moon: Color = Color(0xFFF4EAD8), moonOnPaper: Boolean = false) {
    val c = Bm.colors
    val moonColor = if (moonOnPaper) c.moon else moon
    Canvas(modifier) {
        // Geometry from the launcher icon: board 32..76 mapped to this canvas.
        val k = size.minDimension / 44f
        fun x(v: Float) = (v - 32f) * k
        fun y(v: Float) = (v - 32f) * k
        val crescent = Path().apply {
            val outer = Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(x(54f), y(54f)), 22f * k)) }
            val inner = Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(x(47f), y(59f)), 18f * k)) }
            op(outer, inner, androidx.compose.ui.graphics.PathOperation.Difference)
        }
        drawPath(crescent, moonColor)
        drawCircle(sun, radius = 13f * k, center = Offset(x(45f), y(61f)))
    }
}

/* ---------------------------------------------------------------- Create sheet */

private val SIZE_PRESETS = listOf(2 to 1, 2 to 2, 3 to 2, 4 to 1, 4 to 2, 4 to 3, 4 to 4, 3 to 3, 4 to 5)

@Composable
private fun CreateWidgetSheet(onDismiss: () -> Unit, onCreate: (String, Int, Int) -> Unit) {
    val c = Bm.colors
    val context = LocalContext.current
    val metrics = remember { LauncherMetrics(context) }
    var name by remember { mutableStateOf("") }
    var size by remember { mutableStateOf(4 to 2) }
    BmSheet(onDismiss = onDismiss, title = uiText("new_widget")) {
        Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
            Spacer(Modifier.height(8.dp))
            BmOverline(uiText("name"))
            Spacer(Modifier.height(8.dp))
            BmTextField(name, { name = it.take(40) }, Modifier.fillMaxWidth(), placeholder = uiText("name_placeholder"))
            Spacer(Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                BmOverline(uiText("home_screen_size"), Modifier.weight(1f))
                Text(uiFormat("cells_format", size.first, size.second), style = Bm.type.numeric, color = c.ink)
            }
            Spacer(Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().height(272.dp),
                userScrollEnabled = false,
            ) {
                items(SIZE_PRESETS) { preset ->
                    SizeTile(preset.first, preset.second, metrics.cellWidthDp / metrics.cellHeightDp, preset == size) { size = preset }
                }
            }
            Text(
                if (metrics.calibrated) uiText("size_note_calibrated") else uiText("size_note_default"),
                style = Bm.type.bodySmall, color = c.inkSoft,
            )
            Spacer(Modifier.height(20.dp))
            BmButton(
                uiText("start_designing"),
                { onCreate(name.trim(), size.first, size.second) },
                Modifier.fillMaxWidth(),
                size = BmButtonSize.Large,
            )
        }
    }
}

@Composable
private fun SizeTile(cols: Int, rows: Int, cellAspect: Float, selected: Boolean, onClick: () -> Unit) {
    val c = Bm.colors
    Column(
        Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) c.accentSoft else c.surfaceSunken)
            .border(if (selected) 1.5.dp else 0.dp, if (selected) c.accentInk else Color.Transparent, RoundedCornerShape(16.dp))
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { this.selected = selected }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Canvas(Modifier.weight(1f).fillMaxWidth()) {
            // A slice of a 4-column home screen with the chosen span filled.
            val shownRows = rows.coerceIn(2, 5)
            var cw = size.width / 4f
            var ch = cw / cellAspect.coerceIn(.6f, 1.6f)
            val fitK = minOf(1f, size.height / (ch * shownRows))
            cw *= fitK; ch *= fitK
            val ox = (size.width - cw * 4) / 2f; val oy = (size.height - ch * shownRows) / 2f
            val gap = 2.dp.toPx()
            for (gx in 0 until 4) for (gy in 0 until shownRows) {
                val on = gx < cols && gy < rows
                val tl = Offset(ox + gx * cw + gap / 2, oy + gy * ch + gap / 2)
                val sz = androidx.compose.ui.geometry.Size(cw - gap, ch - gap)
                val radius = androidx.compose.ui.geometry.CornerRadius(2.5.dp.toPx())
                if (on) drawRoundRect(if (selected) c.accentInk else c.ink.copy(alpha = .82f), tl, sz, radius)
                else drawRoundRect(c.hairlineStrong, tl, sz, radius, style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()))
            }
        }
        Spacer(Modifier.height(6.dp))
        Text("$cols × $rows", style = Bm.type.caption, color = if (selected) c.accentInk else c.inkSoft)
    }
}
