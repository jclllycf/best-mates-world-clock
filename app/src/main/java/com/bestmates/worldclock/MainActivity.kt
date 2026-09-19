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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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

private enum class CityPart { NAME, TIME, DATE, SOLAR }

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

            Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
    when (selection) {
        is CanvasSelection.ComponentItem -> config.layers.getOrNull(selection.index)?.let { layer ->
            fun update(changed: WidgetLayer) = onConfigChange(config.copy(layers = config.layers.toMutableList().also { it[selection.index] = changed }))
            fun removeLayer(copyFirst: Boolean) {
                if (copyFirst) onCopy(EditorClipboard.Layer(layer))
                val replacementId = config.layers.firstOrNull { it.id != layer.id }?.id ?: 0L
                onConfigChange(config.copy(
                    layers = config.layers.toMutableList().also { it.removeAt(selection.index) },
                    cities = config.cities.map { if (it.componentId == layer.id) it.copy(componentId = replacementId) else it },
                    texts = config.texts.map { if (it.componentId == layer.id) it.copy(componentId = replacementId) else it }
                ))
                onClearSelection()
            }
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
                    Text(uiText("drag_shape_hint"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ShapePicker(layer.shape) { shape ->
                        val natural = newWidgetLayer(shape)
                        update(layer.copy(shape = shape, width = natural.width, height = natural.height, x = 0f, y = 0f))
                    }
                    DayNightPaletteField(layer.dayColor, layer.nightColor) { day, night -> update(layer.copy(dayColor = day, nightColor = night)) }
                    Text(uiText("outline_light"), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    SliderRow(uiText("outline_width"), layer.borderWidth, 0f..6f) { update(layer.copy(borderWidth = it)) }
                    SliderRow(uiText("outline_opacity"), layer.borderOpacity, 0f..1f) { update(layer.copy(borderOpacity = it)) }
                    SliderRow(uiText("ambient_glow"), layer.softness, 0f..48f) { update(layer.copy(softness = it)) }
                    SliderRow(uiText("glow_strength"), layer.glowOpacity, 0f..0.45f) { update(layer.copy(glowOpacity = it)) }
                    Text(uiText("depth"), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    SliderRow(uiText("shadow_spread"), layer.shadow, 0f..32f) { update(layer.copy(shadow = it)) }
                    SliderRow(uiText("shadow_opacity"), layer.shadowOpacity, 0f..0.4f) { update(layer.copy(shadowOpacity = it)) }
                    SliderRow(uiText("shadow_offset"), layer.shadowYOffset, -8f..16f) { update(layer.copy(shadowYOffset = it)) }
                }
            }
        }
        is CanvasSelection.CityItem -> config.cities.getOrNull(selection.index)?.let { city ->
            var part by remember(selection.index) { mutableStateOf<CityPart?>(null) }
            var replaceCity by remember(selection.index) { mutableStateOf(false) }
            fun update(changed: City) = onConfigChange(config.copy(cities = config.cities.toMutableList().also { it[selection.index] = changed }))
            CrystalPanel { Column {
                EditorActionRow(
                    onClear = onClearSelection,
                    onCopy = { onCopy(EditorClipboard.CityValue(city)) },
                    onPaste = onPaste,
                    locked = city.locked,
                    onLock = { update(city.copy(locked = !city.locked)) },
                    onCut = { onCopy(EditorClipboard.CityValue(city)); onConfigChange(config.copy(cities = config.cities.toMutableList().also { it.removeAt(selection.index) })); onClearSelection() },
                    onDelete = { onConfigChange(config.copy(cities = config.cities.toMutableList().also { it.removeAt(selection.index) })); onClearSelection() }
                )
                OrbitButton(
                    label = uiText("change_city"),
                    onClick = { replaceCity = true },
                    modifier = Modifier.fillMaxWidth(),
                    style = OrbitButtonStyle.Secondary,
                )
                CrystalTextField(city.nickname, { nickname -> onConfigChange(config.copy(cities = config.cities.toMutableList().also { it[selection.index] = city.copy(nickname = nickname.take(30)) })) }, Modifier.fillMaxWidth(), label = { Text(uiText("display_name_hint")) }, singleLine = true)
                Text(uiText("drag_city_hint"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                EditableDisplayRow(uiText("city_name"), true, null, onEdit = { part = CityPart.NAME })
                EditableDisplayRow(uiText("show_time"), city.showTime, { value -> update(city.copy(showTime = value)) }, { part = CityPart.TIME })
                EditableDisplayRow(uiText("show_date"), city.showDate, { value -> update(city.copy(showDate = value)) }, { part = CityPart.DATE })
                EditableDisplayRow(uiText("show_country"), city.showCountry, { value -> update(city.copy(showCountry = value)) }, { part = CityPart.NAME })
                EditableDisplayRow(uiText("show_solar"), city.showSolar, { value -> update(city.copy(showSolar = value)) }, { part = CityPart.SOLAR })
            } }
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
        is CanvasSelection.TextItem -> config.texts.getOrNull(selection.index)?.let { item ->
            CrystalPanel { Column {
                EditorActionRow(
                    onClear = onClearSelection,
                    onCopy = { onCopy(EditorClipboard.TextValue(item)) },
                    onPaste = onPaste,
                    locked = item.locked,
                    onLock = { onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[selection.index] = item.copy(locked = !item.locked) })) },
                    onCut = { onCopy(EditorClipboard.TextValue(item)); onConfigChange(config.copy(texts = config.texts.toMutableList().also { it.removeAt(selection.index) })); onClearSelection() },
                    onDelete = { onConfigChange(config.copy(texts = config.texts.toMutableList().also { it.removeAt(selection.index) })); onClearSelection() }
                )
                CrystalTextField(item.content, { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[selection.index] = item.copy(content = value.take(80)) })) }, Modifier.fillMaxWidth(), label = { Text(uiText("text_content")) })
                FontField(uiText("font"), item.font, item.content.ifBlank { "Aa 12:34" }) { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[selection.index] = item.copy(font = value) })) }
                ColorField(uiText("text_color"), item.color) { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { it[selection.index] = item.copy(color = value) })) }
                SliderRow(uiText("opacity"), item.alpha, 0f..1f) { value -> onConfigChange(config.copy(texts = config.texts.toMutableList().also { list -> list[selection.index] = item.copy(alpha = value) })) }
                Text(uiText("drag_text_hint"), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } }
        }
        null -> Text(uiText("select_hint"), style = MaterialTheme.typography.bodySmall)
    }
}

