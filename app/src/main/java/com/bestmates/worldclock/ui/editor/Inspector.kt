package com.bestmates.worldclock.ui.editor

import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.model.*
import com.bestmates.worldclock.ui.components.*
import com.bestmates.worldclock.ui.i18n.*
import com.bestmates.worldclock.ui.icons.*
import com.bestmates.worldclock.ui.theme.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.math.min
import kotlin.math.cos
import kotlin.math.sin
import com.bestmates.worldclock.widget.BackgroundRenderer
import com.bestmates.worldclock.ui.settings.AppLanguage
import java.util.Locale

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

internal enum class CityPart { NAME, TIME, DATE, SOLAR }
internal enum class EditorAction { CLEAR, COPY, PASTE, PIN, CUT, DELETE, RENAME, ADD_COMPONENT, ADD_CITY, ADD_TEXT, UNDO, REDO, AUTO_SAVE }

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EditorActionRow(
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
internal fun EditorActionButton(
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

@Composable internal fun EditableDisplayRow(label: String, checked: Boolean, onChecked: ((Boolean) -> Unit)?, onEdit: () -> Unit) {
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

// Curated display list. Legacy styles like LUCIDE_SUN are intentionally excluded but remain compatible if saved.
internal val SUN_ICON_LIBRARY = listOf(SunIconStyle.BM_SUN_SIMPLE_FILL, SunIconStyle.BM_SUN_SIMPLE_ROUND, SunIconStyle.BM_SUN_CLOUD_FILL, SunIconStyle.BM_SUN_HORIZON_FILL, SunIconStyle.BM_SUN_WAVE_FILL, SunIconStyle.BM_SUN_MIST)
internal val MOON_ICON_LIBRARY = listOf(MoonIconStyle.BM_MOON_PLAIN_FILL, MoonIconStyle.BM_MOON_STARS_FILL, MoonIconStyle.BM_MOON_CLOUD_FILL, MoonIconStyle.BM_MOON_HORIZON_STARS, MoonIconStyle.BM_MOON_SLEEP_FILL, MoonIconStyle.BM_ISLAMIC_CRESCENT_STAR)


@Composable internal fun FontField(label: String, value: String, sample: String, onChange: (String) -> Unit) {
    var open by remember { mutableStateOf(false) }
    val choice = FONT_CHOICES.firstOrNull { it.id == value } ?: FONT_CHOICES.first()
    CrystalPickerRow(onClick = { open = true }) {
            Icon(OrbitIcons.Font, null, modifier = Modifier.size(24.dp)); Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(label, style = MaterialTheme.typography.bodySmall); Text(sample, fontFamily = textFont(choice.id), fontSize = 20.sp, maxLines = 2, lineHeight = 25.sp, overflow = TextOverflow.Ellipsis) }
            Text(uiText("choose"))
    }
    if (open) {
        val displayFonts = remember(value) { if (VISIBLE_FONTS.any { it.id == value }) VISIBLE_FONTS else listOfNotNull(FONT_CHOICES.firstOrNull { it.id == value }) + VISIBLE_FONTS }
        CrystalDialog(onDismissRequest = { open = false }, title = { Text(uiText("choose_font")) }, confirmButton = { TextButton(onClick = { open = false }) { Text(uiText("done")) } }, text = {
        LazyColumn(Modifier.heightIn(max = 540.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            items(displayFonts.chunked(2)) { row -> Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                row.forEach { item -> CrystalChoiceTile(selected = item.id == value, selectedDescription = uiText("selected"), onClick = { onChange(item.id); open = false }, modifier = Modifier.weight(1f).height(82.dp)) {
                    Box(Modifier.fillMaxSize().padding(9.dp), contentAlignment = Alignment.Center) { Text(sample, fontFamily = textFont(item.id), fontSize = 18.sp, lineHeight = 23.sp, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                } }; if(row.size==1) Spacer(Modifier.weight(1f))
            } }
        }
    })
}
    }

@Composable internal fun CityPartDialog(part: CityPart, city: City, use24Hour: Boolean, onDismiss: () -> Unit, onChange: (City) -> Unit) {
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

@Composable internal fun SunIconLibraryField(selected: SunIconStyle, moon: MoonIconStyle, color: Long, onChange: (SunIconStyle) -> Unit) {
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick={open=true}) {
        SolarIconSample(true, selected, moon, color, 34f); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)){Text(uiText("sun_library"),style=MaterialTheme.typography.bodySmall);Text(uiText("browse_icons"),fontWeight=FontWeight.Bold)}; Text(uiText("choose"))
    }
    if(open) {
        val displaySuns = remember(selected) { if (SUN_ICON_LIBRARY.contains(selected)) SUN_ICON_LIBRARY else listOf(selected) + SUN_ICON_LIBRARY }
        CrystalDialog(onDismissRequest={open=false},title={Text(uiText("sun_library"))},confirmButton={TextButton(onClick={open=false}){Text(uiText("done"))}},text={
        LazyColumn(Modifier.heightIn(max=540.dp), verticalArrangement=Arrangement.spacedBy(7.dp)){items(displaySuns.chunked(4)){row->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(7.dp)){row.forEach{style->
            CrystalChoiceTile(selected=style==selected,selectedDescription=uiText("selected"),onClick={onChange(style);open=false},modifier=Modifier.weight(1f).aspectRatio(1f)){Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){SolarIconSample(true,style,moon,color,43f)}}
        };repeat(4-row.size){Spacer(Modifier.weight(1f))}}}}
    })
    }
}

