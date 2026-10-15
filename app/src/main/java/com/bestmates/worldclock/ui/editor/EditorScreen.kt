package com.bestmates.worldclock.ui.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.bestmates.worldclock.data.LauncherMetrics
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.ThemeMode
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.MATERIAL_MIST
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.scene.CityPlacement
import com.bestmates.worldclock.scene.SceneGeometry
import com.bestmates.worldclock.ui.common.Backdrop
import com.bestmates.worldclock.ui.common.backdropBrush
import com.bestmates.worldclock.ui.common.rememberWallpaperTones
import com.bestmates.worldclock.ui.design.Bm
import com.bestmates.worldclock.ui.design.BmButton
import com.bestmates.worldclock.ui.design.BmButtonSize
import com.bestmates.worldclock.ui.design.BmButtonStyle
import com.bestmates.worldclock.ui.design.BmDialog
import com.bestmates.worldclock.ui.design.BmIconButton
import com.bestmates.worldclock.ui.design.BmIcons
import com.bestmates.worldclock.ui.design.BmOverline
import com.bestmates.worldclock.ui.design.BmRow
import com.bestmates.worldclock.ui.design.BmSegmented
import com.bestmates.worldclock.ui.design.BmSheet
import com.bestmates.worldclock.ui.design.BmSwitch
import com.bestmates.worldclock.ui.design.BmTextField
import com.bestmates.worldclock.ui.design.pressable
import com.bestmates.worldclock.ui.i18n.uiFormat
import com.bestmates.worldclock.ui.i18n.uiText
import kotlinx.coroutines.delay

enum class SaveState { Saved, Saving, Unsaved }

/**
 * Widget editor. Saves automatically a moment after each committed change, on leaving and
 * when the app goes to the background; Undo/Redo cover every change made in the session.
 */