private enum class EditorAction { CLEAR, COPY, PASTE, PIN, CUT, DELETE, RENAME, ADD_COMPONENT, ADD_CITY, ADD_TEXT, UNDO, REDO, AUTO_SAVE }

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditorActionRow(
    onClear: () -> Unit,
    onCopy: () -> Unit,
    onPaste: () -> Unit,
    locked: Boolean,
    onLock: () -> Unit,
    onCut: () -> Unit,
    onDelete: () -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EditorActionButton(EditorAction.CLEAR, uiText("deselect"), onClear)
        EditorActionButton(EditorAction.COPY, uiText("copy"), onCopy)
        EditorActionButton(EditorAction.PASTE, uiText("paste"), onPaste)
        EditorActionButton(EditorAction.PIN, if (locked) uiText("unpin") else uiText("pin"), onLock, selected = locked)
        EditorActionButton(EditorAction.CUT, uiText("cut"), onCut)
        EditorActionButton(EditorAction.DELETE, uiText("delete"), onDelete, destructive = true)
    }
}

@Composable
private fun EditorActionButton(
    action: EditorAction,
    description: String,
    onClick: () -> Unit,
    selected: Boolean = false,
    enabled: Boolean = true,
    destructive: Boolean = false
) {
    if (action == EditorAction.PIN) {
        OrbitCustomIconButton(
            contentDescription = description,
            onClick = onClick,
            selected = selected,
            destructive = destructive,
            enabled = enabled,
        ) { tint ->
            Canvas(Modifier.size(20.dp)) { drawProductionPin(tint) }
        }
        return
    }
    val icon = when (action) {
        EditorAction.CLEAR -> OrbitIcons.Close
        EditorAction.COPY -> OrbitIcons.Copy
        EditorAction.PASTE -> OrbitIcons.Paste
        EditorAction.CUT -> OrbitIcons.Cut
        EditorAction.DELETE -> OrbitIcons.Delete
        EditorAction.RENAME -> OrbitIcons.Rename
        EditorAction.ADD_COMPONENT -> OrbitIcons.AddComponent
        EditorAction.ADD_CITY -> OrbitIcons.AddCity
        EditorAction.ADD_TEXT -> OrbitIcons.AddText
        EditorAction.UNDO -> OrbitIcons.Undo
        EditorAction.REDO -> OrbitIcons.Redo
        EditorAction.AUTO_SAVE -> OrbitIcons.Confirm
        EditorAction.PIN -> error("Handled above")
    }
    OrbitIconButton(
        icon = icon,
        contentDescription = description,
        onClick = onClick,
        selected = selected,
        destructive = destructive,
        enabled = enabled,
    )
}

@Composable private fun EditableDisplayRow(label: String, checked: Boolean, onChecked: ((Boolean) -> Unit)?, onEdit: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 52.dp), verticalAlignment = Alignment.CenterVertically) {
        if (onChecked != null) Switch(checked, onChecked) else Spacer(Modifier.width(52.dp))
        Text(label, Modifier.weight(1f).padding(start = 10.dp))
        OrbitButton(
            label = uiText("tap_edit"),
            onClick = onEdit,
            icon = OrbitIcons.Adjust,
            style = OrbitButtonStyle.Quiet,
            enabled = checked,
            compact = true,
        )
    }
}

private val SUN_ICON_LIBRARY = SunIconStyle.entries
private val MOON_ICON_LIBRARY = MoonIconStyle.entries


