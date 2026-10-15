package com.bestmates.worldclock.ui.editor

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.data.CityCatalog
import com.bestmates.worldclock.fonts.FontCategory
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import com.bestmates.worldclock.model.WidgetShape
import com.bestmates.worldclock.scene.ShapePaths
import com.bestmates.worldclock.ui.design.Bm
import com.bestmates.worldclock.ui.design.BmChip
import com.bestmates.worldclock.ui.design.BmIcons
import com.bestmates.worldclock.ui.design.BmOverline
import com.bestmates.worldclock.ui.design.BmSheet
import com.bestmates.worldclock.ui.design.BmSlider
import com.bestmates.worldclock.ui.design.BmSwatch
import com.bestmates.worldclock.ui.design.BmTextField
import com.bestmates.worldclock.ui.design.CheckerBrush
import com.bestmates.worldclock.ui.design.bleedHorizontally
import com.bestmates.worldclock.ui.i18n.LocalBestMatesLanguage
import com.bestmates.worldclock.ui.i18n.dateLocale
import com.bestmates.worldclock.ui.i18n.localizedCountry
import com.bestmates.worldclock.ui.i18n.localizedCountryName
import com.bestmates.worldclock.ui.i18n.translated
import com.bestmates.worldclock.ui.i18n.uiFormat
import com.bestmates.worldclock.ui.i18n.uiText
import com.bestmates.worldclock.ui.settings.AppLanguage
import com.bestmates.worldclock.widget.SolarIconRenderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/* ------------------------------------------------------------------ Fonts */

@Composable
fun rememberWidgetFontFamily(id: String): FontFamily {
    val context = LocalContext.current
    return remember(id) { FontFamily(androidx.compose.ui.text.font.Typeface(WidgetFonts.typeface(context, id))) }
}

@Composable
fun FontSheet(title: String, selected: String, sample: String, onDismiss: () -> Unit, liveOnly: Boolean = false, onPick: (String) -> Unit) {
    val c = Bm.colors
    val current = if (liveOnly) WidgetFonts.live(selected).id else WidgetFonts.canonicalId(selected)
    BmSheet(onDismiss = onDismiss, title = title, fullHeight = true) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (liveOnly) item(span = { GridItemSpan(2) }) {
                Text(uiText("live_fonts_note"), style = Bm.type.bodySmall, color = c.inkSoft, modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp))
            }
            FontCategory.entries.filter { !liveOnly || it == FontCategory.LIVE }.forEach { category ->
                val fonts = WidgetFonts.all.filter { it.category == category }
                if (fonts.isEmpty()) return@forEach
                item(span = { GridItemSpan(2) }) { BmOverline(uiText("font_cat_${category.name.lowercase()}"), Modifier.padding(top = 8.dp, start = 4.dp)) }
                items(fonts, key = { it.id }) { font ->
                    val isSelected = font.id == current
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(96.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isSelected) c.ink else c.surfaceSunken)
                            .clickable(role = Role.RadioButton) { onPick(font.id) }
                            .semantics { this.selected = isSelected }
                            .padding(14.dp),
                    ) {
                        Text(
                            sample, fontFamily = rememberWidgetFontFamily(font.id), fontSize = 26.sp, lineHeight = 30.sp,
                            color = if (isSelected) c.paper else c.ink, maxLines = 1, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                        Text(font.label, style = Bm.type.caption, color = if (isSelected) c.paper.copy(alpha = .75f) else c.inkSoft)
                    }
                }
            }
        }
    }
}

/* ------------------------------------------------------------------ Colours */

val SWATCHES: List<Long> = listOf(
    0xFFFFFFFF, 0xFFFFF6E8, 0xFFF4EAD8, 0xFFFFD58A, 0xFFF2A93B, 0xFFE98E6A,
    0xFFD06A6E, 0xFFF3C4C4, 0xFFB3A6F2, 0xFFE4E2FF, 0xFF8FB6D9, 0xFF4A4E8C,
    0xFF9AB9A2, 0xFF4D7A5B, 0xFF5D5459, 0xFF3A3640, 0xFF211C22, 0xFF000000,
)