@Composable
fun EditorScreen(
    initial: WidgetConfig,
    isProject: Boolean,
    onPersist: (WidgetConfig) -> Unit,
    onClose: (WidgetConfig) -> Unit,
    onAddToHome: ((WidgetConfig) -> Unit)?,
) {
    val c = Bm.colors
    val state = remember { EditorState(initial) }
    val wallpaper = rememberWallpaperTones()
    val systemNight = isSystemInDarkTheme()
    var previewNight by remember { mutableStateOf(initial.theme == ThemeMode.DARK || (initial.theme == ThemeMode.SYSTEM && systemNight)) }
    var snap by remember { mutableStateOf(true) }
    var grid by remember { mutableStateOf(false) }
    var saveState by remember { mutableStateOf(SaveState.Saved) }
    var sheet by remember { mutableStateOf<EditorSheet?>(null) }
    var renaming by remember { mutableStateOf(false) }
    val latestPersist by rememberUpdatedState(onPersist)
    var lastSaved by remember { mutableStateOf(initial) }
    fun saveNow() {
        val cfg = state.config
        if (cfg != lastSaved) { latestPersist(cfg); lastSaved = cfg }
        saveState = SaveState.Saved
    }

    // Debounced autosave after every committed change.
    LaunchedEffect(state.revision) {
        if (state.revision == 0) return@LaunchedEffect
        saveState = SaveState.Unsaved
        delay(650)
        saveState = SaveState.Saving
        saveNow()
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event -> if (event == Lifecycle.Event.ON_PAUSE) saveNow() }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    fun close() { state.endGesture(); saveNow(); onClose(state.config) }
    BackHandler { if (state.selection != null) state.selection = null else close() }

    Column(Modifier.fillMaxSize().background(c.desk)) {
        EditorTopBar(
            title = state.config.title.ifBlank { uiText("unnamed") },
            subtitle = uiFormat("cells_format", state.config.columns, state.config.rows) + "  ·  " + when (saveState) {
                SaveState.Saved -> uiText("saved"); SaveState.Saving -> uiText("saving"); SaveState.Unsaved -> uiText("editing")
            },
            canUndo = state.canUndo,
            canRedo = state.canRedo,
            onBack = ::close,
            onUndo = state::undo,
            onRedo = state::redo,
            onTitle = { if (isProject) renaming = true },
        )
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
            // The canvas takes the height the widget's proportions need at full width, so a
            // 2×1 strip is as large as the screen allows and the panel gets the rest. The
            // split never follows the selection (that made the board jump); the user can
            // drag it, and a double tap on the grabber restores the default.
            val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            val aspect = (state.config.boardWidthDp / state.config.boardHeightDp).coerceIn(.3f, 5f)
            val maxCanvas = (maxHeight - PANEL_MIN - navBottom).coerceAtLeast(CANVAS_MIN)
            val ideal = ((maxWidth - 32.dp) / aspect + CANVAS_CHROME).coerceIn(CANVAS_MIN, maxCanvas)
            var userCanvas by rememberSaveable(aspect) { mutableStateOf(Float.NaN) }
            val canvasHeight = if (userCanvas.isNaN()) ideal else userCanvas.dp.coerceIn(CANVAS_MIN, (maxHeight - PANEL_COLLAPSED - navBottom).coerceAtLeast(CANVAS_MIN))
            val latestCanvas by rememberUpdatedState(canvasHeight)
            val density = LocalDensity.current
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxWidth().height(canvasHeight)) {
                    EditorCanvas(
                        state = state,
                        night = previewNight,
                        backdrop = Backdrop.from(state.config.backdrop),
                        wallpaper = wallpaper,
                        snap = snap,
                        showGrid = grid,
                        emptyHint = uiText("canvas_empty_hint"),
                        modifier = Modifier.fillMaxSize(),
                        bottomInset = 46.dp,
                    )
                    CanvasToolbar(
                        night = previewNight, onNight = { previewNight = !previewNight },
                        snap = snap, onSnap = { snap = !snap },
                        grid = grid, onGrid = { grid = !grid },
                        backdrop = Backdrop.from(state.config.backdrop),
                        onBackdrop = { sheet = EditorSheet.Backdrops },
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp),
                    )
                }
                BottomPanel(
                    state = state,
                    previewNight = previewNight,
                    onSheet = { sheet = it },
                    onAddToHome = onAddToHome?.let { add -> { state.endGesture(); saveNow(); add(state.config) } },
                    onResize = { dy -> userCanvas = (latestCanvas + with(density) { dy.toDp() }).value },
                    onResetSize = { userCanvas = Float.NaN },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

    when (val s = sheet) {
        null -> Unit
        EditorSheet.AddShape -> BmSheet(onDismiss = { sheet = null }, title = uiText("add_shape"), fullHeight = true) {
            Column(Modifier.verticalScroll(rememberScrollState()).padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
                ShapeGrid(null, onPick = { shape ->
                    val c0 = state.config
                    if (c0.layers.size < MAX_LAYERS) {
                        state.commit(c0.copy(layers = c0.layers + freshLayer(shape, state.newId(c0), c0)))
                        state.selection = EditorSelection.Layer(c0.layers.size)
                    }
                    sheet = null
                })
            }
        }
        EditorSheet.AddCity -> CityPickerSheet(onDismiss = { sheet = null }) { picked ->
            val c0 = state.config
            if (c0.cities.size < MAX_CITIES) {
                state.commit(c0.copy(cities = CityPlacement.add(c0.cities, freshCity(picked, c0), c0.boardWidthDp, c0.boardHeightDp)))
                state.selection = EditorSelection.CityItem(c0.cities.size)
            }
            sheet = null
        }
        is EditorSheet.ReplaceCity -> CityPickerSheet(onDismiss = { sheet = null }) { picked ->
            state.updateCity(s.index) { old ->
                old.copy(name = picked.name, country = picked.country, latitude = picked.latitude, longitude = picked.longitude, zoneId = picked.zoneId, geoId = picked.geoId, nickname = "")
            }
            sheet = null
        }
        is EditorSheet.Font -> FontSheet(s.title, s.current, s.sample, onDismiss = { sheet = null }, liveOnly = s.liveOnly) { id -> s.onPick(id); sheet = null }
        is EditorSheet.Colour -> ColorSheet(s.title, s.current, onDismiss = { state.endGesture(); sheet = null }, allowAlpha = s.alpha) { color, final ->
            s.onLive(color); if (final) state.endGesture()
        }
        EditorSheet.Layers -> LayersSheet(state, onDismiss = { sheet = null })
        EditorSheet.Widget -> WidgetSettingsSheet(state, onDismiss = { sheet = null })
        EditorSheet.Backdrops -> BackdropSheet(Backdrop.from(state.config.backdrop), wallpaper, onDismiss = { sheet = null }) { b ->
            state.updateConfig { it.copy(backdrop = b.id) }
        }
    }

    if (renaming) {
        var draft by remember { mutableStateOf(state.config.title) }
        BmDialog(
            title = uiText("rename"), onDismiss = { renaming = false }, confirmText = uiText("apply"),
            onConfirm = { state.updateConfig { it.copy(title = draft.trim()) }; renaming = false }, dismissText = uiText("cancel"),
        ) { BmTextField(draft, { draft = it.take(40) }, Modifier.fillMaxWidth(), placeholder = uiText("widget_name")) }
    }
}