@Composable private fun FontField(label: String, value: String, sample: String, onChange: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    val choice = FONT_CHOICES.firstOrNull { it.id == value } ?: FONT_CHOICES.first()
    CrystalPickerRow(onClick = { open = true }) {
            Icon(OrbitIcons.Font, null, modifier = Modifier.size(24.dp)); Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(label, style = MaterialTheme.typography.bodySmall); Text(sample, fontFamily = textFont(choice.id), fontSize = 20.sp, maxLines = 2, lineHeight = 25.sp, overflow = TextOverflow.Ellipsis) }
            Text(uiText("choose"))
    }
    if (open) CrystalDialog(onDismissRequest = { open = false }, title = { Text(uiText("choose_font")) }, confirmButton = { TextButton(onClick = { open = false }) { Text(uiText("done")) } }, text = {
        LazyColumn(Modifier.heightIn(max = 540.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(FONT_CHOICES.chunked(2)) { row -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                row.forEach { item -> CrystalChoiceTile(selected = item.id == value, selectedDescription = uiText("selected"), onClick = { onChange(item.id); open = false }, modifier = Modifier.weight(1f).height(82.dp)) {
                    Box(Modifier.fillMaxSize().padding(9.dp), contentAlignment = Alignment.Center) { Text(sample, fontFamily = textFont(item.id), fontSize = 18.sp, lineHeight = 23.sp, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                } }; if(row.size==1) Spacer(Modifier.weight(1f))
            } }
        }
    })
}

@Composable private fun CityPartDialog(part: CityPart, city: City, use24Hour: Boolean, onDismiss: () -> Unit, onChange: (City) -> Unit) {
    val now = remember(city.zoneId, use24Hour) { runCatching { ZonedDateTime.now(java.time.ZoneId.of(city.zoneId)) }.getOrNull() }
    CrystalDialog(onDismissRequest = onDismiss, title = { Text(when(part){CityPart.NAME->uiText("name_country");CityPart.TIME->uiText("time");CityPart.DATE->uiText("date");CityPart.SOLAR->uiText("sun_moon")}) }, confirmButton = { TextButton(onClick = onDismiss) { Text(uiText("done")) } }, text = {
        Column(Modifier.heightIn(max = 570.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            when (part) {
                CityPart.NAME -> { FontField(uiText("city_name_font"), city.nameFont, city.nickname.ifBlank { city.name }) { onChange(city.copy(nameFont = it)) }; ColorField(uiText("city_name_color"), city.nameColor) { onChange(city.copy(nameColor = it)) } }
                CityPart.TIME -> { val sample = now?.format(DateTimeFormatter.ofPattern(if(use24Hour) "HH:mm" else "h:mm a", LocalBestMatesLanguage.current.dateLocale())) ?: "20:26"; FontField(uiText("time_font"), city.timeFont, sample) { onChange(city.copy(timeFont = it)) }; ColorField(uiText("time_color"), city.timeColor) { onChange(city.copy(timeColor = it)) } }
                CityPart.DATE -> { val sample = now?.format(DateTimeFormatter.ofPattern("EEE, MMM d", LocalBestMatesLanguage.current.dateLocale())) ?: "Thu, Sep 3"; FontField(uiText("date_font"), city.dateFont, sample) { onChange(city.copy(dateFont = it)) }; ColorField(uiText("date_color"), city.dateColor) { onChange(city.copy(dateColor = it)) } }
                CityPart.SOLAR -> {
                    SunIconLibraryField(city.sunStyle, city.moonStyle, city.sunColor) { onChange(city.copy(sunStyle = it)) }
                    ColorField(uiText("sun_color"), city.sunColor) { onChange(city.copy(sunColor = it)) }
                    MoonIconLibraryField(city.sunStyle, city.moonStyle, city.moonColor) { onChange(city.copy(moonStyle = it)) }
                    ColorField(uiText("moon_color"), city.moonColor) { onChange(city.copy(moonColor = it)) }
                }
            }
        }
    })
}

@Composable private fun SunIconLibraryField(selected: SunIconStyle, moon: MoonIconStyle, color: Long, onChange: (SunIconStyle) -> Unit) {
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick={open=true}) {
        SolarIconSample(true, selected, moon, color, 34f); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)){Text(uiText("sun_library"),style=MaterialTheme.typography.bodySmall);Text(uiText("browse_icons"),fontWeight=FontWeight.Bold)}; Text(uiText("choose"))
    }
    if(open) CrystalDialog(onDismissRequest={open=false},title={Text(uiText("sun_library"))},confirmButton={TextButton(onClick={open=false}){Text(uiText("done"))}},text={
        LazyColumn(Modifier.heightIn(max=540.dp), verticalArrangement=Arrangement.spacedBy(7.dp)){items(SUN_ICON_LIBRARY.chunked(4)){row->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(7.dp)){row.forEach{style->
            CrystalChoiceTile(selected=style==selected,selectedDescription=uiText("selected"),onClick={onChange(style);open=false},modifier=Modifier.weight(1f).aspectRatio(1f)){Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){SolarIconSample(true,style,moon,color,43f)}}
        };repeat(4-row.size){Spacer(Modifier.weight(1f))}}}}
    })
}