private fun hsvOf(color: Long): FloatArray = FloatArray(3).also { android.graphics.Color.colorToHSV(color.toInt(), it) }
private fun argb(alpha: Float, h: Float, s: Float, v: Float): Long =
    android.graphics.Color.HSVToColor((alpha * 255).roundToInt().coerceIn(0, 255), floatArrayOf(h, s, v)).toLong() and 0xFFFFFFFFL

/**
 * Colour sheet. [onChange] reports every adjustment with `final = false` while a control
 * is being dragged and `final = true` when it is released or a swatch is tapped, so the
 * editor records one undo step per deliberate change.
 */
@Composable
fun ColorSheet(title: String, value: Long, onDismiss: () -> Unit, allowAlpha: Boolean = true, onChange: (Long, Boolean) -> Unit) {
    val c = Bm.colors
    val hsv0 = remember { hsvOf(value) }
    var hue by remember { mutableFloatStateOf(hsv0[0]) }
    var sat by remember { mutableFloatStateOf(hsv0[1]) }
    var bri by remember { mutableFloatStateOf(hsv0[2]) }
    var alpha by remember { mutableFloatStateOf(((value ushr 24) and 0xFF) / 255f) }
    fun current() = argb(if (allowAlpha) alpha else 1f, hue, sat, bri)
    val latest by rememberUpdatedState(onChange)
    BmSheet(onDismiss = onDismiss, title = title) {
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp).heightIn(max = 620.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(44.dp).clip(CircleShape).background(CheckerBrush.brush(c.isDark)).background(Color(current())).border(1.dp, c.hairlineStrong, CircleShape))
                Spacer(Modifier.width(12.dp))
                Text("#" + Integer.toHexString(current().toInt()).uppercase().padStart(8, '0').let { if (it.startsWith("FF")) it.drop(2) else it },
                    style = Bm.type.numeric, color = c.ink)
            }
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SWATCHES.forEach { s ->
                    BmSwatch(Color(s), selected = (s and 0xFFFFFF) == (current() and 0xFFFFFF), onClick = {
                        val h = hsvOf(s); hue = h[0]; sat = h[1]; bri = h[2]
                        latest(current(), true)
                    })
                }
            }
            // Saturation × value field for the current hue.
            Box(
                Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(16.dp))
                    .background(Brush.horizontalGradient(listOf(Color.White, Color.hsv(hue, 1f, 1f))))
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                    .pointerInput(Unit) {
                        fun set(p: Offset) { sat = (p.x / size.width).coerceIn(0f, 1f); bri = 1f - (p.y / size.height).coerceIn(0f, 1f) }
                        detectTapGestures { set(it); latest(current(), true) }
                    }
                    .pointerInput(Unit) {
                        fun set(p: Offset) { sat = (p.x / size.width).coerceIn(0f, 1f); bri = 1f - (p.y / size.height).coerceIn(0f, 1f) }
                        detectDragGestures(onDragStart = { set(it); latest(current(), false) }, onDragEnd = { latest(current(), true) }) { ch, _ -> ch.consume(); set(ch.position); latest(current(), false) }
                    },
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    val p = Offset(sat * size.width, (1f - bri) * size.height)
                    drawCircle(Color.Black.copy(alpha = .25f), 11.dp.toPx(), p, style = Stroke(4.dp.toPx()))
                    drawCircle(Color.White, 10.dp.toPx(), p, style = Stroke(2.5.dp.toPx()))
                }
            }
            GradientBar(Brush.horizontalGradient((0..6).map { Color.hsv(it * 60f % 360f, 1f, 1f) }), hue / 360f,
                onChange = { f, done -> hue = (f * 360f).coerceIn(0f, 359.9f); latest(current(), done) })
            if (allowAlpha) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(uiText("opacity_label"), style = Bm.type.bodySmall, color = c.inkSoft, modifier = Modifier.weight(1f))
                    Text("${(alpha * 100).roundToInt()}%", style = Bm.type.numeric, color = c.ink)
                }
                GradientBar(Brush.horizontalGradient(listOf(Color(argb(0f, hue, sat, bri)), Color(argb(1f, hue, sat, bri)))), alpha, checker = true,
                    onChange = { f, done -> alpha = f; latest(current(), done) })
            }
        }
    }
}