sealed interface EditorSheet {
    data object AddShape : EditorSheet
    data object AddCity : EditorSheet
    data class ReplaceCity(val index: Int) : EditorSheet
    data class Font(val title: String, val current: String, val sample: String, val liveOnly: Boolean = false, val onPick: (String) -> Unit) : EditorSheet
    data class Colour(val title: String, val current: Long, val alpha: Boolean, val onLive: (Long) -> Unit) : EditorSheet
    data object Layers : EditorSheet
    data object Widget : EditorSheet
    data object Backdrops : EditorSheet
}

/* ------------------------------------------------------------------ new elements */

private fun freshLayer(shape: WidgetShape, id: Long, config: WidgetConfig): WidgetLayer {
    // Fill the board along the shape's natural proportion, centred.
    val boardAspect = config.boardWidthDp / config.boardHeightDp
    val ratio = shape.designAspect / boardAspect
    // A card is the widget's own surface, so it starts full bleed; other shapes start at
    // their natural proportion, as large as the board allows.
    val (w, h) = if (shape == WidgetShape.ROUNDED) 1f to 1f else if (ratio >= 1f) 1f to (1f / ratio) else ratio to 1f
    // New shapes are Mist: a frosted veil that keeps white type legible on bright and dark
    // wallpapers alike. Existing layers keep whatever finish they were saved with.
    val mist = PALETTES.first { it.key == "mist" }
    return WidgetLayer(
        id = id, shape = shape, width = w.coerceIn(.2f, 1f), height = h.coerceIn(.2f, 1f),
        dayColor = mist.light, nightColor = mist.dark, textColor = 0xFFFFFFFF,
        borderWidth = 0f, borderOpacity = .3f, softness = 12f, glowOpacity = 0f,
        shadow = 12f, shadowOpacity = .14f, shadowYOffset = 4f,
        material = MATERIAL_MIST,
    )
}

/** House style for a new city; position and size come from [CityPlacement]. */
private fun freshCity(picked: City, config: WidgetConfig): City {
    return picked.copy(
        componentId = config.layers.firstOrNull()?.id ?: 0L,
        nameFont = WidgetFonts.DEFAULT_NAME, timeFont = WidgetFonts.DEFAULT_TIME, dateFont = WidgetFonts.DEFAULT_DATE,
        nameColor = 0xFFFFFFFF, timeColor = 0xFFFFFFFF, dateColor = 0xCCFFFFFF,
    )
}

/* ------------------------------------------------------------------ chrome */

