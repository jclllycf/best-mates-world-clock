package com.bestmates.worldclock
import com.bestmates.worldclock.ui.editor.CanvasSelection
import com.bestmates.worldclock.ui.editor.EditorClipboard
import com.bestmates.worldclock.ui.editor.*
import com.bestmates.worldclock.ui.editor.*
import com.bestmates.worldclock.ui.editor.LayoutCanvas
import com.bestmates.worldclock.ui.editor.SolarIconSample

import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.components.CrystalPanel
import com.bestmates.worldclock.ui.components.CrystalChoiceTile
import com.bestmates.worldclock.ui.components.CrystalDialog
import com.bestmates.worldclock.ui.components.CrystalPickerRow
import com.bestmates.worldclock.ui.components.CrystalTextField
import com.bestmates.worldclock.ui.components.OrbitButton
import com.bestmates.worldclock.ui.components.OrbitButtonStyle
import com.bestmates.worldclock.ui.components.OrbitChoiceChip
import com.bestmates.worldclock.ui.components.OrbitCustomIconButton
import com.bestmates.worldclock.ui.components.OrbitIconButton
import com.bestmates.worldclock.ui.components.OrbitSliderRow
import com.bestmates.worldclock.ui.components.OrbitSwitchRow
import com.bestmates.worldclock.ui.editor.EditorChromeStrings
import com.bestmates.worldclock.ui.editor.EditorControlShelf
import com.bestmates.worldclock.ui.editor.EditorFrame
import com.bestmates.worldclock.ui.editor.rotateCanvasVector
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import com.bestmates.worldclock.ui.theme.BestMatesDesignTheme
import com.bestmates.worldclock.ui.settings.AppAppearanceMode
import com.bestmates.worldclock.ui.settings.AppLanguage
import com.bestmates.worldclock.ui.settings.AppThemeStyle
import com.bestmates.worldclock.ui.settings.SettingsScreen
import com.bestmates.worldclock.ui.settings.SettingsStrings
import com.bestmates.worldclock.ui.settings.legacyAppearanceFor
import com.bestmates.worldclock.ui.workspace.WorkspaceRoute
import com.bestmates.worldclock.ui.workspace.WorkspaceStrings
import androidx.compose.ui.unit.Dp

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.togetherWith
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.zIndex
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.data.*
import com.bestmates.worldclock.model.*
import com.bestmates.worldclock.widget.WorldClockWidgetProvider
import com.bestmates.worldclock.widget.PinWidgetReceiver
import com.bestmates.worldclock.widget.BackgroundRenderer
import com.bestmates.worldclock.solar.SolarCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.bestmates.worldclock.ui.i18n.*
import kotlin.math.roundToInt
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) setResult(Activity.RESULT_CANCELED)
        setContent { BestMatesApp(widgetId) }
    }
}