@Composable
private fun GradientBar(brush: Brush, fraction: Float, checker: Boolean = false, onChange: (Float, Boolean) -> Unit) {
    val c = Bm.colors
    val latest by rememberUpdatedState(onChange)
    var last by remember { mutableFloatStateOf(fraction) }
    Box(
        Modifier.fillMaxWidth().height(30.dp).clip(RoundedCornerShape(15.dp))
            .then(if (checker) Modifier.background(CheckerBrush.brush(c.isDark)) else Modifier)
            .background(brush)
            .pointerInput(Unit) { detectTapGestures { last = (it.x / size.width).coerceIn(0f, 1f); latest(last, true) } }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { last = (it.x / size.width).coerceIn(0f, 1f); latest(last, false) },
                    onDragEnd = { latest(last, true) },
                    onDragCancel = { latest(last, true) },
                ) { ch, _ -> ch.consume(); last = (ch.position.x / size.width).coerceIn(0f, 1f); latest(last, false) }
            },
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val x = (fraction.coerceIn(0f, 1f) * (size.width - size.height)) + size.height / 2f
            drawCircle(Color.Black.copy(alpha = .2f), size.height / 2f - 1.dp.toPx(), Offset(x, size.height / 2f), style = Stroke(4.dp.toPx()))
            drawCircle(Color.White, size.height / 2f - 3.dp.toPx(), Offset(x, size.height / 2f), style = Stroke(3.dp.toPx()))
        }
    }
}

/** Compact inline colour row: current colour plus quick swatches; "More" opens the sheet. */
@Composable
fun ColorRow(label: String, value: Long, onPick: (Long) -> Unit, onMore: () -> Unit) {
    val c = Bm.colors
    val moreLabel = uiText("more_colours")
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = Bm.type.bodySmall, color = c.inkSoft)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth().bleedHorizontally(20.dp).horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(34.dp).clip(CircleShape).background(c.surfaceSunken).border(1.dp, c.hairlineStrong, CircleShape)
                    .clickable(onClick = onMore).semantics { contentDescription = moreLabel },
                contentAlignment = Alignment.Center,
            ) { Icon(BmIcons.Palette, null, tint = c.ink, modifier = Modifier.size(18.dp)) }
            SWATCHES.take(12).forEach { s ->
                val alpha = (value ushr 24) and 0xFF
                BmSwatch(Color((s and 0xFFFFFF) or (alpha shl 24)), selected = (s and 0xFFFFFF) == (value and 0xFFFFFF), onClick = { onPick((s and 0xFFFFFF) or (alpha shl 24)) })
            }
        }
    }
}

/* ------------------------------------------------------------------ Shapes */

/** Localised shape name; keys are shape_<enum name in lower case>. */
@Composable
fun shapeName(shape: WidgetShape): String = uiText("shape_" + shape.name.lowercase())

/** The gallery, grouped so it reads as a curated library rather than a list. */
val SHAPE_GROUPS: List<Pair<String, List<WidgetShape>>> = listOf(
    "shape_group_basic" to listOf(WidgetShape.ROUNDED, WidgetShape.SQUIRCLE, WidgetShape.CAPSULE, WidgetShape.CIRCLE, WidgetShape.PILL, WidgetShape.PEBBLE),
    "shape_group_paper" to listOf(WidgetShape.TICKET, WidgetShape.STAMP, WidgetShape.FRAME, WidgetShape.NOTE, WidgetShape.BUBBLE, WidgetShape.RIBBON, WidgetShape.WAVE),
    "shape_group_nature" to listOf(WidgetShape.CLOUD, WidgetShape.PUFF, WidgetShape.PETAL, WidgetShape.BLOOM, WidgetShape.DIAMOND, WidgetShape.DROP),
    "shape_group_sky" to listOf(WidgetShape.HEART, WidgetShape.HEART_SOFT, WidgetShape.SPARKLE, WidgetShape.CRESCENT, WidgetShape.SUNBURST, WidgetShape.ROSETTE),
)