@Composable private fun MoonIconLibraryField(sun: SunIconStyle, selected: MoonIconStyle, color: Long, onChange: (MoonIconStyle) -> Unit) {
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick={open=true}) {
        SolarIconSample(false, sun, selected, color, 34f); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)){Text(uiText("moon_library"),style=MaterialTheme.typography.bodySmall);Text(uiText("browse_icons"),fontWeight=FontWeight.Bold)}; Text(uiText("choose"))
    }
    if(open) CrystalDialog(onDismissRequest={open=false},title={Text(uiText("moon_library"))},confirmButton={TextButton(onClick={open=false}){Text(uiText("done"))}},text={
        LazyColumn(Modifier.heightIn(max=540.dp), verticalArrangement=Arrangement.spacedBy(7.dp)){items(MOON_ICON_LIBRARY.chunked(4)){row->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(7.dp)){row.forEach{style->
            CrystalChoiceTile(selected=style==selected,selectedDescription=uiText("selected"),onClick={onChange(style);open=false},modifier=Modifier.weight(1f).aspectRatio(1f)){Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){SolarIconSample(false,sun,style,color,43f)}}
        };repeat(4-row.size){Spacer(Modifier.weight(1f))}}}}
    })
}

// Shared artwork only. Production Pin proportions and 20dp stroke are unchanged.
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawProductionPin(tint: Color) {
    val stroke = size.width * .08f
    val cap = androidx.compose.ui.graphics.StrokeCap.Round
    fun p(x:Float,y:Float) = Offset(size.width*x,size.height*y)
    fun line(a:Offset,b:Offset) = drawLine(tint,a,b,stroke,cap=cap)
    drawRoundRect(tint,p(.33f,.12f),androidx.compose.ui.geometry.Size(size.width*.34f,size.height*.20f),androidx.compose.ui.geometry.CornerRadius(size.width*.08f),style=Stroke(stroke,cap=cap,join=androidx.compose.ui.graphics.StrokeJoin.Round))
    line(p(.37f,.33f),p(.28f,.60f)); line(p(.63f,.33f),p(.72f,.60f))
    line(p(.28f,.60f),p(.72f,.60f)); line(p(.50f,.60f),p(.50f,.88f))
}

@Composable internal fun ProductionPinStudy(artworkSize: Dp) {
    val tint=MaterialTheme.colorScheme.onSurface
    Canvas(Modifier.size(artworkSize)) { drawProductionPin(tint) }
}

/** The exact Orbit Rotate the user identified in Future canvas language. */
@Composable internal fun ProductionRotateStudy(artworkSize: Dp) {
    Icon(OrbitIcons.Rotate,"Rotate",tint=MaterialTheme.colorScheme.onSurface,modifier=Modifier.size(artworkSize))
}

/** Debug review bridge to actual production buttons; never saves a project. */
@Composable internal fun BestMatesProductionToolbarReview() {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically) {
        EditorActionButton(EditorAction.UNDO,"Undo",{})
        EditorActionButton(EditorAction.REDO,"Redo",{})
        EditorActionButton(EditorAction.PIN,"Pin position",{})
        Icon(OrbitIcons.Font,"Font",modifier=Modifier.size(20.dp))
        Icon(OrbitIcons.Adjust,"Local adjustments",modifier=Modifier.size(20.dp))
        ProductionRotateStudy(20.dp)
    }
}

