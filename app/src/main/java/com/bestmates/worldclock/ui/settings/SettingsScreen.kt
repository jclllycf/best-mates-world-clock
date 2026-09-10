package com.bestmates.worldclock.ui.settings

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.bestmates.worldclock.ui.components.CrystalLevel
import com.bestmates.worldclock.ui.components.CrystalPanel
import com.bestmates.worldclock.ui.components.OrbitButton
import com.bestmates.worldclock.ui.components.OrbitButtonStyle
import com.bestmates.worldclock.ui.components.OrbitChoiceChip
import com.bestmates.worldclock.ui.components.OrbitIconButton
import com.bestmates.worldclock.ui.components.crystalSurface
import com.bestmates.worldclock.ui.icons.OrbitIcons
import com.bestmates.worldclock.ui.theme.BestMatesDesign
import java.io.File

data class SettingsStrings(
    val title: String,
    val back: String,
    val identity: String,
    val themeDescriptions: Map<AppThemeStyle, String>,
    val appearance: String,
    val appearanceNote: String,
    val followSystem: String,
    val light: String,
    val dark: String,
    val displayLanguage: String,
    val resourceImport: String,
    val importFontTemplate: String,
    val importIconTemplate: String,
    val notImported: String,
    val importNote: String,
    val selected: String,
    val about: String,
    val versionTemplate: String,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    strings: SettingsStrings,
    versionName: String,
    currentTheme: AppThemeStyle,
    currentAppearance: AppAppearanceMode?,
    onThemeChange: (AppThemeStyle) -> Unit,
    onAppearanceChange: (AppAppearanceMode) -> Unit,
    currentLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onBack: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = remember {
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    }
    var fontName by remember { mutableStateOf(preferences.getString("font_name", "").orEmpty()) }
    var iconName by remember { mutableStateOf(preferences.getString("icon_name", "").orEmpty()) }

    fun import(uri: Uri?, type: String) {
        if (uri == null) return
        runCatching {
            val target = File(context.filesDir, "imports/$type-${System.currentTimeMillis()}")
            target.parentFile?.mkdirs()
            context.contentResolver.openInputStream(uri)?.use { input ->
                target.outputStream().use(input::copyTo)
            }
            val name = uri.lastPathSegment ?: target.name
            preferences.edit().putString("${type}_name", name).apply()
            if (type == "font") fontName = name else iconName = name
        }
    }

    val fontPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        import(it, "font")
    }
    val iconPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) {
        import(it, "icon")
    }
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val resolvedAppearance = currentAppearance ?: legacyAppearanceFor(currentTheme)

    BackHandler(onBack = onBack)
    Box(
        Modifier
            .fillMaxSize()
            .background(colors.background)
            .background(
                Brush.linearGradient(
                    listOf(
                        colors.atmosphereA.copy(alpha = if (colors.isDark) .12f else .22f),
                        Color.Transparent,
                        colors.atmosphereB.copy(alpha = if (colors.isDark) .10f else .18f),
                    ),
                ),
            ),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(
                start = dimensions.shared.compactScreenGutter,
                end = dimensions.shared.compactScreenGutter,
                top = dimensions.spacing.space3,
                bottom = dimensions.spacing.space8,
            ),
            verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space5),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OrbitIconButton(
                        icon = OrbitIcons.Back,
                        contentDescription = strings.back,
                        onClick = onBack,
                    )
                    Text(
                        text = strings.title,
                        modifier = Modifier.padding(start = dimensions.spacing.space4),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colors.textPrimary,
                    )
                }
            }

            item { SettingsSectionHeading(strings.identity) }
            items(AppThemeStyle.entries.size) { index ->
                val style = AppThemeStyle.entries[index]
                ThemeIdentityRow(
                    style = style,
                    description = strings.themeDescriptions[style].orEmpty(),
                    selected = style == currentTheme,
                    selectedDescription = strings.selected,
                    onClick = { onThemeChange(style) },
                )
            }

            item {
                CrystalPanel {
                    SettingsSectionHeading(strings.appearance)
                    Text(
                        strings.appearanceNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                    Spacer(Modifier.height(dimensions.spacing.space4))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
                        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
                    ) {
                        OrbitChoiceChip(
                            label = strings.followSystem,
                            selected = resolvedAppearance == AppAppearanceMode.FOLLOW_SYSTEM,
                            selectedDescription = strings.selected,
                            onClick = { onAppearanceChange(AppAppearanceMode.FOLLOW_SYSTEM) },
                        )
                        OrbitChoiceChip(
                            label = strings.light,
                            selected = resolvedAppearance == AppAppearanceMode.LIGHT,
                            selectedDescription = strings.selected,
                            onClick = { onAppearanceChange(AppAppearanceMode.LIGHT) },
                        )
                        OrbitChoiceChip(
                            label = strings.dark,
                            selected = resolvedAppearance == AppAppearanceMode.DARK,
                            selectedDescription = strings.selected,
                            onClick = { onAppearanceChange(AppAppearanceMode.DARK) },
                        )
                    }
                }
            }

            item {
                CrystalPanel {
                    SettingsSectionHeading(strings.displayLanguage)
                    Spacer(Modifier.height(dimensions.spacing.space4))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
                        verticalArrangement = Arrangement.spacedBy(dimensions.spacing.space3),
                    ) {
                        AppLanguage.entries.forEach { language ->
                            OrbitChoiceChip(
                                label = language.menuLabel,
                                selected = currentLanguage == language,
                                selectedDescription = strings.selected,
                                onClick = { onLanguageChange(language) },
                            )
                        }
                    }
                }
            }

            item {
                CrystalPanel {
                    SettingsSectionHeading(strings.resourceImport)
                    Spacer(Modifier.height(dimensions.spacing.space4))
                    OrbitButton(
                        label = strings.importFontTemplate.format(
                            fontName.ifBlank { strings.notImported },
                        ),
                        onClick = {
                            fontPicker.launch(
                                arrayOf("font/ttf", "font/otf", "application/octet-stream"),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = OrbitIcons.Font,
                        style = OrbitButtonStyle.Secondary,
                    )
                    Spacer(Modifier.height(dimensions.spacing.space3))
                    OrbitButton(
                        label = strings.importIconTemplate.format(
                            iconName.ifBlank { strings.notImported },
                        ),
                        onClick = {
                            iconPicker.launch(
                                arrayOf("image/svg+xml", "image/png", "application/zip"),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = OrbitIcons.Sun,
                        style = OrbitButtonStyle.Secondary,
                    )
                    Spacer(Modifier.height(dimensions.spacing.space4))
                    Text(
                        strings.importNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary,
                    )
                }
            }

            item {
                Column(Modifier.padding(horizontal = dimensions.spacing.space2)) {
                    Text(
                        strings.about,
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.textSecondary,
                    )
                    Text(
                        strings.versionTemplate.format(versionName),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted,
                    )
                    Text(
                        "Apache-2.0 · GeoNames city data",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted,
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeading(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = BestMatesDesign.colors.textPrimary,
    )
}

@Composable
private fun ThemeIdentityRow(
    style: AppThemeStyle,
    description: String,
    selected: Boolean,
    selectedDescription: String,
    onClick: () -> Unit,
) {
    val colors = BestMatesDesign.colors
    val dimensions = BestMatesDesign.dimensions
    val shape = RoundedCornerShape(dimensions.radii.radiusXl)
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { this.selected = selected }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .crystalSurface(
                baseColor = if (selected) colors.selectionFill else colors.surfaceRaised,
                shape = shape,
                level = CrystalLevel.MilkCrystal,
                pressed = pressed,
                borderColor = if (selected) colors.selectionOutline else colors.borderSubtle,
                borderWidth = if (selected) BestMatesDesign.states.selectedOutlineWidth else 1.dp,
                intensity = if (selected) .92f else .70f,
            )
            .clip(shape)
            .padding(dimensions.spacing.space5),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // The click target is supplied by the transparent full-row button overlay below.
        val swatch = when (style) {
            AppThemeStyle.CLOUD -> Color(0xFFF2EDF5)
            AppThemeStyle.DREAM -> Color(0xFFF4E6F1)
            AppThemeStyle.MIDNIGHT -> Color(0xFF272230)
            AppThemeStyle.MONO -> Color(0xFFE8E8E8)
        }
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(swatch),
        )
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = dimensions.spacing.space4),
        ) {
            Text(
                style.label,
                style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary,
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (selected) {
            Icon(
                OrbitIcons.Confirm,
                contentDescription = selectedDescription,
                modifier = Modifier.size(BestMatesDesign.states.selectionCheckIconSize),
                tint = colors.selectionContent,
            )
        }
    }
}