val SHAPE_ORDER: List<WidgetShape> = SHAPE_GROUPS.flatMap { it.second }
@Composable
fun ShapeGlyph(shape: WidgetShape, fill: Color, modifier: Modifier = Modifier, stroke: Color? = null) {
    Canvas(modifier) {
        val aspect = shape.designAspect
        val w: Float; val h: Float
        if (aspect >= size.width / size.height) { w = size.width; h = size.width / aspect } else { h = size.height; w = size.height * aspect }
        val rect = RectF((size.width - w) / 2f, (size.height - h) / 2f, (size.width + w) / 2f, (size.height + h) / 2f)
        val path = ShapePaths.path(shape, rect).asComposePath()
        drawPath(path, fill)
        if (stroke != null) drawPath(path, stroke, style = Stroke(1.2.dp.toPx()))
    }
}

@Composable
fun ShapeGrid(selected: WidgetShape?, onPick: (WidgetShape) -> Unit, columns: Int = 4) {
    val c = Bm.colors
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SHAPE_GROUPS.forEach { (group, shapes) ->
            BmOverline(uiText(group), Modifier.padding(top = 6.dp, start = 2.dp))
            shapes.chunked(columns).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { shape ->
                        val isSelected = shape == selected
                        Column(
                            Modifier.weight(1f).clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) c.accentSoft else c.surfaceSunken)
                                .border(if (isSelected) 1.5.dp else 0.dp, if (isSelected) c.accentInk else Color.Transparent, RoundedCornerShape(16.dp))
                                .clickable(role = Role.RadioButton) { onPick(shape) }
                                .semantics { this.selected = isSelected }
                                .padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            ShapeGlyph(shape, if (isSelected) c.accentInk else c.ink.copy(alpha = .78f), Modifier.size(width = 52.dp, height = 36.dp))
                            Spacer(Modifier.height(6.dp))
                            // Two centred lines, so longer names (German, Russian, French) read in full.
                            Text(
                                shapeName(shape), style = Bm.type.caption.copy(lineHeight = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center),
                                color = if (isSelected) c.accentInk else c.inkSoft, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                    repeat(columns - row.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
    }
}
/* ------------------------------------------------------------------ Background palettes */

data class Palette(val key: String, val light: Long, val dark: Long)

/** Curated light-mode / dark-mode fills. The first entries suit dark text, the rest light text. */
val PALETTES = listOf(
    // Mist, the default for new shapes: a smoky veil that darkens bright wallpaper enough
    // for white type, and stays a soft glass on dark wallpaper.
    Palette("mist", 0x57302B3DL, 0x6115131CL),
    Palette("frost", 0x40FFFFFFL, 0x30FFFFFFL),
    Palette("paper", 0xF5FBF8F2L, 0xF21B1A21L),
    Palette("marigold", 0xE6F2A93BL, 0xD9A3661FL),
    Palette("apricot", 0xE6E98E6AL, 0xD98A4A3AL),
    Palette("rose", 0xDDD06A6EL, 0xD97A3842L),
    Palette("lilac", 0xD9B3A6F2L, 0xD9524A8CL),
    Palette("dusk", 0xE655507FL, 0xE62A2C52L),
    Palette("sage", 0xE69AB9A2L, 0xD93E5A48L),
    Palette("ink", 0xCC211C22L, 0xE6000000L),
    Palette("lavender", 0x205D5774L, 0x42473D5DL),
)

@Composable
fun PaletteRow(light: Long, dark: Long, onPick: (Palette) -> Unit) {
    val c = Bm.colors
    Row(Modifier.fillMaxWidth().bleedHorizontally(20.dp).horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        PALETTES.forEach { p ->
            val isSelected = p.light == light && p.dark == dark
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(role = Role.RadioButton) { onPick(p) }.semantics { selected = isSelected }) {
                Box(
                    Modifier.size(width = 52.dp, height = 40.dp).clip(RoundedCornerShape(12.dp))
                        .border(if (isSelected) 2.dp else 1.dp, if (isSelected) c.ink else c.hairlineStrong, RoundedCornerShape(12.dp)),
                ) {
                    Row(Modifier.fillMaxSize()) {
                        Box(Modifier.weight(1f).fillMaxSize().background(Color(0xFFCFC6B8)).background(Color(p.light)))
                        Box(Modifier.weight(1f).fillMaxSize().background(Color(0xFF2A2830)).background(Color(p.dark)))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(uiText("palette_${p.key}"), style = Bm.type.caption, color = if (isSelected) c.ink else c.inkSoft)
            }
        }
    }
}

/* ------------------------------------------------------------------ Sun and moon */

/** The Daybook set is the only one offered; older styles stay renderable for saved designs. */
val SUN_LIBRARY = com.bestmates.worldclock.solar.SolarCatalog.pickerSuns
val MOON_LIBRARY = com.bestmates.worldclock.solar.SolarCatalog.pickerMoons

@Composable
fun SolarGlyph(day: Boolean, sun: SunIconStyle, moon: MoonIconStyle, color: Long, size: Float, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bitmap = remember(day, sun, moon, color, size) {
        SolarIconRenderer.render(context, (size * context.resources.displayMetrics.density).roundToInt(), day, sun, moon, color.toInt()).asImageBitmap()
    }
    Image(bitmap, null, modifier.size(size.dp), filterQuality = FilterQuality.High)
}

/**
 * Sun or moon library as a wrapping grid: every glyph is visible at once, nothing is cut at
 * the panel edges. A saved design whose icon predates the Daybook set shows that icon first,
 * marked as its current classic icon, so keeping it stays one tap away.
 */
@Composable
fun SolarLibrary(day: Boolean, sun: SunIconStyle, moon: MoonIconStyle, color: Long, onSun: (SunIconStyle) -> Unit, onMoon: (MoonIconStyle) -> Unit) {
    val c = Bm.colors
    // A redrawn older style is shown and selected as its Daybook counterpart; only styles
    // without one (other icon packs) appear as their own "classic" tile.
    val current: Any = if (day) com.bestmates.worldclock.solar.SolarCatalog.daybookSun(sun) ?: sun else com.bestmates.worldclock.solar.SolarCatalog.daybookMoon(moon) ?: moon
    val library: List<Any> = if (day) SUN_LIBRARY else MOON_LIBRARY
    val styles = if (current in library) library else listOf(current) + library
    val tile = if (c.isDark) Color(0xFF26233A) else Color(0xFF2E2A45)
    @OptIn(ExperimentalLayoutApi::class)
    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), maxItemsInEachRow = 5) {
        styles.forEach { style ->
            val isSelected = style == current
            val legacy = style !in library
            Box(
                Modifier.weight(1f).aspectRatio(1f).clip(RoundedCornerShape(16.dp))
                    .background(tile)
                    .border(if (isSelected) 2.dp else 0.dp, if (isSelected) c.accent else Color.Transparent, RoundedCornerShape(16.dp))
                    .clickable(role = Role.RadioButton) { if (day) onSun(style as SunIconStyle) else onMoon(style as MoonIconStyle) }
                    .semantics { selected = isSelected },
                contentAlignment = Alignment.Center,
            ) {
                SolarGlyph(day, if (day) style as SunIconStyle else sun, if (!day) style as MoonIconStyle else moon, color, 36f)
                if (legacy) Text(
                    uiText("classic_icon"), style = Bm.type.caption, color = Color.White.copy(alpha = .7f), maxLines = 1,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 3.dp),
                )
            }
        }
        // Keep the last row's tiles the same width as the others.
        repeat((5 - styles.size % 5) % 5) { Spacer(Modifier.weight(1f)) }
    }
}
/* ------------------------------------------------------------------ City picker */