/** Disposable local state around the unchanged geometry/gesture engine. */
@Composable internal fun BestMatesCanvasReview() {
    var study by remember { mutableStateOf(WidgetConfig(
        layers=listOf(WidgetLayer(id=1L,shape=WidgetShape.HEART,width=.82f,height=.82f,dayColor=0xDDEEDCEC,nightColor=0xDDEEDCEC,textColor=0xFF796584,softness=4f,glowOpacity=.08f)),
        cities=listOf(City("Tokyo","Japan",35.68,139.69,"Asia/Tokyo",componentId=1L,scale=.85f,nameColor=0xFF4A3A53,timeColor=0xFF4A3A53,dateColor=0xFF63536C))
    )) }
    var selection by remember { mutableStateOf<CanvasSelection?>(CanvasSelection.ComponentItem(0)) }
    Column(Modifier.fillMaxWidth().height(570.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        BestMatesProductionToolbarReview()
        LayoutCanvas(study,{index,value -> study=study.copy(cities=study.cities.toMutableList().also { it[index]=value })},
            {index,value -> study=study.copy(texts=study.texts.toMutableList().also { it[index]=value })},
            {index,value -> study=study.copy(layers=study.layers.toMutableList().also { it[index]=value })},
            {},{},selection,{selection=it},true, true, emptyCanvasText = uiText("empty_canvas"))
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(12.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text("Soft inspector · local study",style=MaterialTheme.typography.titleMedium)
            FontField("Time typeface",study.cities.first().timeFont,"18:40") { next -> study=study.copy(cities=study.cities.map { it.copy(timeFont=next) }) }
            EditableDisplayRow("Date",study.cities.first().showDate,{next -> study=study.copy(cities=study.cities.map { it.copy(showDate=next) })},{})
            Text("Current production Heart · neutral work mat · no saved project",style=MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable private fun shapeLabel(shape: WidgetShape): String {
    val english = when (shape) {
        WidgetShape.ROUNDED -> "Mist card"; WidgetShape.SQUIRCLE -> "Soft square"
        WidgetShape.PILL -> "Soft arch"; WidgetShape.CAPSULE -> "Capsule"
        WidgetShape.CIRCLE -> "Moon circle"; WidgetShape.CLOUD -> "Cloud"
        WidgetShape.DIAMOND -> "Four-petal"; WidgetShape.PETAL -> "Petal"
        WidgetShape.HEART -> "Heart"; WidgetShape.DROP -> "Drop"
        WidgetShape.TICKET -> "Ticket"; WidgetShape.WAVE -> "Flag"
    }
    return if (LocalBestMatesLanguage.current == AppLanguage.CHINESE) shape.label else english
}

@Composable
private fun ShapePicker(selected: WidgetShape, onSelect: (WidgetShape) -> Unit) {
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick = { open = true }) {
            ShapePreviewIcon(selected, Modifier.size(42.dp, 32.dp), true)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(uiText("shape_library"), style = MaterialTheme.typography.bodySmall); Text(shapeLabel(selected), fontWeight = FontWeight.Bold) }
            Text(uiText("choose"))
    }
    if (open) ShapePickerDialog(selected, uiText("choose_shape"), { open = false }) { shape -> onSelect(shape); open = false }
}

@Composable
private fun ShapePickerDialog(selected: WidgetShape?, title: String, onDismiss: () -> Unit, onSelect: (WidgetShape) -> Unit) {
    CrystalDialog(onDismissRequest = onDismiss, title = { Text(title) }, confirmButton = { TextButton(onClick = onDismiss) { Text(uiText("cancel")) } }, text = {
        LazyColumn(Modifier.heightIn(max = 560.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(WidgetShape.entries.chunked(3)) { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    row.forEach { shape ->
                        CrystalChoiceTile(selected = shape == selected, selectedDescription = uiText("selected"), onClick = { onSelect(shape) }, modifier = Modifier.weight(1f).height(78.dp)) {
                            Column(Modifier.fillMaxSize().padding(7.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                ShapePreviewIcon(shape, Modifier.size(42.dp, 30.dp), shape == selected)
                                Spacer(Modifier.height(4.dp)); Text(shapeLabel(shape), fontSize = 10.sp, maxLines = 1)
                            }
                        }
                    }
                    repeat(3-row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    })
}

private fun initialPlacement(index: Int, city: City): City {
    val points = listOf(0f to 0f, -.46f to .42f, .46f to .42f, -.46f to -.42f, .46f to -.42f, 0f to .68f, 0f to -.68f)
    val point = points[index % points.size]
    return city.copy(x = point.first, y = point.second)
}

private fun shapeRecommendedSizes(shape: WidgetShape, count: Int): List<Pair<Int,Int>> = when(shape) {
    WidgetShape.CLOUD, WidgetShape.CAPSULE, WidgetShape.WAVE, WidgetShape.TICKET -> if (count <= 2) listOf(4 to 1, 4 to 2) else listOf(4 to 2, 4 to 3)
    WidgetShape.PILL, WidgetShape.PETAL, WidgetShape.DROP -> if (count <= 2) listOf(2 to 2, 3 to 2) else listOf(3 to 3, 4 to 3)
    WidgetShape.CIRCLE, WidgetShape.DIAMOND, WidgetShape.HEART, WidgetShape.SQUIRCLE -> if (count <= 3) listOf(3 to 3, 4 to 4) else listOf(4 to 4, 4 to 5)
    WidgetShape.ROUNDED -> when(count.coerceAtLeast(1)) { 1 -> listOf(1 to 1,2 to 1); 2 -> listOf(2 to 1,2 to 2); in 3..4 -> listOf(3 to 2,4 to 2); in 5..6 -> listOf(4 to 3); else -> listOf(4 to 4,4 to 5) }
}

@Composable private fun ShapePreviewIcon(shape: WidgetShape, modifier: Modifier = Modifier, selected: Boolean = false) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val bitmap = remember(shape, selected) {
        BackgroundRenderer.render(
            WidgetConfig(shape = shape, dayColor = if (selected) 0x553D2A62 else 0x303F394A, nightColor = if (selected) 0x553D2A62 else 0x303F394A,
                borderWidth = 1.2f, borderOpacity = .72f, softness = 0f, glowOpacity = 0f, shadow = 0f, shadowOpacity = 0f),
            96, 96, context.resources.displayMetrics.density, night = false
        ).asImageBitmap()
    }
    Image(bitmap, contentDescription = shapeLabel(shape), modifier = modifier, contentScale = ContentScale.Fit, filterQuality = androidx.compose.ui.graphics.FilterQuality.High)
    /* Kept as a single renderer-backed preview so picker geometry cannot drift
       away from the actual widget geometry. */
    /* Canvas(modifier) {
        val stroke = Stroke(width = 3.0f, cap = androidx.compose.ui.graphics.StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
        val w = size.width; val h = size.height
        val p = androidx.compose.ui.graphics.Path()
        when (shape) {
            WidgetShape.ROUNDED -> drawRoundRect(color, topLeft=Offset(w*.04f,h*.08f), size=androidx.compose.ui.geometry.Size(w*.92f,h*.84f), cornerRadius=androidx.compose.ui.geometry.CornerRadius(h*.18f), style=stroke)
            WidgetShape.SQUIRCLE -> drawRoundRect(color, topLeft=Offset(w*.08f,h*.04f), size=androidx.compose.ui.geometry.Size(w*.84f,h*.92f), cornerRadius=androidx.compose.ui.geometry.CornerRadius(h*.28f), style=stroke)
            WidgetShape.CIRCLE -> drawCircle(color, radius=min(w,h)*.43f, style=stroke)
            WidgetShape.CAPSULE -> drawRoundRect(color, topLeft=Offset(w*.03f,h*.20f), size=androidx.compose.ui.geometry.Size(w*.94f,h*.60f), cornerRadius=androidx.compose.ui.geometry.CornerRadius(h*.30f), style=stroke)
            WidgetShape.PILL -> { p.moveTo(w*.08f,h*.90f); p.lineTo(w*.08f,h*.44f); p.cubicTo(w*.08f,h*.10f,w*.30f,h*.04f,w*.50f,h*.04f); p.cubicTo(w*.70f,h*.04f,w*.92f,h*.10f,w*.92f,h*.44f); p.lineTo(w*.92f,h*.90f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.CLOUD -> { p.moveTo(w*.04f,h*.68f); p.cubicTo(w*.01f,h*.43f,w*.18f,h*.27f,w*.34f,h*.34f); p.cubicTo(w*.43f,h*.04f,w*.70f,h*.08f,w*.75f,h*.37f); p.cubicTo(w*.96f,h*.32f,w*.99f,h*.62f,w*.86f,h*.76f); p.cubicTo(w*.70f,h*.89f,w*.20f,h*.87f,w*.08f,h*.76f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.DIAMOND -> { p.moveTo(w*.5f,h*.03f); p.cubicTo(w*.67f,h*.03f,w*.74f,h*.22f,w*.72f,h*.30f); p.cubicTo(w*.82f,h*.28f,w*.97f,h*.37f,w*.97f,h*.5f); p.cubicTo(w*.97f,h*.63f,w*.82f,h*.72f,w*.72f,h*.70f); p.cubicTo(w*.74f,h*.78f,w*.67f,h*.97f,w*.5f,h*.97f); p.cubicTo(w*.33f,h*.97f,w*.26f,h*.78f,w*.28f,h*.70f); p.cubicTo(w*.18f,h*.72f,w*.03f,h*.63f,w*.03f,h*.5f); p.cubicTo(w*.03f,h*.37f,w*.18f,h*.28f,w*.28f,h*.30f); p.cubicTo(w*.26f,h*.22f,w*.33f,h*.03f,w*.5f,h*.03f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.PETAL -> { p.moveTo(w*.5f,h*.03f); p.cubicTo(w*.94f,h*.05f,w*.98f,h*.38f,w*.5f,h*.97f); p.cubicTo(w*.02f,h*.38f,w*.06f,h*.05f,w*.5f,h*.03f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.HEART -> { p.moveTo(w*.5f,h*.94f); p.cubicTo(w*.37f,h*.80f,w*.05f,h*.60f,w*.05f,h*.32f); p.cubicTo(w*.05f,h*.06f,w*.37f,h*.03f,w*.5f,h*.25f); p.cubicTo(w*.63f,h*.03f,w*.95f,h*.06f,w*.95f,h*.32f); p.cubicTo(w*.95f,h*.60f,w*.63f,h*.80f,w*.5f,h*.94f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.DROP -> { p.moveTo(w*.5f,h*.02f); p.cubicTo(w*.88f,h*.38f,w*.96f,h*.62f,w*.86f,h*.80f); p.cubicTo(w*.70f,h*1.02f,w*.30f,h*1.02f,w*.14f,h*.80f); p.cubicTo(w*.04f,h*.62f,w*.12f,h*.38f,w*.5f,h*.02f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.TICKET -> { p.moveTo(w*.10f,h*.08f); p.lineTo(w*.90f,h*.08f); p.lineTo(w*.96f,h*.35f); p.cubicTo(w*.82f,h*.40f,w*.82f,h*.60f,w*.96f,h*.65f); p.lineTo(w*.90f,h*.92f); p.lineTo(w*.10f,h*.92f); p.lineTo(w*.04f,h*.65f); p.cubicTo(w*.18f,h*.60f,w*.18f,h*.40f,w*.04f,h*.35f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.WAVE -> {
                val pole = w * .13f
                p.moveTo(pole, h * .12f)
                p.lineTo(pole, h * .88f)
                p.moveTo(pole, h * .16f)
                p.cubicTo(w*.38f, h*.06f, w*.60f, h*.28f, w*.94f, h*.16f)
                p.cubicTo(w*.66f, h*.38f, w*.40f, h*.20f, pole, h*.33f)
                p.moveTo(pole, h*.67f)
                p.cubicTo(w*.38f, h*.58f, w*.60f, h*.80f, w*.94f, h*.68f)
                p.cubicTo(w*.66f, h*.90f, w*.40f, h*.72f, pole, h*.84f)
                drawPath(p, color, style = stroke)
            }
        }
    } */
}

private data class DayNightPalette(val name: String, val day: Long, val night: Long)
private val DAY_NIGHT_PALETTES = listOf(
    DayNightPalette("Lavender Air", 0x205D5774L, 0x42473D5DL),
    DayNightPalette("Dream Haze", 0x22B99AAEL, 0x424C405DL),
    DayNightPalette("Moon Glass", 0x1DE8E0F4L, 0x404A415DL),
    DayNightPalette("Quiet Rose", 0x22A67A91L, 0x424A384AL),
    DayNightPalette("Mono Glass", 0x18FFFFFFL, 0x3518191FL),
    DayNightPalette("Ruby Mist", 0x24D98B9AL, 0x493D2533L),
    DayNightPalette("Apricot Air", 0x24E4A477L, 0x494B3028L),
    DayNightPalette("Honey Veil", 0x22E2C27AL, 0x49483E25L),
    DayNightPalette("Sage Glass", 0x229AB9A2L, 0x49304439L),
    DayNightPalette("Blue Hush", 0x2287A9C9L, 0x492C3D55L),
    DayNightPalette("Indigo Rain", 0x228C91C8L, 0x49303658L),
    DayNightPalette("Violet Bloom", 0x22B092C8L, 0x493F3055L)
)

@Composable private fun DayNightPaletteField(day: Long, night: Long, change: (Long, Long) -> Unit) {
    var open by remember { mutableStateOf(false) }
    var custom by remember { mutableStateOf(false) }
    val match = DAY_NIGHT_PALETTES.firstOrNull { it.day == day && it.night == night }
    CrystalPickerRow(onClick = { custom = match == null; open = true }) {
            Column(Modifier.weight(1f)) { Text(uiText("palette"), style = MaterialTheme.typography.bodySmall); Text(match?.name ?: uiText("custom"), fontWeight = FontWeight.Medium) }
            PalettePair(day, night)
            Spacer(Modifier.width(12.dp)); Text(uiText("choose"))
    }
    if (open) CrystalDialog(
        onDismissRequest = { open = false },
        title = { Text(uiText("choose_palette")) },
        confirmButton = { TextButton(onClick = { open = false }) { Text(uiText("done")) } },
        text = {
            Column(Modifier.heightIn(max = 610.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DAY_NIGHT_PALETTES.forEach { item ->
                    CrystalChoiceTile(
                        selected = !custom && item.day == day && item.night == night,
                        selectedDescription = uiText("selected"),
                        onClick = { change(item.day, item.night); custom = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(item.name, Modifier.weight(1f), fontWeight = FontWeight.Medium)
                            PalettePair(item.day, item.night)
                        }
                    }
                }
                CrystalChoiceTile(
                    selected = custom || match == null,
                    selectedDescription = uiText("selected"),
                    onClick = { custom = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) { Text(uiText("custom"), fontWeight = FontWeight.Medium); Text(uiText("custom_palette_note"), style = MaterialTheme.typography.bodySmall) }
                        PalettePair(day, night)
                    }
                }
                if (custom) {
                    ColorField(uiText("day_color"), day) { change(it, night) }
                    ColorField(uiText("night_color"), night) { change(day, it) }
                }
            }
        }
    )
}

@Composable private fun PalettePair(day: Long, night: Long) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(32.dp).clip(RoundedCornerShape(16.dp)).background(Color(day)).border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)))
            Text("D ${((day ushr 24) and 0xFF) * 100 / 255}%", fontSize = 9.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(32.dp).clip(RoundedCornerShape(16.dp)).background(Color(night)).border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)))
            Text("N ${((night ushr 24) and 0xFF) * 100 / 255}%", fontSize = 9.sp)
        }
    }
}
@Composable private fun ColorField(label:String,value:Long,change:(Long)->Unit){
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick = { open = true }) {
            Box(Modifier.size(34.dp).clip(RoundedCornerShape(17.dp)).background(Color(value)).border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(17.dp)))
            Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(label, fontWeight = FontWeight.Medium); Text(uiText("open_color"), style = MaterialTheme.typography.bodySmall) }
            Text(uiText("choose"))
    }
    if (open) VisualColorPickerDialog(label, value, onDismiss = { open = false }, onApply = { change(it); open = false })
}