@Composable
private fun EditorTopBar(
    title: String, subtitle: String, canUndo: Boolean, canRedo: Boolean,
    onBack: () -> Unit, onUndo: () -> Unit, onRedo: () -> Unit, onTitle: () -> Unit,
) {
    val c = Bm.colors
    Row(
        Modifier.fillMaxWidth().background(c.paper).statusBarsPadding().padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BmIconButton(BmIcons.Back, uiText("back"), onBack)
        Column(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).clickable(onClick = onTitle).padding(horizontal = 6.dp, vertical = 2.dp)) {
            Text(title, style = Bm.type.title, color = c.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, style = Bm.type.caption, color = c.inkSoft, maxLines = 1)
        }
        BmIconButton(BmIcons.Undo, uiText("undo"), onUndo, enabled = canUndo)
        BmIconButton(BmIcons.Redo, uiText("redo"), onRedo, enabled = canRedo)
    }
    Box(Modifier.fillMaxWidth().height(1.dp).background(c.hairline))
}

@Composable
private fun CanvasToolbar(
    night: Boolean, onNight: () -> Unit, snap: Boolean, onSnap: () -> Unit, grid: Boolean, onGrid: () -> Unit,
    backdrop: Backdrop, onBackdrop: () -> Unit, modifier: Modifier = Modifier,
) {
    val c = Bm.colors
    val wallpaper = rememberWallpaperTones()
    Row(
        modifier.shadow(10.dp, CircleShape, ambientColor = c.shadow, spotColor = c.shadow).clip(CircleShape).background(c.surfaceRaised).padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BmIconButton(if (night) BmIcons.Moon else BmIcons.Sun, uiText(if (night) "preview_dark" else "preview_light"), onNight, size = 38.dp, iconSize = 19.dp, tint = if (night) c.moon else c.sun)
        BmIconButton(BmIcons.Magnet, uiText("snap"), onSnap, selected = snap, size = 38.dp, iconSize = 19.dp)
        BmIconButton(BmIcons.Grid, uiText("grid"), onGrid, selected = grid, size = 38.dp, iconSize = 19.dp)
        Box(
            Modifier.padding(horizontal = 4.dp).size(30.dp).clip(CircleShape).background(backdropBrush(backdrop, wallpaper))
                .border(1.5.dp, c.hairlineStrong, CircleShape).clickable(role = Role.Button, onClick = onBackdrop),
        )
    }
}

private val PANEL_MIN = 292.dp
private val PANEL_COLLAPSED = 168.dp
private val CANVAS_MIN = 200.dp
/** Canvas space around the board: margins, rotate-knob room and the floating toolbar. */
private val CANVAS_CHROME = 112.dp

@Composable
private fun BottomPanel(
    state: EditorState, previewNight: Boolean, onSheet: (EditorSheet) -> Unit, onAddToHome: (() -> Unit)?,
    onResize: (Float) -> Unit, onResetSize: () -> Unit, modifier: Modifier = Modifier,
) {
    val c = Bm.colors
    val resizeLabel = uiText("resize_panel")
    Column(
        modifier.fillMaxWidth()
            .shadow(20.dp, RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp), ambientColor = c.shadow, spotColor = c.shadow)
            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            .background(c.surfaceRaised)
            .navigationBarsPadding(),
    ) {
        // Grabber: drag to give the canvas or the panel more room.
        Box(
            Modifier.fillMaxWidth().height(18.dp)
                .pointerInput(Unit) { detectVerticalDragGestures { change, dy -> change.consume(); onResize(dy) } }
                .pointerInput(Unit) { detectTapGestures(onDoubleTap = { onResetSize() }) }
                .semantics { contentDescription = resizeLabel },
            contentAlignment = Alignment.Center,
        ) { Box(Modifier.size(width = 36.dp, height = 4.dp).clip(CircleShape).background(c.hairlineStrong)) }
        AnimatedContent(
            targetState = state.selection?.let { it::class to it.index },
            transitionSpec = { fadeIn(tween(180)) + slideInVertically(tween(220)) { it / 10 } togetherWith fadeOut(tween(120)) },
            label = "panel",
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) { key ->
            val sel = state.selection
            if (key == null || sel == null) AddDock(state, onSheet, onAddToHome)
            else Inspector(state, sel, previewNight, onSheet)
        }
    }
}

