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
import com.bestmates.worldclock.ui.design.bmColors
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

        Section(uiText("accent")) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AppThemeStyle.entries.forEach { style ->
                    val selected = style == currentTheme
                    val swatch = bmColors(c.isDark, style.accent)
                    Column(
                        Modifier.weight(1f).clip(RoundedCornerShape(18.dp))
                            .background(if (selected) c.surfaceSunken else c.surface)
                            .border(if (selected) 1.5.dp else 1.dp, if (selected) c.ink else c.hairline, RoundedCornerShape(18.dp))
                            .clickable(role = Role.RadioButton) { onThemeChange(style) }
                            .semantics { this.selected = selected }
                            .padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(Modifier.size(30.dp).clip(CircleShape).background(swatch.accent), contentAlignment = Alignment.Center) {
                            if (selected) Icon(BmIcons.Check, null, tint = swatch.onAccent, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(uiText("accent_${style.accent.name.lowercase()}"), style = Bm.type.caption, color = c.ink)
                    }
                }
            }
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
                Box(Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(androidx.compose.ui.graphics.Color(0xFF26223A)), contentAlignment = Alignment.Center) {
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
            LICENCES.forEach { (name, detail) ->
                Text(name, style = Bm.type.bodyStrong, color = c.ink)
                Text(detail, style = Bm.type.bodySmall, color = c.inkSoft, modifier = Modifier.padding(bottom = 12.dp))
            }
        }
    }
}

private val LICENCES = listOf(
    "Best Mates" to "Apache License 2.0",
    "Fraunces, Figtree, Instrument Serif, Space Grotesk, JetBrains Mono, Nunito, Caveat, Bricolage Grotesque, Syne, Playfair Display, Pacifico, Silkscreen, Unbounded, Oswald, Baloo 2, DM Serif Display, Sora, Young Serif" to
        "SIL Open Font License 1.1 — full texts are bundled in the app (assets/licenses).",
    "City data" to "GeoNames (geonames.org), Creative Commons Attribution 4.0.",
    "Classic sun and moon sets" to "Lucide (ISC), Tabler, Phosphor, Fluent UI, Iconoir, Meteocons (MIT), Material Design Icons (Apache 2.0).",
    "AndroidSVG" to "Apache License 2.0",
)

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    BmOverline(title, Modifier.padding(start = 6.dp, bottom = 8.dp))
    BmCard { content() }
    Spacer(Modifier.height(22.dp))
}