@Composable private fun VisualColorPickerDialog(title: String, original: Long, onDismiss: () -> Unit, onApply: (Long) -> Unit) {
    val initial = remember(original) { FloatArray(3).also { android.graphics.Color.colorToHSV(original.toInt(), it) } }
    var hue by remember(original) { mutableFloatStateOf(initial[0]) }
    var saturation by remember(original) { mutableFloatStateOf(initial[1]) }
    var brightness by remember(original) { mutableFloatStateOf(initial[2]) }
    var alpha by remember(original) { mutableFloatStateOf(((original ushr 24) and 0xFF).toFloat() / 255f) }
    fun result(): Long = android.graphics.Color.HSVToColor((alpha * 255).roundToInt(), floatArrayOf(hue, saturation, brightness)).toLong() and 0xFFFFFFFFL
    // The wheel is built from the same HSV formula used by result().  The previous
    // sweep-gradient approximation was visually reversed in parts of the circle,
    // so its picked location could disagree with the applied colour.
    val wheel = remember(brightness) { hsvWheelBitmap(512, brightness).asImageBitmap() }
    CrystalDialog(onDismissRequest = onDismiss, title = { Text(title) }, confirmButton = { OrbitButton(label = uiText("apply"), onClick = { onApply(result()) }, style = OrbitButtonStyle.Primary, compact = true) }, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("cancel")) } }, text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(Modifier.size(252.dp).pointerInput(Unit) {
                fun update(position: Offset) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val dx = position.x - center.x; val dy = position.y - center.y
                    hue = ((Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 360f) % 360f)
                    saturation = (sqrt(dx * dx + dy * dy) / (min(size.width, size.height) / 2f)).coerceIn(0f, 1f)
                }
                detectDragGestures(onDragStart = { update(it) }) { change, _ -> update(change.position); change.consume() }
            }) {
                Image(wheel, null, Modifier.fillMaxSize())
                Canvas(Modifier.fillMaxSize()) {
                    val radius = min(size.width, size.height) / 2f - 7f
                    val center = this.center
                val angle = Math.toRadians(hue.toDouble())
                val marker = Offset(center.x + cos(angle).toFloat() * radius * saturation, center.y + sin(angle).toFloat() * radius * saturation)
                drawCircle(Color.White, 10f, marker, style = Stroke(4f)); drawCircle(Color.Black.copy(alpha = .35f), 12f, marker, style = Stroke(2f))
                }
            }
            Text(uiFormat("brightness", (brightness * 100).roundToInt()), Modifier.fillMaxWidth()); Slider(brightness, { brightness = it }, valueRange = 0f..1f)
            Text(uiFormat("opacity", (alpha * 100).roundToInt()), Modifier.fillMaxWidth()); Slider(alpha, { alpha = it }, valueRange = 0f..1f)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) { Box(Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(12.dp)).background(Color(original))); Text(uiText("original")) }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) { Box(Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(12.dp)).background(Color(result()))); Text(uiText("new_color")) }
            }
        }
    })
}