@Composable
private fun AddDock(state: EditorState, onSheet: (EditorSheet) -> Unit, onAddToHome: (() -> Unit)?) {
    val c = state.config
    val placeholder = uiText("text_placeholder")
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 2.dp, bottom = 14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AddTile(BmIcons.Shape, uiText("shape"), c.layers.size < MAX_LAYERS, Modifier.weight(1f)) { onSheet(EditorSheet.AddShape) }
            AddTile(BmIcons.City, uiText("city"), c.cities.size < MAX_CITIES, Modifier.weight(1f)) { onSheet(EditorSheet.AddCity) }
            AddTile(BmIcons.Text, uiText("text"), c.texts.size < MAX_TEXTS, Modifier.weight(1f)) {
                // Text is added only on explicit request, selected straight away for editing.
                val c0 = state.config
                state.commit(c0.copy(texts = c0.texts + CustomText(
                    content = placeholder, size = (minOf(c0.boardWidthDp, c0.boardHeightDp) / 12f).coerceIn(12f, 28f),
                    font = WidgetFonts.DEFAULT_TEXT, y = if (c0.cities.isEmpty()) 0f else -.7f,
                    componentId = c0.layers.firstOrNull()?.id ?: 0L,
                )))
                state.selection = EditorSelection.TextItem(c0.texts.size)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            BmButton(uiText("layers"), { onSheet(EditorSheet.Layers) }, icon = BmIcons.Layers, style = BmButtonStyle.Secondary, size = BmButtonSize.Small)
            BmButton(uiText("widget_settings"), { onSheet(EditorSheet.Widget) }, icon = BmIcons.Frame, style = BmButtonStyle.Secondary, size = BmButtonSize.Small)
            Spacer(Modifier.weight(1f))
        }
        Spacer(Modifier.weight(1f).heightIn(min = 14.dp))
        if (onAddToHome != null) {
            BmButton(uiText("add_home"), onAddToHome, Modifier.fillMaxWidth(), icon = BmIcons.AddToHome, size = BmButtonSize.Large)
        }
    }
}

@Composable
private fun AddTile(icon: ImageVector, label: String, enabled: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val c = Bm.colors
    val interaction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    Column(
        modifier.pressable(interaction, enabled).clip(RoundedCornerShape(20.dp)).background(c.surfaceSunken)
            .clickable(interaction, indication = null, enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(c.surfaceRaised), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = c.ink.copy(alpha = if (enabled) 1f else .3f), modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = Bm.type.label, color = c.ink.copy(alpha = if (enabled) 1f else .35f))
    }
}

/* ------------------------------------------------------------------ sheets */

@Composable
private fun LayersSheet(state: EditorState, onDismiss: () -> Unit) {
    val c = Bm.colors
    BmSheet(onDismiss = onDismiss, title = uiText("layers")) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 24.dp).verticalScroll(rememberScrollState())) {
            val cfg = state.config
            if (cfg.layers.isEmpty() && cfg.cities.isEmpty() && cfg.texts.isEmpty()) Text(uiText("no_layers"), style = Bm.type.body, color = c.inkSoft)
            // Drawing order is texts above cities above shapes; list from top to bottom.
            cfg.texts.indices.reversed().forEach { i -> val t = cfg.texts[i]
                LayerRow(BmIcons.Text, t.content.ifBlank { uiText("text") }, t.locked, state.selection == EditorSelection.TextItem(i)) { state.selection = EditorSelection.TextItem(i); onDismiss() } }
            cfg.cities.indices.reversed().forEach { i -> val city = cfg.cities[i]
                LayerRow(BmIcons.City, city.nickname.ifBlank { city.name }, city.locked, state.selection == EditorSelection.CityItem(i)) { state.selection = EditorSelection.CityItem(i); onDismiss() } }
            cfg.layers.indices.reversed().forEach { i -> val l = cfg.layers[i]
                LayerRow(BmIcons.Shape, shapeName(l.shape), l.locked, state.selection == EditorSelection.Layer(i)) { state.selection = EditorSelection.Layer(i); onDismiss() } }
        }
    }
}