@Composable
fun CityPickerSheet(onDismiss: () -> Unit, onPick: (City) -> Unit) {
    val c = Bm.colors
    val context = LocalContext.current
    val catalog = remember { CityCatalog(context) }
    val language = LocalBestMatesLanguage.current
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(emptyList<City>()) }
    var continent by remember { mutableStateOf<String?>(null) }
    var country by remember { mutableStateOf<Pair<String, String>?>(null) }
    var countries by remember { mutableStateOf(emptyList<Pair<String, String>>()) }
    var countryCities by remember { mutableStateOf(emptyList<City>()) }
    LaunchedEffect(query) { delay(160); results = withContext(Dispatchers.IO) { runCatching { catalog.search(query) }.getOrDefault(emptyList()) } }
    LaunchedEffect(continent, language) {
        // Countries are listed by their name in the app's language, in that language's order.
        val collator = java.text.Collator.getInstance(language.dateLocale())
        countries = continent?.let { withContext(Dispatchers.IO) { catalog.countries(it) } }.orEmpty()
            .sortedWith(compareBy(collator) { localizedCountry(it.first, language, it.second) })
    }
    LaunchedEffect(country) { countryCities = country?.let { withContext(Dispatchers.IO) { catalog.cities(it.first, "", 80) } }.orEmpty() }
    BmSheet(onDismiss = onDismiss, title = uiText("choose_city"), fullHeight = true) {
        Column(Modifier.padding(horizontal = 20.dp)) {
            BmTextField(query, { query = it }, Modifier.fillMaxWidth(), placeholder = uiText("search_city_any"), leadingIcon = BmIcons.Search)
            Spacer(Modifier.height(12.dp))
        }
        LazyColumn(Modifier.fillMaxWidth().weight(1f), contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp)) {
            when {
                query.isNotBlank() -> {
                    if (results.isEmpty()) item { Text(uiText("no_results"), style = Bm.type.bodySmall, color = c.inkSoft, modifier = Modifier.padding(vertical = 16.dp)) }
                    items(results, key = { it.geoId }) { city -> CityRow(city) { onPick(city) } }
                }
                country != null -> {
                    item { BackRow(localizedCountry(country!!.first, language, country!!.second)) { country = null } }
                    items(countryCities, key = { it.geoId }) { city -> CityRow(city) { onPick(city) } }
                }
                continent != null -> {
                    item { BackRow(continentLabel(continent!!, language)) { continent = null } }
                    items(countries, key = { it.first }) { co -> BrowseRow(localizedCountry(co.first, language, co.second)) { country = co } }
                }
                else -> {
                    item { BmOverline(uiText("browse"), Modifier.padding(vertical = 8.dp)) }
                    items(catalog.continents().filter { it != "AN" }) { code -> BrowseRow(continentLabel(code, language)) { continent = code } }
                }
            }
        }
    }
}