@Composable private fun BestMatesApp(widgetId: Int) {
    val activity = androidx.activity.compose.LocalActivity.current!!
    val preferences = remember { activity.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE) }
    var appTheme by remember { mutableStateOf(runCatching { AppThemeStyle.valueOf(preferences.getString("app_theme", "CLOUD") ?: "CLOUD") }.getOrDefault(AppThemeStyle.CLOUD)) }
    var appAppearance by remember { mutableStateOf(AppAppearanceMode.from(preferences.getString("appearance_mode", null))) }
    var appLanguage by remember { mutableStateOf(AppLanguage.from(preferences.getString("language", "en"))) }
    var editingProject by remember { mutableStateOf<String?>(null) }
    var showSettings by remember { mutableStateOf(false) }
    val systemIsDark = isSystemInDarkTheme()
    val brightnessMode = (appAppearance ?: legacyAppearanceFor(appTheme)).brightness
    CompositionLocalProvider(LocalBestMatesLanguage provides appLanguage) {
        BestMatesDesignTheme(
            systemIsDark = systemIsDark,
            identity = appTheme.identity,
            brightnessMode = brightnessMode,
        ) {
            val colors = BestMatesDesign.colors
            SideEffect {
                updateSystemBars(activity, colors.background.toArgb(), colors.isDark)
            }
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val widgetStore = ConfigStore(activity)
                val pinnedProject = AppWidgetManager.getInstance(activity)
                    .getAppWidgetOptions(widgetId)
                    .getString("projectId")
                WidgetEditor(
                    pinnedProject?.let(widgetStore::loadProject) ?: widgetStore.load(widgetId),
                    null,
                    onBack = {
                        activity.setResult(Activity.RESULT_CANCELED)
                        activity.finish()
                    },
                ) { config ->
                    ConfigStore(activity).save(widgetId, config)
                    WorldClockWidgetProvider.render(
                        activity,
                        AppWidgetManager.getInstance(activity),
                        widgetId,
                    )
                    activity.setResult(
                        Activity.RESULT_OK,
                        Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId),
                    )
                    activity.finish()
                }
            } else {
                val destination = when {
                    showSettings -> "settings"
                    editingProject != null -> "editor:${editingProject}"
                    else -> "workspace"
                }
                AnimatedContent(
                    targetState = destination,
                    transitionSpec = {
                        (fadeIn(tween(240)) +
                            slideInHorizontally(tween(260), initialOffsetX = { it / 12 }) +
                            scaleIn(tween(240), initialScale = .985f)) togetherWith
                            (fadeOut(tween(170)) +
                                slideOutHorizontally(tween(190), targetOffsetX = { -it / 18 }) +
                                scaleOut(tween(170), targetScale = .992f))
                    },
                    label = "best-mates-navigation",
                ) { screen ->
                    when {
                        screen == "settings" -> SettingsScreen(
                            strings = settingsStrings(),
                            versionName = BuildConfig.VERSION_NAME,
                            currentTheme = appTheme,
                            currentAppearance = appAppearance,
                            onThemeChange = { next ->
                                appTheme = next
                                preferences.edit().putString("app_theme", next.name).apply()
                            },
                            onAppearanceChange = { next ->
                                appAppearance = next
                                preferences.edit().putString("appearance_mode", next.stored).apply()
                            },
                            currentLanguage = appLanguage,
                            onLanguageChange = { next ->
                                appLanguage = next
                                preferences.edit().putString("language", next.stored).apply()
                            },
                            onBack = { showSettings = false },
                        )
                        screen.startsWith("editor:") -> {
                            val store = remember { ProjectStore(activity) }
                            val projectId = screen.substringAfter("editor:")
                            WidgetEditor(
                                store.load(projectId),
                                projectId,
                                onBack = { editingProject = null },
                            ) { config ->
                                store.save(projectId, config)
                                refreshProjectWidgets(activity, projectId)
                            }
                        }
                        else -> WorkspaceRoute(
                            strings = workspaceStrings(),
                            onOpenProject = { editingProject = it },
                            onSettings = { showSettings = true },
                        )
                    }
                }
            }
        }
    }
}

private fun updateSystemBars(activity: Activity, backgroundColor: Int, isDark: Boolean) {
    activity.window.statusBarColor = backgroundColor
    activity.window.navigationBarColor = backgroundColor
    val controller = androidx.core.view.WindowCompat.getInsetsController(activity.window, activity.window.decorView)
    controller.isAppearanceLightStatusBars = !isDark
    controller.isAppearanceLightNavigationBars = !isDark
}

@Composable
private fun workspaceStrings() = WorkspaceStrings(
    brand = "Best Mates",
    collectionTitle = uiText("workspace_collection"),
    collectionCountTemplate = uiText("workspace_collection_count"),
    selectedCountTemplate = uiText("selected_count"),
    settings = uiText("settings"),
    createWidget = uiText("create_widget"),
    widgetName = uiText("widget_name"),
    unnamed = uiText("unnamed"),
    emptyTitle = uiText("empty_workspace_title"),
    emptyBody = uiText("empty_workspace_body"),
    backgroundsCitiesTemplate = uiText("backgrounds_cities"),
    autoSaveOn = uiText("auto_save_on"),
    editedLocally = uiText("edited_locally"),
    selected = uiText("selected"),
    rename = uiText("rename"),
    copy = uiText("copy"),
    cut = uiText("cut"),
    paste = uiText("paste"),
    delete = uiText("delete"),
    cancel = uiText("cancel"),
    apply = uiText("apply"),
    close = uiText("close"),
    deleteTitle = uiText("delete_projects_title"),
    deleteMessageTemplate = uiText("delete_projects_message"),
)

