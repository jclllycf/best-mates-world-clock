package com.bestmates.worldclock.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.ui.design.Bm
import com.bestmates.worldclock.ui.design.BmButton
import com.bestmates.worldclock.ui.design.BmButtonSize
import com.bestmates.worldclock.ui.design.BmButtonStyle
import com.bestmates.worldclock.ui.design.BmIconButton
import com.bestmates.worldclock.ui.design.BmIcons
import com.bestmates.worldclock.ui.design.BmSegmented
import com.bestmates.worldclock.ui.design.BmSliderRow
import com.bestmates.worldclock.ui.design.BmSwitch
import com.bestmates.worldclock.ui.design.BmTextField
import com.bestmates.worldclock.ui.i18n.LocalBestMatesLanguage
import com.bestmates.worldclock.ui.i18n.translated
import com.bestmates.worldclock.ui.i18n.uiText
import kotlin.math.roundToInt

@Composable
fun Inspector(state: EditorState, sel: EditorSelection, previewNight: Boolean, onSheet: (EditorSheet) -> Unit) {
    val (icon, title) = when (sel) {
        is EditorSelection.Layer -> BmIcons.Shape to (state.layer(sel.index)?.let { shapeName(it.shape) } ?: "")
        is EditorSelection.CityItem -> BmIcons.City to (state.city(sel.index)?.let { it.nickname.ifBlank { it.name } } ?: "")
        is EditorSelection.TextItem -> BmIcons.Text to (state.text(sel.index)?.content?.lineSequence()?.firstOrNull().orEmpty().ifBlank { uiText("text") })
    }
    val locked = state.isLocked(sel)
    Column(Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            Icon(icon, null, tint = Bm.colors.accentInk, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Text(title, style = Bm.type.title, color = Bm.colors.ink, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            BmIconButton(BmIcons.Duplicate, uiText("duplicate"), state::duplicateSelected, size = 38.dp, iconSize = 19.dp)
            BmIconButton(if (locked) BmIcons.Lock else BmIcons.Unlock, uiText(if (locked) "unlock" else "lock"), state::toggleLock, selected = locked, size = 38.dp, iconSize = 19.dp)
            BmIconButton(BmIcons.BringForward, uiText("bring_forward"), { state.reorder(+1) }, size = 38.dp, iconSize = 19.dp)
            BmIconButton(BmIcons.SendBackward, uiText("send_backward"), { state.reorder(-1) }, size = 38.dp, iconSize = 19.dp)
            BmIconButton(BmIcons.Trash, uiText("delete"), state::deleteSelected, tint = Bm.colors.danger, size = 38.dp, iconSize = 19.dp)
            BmIconButton(BmIcons.Check, uiText("done"), { state.selection = null }, tonal = true, size = 38.dp, iconSize = 19.dp)
        }
        when (sel) {
            is EditorSelection.Layer -> LayerInspector(state, sel.index, previewNight, onSheet)
            is EditorSelection.CityItem -> CityInspector(state, sel.index, onSheet)
            is EditorSelection.TextItem -> TextInspector(state, sel.index, onSheet)
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun Tabs(labels: List<String>, icons: List<ImageVector?> = emptyList(), content: @Composable ColumnScope.(Int) -> Unit) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    Column(Modifier.fillMaxWidth()) {
        BmSegmented(labels, tab.coerceIn(0, labels.lastIndex), { tab = it }, Modifier.padding(horizontal = 20.dp, vertical = 10.dp), icons)
        Column(
            Modifier.fillMaxWidth().height(212.dp).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) { content(tab.coerceIn(0, labels.lastIndex)) }
    }
}

private fun f1(v: Float) = if (v >= 10f || v == v.roundToInt().toFloat()) "${v.roundToInt()}" else "%.1f".format(v)
private fun pct(v: Float) = "${(v * 100).roundToInt()}%"
private fun deg(v: Float) = "${v.roundToInt()}°"

/* ------------------------------------------------------------------ Shape */

@Composable
private fun LayerInspector(state: EditorState, index: Int, previewNight: Boolean, onSheet: (EditorSheet) -> Unit) {
    val layer = state.layer(index) ?: return
    val lang = LocalBestMatesLanguage.current
    Tabs(listOf(uiText("tab_shape"), uiText("tab_colour"), uiText("tab_edge"), uiText("tab_depth"))) { tab ->
        when (tab) {
            0 -> ShapeGrid(layer.shape, onPick = { s -> state.updateLayer(index) { it.copy(shape = s) } })
            1 -> {
                PaletteRow(layer.dayColor, layer.nightColor) { p -> state.updateLayer(index) { it.copy(dayColor = p.light, nightColor = p.dark) } }
                ColorRow(uiText("fill_light_mode"), layer.dayColor, onPick = { v -> state.updateLayer(index) { it.copy(dayColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "fill_light_mode"), layer.dayColor, true) { v -> state.liveLayer(index) { it.copy(dayColor = v) } })
                }
                ColorRow(uiText("fill_dark_mode"), layer.nightColor, onPick = { v -> state.updateLayer(index) { it.copy(nightColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "fill_dark_mode"), layer.nightColor, true) { v -> state.liveLayer(index) { it.copy(nightColor = v) } })
                }
                Text(uiText(if (previewNight) "previewing_dark" else "previewing_light"), style = Bm.type.caption, color = Bm.colors.inkFaint)
            }
            2 -> {
                BmSliderRow(uiText("outline_width"), f1(layer.borderWidth), layer.borderWidth, 0f..6f, { v -> state.liveLayer(index) { it.copy(borderWidth = v) } }, state::endGesture)
                BmSliderRow(uiText("outline_opacity"), pct(layer.borderOpacity), layer.borderOpacity, 0f..1f, { v -> state.liveLayer(index) { it.copy(borderOpacity = v) } }, state::endGesture)
                ColorRow(uiText("outline_glow_colour"), layer.textColor, onPick = { v -> state.updateLayer(index) { it.copy(textColor = v or 0xFF000000) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "outline_glow_colour"), layer.textColor, false) { v -> state.liveLayer(index) { it.copy(textColor = v) } })
                }
                BmSliderRow(uiText("glow_softness"), f1(layer.softness), layer.softness, 0f..48f, { v -> state.liveLayer(index) { it.copy(softness = v) } }, state::endGesture)
                BmSliderRow(uiText("glow_strength"), pct(layer.glowOpacity), layer.glowOpacity, 0f..0.6f, { v -> state.liveLayer(index) { it.copy(glowOpacity = v) } }, state::endGesture)
            }
            else -> {
                BmSliderRow(uiText("shadow_spread"), f1(layer.shadow), layer.shadow, 0f..32f, { v -> state.liveLayer(index) { it.copy(shadow = v) } }, state::endGesture)
                BmSliderRow(uiText("shadow_opacity"), pct(layer.shadowOpacity), layer.shadowOpacity, 0f..0.5f, { v -> state.liveLayer(index) { it.copy(shadowOpacity = v) } }, state::endGesture)
                BmSliderRow(uiText("shadow_offset"), f1(layer.shadowYOffset), layer.shadowYOffset, -8f..16f, { v -> state.liveLayer(index) { it.copy(shadowYOffset = v) } }, state::endGesture)
                RotationRow(layer.rotation, { v -> state.liveLayer(index) { it.copy(rotation = v) } }, state::endGesture) { state.updateLayer(index) { it.copy(rotation = 0f) } }
            }
        }
    }
}

@Composable
private fun RotationRow(value: Float, onChange: (Float) -> Unit, onFinished: () -> Unit, onReset: () -> Unit) {
    var v = value % 360f; if (v > 180f) v -= 360f; if (v < -180f) v += 360f
    Row(verticalAlignment = Alignment.Bottom) {
        Box(Modifier.weight(1f)) { BmSliderRow(uiText("rotation"), deg(v), v, -180f..180f, { onChange(Math.round(it).toFloat()) }, onFinished) }
        BmIconButton(BmIcons.Rotate, uiText("reset_rotation"), onReset, size = 40.dp, iconSize = 18.dp, enabled = v != 0f)
    }
}

/* ------------------------------------------------------------------ City */

@Composable
private fun CityInspector(state: EditorState, index: Int, onSheet: (EditorSheet) -> Unit) {
    val city = state.city(index) ?: return
    val lang = LocalBestMatesLanguage.current
    val c = Bm.colors
    Tabs(listOf(uiText("tab_city"), uiText("tab_type"), uiText("tab_colour"), uiText("tab_sky"))) { tab ->
        when (tab) {
            0 -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BmTextField(city.nickname, { v -> state.liveCity(index) { it.copy(nickname = v.take(30)) } }, Modifier.weight(1f), placeholder = city.name, onDone = state::endGesture)
                    Spacer(Modifier.width(8.dp))
                    BmButton(uiText("change"), { state.endGesture(); onSheet(EditorSheet.ReplaceCity(index)) }, icon = BmIcons.Swap, style = BmButtonStyle.Secondary, size = BmButtonSize.Small)
                }
                BmSliderRow(uiText("size"), pct(city.scale), city.scale, .35f..3f, { v -> state.liveCity(index) { it.copy(scale = v) } }, state::endGesture)
                ToggleGrid(
                    listOf(
                        Triple(uiText("show_time"), city.showTime) { v: Boolean -> state.updateCity(index) { it.copy(showTime = v) } },
                        Triple(uiText("show_date"), city.showDate) { v: Boolean -> state.updateCity(index) { it.copy(showDate = v) } },
                        Triple(uiText("show_solar"), city.showSolar) { v: Boolean -> state.updateCity(index) { it.copy(showSolar = v) } },
                        Triple(uiText("show_country"), city.showCountry) { v: Boolean -> state.updateCity(index) { it.copy(showCountry = v) } },
                    ),
                )
                RotationRow(city.rotation, { v -> state.liveCity(index) { it.copy(rotation = v) } }, state::endGesture) { state.updateCity(index) { it.copy(rotation = 0f) } }
            }
            1 -> {
                val nowSample = remember(city.zoneId) {
                    runCatching { java.time.ZonedDateTime.now(java.time.ZoneId.of(city.zoneId)).format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) }.getOrDefault("12:34")
                }
                FontRow(uiText("font_name"), city.nameFont, city.nickname.ifBlank { city.name }) {
                    onSheet(EditorSheet.Font(translated(lang, "font_name"), city.nameFont, city.nickname.ifBlank { city.name }) { id -> state.updateCity(index) { it.copy(nameFont = id) } })
                }
                // Time and date stay live on the home screen, so they offer live (system) styles.
                val timeFont = WidgetFonts.live(city.timeFont).id
                val dateFont = WidgetFonts.live(city.dateFont).id
                FontRow(uiText("font_time"), timeFont, nowSample) {
                    onSheet(EditorSheet.Font(translated(lang, "font_time"), timeFont, nowSample, liveOnly = true) { id -> state.updateCity(index) { it.copy(timeFont = id) } })
                }
                val dateSample = remember(city.zoneId) {
                    runCatching { java.time.ZonedDateTime.now(java.time.ZoneId.of(city.zoneId)).format(java.time.format.DateTimeFormatter.ofPattern("EEE, MMM d")) }.getOrDefault("Mon, Jan 1")
                }
                FontRow(uiText("font_date"), dateFont, dateSample) {
                    onSheet(EditorSheet.Font(translated(lang, "font_date"), dateFont, dateSample, liveOnly = true) { id -> state.updateCity(index) { it.copy(dateFont = id) } })
                }
                BmButton(uiText("same_type_all"), {
                    state.updateConfig { cfg -> cfg.copy(cities = cfg.cities.map { it.copy(nameFont = city.nameFont, timeFont = city.timeFont, dateFont = city.dateFont) }) }
                }, style = BmButtonStyle.Ghost, size = BmButtonSize.Small, icon = BmIcons.Sparkle)
            }
            2 -> {
                ColorRow(uiText("colour_name"), city.nameColor, { v -> state.updateCity(index) { it.copy(nameColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "colour_name"), city.nameColor, true) { v -> state.liveCity(index) { it.copy(nameColor = v) } })
                }
                ColorRow(uiText("colour_time"), city.timeColor, { v -> state.updateCity(index) { it.copy(timeColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "colour_time"), city.timeColor, true) { v -> state.liveCity(index) { it.copy(timeColor = v) } })
                }
                ColorRow(uiText("colour_date"), city.dateColor, { v -> state.updateCity(index) { it.copy(dateColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "colour_date"), city.dateColor, true) { v -> state.liveCity(index) { it.copy(dateColor = v) } })
                }
            }
            else -> {
                Text(uiText("sun_label"), style = Bm.type.bodySmall, color = c.inkSoft)
                SolarLibrary(true, city.sunStyle, city.moonStyle, city.sunColor, { s -> state.updateCity(index) { it.copy(sunStyle = s) } }, {})
                ColorRow(uiText("sun_colour"), city.sunColor, { v -> state.updateCity(index) { it.copy(sunColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "sun_colour"), city.sunColor, true) { v -> state.liveCity(index) { it.copy(sunColor = v) } })
                }
                Text(uiText("moon_label"), style = Bm.type.bodySmall, color = c.inkSoft)
                SolarLibrary(false, city.sunStyle, city.moonStyle, city.moonColor, {}, { m -> state.updateCity(index) { it.copy(moonStyle = m) } })
                ColorRow(uiText("moon_colour"), city.moonColor, { v -> state.updateCity(index) { it.copy(moonColor = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "moon_colour"), city.moonColor, true) { v -> state.liveCity(index) { it.copy(moonColor = v) } })
                }
            }
        }
    }
}