private fun hsvWheelBitmap(size: Int, value: Float): Bitmap {
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val pixels = IntArray(size * size)
    val center = (size - 1) / 2f
    val radius = center - 5f
    for (y in 0 until size) for (x in 0 until size) {
        val dx = x - center
        val dy = y - center
        val distance = sqrt(dx * dx + dy * dy)
        pixels[y * size + x] = if (distance <= radius) {
            val hue = ((Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 360f) % 360f)
            android.graphics.Color.HSVToColor(floatArrayOf(hue, (distance / radius).coerceIn(0f, 1f), value))
        } else android.graphics.Color.TRANSPARENT
    }
    bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
    return bitmap
}
@Composable private fun SectionTitle(text:String)=Text(text,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
@Composable private fun SliderRow(label:String,value:Float,range:ClosedFloatingPointRange<Float>,change:(Float)->Unit)=OrbitSliderRow(label,"%.1f".format(value),value,range,change)
@Composable private fun SwitchRow(label:String,checked:Boolean,change:(Boolean)->Unit)=OrbitSwitchRow(label,checked,change)
@OptIn(ExperimentalLayoutApi::class)
@Composable private fun ThemePicker(selected:ThemeMode,pick:(ThemeMode)->Unit)=FlowRow(horizontalArrangement=Arrangement.spacedBy(6.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){ThemeMode.entries.forEach{OrbitChoiceChip(label=when(it){ThemeMode.SYSTEM->uiText("follow_system");ThemeMode.LIGHT->uiText("light");ThemeMode.DARK->uiText("dark")},selected=selected==it,selectedDescription=uiText("selected"),onClick={pick(it)})}}

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
