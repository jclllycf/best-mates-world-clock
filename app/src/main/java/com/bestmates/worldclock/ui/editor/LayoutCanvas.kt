package com.bestmates.worldclock.ui.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

import com.bestmates.worldclock.model.*
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import com.bestmates.worldclock.widget.SolarIconRenderer
import com.bestmates.worldclock.widget.BackgroundRenderer
import com.bestmates.worldclock.solar.SolarCalculator
import com.bestmates.worldclock.ui.i18n.LocalBestMatesLanguage
import com.bestmates.worldclock.ui.i18n.dateLocale
import com.bestmates.worldclock.ui.settings.AppLanguage
import com.bestmates.worldclock.ui.*
import kotlinx.coroutines.delay
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable internal fun SolarIconSample(isDay: Boolean, sun: SunIconStyle, moon: MoonIconStyle, color: Long, sizeDp: Float) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val bitmap = remember(isDay, sun, moon, color, sizeDp) { com.bestmates.worldclock.widget.SolarIconRenderer.render(context, (sizeDp * 3f).roundToInt(), isDay, sun, moon, color.toInt()).asImageBitmap() }
    Image(
        bitmap = bitmap,
        contentDescription = null,
        modifier = Modifier.size(sizeDp.dp),
        filterQuality = androidx.compose.ui.graphics.FilterQuality.High
    )
}
@Composable
internal fun LayoutCanvas(
    config: WidgetConfig,
    onCityChange: (Int, City) -> Unit,
    onTextChange: (Int, CustomText) -> Unit,
    onLayerChange: (Int, WidgetLayer) -> Unit,
    onGestureStart: () -> Unit,
    onGestureFinished: () -> Unit,
    selection: CanvasSelection?,
    onSelect: (CanvasSelection?) -> Unit,
    snapEnabled: Boolean,
    guidesVisible: Boolean,
    emptyCanvasText: String
) {
    var tick by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { while (true) { delay(30_000); tick++ } }
    val density = LocalDensity.current
    val canvasWidth = (LocalConfiguration.current.screenWidthDp - 32).coerceAtLeast(240).toFloat()
    val canvasHeight = canvasWidth
    val boardBounds = android.graphics.RectF(7f, 7f, canvasWidth - 7f, canvasHeight - 7f)
    // All rotation geometry is expressed in this unrotated canvas/root coordinate
    // space. The position is measured from Compose rather than inferred from a
    // child handle's local offset.
    var canvasOriginInRoot by remember { mutableStateOf(Offset.Zero) }
    var guideX by remember { mutableStateOf<Float?>(null) }
    var guideY by remember { mutableStateOf<Float?>(null) }
    var guideAngle by remember { mutableStateOf<Pair<Float, Offset>?>(null) }
    var sizeGuide by remember { mutableStateOf<String?>(null) }
    var sizeGuideTarget by remember { mutableStateOf<CanvasSelection?>(null) }
    val hostView = LocalView.current
    var lastSnapFeedback by remember { mutableStateOf("") }
    LaunchedEffect(guideX, guideY, guideAngle, sizeGuide) {
        val key = "${guideX?.roundToInt()}:${guideY?.roundToInt()}:${guideAngle?.first?.roundToInt()}:${sizeGuide.orEmpty()}"
        if (key != "null:null:null:" && key != lastSnapFeedback) {
            hostView.performHapticFeedback(android.view.HapticFeedbackConstants.CLOCK_TICK)
            lastSnapFeedback = key
        }
        if (key == "null:null:null:") lastSnapFeedback = ""
    }
    val systemNight = (androidx.compose.ui.platform.LocalContext.current.resources.configuration.uiMode and
        android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
    val previewNight = config.theme == ThemeMode.DARK || (config.theme == ThemeMode.SYSTEM && systemNight)
    val designColors = BestMatesDesign.colors
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(Modifier.width(canvasWidth.dp).height(canvasHeight.dp)
            .onGloballyPositioned { canvasOriginInRoot = it.positionInRoot() }
            .clip(RoundedCornerShape(18.dp)).background(designColors.canvasMat).border(1.dp, designColors.borderSubtle, RoundedCornerShape(18.dp)).pointerInput(Unit) {
            detectTapGestures(onTap = { onSelect(null) })
        }) {
            Canvas(Modifier.fillMaxSize()) {
                val grid = designColors.onCanvasMat.copy(alpha = .09f)
                for (index in 1..15) {
                    if (index % 4 != 0) {
                        val p = size.width * index / 16f
                        drawLine(grid.copy(alpha = .035f), Offset(p, 0f), Offset(p, size.height), 1f)
                        drawLine(grid.copy(alpha = .035f), Offset(0f, p), Offset(size.width, p), 1f)
                    }
                }
                for (index in 1..3) {
                    val p = size.width * index / 4f
                    drawLine(grid, Offset(p, 0f), Offset(p, size.height), 1f)
                    drawLine(grid, Offset(0f, p), Offset(size.width, p), 1f)
                }
                if (guidesVisible) {
                    val guideColor = designColors.guide.copy(alpha = .58f)
                    guideX?.let { drawLine(guideColor, Offset(it, boardBounds.top), Offset(it, boardBounds.bottom), 1.3f) }
                    guideY?.let { drawLine(guideColor, Offset(boardBounds.left, it), Offset(boardBounds.right, it), 1.3f) }
                    guideAngle?.let { (angle, origin) ->
                        val radians = Math.toRadians(angle.toDouble())
                        val radius = boardBounds.width() * .72f
                        val dx = (cos(radians) * radius).toFloat()
                        val dy = (sin(radians) * radius).toFloat()
                        drawLine(guideColor, Offset(origin.x-dx, origin.y-dy), Offset(origin.x+dx, origin.y+dy), 1.3f)
                    }
                }
            }

            config.layers.forEachIndexed { index, layer ->
                val liveLayer by rememberUpdatedState(layer)
                val rect = layerBoundsOnBoard(layer, boardBounds)
                val isSelected = selection == CanvasSelection.ComponentItem(index)
                val layerConfig = config.copy(
                    shape = layer.shape,
                    dayColor = layer.dayColor,
                    nightColor = layer.nightColor,
                    textColor = layer.textColor,
                    borderWidth = layer.borderWidth,
                    borderOpacity = layer.borderOpacity,
                    softness = layer.softness,
                    glowOpacity = layer.glowOpacity,
                    shadow = layer.shadow,
                    shadowOpacity = layer.shadowOpacity,
                    shadowYOffset = layer.shadowYOffset
                )
                // Rendering a new bitmap for every drag event made component movement
                // appear to skip frames. Keep a high-resolution visual texture and let
                // Compose transform it live; only visual style changes rebuild it.
                val background = remember(
                    layer.shape, layer.dayColor, layer.nightColor, layer.textColor,
                    layer.borderWidth, layer.borderOpacity, layer.softness, layer.glowOpacity,
                    layer.shadow, layer.shadowOpacity, layer.shadowYOffset, previewNight
                ) {
                    BackgroundRenderer.render(layerConfig, 720, 720, 1f, previewNight).asImageBitmap()
                }
                Image(
                    background,
                    null,
                    Modifier.offset(rect.left.dp, rect.top.dp).width(rect.width().dp).height(rect.height().dp).graphicsLayer { rotationZ = layer.rotation },
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    Modifier.offset(rect.left.dp, rect.top.dp).width(rect.width().dp).height(rect.height().dp)
                        .graphicsLayer { rotationZ = layer.rotation }
                        .then(if (isSelected) Modifier.border(3.dp, designColors.selectionOutline.copy(alpha = .12f), RoundedCornerShape(7.dp)).border(1.dp, designColors.selectionOutline.copy(alpha = .85f), RoundedCornerShape(7.dp)) else Modifier)
                        .pointerInput(index, layer.locked, canvasWidth, snapEnabled) {
                            var start = layer
                            var total = Offset.Zero
                            if (!layer.locked) detectDragGestures(onDragStart = {
                                start = liveLayer; total = Offset.Zero; onGestureStart(); onSelect(CanvasSelection.ComponentItem(index))
                            }, onDragEnd = { guideX = null; guideY = null; onGestureFinished() }, onDragCancel = { guideX = null; guideY = null; onGestureFinished() }) { change, drag ->
                                change.consume()
                                total += drag
                                // Undo the input layer's inverse mapping before canvas snapping.
                                val canvasPixels = rotateCanvasVector(total, start.rotation)
                                val canvasDelta = with(density) { Offset(canvasPixels.x.toDp().value, canvasPixels.y.toDp().value) }
                                val dx = canvasDelta.x / boardBounds.width()
                                val dy = canvasDelta.y / boardBounds.height()
                                val left0 = ((start.x + 1f) * .5f - start.width * .5f)
                                val top0 = ((start.y + 1f) * .5f - start.height * .5f)
                                val otherLayers = config.layers.filterIndexed { otherIndex, _ -> otherIndex != index }
                                val extraLeft = otherLayers.flatMap { other ->
                                    val otherLeft = (other.x + 1f) * .5f - other.width * .5f
                                    listOf(otherLeft, otherLeft + other.width*.5f - start.width*.5f, otherLeft + other.width - start.width)
                                }
                                val extraTop = otherLayers.flatMap { other ->
                                    val otherTop = (other.y + 1f) * .5f - other.height * .5f
                                    listOf(otherTop, otherTop + other.height*.5f - start.height*.5f, otherTop + other.height - start.height)
                                }
                                val left = snapCanvasFraction((left0 + dx).coerceIn(0f, 1f-start.width), 1f / 16f, snapEnabled, extraLeft)
                                val top = snapCanvasFraction((top0 + dy).coerceIn(0f, 1f-start.height), 1f / 16f, snapEnabled, extraTop)
                                val centreX = left.value + start.width * .5f
                                val centreY = top.value + start.height * .5f
                                guideX = left.guide?.let { boardBounds.left + it * boardBounds.width() }
                                guideY = top.guide?.let { boardBounds.top + it * boardBounds.height() }
                                onLayerChange(index, start.copy(x = centreX * 2f - 1f, y = centreY * 2f - 1f))
                            }
                        }
                        .clickable { onSelect(CanvasSelection.ComponentItem(index))
                        }
                ) {
                    if (isSelected && !layer.locked) ResizeHandle(Modifier.align(Alignment.BottomEnd).pointerInput(index, canvasWidth) {
                        var start = layer
                        var total = Offset.Zero
                        detectDragGestures(onDragStart = { start = liveLayer; total = Offset.Zero; onGestureStart() }, onDragEnd = { guideX = null; guideY = null; onGestureFinished() }, onDragCancel = { guideX = null; guideY = null; onGestureFinished() }) { change, drag ->
                            change.consume()
                            total += drag
                            val dx = with(density) { total.x.toDp().value } / boardBounds.width()
                            val dy = with(density) { total.y.toDp().value } / boardBounds.height()
                            val left = ((start.x + 1f) * .5f - start.width * .5f).coerceIn(0f, 1f)
                            val top = ((start.y + 1f) * .5f - start.height * .5f).coerceIn(0f, 1f)
                            val right = snapCanvasFraction((left + start.width + dx).coerceIn(left + .08f, 1f), 1f / 16f, snapEnabled)
                            val bottom = snapCanvasFraction((top + start.height + dy).coerceIn(top + .08f, 1f), 1f / 16f, snapEnabled)
                            val width = (right.value-left).coerceAtLeast(if (snapEnabled) 1f / 16f else .08f)
                            val height = (bottom.value-top).coerceAtLeast(if (snapEnabled) 1f / 16f else .08f)
                            if (start.rotation % 360f == 0f) {
                                guideX = right.guide?.let { boardBounds.left + it * boardBounds.width() }
                                guideY = bottom.guide?.let { boardBounds.top + it * boardBounds.height() }
                            } else { guideX = null; guideY = null }
                            onLayerChange(index, start.copy(width = width, height = height, x = (left+width*.5f)*2f-1f, y = (top+height*.5f)*2f-1f))
                        }
                    })
                }
                if (isSelected && !layer.locked) {
                    RotationHandleUnified(
                        objectCenterOnCanvas = Offset(rect.centerX(), rect.centerY()),
                        objectSizeOnCanvas = Offset(rect.width(), rect.height()),
                        rotationDegrees = layer.rotation,
                        canvasOriginInRoot = canvasOriginInRoot,
                        canvasSizeOnCanvas = Offset(canvasWidth, canvasHeight),
                        snapEnabled = snapEnabled,
                        onRotationChange = { snapped ->
                            guideAngle = snapped.guide?.let { it to Offset(rect.centerX(), rect.centerY()) }
                            onLayerChange(index, liveLayer.copy(rotation = snapped.value))
                        },
                        onGestureStart = onGestureStart,
                        onGestureFinished = {
                            guideAngle = null
                            onGestureFinished()
                        }
                    )
                }
            }

            if (guidesVisible && (guideX != null || guideY != null)) Canvas(Modifier.fillMaxSize()) {
                val guideColor = designColors.guide.copy(alpha = .62f)
                guideX?.let { drawLine(guideColor, Offset(it, boardBounds.top), Offset(it, boardBounds.bottom), 1.4f) }
                guideY?.let { drawLine(guideColor, Offset(boardBounds.left, it), Offset(boardBounds.right, it), 1.4f) }
            }
            if (guidesVisible && sizeGuide != null) {
                Text(
                    sizeGuide!!,
                    Modifier.align(Alignment.TopStart).padding(12.dp).clip(RoundedCornerShape(10.dp)).background(designColors.accent.copy(alpha = .93f)).border(1.dp, designColors.borderControl, RoundedCornerShape(10.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                    color = designColors.onAccent,
                    fontSize = 11.sp
                )
            }

            if (config.cities.isEmpty() && config.texts.isEmpty()) Text(emptyCanvasText, Modifier.align(Alignment.Center), color = designColors.onCanvasMat.copy(alpha = .65f), fontSize = 11.sp)
            config.cities.forEachIndexed { index, city ->
                tick
                val liveCity by rememberUpdatedState(city)
                val parent = config.layers.firstOrNull { it.id == city.componentId }
                // A city is an independent design object: it may overlap or sit outside a
                // background component, but never leaves the fixed 4×4 canvas.
                val shapeBounds = boardBounds
                val locale = LocalBestMatesLanguage.current.dateLocale()
                val currentTime = runCatching {
                    ZonedDateTime.now(java.time.ZoneId.of(city.zoneId)).format(
                        DateTimeFormatter.ofPattern(if (config.use24Hour) "HH:mm" else "h:mm a", locale)
                    )
                }.getOrDefault("--:--")
                val currentDate = runCatching {
                    ZonedDateTime.now(java.time.ZoneId.of(city.zoneId)).format(DateTimeFormatter.ofPattern("EEE, MMM d", locale))
                }.getOrDefault("")
                val isSelected = selection == CanvasSelection.CityItem(index)
                val blockWidth = (130f * city.scale).coerceAtMost(shapeBounds.width() * .90f)
                val blockHeight = (86f * city.scale).coerceAtMost(shapeBounds.height() * .90f)
                val requestedX = shapeBounds.centerX() + city.x * shapeBounds.width() * .5f
                val requestedY = shapeBounds.centerY() + city.y * shapeBounds.height() * .5f
                val centre = freeCanvasCenter(requestedX, requestedY, blockWidth / 2f, blockHeight / 2f, boardBounds)
                val left = centre.x - blockWidth / 2f
                val top = centre.y - blockHeight / 2f
                Box(
                    Modifier.offset(left.dp, top.dp).width(blockWidth.dp).height(blockHeight.dp)
                        .graphicsLayer { rotationZ = city.rotation }
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) designColors.selectionFill.copy(alpha = .62f) else Color.Transparent)
                        .then(if (isSelected) Modifier.border(1.dp, designColors.selectionOutline.copy(alpha = .90f), RoundedCornerShape(8.dp)) else Modifier)
                        .pointerInput(index, city.locked, parent?.id, parent?.width, parent?.height, snapEnabled) {
                            var start = city
                            var total = Offset.Zero
                            if (!city.locked) detectDragGestures(onDragStart = { start = liveCity; total = Offset.Zero; onGestureStart(); onSelect(CanvasSelection.CityItem(index)) }, onDragEnd = { guideX = null; guideY = null; onGestureFinished() }, onDragCancel = { guideX = null; guideY = null; onGestureFinished() }) { change, drag ->
                                change.consume()
                                total += drag
                                val canvasPixels = rotateCanvasVector(total, start.rotation)
                                val canvasDelta = with(density) { Offset(canvasPixels.x.toDp().value, canvasPixels.y.toDp().value) }
                                val cx = shapeBounds.centerX() + start.x * shapeBounds.width() * .5f + canvasDelta.x
                                val cy = shapeBounds.centerY() + start.y * shapeBounds.height() * .5f + canvasDelta.y
                                val safe = freeCanvasCenter(cx, cy, blockWidth / 2f, blockHeight / 2f, boardBounds)
                                val otherX = config.cities.filterIndexed { otherIndex, _ -> otherIndex != index }.map { it.x } + config.texts.map { it.x }
                                val otherY = config.cities.filterIndexed { otherIndex, _ -> otherIndex != index }.map { it.y } + config.texts.map { it.y }
                                val sx = snapCoordinate((safe.x - shapeBounds.centerX()) / (shapeBounds.width() * .5f), snapEnabled, 16, otherX)
                                val sy = snapCoordinate((safe.y - shapeBounds.centerY()) / (shapeBounds.height() * .5f), snapEnabled, 16, otherY)
                                guideX = sx.guide?.let { shapeBounds.centerX() + it * shapeBounds.width() * .5f }
                                guideY = sy.guide?.let { shapeBounds.centerY() + it * shapeBounds.height() * .5f }
                                onCityChange(index, start.copy(x = sx.value, y = sy.value))
                            }
                        }.clickable { onSelect(CanvasSelection.CityItem(index))
                        }
                ) {
                    Column(Modifier.fillMaxSize().padding(horizontal = 5.dp, vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val day = SolarCalculator.isDay(city)
                        if (config.showSolarIcon && city.showSolar) SolarIconSample(day, city.sunStyle, city.moonStyle, if (day) city.sunColor else city.moonColor, 18f * city.scale)
                        Text(city.nickname.ifBlank { city.name } + if (config.showCountry && city.showCountry && city.country.isNotBlank()) " · ${city.country}" else "", color = Color(city.nameColor), fontSize = (11f * city.scale).sp, lineHeight = (13f * city.scale).sp, fontFamily = textFont(city.nameFont), fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (config.showTime && city.showTime) Text(currentTime, color = Color(city.timeColor), fontSize = (22f * city.scale).sp, lineHeight = (24f * city.scale).sp, fontFamily = textFont(city.timeFont), fontWeight = FontWeight.Bold)
                        if (config.showDate && city.showDate) Text(currentDate, color = Color(city.dateColor), fontSize = (9f * city.scale).sp, lineHeight = (11f * city.scale).sp, fontFamily = textFont(city.dateFont))
                    }
                    if (isSelected && !city.locked) ResizeHandle(Modifier.align(Alignment.BottomEnd).pointerInput(index, parent?.id, parent?.width, parent?.height) {
                        var start = city; var total = Offset.Zero
                        detectDragGestures(onDragStart = { start = liveCity; total = Offset.Zero; sizeGuide = null; sizeGuideTarget = null; onGestureStart() }, onDragEnd = { sizeGuide = null; sizeGuideTarget = null; onGestureFinished() }, onDragCancel = { sizeGuide = null; sizeGuideTarget = null; onGestureFinished() }) { change, drag ->
                            change.consume()
                            total += drag
                            val delta = with(density) { (total.x + total.y).toDp().value } * .008f
                            val requestedScale = (start.scale + delta).coerceIn(.35f, 2.2f)
                            val matching = config.cities.withIndex().filter { it.index != index }
                                .minByOrNull { kotlin.math.abs(it.value.scale - requestedScale) }
                            val newScale = if (snapEnabled && matching != null && kotlin.math.abs(matching.value.scale - requestedScale) <= .055f) {
                                sizeGuide = "Same city size"; sizeGuideTarget = CanvasSelection.CityItem(matching.index); matching.value.scale
                            } else { sizeGuide = null; sizeGuideTarget = null; requestedScale }
                            val newWidth = (130f * newScale).coerceAtMost(shapeBounds.width() * .90f)
                            val newHeight = (86f * newScale).coerceAtMost(shapeBounds.height() * .90f)
                            val cx = shapeBounds.centerX() + start.x * shapeBounds.width() * .5f
                            val cy = shapeBounds.centerY() + start.y * shapeBounds.height() * .5f
                            val safe = freeCanvasCenter(cx, cy, newWidth / 2f, newHeight / 2f, boardBounds)
                            onCityChange(index, start.copy(scale = newScale,
                                x = ((safe.x - shapeBounds.centerX()) / (shapeBounds.width() * .5f)).coerceIn(-1f, 1f),
                                y = ((safe.y - shapeBounds.centerY()) / (shapeBounds.height() * .5f)).coerceIn(-1f, 1f)))
                        }
                    })
                }
                if (isSelected && !city.locked) {
                    RotationHandleUnified(
                        objectCenterOnCanvas = Offset(centre.x, centre.y),
                        objectSizeOnCanvas = Offset(blockWidth, blockHeight),
                        rotationDegrees = city.rotation,
                        canvasOriginInRoot = canvasOriginInRoot,
                        canvasSizeOnCanvas = Offset(canvasWidth, canvasHeight),
                        snapEnabled = snapEnabled,
                        onRotationChange = { snapped ->
                            guideAngle = snapped.guide?.let { it to Offset(centre.x, centre.y) }
                            onCityChange(index, liveCity.copy(rotation = snapped.value))
                        },
                        onGestureStart = onGestureStart,
                        onGestureFinished = {
                            guideAngle = null
                            onGestureFinished()
                        }
                    )
                }
            }
            config.texts.forEachIndexed { index, item ->
                if (item.content.isNotBlank()) {
                    val liveText by rememberUpdatedState(item)
                    val parent = config.layers.firstOrNull { it.id == item.componentId }
                    // Text follows the same independent-canvas rule as cities.
                    val shapeBounds = boardBounds
                    var measured by remember { mutableStateOf(androidx.compose.ui.unit.IntSize(1, 1)) }
                    val widthDp = with(density) { measured.width.toDp().value }.coerceAtLeast(20f)
                    val heightDp = with(density) { measured.height.toDp().value }.coerceAtLeast(20f)
                    val requestedX = shapeBounds.centerX() + item.x * shapeBounds.width() * .5f
                    val requestedY = shapeBounds.centerY() + item.y * shapeBounds.height() * .5f
                    val centre = freeCanvasCenter(requestedX, requestedY, widthDp / 2f, heightDp / 2f, boardBounds)
                    val isSelected = selection == CanvasSelection.TextItem(index)
                    val itemColor = Color(item.color)
                    Box(Modifier.offset((centre.x-widthDp/2f).dp, (centre.y-heightDp/2f).dp)
                        .graphicsLayer { rotationZ = item.rotation }
                        .onSizeChanged { measured = it }
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (isSelected) designColors.selectionFill.copy(alpha = .62f) else Color.Transparent)
                        .then(if (isSelected) Modifier.border(1.dp, designColors.selectionOutline.copy(alpha = .90f), RoundedCornerShape(5.dp)) else Modifier)
                        .pointerInput(index, item.locked, parent?.id, parent?.width, parent?.height, snapEnabled) {
                            var start = item; var total = Offset.Zero
                            if (!item.locked) detectDragGestures(onDragStart = { start = liveText; total = Offset.Zero; onGestureStart(); onSelect(CanvasSelection.TextItem(index)) }, onDragEnd = { guideX = null; guideY = null; onGestureFinished() }, onDragCancel = { guideX = null; guideY = null; onGestureFinished() }) { change, drag ->
                                change.consume()
                                total += drag
                                val canvasPixels = rotateCanvasVector(total, start.rotation)
                                val canvasDelta = with(density) { Offset(canvasPixels.x.toDp().value, canvasPixels.y.toDp().value) }
                                val cx = shapeBounds.centerX() + start.x * shapeBounds.width() * .5f + canvasDelta.x
                                val cy = shapeBounds.centerY() + start.y * shapeBounds.height() * .5f + canvasDelta.y
                                val safe = freeCanvasCenter(cx, cy, widthDp/2f, heightDp/2f, boardBounds)
                                val otherX = config.texts.filterIndexed { otherIndex, _ -> otherIndex != index }.map { it.x } + config.cities.map { it.x }
                                val otherY = config.texts.filterIndexed { otherIndex, _ -> otherIndex != index }.map { it.y } + config.cities.map { it.y }
                                val sx = snapCoordinate((safe.x - shapeBounds.centerX()) / (shapeBounds.width() * .5f), snapEnabled, 16, otherX)
                                val sy = snapCoordinate((safe.y - shapeBounds.centerY()) / (shapeBounds.height() * .5f), snapEnabled, 16, otherY)
                                guideX = sx.guide?.let { shapeBounds.centerX() + it * shapeBounds.width() * .5f }
                                guideY = sy.guide?.let { shapeBounds.centerY() + it * shapeBounds.height() * .5f }
                                onTextChange(index, start.copy(x = sx.value, y = sy.value))
                            }
                        }.clickable { onSelect(CanvasSelection.TextItem(index))
                        }.padding(horizontal = 5.dp, vertical = 3.dp)) {
                        Text(
                            item.content,
                            color = itemColor.copy(alpha = itemColor.alpha * item.alpha),
                            fontSize = item.size.sp,
                            lineHeight = (item.size * 1.55f).sp,
                            fontFamily = textFont(item.font),
                            softWrap = true
                        )
                        if (isSelected && !item.locked) ResizeHandle(Modifier.align(Alignment.BottomEnd).pointerInput(index) {
                            var startSize = item.size; var total = Offset.Zero
                            detectDragGestures(onDragStart = { startSize = liveText.size; total = Offset.Zero; sizeGuide = null; sizeGuideTarget = null; onGestureStart() }, onDragEnd = { sizeGuide = null; sizeGuideTarget = null; onGestureFinished() }, onDragCancel = { sizeGuide = null; sizeGuideTarget = null; onGestureFinished() }) { change, drag ->
                                change.consume(); total += drag
                                val requestedSize = (startSize + (total.x + total.y) / density.density * .10f).coerceIn(7f, 72f)
                                val matching = config.texts.withIndex().filter { it.index != index }
                                    .minByOrNull { kotlin.math.abs(it.value.size - requestedSize) }
                                val newSize = if (snapEnabled && matching != null && kotlin.math.abs(matching.value.size - requestedSize) <= 1.2f) {
                                    sizeGuide = "Same text size"; sizeGuideTarget = CanvasSelection.TextItem(matching.index); matching.value.size
                                } else { sizeGuide = null; sizeGuideTarget = null; requestedSize }
                                onTextChange(index, liveText.copy(size = newSize))
                            }
                        })
                    }
                    if (isSelected && !item.locked) {
                        RotationHandleUnified(
                            objectCenterOnCanvas = Offset(centre.x, centre.y),
                            objectSizeOnCanvas = Offset(widthDp, heightDp),
                            rotationDegrees = item.rotation,
                            canvasOriginInRoot = canvasOriginInRoot,
                            canvasSizeOnCanvas = Offset(canvasWidth, canvasHeight),
                            snapEnabled = snapEnabled,
                            onRotationChange = { snapped ->
                                guideAngle = snapped.guide?.let { it to Offset(centre.x, centre.y) }
                                onTextChange(index, liveText.copy(rotation = snapped.value))
                            },
                            onGestureStart = onGestureStart,
                            onGestureFinished = {
                                guideAngle = null
                                onGestureFinished()
                            }
                        )
                    }
                }
            }
        }
    }
}
private fun layerBoundsOnBoard(layer: WidgetLayer, board: android.graphics.RectF): android.graphics.RectF {
    val width = board.width() * layer.width.coerceIn(.08f, 1f)
    val height = board.height() * layer.height.coerceIn(.08f, 1f)
    val wantedX = board.centerX() + layer.x * board.width() * .5f
    val wantedY = board.centerY() + layer.y * board.height() * .5f
    val cx = wantedX.coerceIn(board.left + width / 2f, board.right - width / 2f)
    val cy = wantedY.coerceIn(board.top + height / 2f, board.bottom - height / 2f)
    return android.graphics.RectF(cx - width / 2f, cy - height / 2f, cx + width / 2f, cy + height / 2f)
}

private fun freeCanvasCenter(x: Float, y: Float, halfWidth: Float, halfHeight: Float, board: android.graphics.RectF): android.graphics.PointF =
    android.graphics.PointF(
        x.coerceIn(board.left + halfWidth, board.right - halfWidth),
        y.coerceIn(board.top + halfHeight, board.bottom - halfHeight)
    )


@Composable private fun ResizeHandle(modifier: Modifier = Modifier) {
    Canvas(modifier.size(28.dp)) {
        val center=Offset(size.width*.73f,size.height*.73f)
        drawCircle(Color(0xEAF8F4FA),size.width*.26f,center)
        drawCircle(Color(0x667E658F),size.width*.26f,center,style=Stroke(.6.dp.toPx()))
        val c = Color(0xFF796288).copy(alpha = .75f)
        val stroke = 1.25.dp.toPx()
        drawLine(c, Offset(size.width*.55f,size.height*.88f), Offset(size.width*.88f,size.height*.55f), stroke)
        drawLine(c, Offset(size.width*.70f,size.height*.90f), Offset(size.width*.90f,size.height*.70f), stroke)
        drawLine(c, Offset(size.width*.82f,size.height*.92f), Offset(size.width*.92f,size.height*.82f), stroke)
    }
}

/**
 * A single rotation control for backgrounds, cities and text.
 *
 * The handle is deliberately a sibling of the rotated object, rather than its child.
 * Its pointer position, its measured position and the object's centre are therefore
 * all converted to root coordinates before the angle is calculated.
 */
@Composable
private fun RotationHandleUnified(
    objectCenterOnCanvas: Offset,
    objectSizeOnCanvas: Offset,
    rotationDegrees: Float,
    canvasOriginInRoot: Offset,
    canvasSizeOnCanvas: Offset,
    snapEnabled: Boolean,
    onRotationChange: (SnapResult) -> Unit,
    onGestureStart: () -> Unit,
    onGestureFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val latestObjectCenter by rememberUpdatedState(objectCenterOnCanvas)
    val latestCanvasOrigin by rememberUpdatedState(canvasOriginInRoot)
    val latestRotation by rememberUpdatedState(rotationDegrees)
    val latestSnapEnabled by rememberUpdatedState(snapEnabled)
    val latestOnRotationChange by rememberUpdatedState(onRotationChange)
    val latestOnGestureStart by rememberUpdatedState(onGestureStart)
    val latestOnGestureFinished by rememberUpdatedState(onGestureFinished)
    var handleOriginInRoot by remember { mutableStateOf(Offset.Zero) }

    // The icon remains quiet at 17dp, while the surrounding 42dp target is easy to grab.
    val touchTargetDp = 42f
    val handleRadiusDp = touchTargetDp / 2f
    val handleAnchor = objectCenterOnCanvas + rotateCanvasVector(
        Offset(objectSizeOnCanvas.x / 2f + 7f, -objectSizeOnCanvas.y / 2f - 7f),
        rotationDegrees
    )
    val handleCenter = Offset(
        handleAnchor.x.coerceIn(handleRadiusDp, canvasSizeOnCanvas.x - handleRadiusDp),
        handleAnchor.y.coerceIn(handleRadiusDp, canvasSizeOnCanvas.y - handleRadiusDp)
    )

    fun objectCenterInRoot(): Offset = Offset(
        latestCanvasOrigin.x + latestObjectCenter.x * density.density,
        latestCanvasOrigin.y + latestObjectCenter.y * density.density
    )

    Box(
        modifier = modifier
            .offset((handleCenter.x - handleRadiusDp).dp, (handleCenter.y - handleRadiusDp).dp)
            .size(touchTargetDp.dp)
            .zIndex(20f)
            .onGloballyPositioned { handleOriginInRoot = it.positionInRoot() }
            .pointerInput(Unit) {
                var gestureStartAngle = 0f
                var previousAngle = 0f
                var startingRotation = 0f
                detectDragGestures(
                    onDragStart = { startPosition ->
                        val pointerInRoot = handleOriginInRoot + startPosition
                        gestureStartAngle = angleBetween(objectCenterInRoot(), pointerInRoot)
                        previousAngle = gestureStartAngle
                        startingRotation = latestRotation
                        latestOnGestureStart()
                    },
                    onDragEnd = latestOnGestureFinished,
                    onDragCancel = latestOnGestureFinished
                ) { change, _ ->
                    val pointerInRoot = handleOriginInRoot + change.position
                    val rawAngle = angleBetween(objectCenterInRoot(), pointerInRoot)
                    val continuousAngle = unwrapAngleDelta(rawAngle, previousAngle)
                    previousAngle = continuousAngle

                    // Four percent compensates for short touch arcs without changing
                    // the circular geometry or introducing a discontinuity.
                    val responsiveRotation = startingRotation +
                        (continuousAngle - gestureStartAngle) * 1.04f
                    latestOnRotationChange(snapRotation(responsiveRotation, latestSnapEnabled))
                    change.consume()
                }
            },
        contentAlignment = Alignment.Center
          ) {
          Box(Modifier.size(24.dp).background(androidx.compose.ui.graphics.Color(0xEAF8F4FA), androidx.compose.foundation.shape.CircleShape).border(.6.dp, androidx.compose.ui.graphics.Color(0x667E658F), androidx.compose.foundation.shape.CircleShape), contentAlignment = Alignment.Center) {
              Icon(OrbitIcons.Rotate, null, tint = androidx.compose.ui.graphics.Color(0xFF796288).copy(alpha = .9f), modifier = Modifier.size(15.dp))
          }
      }
}