@Composable
private fun CityRow(city: City, onClick: () -> Unit) {
    val c = Bm.colors
    val now = remember(city.zoneId) {
        runCatching { java.time.ZonedDateTime.now(java.time.ZoneId.of(city.zoneId)).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) }.getOrDefault("")
    }
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(vertical = 12.dp, horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(city.name, style = Bm.type.bodyStrong, color = c.ink)
            Text("${localizedCountryName(city.country, LocalBestMatesLanguage.current)} · ${city.zoneId.substringAfterLast('/').replace('_', ' ')}", style = Bm.type.bodySmall, color = c.inkSoft, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(now, style = Bm.type.numeric, color = c.inkSoft)
    }
}

@Composable
private fun BrowseRow(label: String, onClick: () -> Unit) {
    val c = Bm.colors
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(vertical = 14.dp, horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = Bm.type.body, color = c.ink, modifier = Modifier.weight(1f))
        Icon(BmIcons.ChevronRight, null, tint = c.inkFaint, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun BackRow(label: String, onClick: () -> Unit) {
    val c = Bm.colors
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(BmIcons.Back, null, tint = c.ink, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(label, style = Bm.type.titleSans, color = c.ink)
    }
}

fun continentLabel(code: String, language: AppLanguage): String = translated(language, "continent_" + code.lowercase())