@Composable internal fun MoonIconLibraryField(sun: SunIconStyle, selected: MoonIconStyle, color: Long, onChange: (MoonIconStyle) -> Unit) {
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick={open=true}) {
        SolarIconSample(false, sun, selected, color, 34f); Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)){Text(uiText("moon_library"),style=MaterialTheme.typography.bodySmall);Text(uiText("browse_icons"),fontWeight=FontWeight.Bold)}; Text(uiText("choose"))
    }
    if(open) {
        val displayMoons = remember(selected) { if (MOON_ICON_LIBRARY.contains(selected)) MOON_ICON_LIBRARY else listOf(selected) + MOON_ICON_LIBRARY }
        CrystalDialog(onDismissRequest={open=false},title={Text(uiText("moon_library"))},confirmButton={TextButton(onClick={open=false}){Text(uiText("done"))}},text={
        LazyColumn(Modifier.heightIn(max=540.dp), verticalArrangement=Arrangement.spacedBy(7.dp)){items(displayMoons.chunked(4)){row->Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(7.dp)){row.forEach{style->
            CrystalChoiceTile(selected=style==selected,selectedDescription=uiText("selected"),onClick={onChange(style);open=false},modifier=Modifier.weight(1f).aspectRatio(1f)){Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){SolarIconSample(false,sun,style,color,43f)}}
        };repeat(4-row.size){Spacer(Modifier.weight(1f))}}}}
    })
    }
}

// Shared artwork only. Production Pin proportions and 20dp stroke are unchanged.
internal fun androidx.compose.ui.graphics.drawscope.DrawScope.drawProductionPin(tint: Color) {
    val stroke = this@pointerInput.size.width * .08f
    val cap = androidx.compose.ui.graphics.StrokeCap.Round
    fun p(x:Float,y:Float) = Offset(this@pointerInput.size.width*x,this@pointerInput.size.height*y)
    fun line(a:Offset,b:Offset) = drawLine(tint,a,b,stroke,cap=cap)
    drawRoundRect(tint,p(.33f,.12f),androidx.compose.ui.geometry.Size(this@pointerInput.size.width*.34f,this@pointerInput.size.height*.20f),androidx.compose.ui.geometry.CornerRadius(this@pointerInput.size.width*.08f),style=Stroke(stroke,cap=cap,join=androidx.compose.ui.graphics.StrokeJoin.Round))
    line(p(.37f,.33f),p(.28f,.60f)); line(p(.63f,.33f),p(.72f,.60f))
    line(p(.28f,.60f),p(.72f,.60f)); line(p(.50f,.60f),p(.50f,.88f))
}