@Composable
private fun settingsStrings() = SettingsStrings(
    title = uiText("settings"),
    back = uiText("back"),
    identity = uiText("identity"),
    themeDescriptions = AppThemeStyle.entries.associateWith {
        uiText("theme_${it.name.lowercase()}_description")
    },
    appearance = uiText("appearance"),
    appearanceNote = uiText("appearance_note"),
    followSystem = uiText("follow_system"),
    light = uiText("light"),
    dark = uiText("dark"),
    displayLanguage = uiText("display_language"),
    resourceImport = uiText("resource_import"),
    importFontTemplate = uiText("import_font"),
    importIconTemplate = uiText("import_icon"),
    notImported = uiText("not_imported"),
    importNote = uiText("import_note"),
    selected = uiText("selected"),
    about = uiText("about"),
    versionTemplate = uiText("version_format"),
)

@Composable
private fun editorChromeStrings() = EditorChromeStrings(
    title = uiText("editor"),
    back = uiText("back"),
    undo = uiText("undo"),
    redo = uiText("redo"),
    autoSave = uiText("auto_save"),
    addComponent = uiText("add_component"),
    addCity = uiText("add_city"),
    addText = uiText("add_text"),
    snap = uiText("snap"),
    guides = uiText("guides"),
    addToHome = uiText("add_home"),
    save = uiText("save"),
    selected = uiText("selected"),
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable private fun WidgetEditor(initial: WidgetConfig, projectId: String?, onBack: () -> Unit = {}, onSave: (WidgetConfig) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val defaultTextLabel = uiText("text")
    var config by remember {
        val layers = initial.layers
        val primaryId = layers.firstOrNull()?.id ?: 0L
        mutableStateOf(
            initial.copy(
                layoutVersion = 8,
                columns = 4,
                rows = 4,
                layers = layers,
                cities = initial.cities.map { if (it.componentId == 0L && primaryId != 0L) it.copy(componentId = primaryId) else it },
                texts = initial.texts.map { if (it.componentId == 0L && primaryId != 0L) it.copy(componentId = primaryId) else it }
            )
        )
    }
    var cityPicker by remember { mutableStateOf(false) }
    var addShapePicker by remember { mutableStateOf(false) }
    var selection by remember { mutableStateOf<CanvasSelection?>(null) }
    var snapEnabled by remember { mutableStateOf(true) }
    var guidesVisible by remember { mutableStateOf(true) }
    var clipboard by remember { mutableStateOf<EditorClipboard?>(null) }
    var history by remember { mutableStateOf(listOf(config)) }
    var historyIndex by remember { mutableIntStateOf(0) }
    var gestureBase by remember { mutableStateOf<WidgetConfig?>(null) }
    var gestureLatest by remember { mutableStateOf(config) }
    fun applyChange(next: WidgetConfig) {
        if (next == config) return
        val base = history.take(historyIndex + 1)
        val updated = (base + next).takeLast(32)
        history = updated
        historyIndex = updated.lastIndex
        config = next
    }
    fun beginGesture() { if (gestureBase == null) { gestureBase = config; gestureLatest = config } }
    fun applyLive(next: WidgetConfig) { if (next != config) { gestureLatest = next; config = next } }
    fun finishGesture() {
        val start = gestureBase
        val final = gestureLatest
        gestureBase = null
        if (start == null || start == final) return
        val base = history.take(historyIndex + 1)
        val updated = (base + final).takeLast(32)
        history = updated
        historyIndex = updated.lastIndex
        config = final
    }
    fun pasteClipboard() {
        when (val value = clipboard) {
            is EditorClipboard.Layer -> if (config.layers.size < 6) {
                val pasted = value.value.copy(id = System.nanoTime(), x = (value.value.x + .06f).coerceIn(-.4f, .4f), y = (value.value.y + .06f).coerceIn(-.4f, .4f))
                applyChange(config.copy(layers = config.layers + pasted))
                selection = CanvasSelection.ComponentItem(config.layers.size)
            }
            is EditorClipboard.CityValue -> if (config.cities.size < 10) {
                applyChange(config.copy(cities = config.cities + value.value.copy(x = value.value.x + .08f, y = value.value.y + .08f)))
                selection = CanvasSelection.CityItem(config.cities.size)
            }
            is EditorClipboard.TextValue -> if (config.texts.size < 5) {
                applyChange(config.copy(texts = config.texts + value.value.copy(x = value.value.x + .08f, y = value.value.y + .08f)))
                selection = CanvasSelection.TextItem(config.texts.size)
            }
            null -> Unit
        }
    }
    val latestConfig by rememberUpdatedState(config)
    LaunchedEffect(config.autoSave) {
        if (config.autoSave) while (true) {
            delay(5 * 60 * 1000L)
            onSave(latestConfig)
        }
    }
    BackHandler { if (cityPicker) cityPicker = false else onBack() }
    EditorFrame(
        strings = editorChromeStrings(),
        canUndo = historyIndex > 0,
        canRedo = historyIndex < history.lastIndex,
        autoSave = config.autoSave,
        onBack = onBack,
        onUndo = { if (historyIndex > 0) { historyIndex--; config = history[historyIndex] } },
        onRedo = { if (historyIndex < history.lastIndex) { historyIndex++; config = history[historyIndex] } },
        onAutoSave = { applyChange(config.copy(autoSave = !config.autoSave)) },
        onSave = { onSave(config) },
        onAddToHome = projectId?.let { id ->
            { onSave(config); pinWidget(context, id, config) }
        },
    ) {
            Spacer(Modifier.height(8.dp))
            EditorControlShelf(
                strings = editorChromeStrings(),
                snapEnabled = snapEnabled,
                guidesVisible = guidesVisible,
                canAddComponent = config.layers.size < 6,
                canAddCity = config.cities.size < 10,
                canAddText = config.texts.size < 5,
                onSnap = { snapEnabled = !snapEnabled },
                onGuides = { guidesVisible = !guidesVisible },
                onAddComponent = { addShapePicker = true },
                onAddCity = { cityPicker = true },
                onAddText = {
                    if (config.texts.size < 5) {
                        val index = config.texts.size
                        val componentId = (selection as? CanvasSelection.ComponentItem)?.index
                            ?.let(config.layers::getOrNull)?.id
                            ?: config.layers.firstOrNull()?.id ?: 0L
                        applyChange(
                            config.copy(
                                texts = config.texts + CustomText(
                                    content = defaultTextLabel,
                                    y = -.65f + index * .24f,
                                    componentId = componentId,
                                ),
                            ),
                        )
                        selection = CanvasSelection.TextItem(index)
                    }
                },
            )
            Spacer(Modifier.height(8.dp))
            LayoutCanvas(config = config, onCityChange = { index, city ->
                applyLive(config.copy(cities = config.cities.toMutableList().also { list ->
                    if (index in list.indices) list[index] = city
                }))
            }, onTextChange = { index, text ->
                applyLive(config.copy(texts = config.texts.toMutableList().also { list ->
                    if (index in list.indices) list[index] = text
                }))
            }, onLayerChange = { index, layer ->
                applyLive(config.copy(layers = config.layers.toMutableList().also { list ->
                    if (index in list.indices) list[index] = layer
                }))
            }, onGestureStart = ::beginGesture, onGestureFinished = ::finishGesture,
                selection = selection, onSelect = { selection = it }, snapEnabled = snapEnabled, guidesVisible = guidesVisible, emptyCanvasText = uiText("empty_canvas"))

            Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).animateContentSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SelectedElementEditor(config, selection, onConfigChange = ::applyChange, onClearSelection = { selection = null }, onCopy = { clipboard = it }, onPaste = ::pasteClipboard)

            SectionTitle(uiText("selected_cities"))
            if (config.cities.isEmpty()) Text(uiText("no_cities"), style = MaterialTheme.typography.bodySmall)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { config.cities.forEachIndexed { index, city ->
                OrbitChoiceChip(
                    label = "${index + 1}. ${city.nickname.ifBlank { city.name }}",
                    selected = selection == CanvasSelection.CityItem(index),
                    selectedDescription = uiText("selected"),
                    onClick = { selection = CanvasSelection.CityItem(index) },
                )
            } }

            SectionTitle(uiText("theme"))
            ThemePicker(config.theme) { applyChange(config.copy(theme = it)) }

            CrystalPanel {
                Text(uiText("time_format"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                SwitchRow(uiText("time_24"), config.use24Hour) { applyChange(config.copy(use24Hour = it)) }
            }
            Spacer(Modifier.height(24.dp))
            }
    }
    if (cityPicker) CityPicker(onDismiss = { cityPicker = false }) { city ->
        if (config.cities.none { it.geoId == city.geoId } && config.cities.size < 10) {
            val componentId = (selection as? CanvasSelection.ComponentItem)?.index?.let(config.layers::getOrNull)?.id
                ?: config.layers.firstOrNull()?.id ?: 0L
            val placed = initialPlacement(config.cities.count { it.componentId == componentId }, city.copy(scale = 1f, componentId = componentId))
            applyChange(config.copy(cities = config.cities + placed))
            selection = CanvasSelection.CityItem(config.cities.lastIndex)
        }
        cityPicker = false
    }
    if (addShapePicker) ShapePickerDialog(
        selected = null,
        title = uiText("choose_new_shape"),
        onDismiss = { addShapePicker = false }
    ) { shape ->
        val index = config.layers.size
        applyChange(config.copy(layers = config.layers + newWidgetLayer(shape, index)))
        selection = CanvasSelection.ComponentItem(index)
        addShapePicker = false
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun SelectedElementEditor(config: WidgetConfig, selection: CanvasSelection?, onConfigChange: (WidgetConfig) -> Unit, onClearSelection: () -> Unit, onCopy: (EditorClipboard) -> Unit, onPaste: () -> Unit) {
    androidx.compose.animation.AnimatedContent(
        targetState = selection,
        label = "selection_editor",
        transitionSpec = { androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(200)).togetherWith(androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(200))) }
    ) { targetSelection ->
        when (targetSelection) {
        is CanvasSelection.ComponentItem -> config.layers.getOrNull(targetSelection.index)?.let { layer ->
            fun update(changed: WidgetLayer) = onConfigChange(config.copy(layers = config.layers.toMutableList().also { it[targetSelection.index] = changed }))
            fun removeLayer(copyFirst: Boolean) {
                if (copyFirst) onCopy(EditorClipboard.Layer(layer))
                val replacementId = config.layers.firstOrNull { it.id != layer.id }?.id ?: 0L
                onConfigChange(config.copy(
                    layers = config.layers.toMutableList().also { it.removeAt(targetSelection.index) },
                    cities = config.cities.map { if (it.componentId == layer.id) it.copy(componentId = replacementId) else it },
                    texts = config.texts.map { if (it.componentId == layer.id) it.copy(componentId = replacementId) else it }
                ))
                onClearSelection()
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        EditorActionRow(
                            onClear = onClearSelection,
                            onCopy = { onCopy(EditorClipboard.Layer(layer)) },
                            onPaste = onPaste,
                            locked = layer.locked,
                            onLock = { update(layer.copy(locked = !layer.locked)) },
                            onCut = { removeLayer(true) },
                            onDelete = { removeLayer(false) }
                        )
                        Text(uiText("drag_shape_hint"), style = MaterialTheme.typography.bodySmall, color = BestMatesDesign.colors.textSecondary)
                    }
                }
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionTitle(uiText("shape_and_color"))
                        ShapePicker(layer.shape) { shape ->
                            val natural = newWidgetLayer(shape)
                            update(layer.copy(shape = shape, width = natural.width, height = natural.height, x = 0f, y = 0f))
                        }
                        DayNightPaletteField(layer.dayColor, layer.nightColor) { day, night -> update(layer.copy(dayColor = day, nightColor = night)) }
                    }
                }
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionTitle(uiText("outline_light"))
                        SliderRow(uiText("outline_width"), layer.borderWidth, 0f..6f) { update(layer.copy(borderWidth = it)) }
                        SliderRow(uiText("outline_opacity"), layer.borderOpacity, 0f..1f) { update(layer.copy(borderOpacity = it)) }
                        SliderRow(uiText("ambient_glow"), layer.softness, 0f..48f) { update(layer.copy(softness = it)) }
                        SliderRow(uiText("glow_strength"), layer.glowOpacity, 0f..0.45f) { update(layer.copy(glowOpacity = it)) }
                    }
                }
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionTitle(uiText("depth"))
                        SliderRow(uiText("shadow_spread"), layer.shadow, 0f..32f) { update(layer.copy(shadow = it)) }
                        SliderRow(uiText("shadow_opacity"), layer.shadowOpacity, 0f..0.4f) { update(layer.copy(shadowOpacity = it)) }
                        SliderRow(uiText("shadow_offset"), layer.shadowYOffset, -8f..16f) { update(layer.copy(shadowYOffset = it)) }
                    }
                }
            }
        }
        is CanvasSelection.CityItem -> config.cities.getOrNull(targetSelection.index)?.let { city ->
            var part by remember(targetSelection.index) { mutableStateOf<CityPart?>(null) }
            var replaceCity by remember(targetSelection.index) { mutableStateOf(false) }
            fun update(changed: City) = onConfigChange(config.copy(cities = config.cities.toMutableList().also { it[targetSelection.index] = changed }))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        EditorActionRow(
                            onClear = onClearSelection,
                            onCopy = { onCopy(EditorClipboard.CityValue(city)) },
                            onPaste = onPaste,
                            locked = city.locked,
                            onLock = { update(city.copy(locked = !city.locked)) },
                            onCut = { onCopy(EditorClipboard.CityValue(city)); onConfigChange(config.copy(cities = config.cities.toMutableList().also { it.removeAt(targetSelection.index) })); onClearSelection() },
                            onDelete = { onConfigChange(config.copy(cities = config.cities.toMutableList().also { it.removeAt(targetSelection.index) })); onClearSelection() }
                        )
                        OrbitButton(
                            label = uiText("change_city"),
                            onClick = { replaceCity = true },
                            modifier = Modifier.fillMaxWidth(),
                            style = OrbitButtonStyle.Secondary,
                        )
                        CrystalTextField(city.nickname, { nickname -> onConfigChange(config.copy(cities = config.cities.toMutableList().also { it[targetSelection.index] = city.copy(nickname = nickname.take(30)) })) }, Modifier.fillMaxWidth(), label = { Text(uiText("display_name_hint")) }, singleLine = true)
                        Text(uiText("drag_city_hint"), style = MaterialTheme.typography.bodySmall, color = BestMatesDesign.colors.textSecondary)
                    }
                }
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionTitle(uiText("city_display"))
                        EditableDisplayRow(uiText("city_name"), true, null, onEdit = { part = CityPart.NAME })
                        EditableDisplayRow(uiText("show_time"), city.showTime, { value -> update(city.copy(showTime = value)) }, { part = CityPart.TIME })
                        EditableDisplayRow(uiText("show_date"), city.showDate, { value -> update(city.copy(showDate = value)) }, { part = CityPart.DATE })
                        EditableDisplayRow(uiText("show_country"), city.showCountry, { value -> update(city.copy(showCountry = value)) }, { part = CityPart.NAME })
                        EditableDisplayRow(uiText("show_solar"), city.showSolar, { value -> update(city.copy(showSolar = value)) }, { part = CityPart.SOLAR })
                    }
                }
            }
            if (part != null) CityPartDialog(part!!, city, config.use24Hour, onDismiss = { part = null }, onChange = { update(it) })
            if (replaceCity) CityPicker(onDismiss = { replaceCity = false }) { picked ->
                update(picked.copy(
                    nickname = city.nickname, x = city.x, y = city.y, scale = city.scale,
                    showTime = city.showTime, showDate = city.showDate, showCountry = city.showCountry, showSolar = city.showSolar,
                    nameColor = city.nameColor, timeColor = city.timeColor, dateColor = city.dateColor,
                    nameFont = city.nameFont, timeFont = city.timeFont, dateFont = city.dateFont,
                    sunStyle = city.sunStyle, moonStyle = city.moonStyle, sunColor = city.sunColor, moonColor = city.moonColor,
                    componentId = city.componentId, rotation = city.rotation, locked = city.locked
                ))
                replaceCity = false
            }
        }
        is CanvasSelection.TextItem -> config.texts.getOrNull(targetSelection.index)?.let { item ->
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        EditorActionRow(
                            onClear = onClearSelection,
                            onCopy = { onCopy(EditorClipboard.TextValue(item)) },
                            onPaste = onPaste,
                            locked = item.locked,
                            onLock = { onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[targetSelection.index] = item.copy(locked = !item.locked) })) },
                            onCut = { onCopy(EditorClipboard.TextValue(item)); onConfigChange(config.copy(texts = config.texts.toMutableList().also { it.removeAt(targetSelection.index) })); onClearSelection() },
                            onDelete = { onConfigChange(config.copy(texts = config.texts.toMutableList().also { it.removeAt(targetSelection.index) })); onClearSelection() }
                        )
                        CrystalTextField(item.content, { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[targetSelection.index] = item.copy(content = value.take(80)) })) }, Modifier.fillMaxWidth(), label = { Text(uiText("text_content")) })
                        Text(uiText("drag_text_hint"), style = MaterialTheme.typography.bodySmall, color = BestMatesDesign.colors.textSecondary)
                    }
                }
                CrystalPanel {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionTitle(uiText("typography"))
                        FontField(uiText("font"), item.font, item.content.ifBlank { "Aa 12:34" }) { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[targetSelection.index] = item.copy(font = value) })) }
                        ColorField(uiText("text_color"), item.color) { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[targetSelection.index] = item.copy(color = value) })) }
                        SliderRow(uiText("opacity"), item.alpha, 0f..1f) { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { list -> list[targetSelection.index] = item.copy(alpha = value) })) }
                    }
                }
            }
        }
        null -> Text(uiText("select_hint"), style = MaterialTheme.typography.bodySmall)
    }
    }
}