@Composable
private fun ToggleGrid(items: List<Triple<String, Boolean, (Boolean) -> Unit>>) {
    val c = Bm.colors
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { (label, checked, onChange) ->
                    Row(
                        Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(c.surfaceSunken)
                            .clickable(role = Role.Switch) { onChange(!checked) }.padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(label, style = Bm.type.caption, color = c.ink, modifier = Modifier.weight(1f), maxLines = 2)
                        BmSwitch(checked, onChange)
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FontRow(label: String, fontId: String, sample: String, onClick: () -> Unit) {
    val c = Bm.colors
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.surfaceSunken).clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(label + " · " + WidgetFonts.resolve(fontId).label, style = Bm.type.caption, color = c.inkSoft)
            Text(sample, fontFamily = rememberWidgetFontFamily(fontId), fontSize = 22.sp, lineHeight = 28.sp, color = c.ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Icon(BmIcons.ChevronRight, null, tint = c.inkFaint, modifier = Modifier.size(18.dp))
    }
}

/* ------------------------------------------------------------------ Text */

@Composable
private fun TextInspector(state: EditorState, index: Int, onSheet: (EditorSheet) -> Unit) {
    val item = state.text(index) ?: return
    val lang = LocalBestMatesLanguage.current
    val c = Bm.colors
    Tabs(listOf(uiText("tab_text"), uiText("tab_font"), uiText("tab_colour"))) { tab ->
        when (tab) {
            0 -> {
                BmTextField(item.content, { v -> state.liveText(index) { it.copy(content = v.take(80)) } }, Modifier.fillMaxWidth(), placeholder = uiText("text_content"), singleLine = false, onDone = state::endGesture)
                BmSliderRow(uiText("size"), "${item.size.roundToInt()} pt", item.size, 6f..120f, { v -> state.liveText(index) { it.copy(size = v) } }, state::endGesture)
                RotationRow(item.rotation, { v -> state.liveText(index) { it.copy(rotation = v) } }, state::endGesture) { state.updateText(index) { it.copy(rotation = 0f) } }
            }
            1 -> {
                val fontScroll = rememberScrollState()
                val density = androidx.compose.ui.platform.LocalDensity.current
                val current = WidgetFonts.all.indexOfFirst { it.id == WidgetFonts.canonicalId(item.font) }
                androidx.compose.runtime.LaunchedEffect(Unit) {
                    if (current > 1) fontScroll.scrollTo(with(density) { (100.dp * (current - 1)).roundToPx() })
                }
                Row(Modifier.fillMaxWidth().horizontalScroll(fontScroll), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    WidgetFonts.all.forEach { font ->
                        val selected = WidgetFonts.canonicalId(item.font) == font.id
                        Column(
                            Modifier.width(92.dp).clip(RoundedCornerShape(16.dp)).background(if (selected) c.ink else c.surfaceSunken)
                                .clickable(role = Role.RadioButton) { state.updateText(index) { it.copy(font = font.id) } }.padding(10.dp),
                        ) {
                            Text("Aa", fontFamily = rememberWidgetFontFamily(font.id), fontSize = 28.sp, lineHeight = 34.sp, color = if (selected) c.paper else c.ink)
                            Text(font.label, style = Bm.type.caption, color = if (selected) c.paper.copy(alpha = .75f) else c.inkSoft, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
                BmButton(uiText("all_fonts"), {
                    onSheet(EditorSheet.Font(translated(lang, "tab_font"), item.font, item.content.lineSequence().first().ifBlank { "Aa" }) { id -> state.updateText(index) { it.copy(font = id) } })
                }, style = BmButtonStyle.Secondary, size = BmButtonSize.Small, icon = BmIcons.Type)
            }
            else -> {
                ColorRow(uiText("text_color"), item.color, { v -> state.updateText(index) { it.copy(color = v) } }) {
                    onSheet(EditorSheet.Colour(translated(lang, "text_color"), item.color, false) { v -> state.liveText(index) { it.copy(color = v) } })
                }
                BmSliderRow(uiText("opacity_label"), pct(item.alpha), item.alpha, 0f..1f, { v -> state.liveText(index) { it.copy(alpha = v) } }, state::endGesture)
            }
        }
    }
}