@Composable internal fun shapeLabel(shape: WidgetShape): String {
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
internal fun ShapePicker(selected: WidgetShape, onSelect: (WidgetShape) -> Unit) {
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
internal fun ShapePickerDialog(selected: WidgetShape?, title: String, onDismiss: () -> Unit, onSelect: (WidgetShape) -> Unit) {
    val groups = listOf(
        uiText("classic") to listOf(WidgetShape.ROUNDED, WidgetShape.SQUIRCLE, WidgetShape.CIRCLE, WidgetShape.CAPSULE),
        uiText("natural") to listOf(WidgetShape.CLOUD, WidgetShape.DROP, WidgetShape.PETAL, WidgetShape.HEART),
        uiText("playful") to listOf(WidgetShape.PILL, WidgetShape.DIAMOND, WidgetShape.TICKET, WidgetShape.WAVE)
    )

    CrystalDialog(onDismissRequest = onDismiss, title = { Text(title) }, confirmButton = { TextButton(onClick = onDismiss) { Text(uiText("cancel")) } }, text = {
        LazyColumn(Modifier.heightIn(max = 600.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            groups.forEach { (groupName, shapes) ->
                item {
                    Text(groupName, style = MaterialTheme.typography.labelMedium, color = BestMatesDesign.colors.textSecondary)
                    Spacer(Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        shapes.chunked(2).forEach { row ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { shape ->
                                    CrystalChoiceTile(selected = shape == selected, selectedDescription = uiText("selected"), onClick = { onSelect(shape) }, modifier = Modifier.weight(1f).height(96.dp)) {
                                        Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                            ShapePreviewIcon(shape, Modifier.height(42.dp).fillMaxWidth(), shape == selected)
                                            Spacer(Modifier.height(8.dp))
                                            Text(shapeLabel(shape), fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                                        }
                                    }
                                }
                                repeat(2 - row.size) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }
                }
            }
        }
    })
}

internal fun initialPlacement(index: Int, city: City): City {
    val points = listOf(0f to 0f, -.46f to .42f, .46f to .42f, -.46f to -.42f, .46f to -.42f, 0f to .68f, 0f to -.68f)
    val point = points[index % points.size]
    return city.copy(x = point.first, y = point.second)
}

internal fun shapeRecommendedSizes(shape: WidgetShape, count: Int): List<Pair<Int,Int>> = when(shape) {
    WidgetShape.CLOUD, WidgetShape.CAPSULE, WidgetShape.WAVE, WidgetShape.TICKET -> if (count <= 2) listOf(4 to 1, 4 to 2) else listOf(4 to 2, 4 to 3)
    WidgetShape.PILL, WidgetShape.PETAL, WidgetShape.DROP -> if (count <= 2) listOf(2 to 2, 3 to 2) else listOf(3 to 3, 4 to 3)
    WidgetShape.CIRCLE, WidgetShape.DIAMOND, WidgetShape.HEART, WidgetShape.SQUIRCLE -> if (count <= 3) listOf(3 to 3, 4 to 4) else listOf(4 to 4, 4 to 5)
    WidgetShape.ROUNDED -> when(count.coerceAtLeast(1)) { 1 -> listOf(1 to 1,2 to 1); 2 -> listOf(2 to 1,2 to 2); in 3..4 -> listOf(3 to 2,4 to 2); in 5..6 -> listOf(4 to 3); else -> listOf(4 to 4,4 to 5) }
}

@Composable internal fun ShapePreviewIcon(shape: WidgetShape, modifier: Modifier = Modifier, selected: Boolean = false) {
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
        val w = this@pointerInput.size.width; val h = this@pointerInput.size.height
        val p = androidx.compose.ui.graphics.Path()
        when (shape) {
            WidgetShape.ROUNDED -> drawRoundRect(color, topLeft=Offset(w*.04f,h*.08f), size=androidx.compose.ui.geometry.Size(w*.92f,h*.84f), cornerRadius=androidx.compose.ui.geometry.CornerRadius(h*.18f), style=stroke)
            WidgetShape.SQUIRCLE -> drawRoundRect(color, topLeft=Offset(w*.08f,h*.04f), size=androidx.compose.ui.geometry.Size(w*.84f,h*.92f), cornerRadius=androidx.compose.ui.geometry.CornerRadius(h*.28f), style=stroke)
            WidgetShape.CIRCLE -> drawCircle(color, radius=min(w,h)*.43f, style=stroke)
            WidgetShape.CAPSULE -> drawRoundRect(color, topLeft=Offset(w*.03f,h*.20f), size=androidx.compose.ui.geometry.Size(w*.94f,h*.60f), cornerRadius=androidx.compose.ui.geometry.CornerRadius(h*.30f), style=stroke)
            WidgetShape.PILL -> { p.moveTo(w*.08f,h*.90f); p.lineTo(w*.08f,h*.44f); p.cubicTo(w*.08f,h*.10f,w*.30f,h*.04f,w*.50f,h*.04f); p.cubicTo(w*.70f,h*.04f,w*.92f,h*.10f,w*.92f,h*.44f); p.lineTo(w*.92f,h*.90f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.CLOUD -> { p.moveTo(w*.04f,h*.68f); p.cubicTo(w*.01f,h*.43f,w*.18f,h*.27f,w*.34f,h*.34f); p.cubicTo(w*.43f,h*.04f,w*.70f,h*.08f,w*.75f,h*.37f); p.cubicTo(w*.96f,h*.32f,w*.99f,h*.62f,w*.86f,h*.76f); p.cubicTo(w*.70f,h*.89f,w*.20f,h*.87f,w*.08f,h*.76f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.DIAMOND -> { p.moveTo(w*.5f,h*.03f); p.cubicTo(w*.67f,h*.03f,w*.74f,h*.22f,w*.72f,h*.30f); p.cubicTo(w*.82f,h*.28f,w*.97f,h*.37f,w*.97f,h*.5f); p.cubicTo(w*.97f,h*.63f,w*.82f,h*.72f,w*.72f,h*.70f); p.cubicTo(w*.74f,h*.78f,w*.67f,h*.97f,w*.5f,h*.97f); p.cubicTo(w*.33f,h*.97f,w*.26f,h*.78f,w*.28f,h*.70f); p.cubicTo(w*.18f,h*.72f,w*.03f,h*.63f,w*.03f,h*.5f); p.cubicTo(w*.03f,h*.37f,w*.18f,h*.28f,w*.28f,h*.30f); p.cubicTo(w*.26f,h*.22f,w*.33f,h*.03f,w*.5f,h*.03f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.PETAL -> { p.moveTo(w*.5f, 0f); p.cubicTo(w*1.166f, 0f, w*1.166f, h, w*.5f, h); p.cubicTo(w*-.166f, h, w*-.166f, 0f, w*.5f, 0f); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.HEART -> { p.moveTo(w*.5f,h); p.cubicTo(w*.39f,h*.90f, 0f,h*.58f, 0f,h*.30f); p.cubicTo(0f,h*.05f, w*.34f,0f, w*.5f,h*.20f); p.cubicTo(w*.66f,0f, w,h*.05f, w,h*.30f); p.cubicTo(w,h*.58f, w*.61f,h*.90f, w*.5f,h); p.close(); drawPath(p,color,style=stroke) }
            WidgetShape.DROP -> { p.moveTo(w*.5f,h*.02f); p.cubicTo(w*.88f,h*.26f, w,h*.52f, w,h*.74f); p.cubicTo(w,h*.92f, w*.73f,h, w*.5f,h); p.cubicTo(w*.27f,h, 0f,h*.92f, 0f,h*.74f); p.cubicTo(0f,h*.52f, w*.12f,h*.26f, w*.5f,h*.02f); p.close(); drawPath(p,color,style=stroke) }
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

internal data class DayNightPalette(val name: String, val day: Long, val night: Long)
internal val DAY_NIGHT_PALETTES = listOf(
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

@Composable internal fun DayNightPaletteField(day: Long, night: Long, change: (Long, Long) -> Unit) {
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

@Composable internal fun PalettePair(day: Long, night: Long) {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(32.dp).clip(RoundedCornerShape(16.dp)).background(Color(day)).border(1.dp, BestMatesDesign.colors.borderSubtle, RoundedCornerShape(16.dp)))
            Text("D ${((day ushr 24) and 0xFF) * 100 / 255}%", fontSize = 9.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(32.dp).clip(RoundedCornerShape(16.dp)).background(Color(night)).border(1.dp, BestMatesDesign.colors.borderSubtle, RoundedCornerShape(16.dp)))
            Text("N ${((night ushr 24) and 0xFF) * 100 / 255}%", fontSize = 9.sp)
        }
    }
}
@Composable internal fun ColorField(label:String,value:Long,change:(Long)->Unit){
    var open by remember { mutableStateOf(false) }
    CrystalPickerRow(onClick = { open = true }) {
            Box(Modifier.size(34.dp).clip(RoundedCornerShape(17.dp)).background(Color(value)).border(1.dp, BestMatesDesign.colors.borderControl, RoundedCornerShape(17.dp)))
            Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(label, fontWeight = FontWeight.Medium); Text(uiText("open_color"), style = MaterialTheme.typography.bodySmall) }
            Text(uiText("choose"))
    }
    if (open) VisualColorPickerDialog(label, value, onDismiss = { open = false }, onApply = { change(it); open = false })
}

@Composable internal fun VisualColorPickerDialog(title: String, original: Long, onDismiss: () -> Unit, onApply: (Long) -> Unit) {
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
                    val center = Offset(this@pointerInput.size.width / 2f, this@pointerInput.size.height / 2f)
                    val dx = position.x - center.x; val dy = position.y - center.y
                    hue = ((Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat() + 360f) % 360f)
                    saturation = (sqrt(dx * dx + dy * dy) / (min(this@pointerInput.size.width, this@pointerInput.size.height) / 2f)).coerceIn(0f, 1f)
                }
                detectDragGestures(onDragStart = { update(it) }) { change, _ -> update(change.position); change.consume() }
            }) {
                Image(wheel, null, Modifier.fillMaxSize())
                Canvas(Modifier.fillMaxSize()) {
                    val radius = min(this@pointerInput.size.width, this@pointerInput.size.height) / 2f - 7f
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

internal fun hsvWheelBitmap(size: Int, value: Float): Bitmap {
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
@Composable internal fun SectionTitle(text:String)=Text(text,style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)
@Composable internal fun SliderRow(label:String,value:Float,range:ClosedFloatingPointRange<Float>,change:(Float)->Unit)=OrbitSliderRow(label,"%.1f".format(value),value,range,change)
@Composable internal fun SwitchRow(label:String,checked:Boolean,change:(Boolean)->Unit)=OrbitSwitchRow(label,checked,change)
@OptIn(ExperimentalLayoutApi::class)
@Composable internal fun ThemePicker(selected:ThemeMode,pick:(ThemeMode)->Unit)=FlowRow(horizontalArrangement=Arrangement.spacedBy(6.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){ThemeMode.entries.forEach{OrbitChoiceChip(label=when(it){ThemeMode.SYSTEM->uiText("follow_system");ThemeMode.LIGHT->uiText("light");ThemeMode.DARK->uiText("dark")},selected=selected==it,selectedDescription=uiText("selected"),onClick={pick(it)})}}