private fun pinWidget(context: android.content.Context, projectId: String, config: WidgetConfig) {
    val manager = AppWidgetManager.getInstance(context)
    if (!manager.isRequestPinAppWidgetSupported) return
    val provider = ComponentName(context.packageName, "${context.packageName}.widget.SizedWidgetProvider_4x4")
    context.packageManager.setComponentEnabledSetting(provider, android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED, android.content.pm.PackageManager.DONT_KILL_APP)
    val callbackIntent = Intent(context, PinWidgetReceiver::class.java).putExtra(PinWidgetReceiver.EXTRA_PROJECT_ID, projectId)
    val callback = PendingIntent.getBroadcast(
        context,
        projectId.hashCode(),
        callbackIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )
    // The unique provider contains the authoritative target cell size. Passing
    // screen-derived pixel dimensions here made some launchers collapse every
    // request back to the old 4×3 widget.
    manager.requestPinAppWidget(provider, null, callback)
}

private fun refreshProjectWidgets(context: android.content.Context, projectId: String) {
    val manager = AppWidgetManager.getInstance(context)
    manager.installedProviders.filter { it.provider.packageName == context.packageName }.forEach { provider ->
        manager.getAppWidgetIds(provider.provider).forEach { id ->
            if (manager.getAppWidgetOptions(id).getString("projectId") == projectId) {
                WorldClockWidgetProvider.render(context, manager, id)
            }
        }
    }
}