@Composable
private fun LayerRow(icon: ImageVector, label: String, locked: Boolean, selected: Boolean, onClick: () -> Unit) {
    val c = Bm.colors
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(if (selected) c.accentSoft else Color.Transparent)
            .clickable(onClick = onClick).padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = c.ink, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, style = Bm.type.body, color = c.ink, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (locked) Icon(BmIcons.Lock, null, tint = c.inkFaint, modifier = Modifier.size(16.dp))
    }
}

private val WIDGET_SIZES = listOf(2 to 1, 2 to 2, 3 to 2, 4 to 1, 4 to 2, 4 to 3, 4 to 4, 3 to 3, 4 to 5)

@Composable
private fun WidgetSettingsSheet(state: EditorState, onDismiss: () -> Unit) {
    val c = Bm.colors
    val context = LocalContext.current
    val cfg = state.config
    BmSheet(onDismiss = onDismiss, title = uiText("widget_settings")) {
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp).verticalScroll(rememberScrollState())) {
            BmOverline(uiText("home_screen_size"))
            Spacer(Modifier.height(10.dp))
            @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
            androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                WIDGET_SIZES.forEach { (cols, rows) ->
                    com.bestmates.worldclock.ui.design.BmChip("$cols × $rows", cfg.columns == cols && cfg.rows == rows, onClick = {
                        val (w, h) = LauncherMetrics(context).boardFor(cols, rows)
                        state.updateConfig { SceneGeometry.reflow(it.copy(columns = cols, rows = rows), w, h) }
                    })
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(uiText("size_change_note"), style = Bm.type.bodySmall, color = c.inkSoft)
            Spacer(Modifier.height(22.dp))
            BmOverline(uiText("colour_mode"))
            Spacer(Modifier.height(10.dp))
            BmSegmented(
                listOf(uiText("follow_phone"), uiText("light"), uiText("dark")),
                when (cfg.theme) { ThemeMode.SYSTEM -> 0; ThemeMode.LIGHT -> 1; ThemeMode.DARK -> 2 },
                { i -> state.updateConfig { it.copy(theme = ThemeMode.entries[i]) } },
            )
            Spacer(Modifier.height(6.dp))
            Text(uiText("colour_mode_note"), style = Bm.type.bodySmall, color = c.inkSoft)
            Spacer(Modifier.height(14.dp))
            BmRow(uiText("time_24"), trailing = { BmSwitch(cfg.use24Hour, { v -> state.updateConfig { it.copy(use24Hour = v) } }) })
        }
    }
}

@Composable
private fun BackdropSheet(current: Backdrop, wallpaper: com.bestmates.worldclock.ui.common.WallpaperTones?, onDismiss: () -> Unit, onPick: (Backdrop) -> Unit) {
    val c = Bm.colors
    BmSheet(onDismiss = onDismiss, title = uiText("preview_backdrop")) {
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
            Text(uiText("backdrop_note"), style = Bm.type.bodySmall, color = c.inkSoft)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Backdrop.entries.forEach { b ->
                    val selected = b == current
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Box(
                            Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(14.dp)).background(backdropBrush(b, wallpaper))
                                .border(if (selected) 2.5.dp else 1.dp, if (selected) c.ink else c.hairline, RoundedCornerShape(14.dp))
                                .clickable(role = Role.RadioButton) { onPick(b) },
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(uiText("backdrop_${b.id}"), style = Bm.type.caption, color = if (selected) c.ink else c.inkSoft, maxLines = 1)
                    }
                }
            }
        }
    }
}
