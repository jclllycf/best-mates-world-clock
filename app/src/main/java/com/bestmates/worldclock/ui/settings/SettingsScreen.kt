package com.bestmates.worldclock.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.ui.design.Bm
import com.bestmates.worldclock.ui.design.BmCard
import com.bestmates.worldclock.ui.design.BmDivider
import com.bestmates.worldclock.ui.design.BmIconButton
import com.bestmates.worldclock.ui.design.BmIcons
import com.bestmates.worldclock.ui.design.BmOverline
import com.bestmates.worldclock.ui.design.BmRow
import com.bestmates.worldclock.ui.design.BmSegmented
import com.bestmates.worldclock.ui.design.BmSheet
import com.bestmates.worldclock.ui.design.BmProvide
import com.bestmates.worldclock.ui.design.bmTokens
import com.bestmates.worldclock.ui.i18n.uiText
import com.bestmates.worldclock.ui.workspace.BrandMark

@Composable
fun SettingsScreen(
    versionName: String,
    currentTheme: AppThemeStyle,
    appearance: AppAppearanceMode,
    onThemeChange: (AppThemeStyle) -> Unit,
    onAppearanceChange: (AppAppearanceMode) -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onBack: () -> Unit,
) {
    val c = Bm.colors
    var licences by remember { mutableStateOf(false) }
    BackHandler(onBack = onBack)
    Column(
        Modifier.fillMaxSize().background(c.paper).verticalScroll(rememberScrollState())
            .statusBarsPadding().navigationBarsPadding().padding(horizontal = 16.dp),
    ) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            BmIconButton(BmIcons.Back, uiText("back"), onBack)
        }
        Text(uiText("settings"), style = Bm.type.display, color = c.ink, modifier = Modifier.padding(start = 4.dp, bottom = 20.dp))

        Section(uiText("appearance")) {
            BmSegmented(
                listOf(uiText("follow_system"), uiText("light"), uiText("dark")),
                AppAppearanceMode.entries.indexOf(appearance),
                { onAppearanceChange(AppAppearanceMode.entries[it]) },
                icons = listOf(null, BmIcons.Sun, BmIcons.Moon),
            )
            Spacer(Modifier.height(8.dp))
            Text(uiText("appearance_note"), style = Bm.type.bodySmall, color = c.inkSoft)
        }

        Section(uiText("app_style")) {
            // Each option is a live miniature drawn with that style's own tokens.
            AppThemeStyle.entries.chunked(2).forEachIndexed { row, pair ->
                if (row > 0) Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    pair.forEach { style ->
                        StylePreview(style, style == currentTheme, c.isDark, Modifier.weight(1f)) { onThemeChange(style) }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(uiText("app_style_note"), style = Bm.type.bodySmall, color = c.inkSoft)
        }
        Section(uiText("display_language")) {
            AppLanguage.entries.forEachIndexed { i, language ->
                if (i > 0) BmDivider()
                BmRow(
                    language.menuLabel,
                    onClick = { onLanguageChange(language) },
                    trailing = { if (language == currentLanguage) Icon(BmIcons.Check, null, tint = c.accentInk, modifier = Modifier.size(20.dp)) },
                )
            }
        }

        Section(uiText("widget_library")) {
            BmRow(uiText("library_fonts"), subtitle = String.format(uiText("library_fonts_note"), WidgetFonts.all.size),
                leading = { Icon(BmIcons.Type, null, tint = c.ink, modifier = Modifier.size(22.dp)) })
            BmDivider()
            BmRow(uiText("library_privacy"), subtitle = uiText("library_privacy_note"),
                leading = { Icon(BmIcons.Globe, null, tint = c.ink, modifier = Modifier.size(22.dp)) })
        }

        Section(uiText("about")) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(52.dp).clip(Bm.shapes.tile).background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(c.markTileTop, c.markTileBottom))), contentAlignment = Alignment.Center) {
                    BrandMark(Modifier.size(34.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Best Mates", style = Bm.type.title, color = c.ink)
                    Text(String.format(uiText("version_format"), versionName), style = Bm.type.bodySmall, color = c.inkSoft)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(uiText("about_body"), style = Bm.type.body, color = c.inkSoft)
            Spacer(Modifier.height(6.dp))
            BmRow(uiText("licences"), onClick = { licences = true }, trailing = { Icon(BmIcons.ChevronRight, null, tint = c.inkFaint, modifier = Modifier.size(18.dp)) })
        }
        Spacer(Modifier.height(32.dp))
    }

    if (licences) BmSheet(onDismiss = { licences = false }, title = uiText("licences")) {
        Column(Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp).verticalScroll(rememberScrollState())) {
            listOf(
                "Best Mates" to "Apache License 2.0",
                uiText("licence_daybook_icons") to uiText("licence_daybook_note"),
                uiText("licence_fonts") + " — " + FONT_FAMILIES to uiText("licence_fonts_note"),
                uiText("licence_city_data") to "GeoNames (geonames.org), Creative Commons Attribution 4.0.",
                uiText("licence_classic_icons") to "Lucide (ISC), Tabler, Phosphor, Fluent UI, Iconoir, Meteocons (MIT), Material Design Icons (Apache 2.0).",
                "AndroidSVG" to "Apache License 2.0",
            ).forEach { (name, detail) ->
                Text(name, style = Bm.type.bodyStrong, color = c.ink)
                Text(detail, style = Bm.type.bodySmall, color = c.inkSoft, modifier = Modifier.padding(bottom = 12.dp))
            }
        }
    }
}

/** Proper names of the bundled typefaces (not translated). */
private const val FONT_FAMILIES = "Fraunces, Figtree, Instrument Serif, Space Grotesk, JetBrains Mono, Nunito, Caveat, Bricolage Grotesque, Syne, Playfair Display, Pacifico, Silkscreen, Unbounded, Oswald, Baloo 2, DM Serif Display, Sora, Young Serif"

@Composable
private fun StylePreview(style: AppThemeStyle, selected: Boolean, dark: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val outer = Bm.colors
    val name = uiText("style_${style.key}")
    val tokens = remember(style, dark) { bmTokens(dark, style.style) }
    Column(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .border(if (selected) 2.dp else 1.dp, if (selected) outer.ink else outer.hairline, RoundedCornerShape(20.dp))
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { this.selected = selected },
    ) {
        BmProvide(tokens) {
            val c = Bm.colors
            Column(Modifier.fillMaxWidth().background(c.paper).padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(26.dp).clip(Bm.shapes.control)
                            .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(c.markTileTop, c.markTileBottom))),
                        contentAlignment = Alignment.Center,
                    ) { BrandMark(Modifier.size(17.dp)) }
                    Spacer(Modifier.width(8.dp))
                    Text("Aa", style = Bm.type.headline, color = c.ink, maxLines = 1)
                }
                Spacer(Modifier.height(10.dp))
                Box(Modifier.fillMaxWidth().height(34.dp).clip(Bm.shapes.tile).background(c.surfaceSunken).padding(7.dp)) {
                    Box(Modifier.size(width = 54.dp, height = 20.dp).clip(Bm.shapes.button).background(c.accent))
                    Box(Modifier.align(Alignment.CenterEnd).size(20.dp).clip(CircleShape).background(c.primary))
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().background(if (selected) outer.surfaceSunken else outer.surface).padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(name, style = Bm.type.label, color = outer.ink, modifier = Modifier.weight(1f))
            if (selected) Icon(BmIcons.Check, null, tint = outer.ink, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    BmOverline(title, Modifier.padding(start = 6.dp, bottom = 8.dp))
    BmCard { content() }
    Spacer(Modifier.height(22.dp))
}
