package com.bestmates.worldclock

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
import kotlin.math.roundToInt
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

private val LocalBestMatesLanguage = staticCompositionLocalOf { AppLanguage.ENGLISH }

@Composable private fun uiText(key: String): String = translated(LocalBestMatesLanguage.current, key)
@Composable private fun uiFormat(key: String, vararg values: Any): String = String.format(uiText(key), *values)
private fun translated(language: AppLanguage, key: String): String {
    val english = mapOf(
        "workspace" to "Workspace", "workspace_title" to "Best Mates Workspace", "workspace_collection" to "Your worlds", "workspace_collection_count" to "%d little time companions", "selected_count" to "%d selected", "settings" to "Settings", "editor" to "Editor", "back" to "Back", "component" to "Component", "city" to "City", "text" to "Text", "save" to "Save", "save_auto" to "Save · Auto", "auto_save" to "Auto-save", "undo" to "Undo", "redo" to "Redo", "add_home" to "Add to Home", "app_theme" to "App theme", "display_language" to "Display language", "wallpaper" to "Wallpaper preview", "neutral_bg" to "Neutral background", "canvas" to "Canvas", "snap" to "Snap", "guides" to "Guides", "copy" to "Copy", "paste" to "Paste", "pin" to "Pin", "unpin" to "Unpin", "cut" to "Cut", "delete" to "Delete", "deselect" to "Deselect", "apply" to "Apply", "cancel" to "Cancel", "done" to "Done", "close" to "Close", "rename" to "Rename", "create_widget" to "Create widget", "widget_name" to "Widget name", "new_widget" to "New widget", "unnamed" to "Untitled widget", "version" to "Best Mates · v0.3", "empty_workspace" to "No widgets yet\nTap Create widget to begin", "empty_workspace_title" to "A quiet space for your worlds", "empty_workspace_body" to "Create your first time companion when you are ready.", "delete_projects_title" to "Delete selected worlds?", "delete_projects_message" to "This will permanently delete %d selected project(s) from this device.", "backgrounds_cities" to "%d backgrounds · %d cities", "auto_save_on" to "Auto-save on", "edited_locally" to "Edited locally", "selected" to "Selected", "change_city" to "Change city", "selected_cities" to "Selected cities", "no_cities" to "No cities yet. Added cities will appear on the canvas.", "theme" to "Theme", "time_format" to "Time format", "time_24" to "24-hour time (turn off for AM/PM)", "resource_import" to "Resource import", "import_font" to "Import font file · %s", "import_icon" to "Import icon file · %s", "not_imported" to "Not imported", "import_note" to "Imported files stay on this device and appear in the font and icon libraries.", "add_component" to "Component +", "add_city" to "City +", "add_text" to "Text +", "empty_canvas" to "Add a city or text from the top right", "select_hint" to "Tap a background, city, or text on the canvas to edit it. Drag to move and use the corner handle to resize.", "drag_shape_hint" to "Drag directly on the canvas. Use the fine corner handle to resize width and height.", "drag_city_hint" to "Drag the city on the canvas; use the lower-right handle to resize it.", "drag_text_hint" to "Drag the text on the canvas; use the lower-right handle to change its size.", "shape_library" to "Built-in background", "choose" to "Choose", "choose_shape" to "Choose background shape", "choose_new_shape" to "Choose new component shape", "palette" to "Day / night palette", "custom" to "Custom", "custom_palette_note" to "Edit colour and opacity separately", "choose_palette" to "Choose day / night palette", "day_color" to "Day colour", "night_color" to "Night colour", "open_color" to "Open colour wheel and opacity", "brightness" to "Brightness: %d%%", "opacity" to "Opacity: %d%%", "original" to "Original", "new_color" to "New colour", "font" to "Font", "choose_font" to "Choose font", "text_content" to "Text content", "text_color" to "Text colour", "name_country" to "City name and country", "time" to "Time", "date" to "Date", "sun_moon" to "Sun and moon", "city_name_font" to "City-name font", "city_name_color" to "City name and country colour", "time_font" to "Time font", "time_color" to "Time colour", "date_font" to "Date font", "date_color" to "Date colour", "sun_library" to "Sun icon library", "moon_library" to "Moon icon library", "browse_icons" to "Tap to browse icons", "sun_color" to "Sun colour · warm cream by default", "moon_color" to "Moon colour · lavender moonlight by default", "display_name_hint" to "Display name (blank uses the English city name)", "city_name" to "City name", "show_time" to "Show time", "show_date" to "Show date", "show_country" to "Show country", "show_solar" to "Show sun / moon", "tap_edit" to "Tap to edit", "outline_light" to "Outline and glow", "outline_width" to "Outline width", "outline_opacity" to "Outline opacity", "ambient_glow" to "Ambient softness", "glow_strength" to "Glow strength", "depth" to "Floating depth", "shadow_spread" to "Shadow spread", "shadow_opacity" to "Shadow opacity", "shadow_offset" to "Vertical shadow offset", "choose_city" to "Choose city", "search_city" to "Enter city name in English", "city_search_note" to "Only notable matches are shown. If your city is missing, choose it manually.", "continent" to "Continent", "country" to "Country", "please_choose" to "Please choose", "choose_continent" to "Choose continent", "search_continent" to "Search continents", "choose_country" to "Choose country", "search_country" to "Search countries", "choose_or_search_city" to "Choose or search a city", "choose_country_first" to "Choose country first", "choose_continent_first" to "Choose continent first", "search_results" to "%d results", "popular_city_note" to "Popular cities are shown first. Type in English to filter.", "search_city_english" to "Search city in English", "follow_system" to "Follow system", "identity" to "Visual identity", "appearance" to "Appearance", "appearance_note" to "Controls the app interface only. Existing widget appearance remains project-specific.", "about" to "About", "version_format" to "Best Mates v%s", "theme_cloud_description" to "Warm pearl · lavender air", "theme_dream_description" to "Rose mist · periwinkle", "theme_midnight_description" to "Aubergine · soft violet", "theme_mono_description" to "Quiet monochrome"
    )
    val chinese = english + mapOf(
        "workspace" to "工作区", "workspace_title" to "Best Mates 工作区", "workspace_collection" to "我的世界", "workspace_collection_count" to "%d 个时间伙伴", "selected_count" to "已选择 %d 项", "settings" to "应用设置", "editor" to "编辑器", "back" to "返回", "component" to "组件", "city" to "城市", "text" to "文字", "save" to "保存", "save_auto" to "保存 · 自动", "add_home" to "添加到桌面", "app_theme" to "应用主题", "display_language" to "显示语言", "wallpaper" to "壁纸预览", "neutral_bg" to "灰白背景", "canvas" to "画布", "snap" to "吸附", "guides" to "辅助线", "copy" to "复制", "paste" to "粘贴", "pin" to "固定", "unpin" to "取消固定", "cut" to "剪切", "delete" to "删除", "deselect" to "取消选择", "apply" to "应用", "cancel" to "取消", "done" to "完成", "close" to "关闭", "rename" to "重命名", "create_widget" to "创建组件", "widget_name" to "组件名称", "new_widget" to "新组件", "unnamed" to "未命名组件", "empty_workspace" to "还没有组件方案\n点击“创建组件”开始", "empty_workspace_title" to "为你的世界留一片安静空间", "empty_workspace_body" to "准备好时，创建第一个时间伙伴。", "delete_projects_title" to "删除选中的世界？", "delete_projects_message" to "将从此设备永久删除选中的 %d 个项目。", "backgrounds_cities" to "%d 个背景 · %d 个城市", "auto_save_on" to "自动保存已开启", "edited_locally" to "本机编辑", "selected" to "已选择", "change_city" to "更改城市", "selected_cities" to "已选择城市", "no_cities" to "还没有选择城市。添加后会显示在上方画布中。", "theme" to "主题", "time_format" to "时间格式", "time_24" to "24 小时制（关闭后显示 AM/PM）", "resource_import" to "资源导入", "import_font" to "导入字体文件 · %s", "import_icon" to "导入 Icon 文件 · %s", "not_imported" to "未导入", "import_note" to "导入的文件仅保存在本机，并会显示在字体和日月图标库中。", "add_component" to "组件＋", "add_city" to "城市＋", "add_text" to "文字＋", "empty_canvas" to "从右上角添加城市或文字", "select_hint" to "点击画布中的背景、城市或文字即可编辑；拖动可移动，右下角手柄可缩放。", "drag_shape_hint" to "可直接在画布拖动；拖动右下角细线手柄可自由调整宽高。", "drag_city_hint" to "请直接在画布拖动城市；拖动右下角手柄可调整整体大小。", "drag_text_hint" to "请直接在画布拖动文字；拖动右下角手柄可调整字号。", "shape_library" to "内置背景", "choose" to "选择", "choose_shape" to "选择内置背景", "choose_new_shape" to "选择新组件形状", "palette" to "昼夜配色", "custom" to "自定义", "custom_palette_note" to "分别编辑颜色与透明度", "choose_palette" to "选择昼夜配色", "day_color" to "白天颜色", "night_color" to "夜间颜色", "open_color" to "点击打开色轮与透明度", "brightness" to "亮度：%d%%", "opacity" to "透明度：%d%%", "original" to "原色", "new_color" to "新颜色", "font" to "字体", "choose_font" to "选择字体", "text_content" to "文字内容", "text_color" to "文字颜色", "name_country" to "城市名称与国家", "time" to "时间", "date" to "日期", "sun_moon" to "太阳与月亮", "city_name_font" to "城市名称字体", "city_name_color" to "城市名称与国家颜色", "time_font" to "时间字体", "time_color" to "时间颜色", "date_font" to "日期字体", "date_color" to "日期颜色", "sun_library" to "太阳图标库", "moon_library" to "月亮图标库", "browse_icons" to "点击浏览图标", "sun_color" to "太阳颜色 · 默认淡暖奶油色", "moon_color" to "月亮颜色 · 默认浅紫薰衣草色", "display_name_hint" to "显示名称（留空使用城市英文名）", "city_name" to "城市名称", "show_time" to "显示时间", "show_date" to "显示日期", "show_country" to "显示国家", "show_solar" to "显示日月", "tap_edit" to "点击编辑", "outline_light" to "轮廓与光感", "outline_width" to "轮廓宽度", "outline_opacity" to "轮廓通透度", "ambient_glow" to "环境柔光", "glow_strength" to "柔光强度", "depth" to "悬浮层次", "shadow_spread" to "阴影扩散", "shadow_opacity" to "阴影浓度", "shadow_offset" to "阴影纵向偏移", "choose_city" to "选择城市", "search_city" to "请输入城市英文名", "city_search_note" to "仅优先显示部分知名城市；如未显示您的城市请手动选择。", "continent" to "大洲", "country" to "国家", "please_choose" to "请选择", "choose_continent" to "选择大洲", "search_continent" to "搜索大洲", "choose_country" to "选择国家", "search_country" to "搜索国家", "choose_or_search_city" to "选择或搜索城市", "choose_country_first" to "请先选择国家", "choose_continent_first" to "请先选择大洲", "search_results" to "搜索结果 %d 个", "popular_city_note" to "默认显示人口较多的城市，可输入英文继续筛选", "search_city_english" to "搜索城市英文名", "follow_system" to "跟随系统", "identity" to "视觉主题", "appearance" to "明暗外观", "appearance_note" to "仅控制应用界面；已有桌面组件仍保留各项目自己的外观。", "about" to "关于", "version_format" to "Best Mates v%s", "theme_cloud_description" to "暖珍珠白 · 薰衣草空气", "theme_dream_description" to "玫瑰薄雾 · 长春花蓝", "theme_midnight_description" to "茄紫夜色 · 柔和紫光", "theme_mono_description" to "安静单色"
    )
    val translated = when (language) {
        AppLanguage.CHINESE -> chinese
        AppLanguage.GERMAN -> english + mapOf("workspace" to "Arbeitsbereich", "workspace_title" to "Best Mates Arbeitsbereich", "settings" to "Einstellungen", "editor" to "Editor", "back" to "Zurück", "save" to "Speichern", "save_auto" to "Speichern · automatisch", "auto_save" to "Automatisch speichern", "undo" to "Rückgängig", "redo" to "Wiederholen", "delete" to "Löschen", "copy" to "Kopieren", "paste" to "Einfügen", "cut" to "Ausschneiden", "deselect" to "Auswahl aufheben", "city" to "Stadt", "text" to "Text", "component" to "Element", "add_component" to "Element hinzufügen", "add_city" to "Stadt hinzufügen", "add_text" to "Text hinzufügen", "apply" to "Anwenden", "cancel" to "Abbrechen", "done" to "Fertig", "create_widget" to "Widget erstellen", "rename" to "Umbenennen", "widget_name" to "Widget-Name", "add_home" to "Zum Startbildschirm", "selected_cities" to "Ausgewählte Städte", "theme" to "Design", "time_format" to "Zeitformat", "choose_city" to "Stadt auswählen", "choose_shape" to "Hintergrundform auswählen", "font" to "Schrift", "choose_font" to "Schrift auswählen", "show_time" to "Zeit anzeigen", "show_date" to "Datum anzeigen", "show_country" to "Land anzeigen", "show_solar" to "Sonne/Mond anzeigen")
        AppLanguage.RUSSIAN -> english + mapOf("workspace" to "Рабочая область", "workspace_title" to "Рабочая область Best Mates", "settings" to "Настройки", "editor" to "Редактор", "back" to "Назад", "save" to "Сохранить", "save_auto" to "Сохранить · автоматически", "auto_save" to "Автосохранение", "undo" to "Отменить", "redo" to "Повторить", "delete" to "Удалить", "copy" to "Копировать", "paste" to "Вставить", "cut" to "Вырезать", "deselect" to "Снять выделение", "city" to "Город", "text" to "Текст", "component" to "Компонент", "add_component" to "Добавить компонент", "add_city" to "Добавить город", "add_text" to "Добавить текст", "apply" to "Применить", "cancel" to "Отмена", "done" to "Готово", "create_widget" to "Создать виджет", "rename" to "Переименовать", "widget_name" to "Название виджета", "add_home" to "Добавить на экран", "selected_cities" to "Выбранные города", "theme" to "Тема", "time_format" to "Формат времени", "choose_city" to "Выбрать город", "choose_shape" to "Выбрать форму фона", "font" to "Шрифт", "choose_font" to "Выбрать шрифт", "show_time" to "Показывать время", "show_date" to "Показывать дату", "show_country" to "Показывать страну", "show_solar" to "Показывать солнце/луну")
        AppLanguage.TURKISH -> english + mapOf("workspace" to "Çalışma alanı", "workspace_title" to "Best Mates Çalışma Alanı", "settings" to "Ayarlar", "editor" to "Düzenleyici", "back" to "Geri", "save" to "Kaydet", "save_auto" to "Kaydet · otomatik", "auto_save" to "Otomatik kaydet", "undo" to "Geri al", "redo" to "Yinele", "delete" to "Sil", "copy" to "Kopyala", "paste" to "Yapıştır", "cut" to "Kes", "deselect" to "Seçimi kaldır", "city" to "Şehir", "text" to "Metin", "component" to "Bileşen", "add_component" to "Bileşen ekle", "add_city" to "Şehir ekle", "add_text" to "Metin ekle", "apply" to "Uygula", "cancel" to "İptal", "done" to "Bitti", "create_widget" to "Widget oluştur", "rename" to "Yeniden adlandır", "widget_name" to "Widget adı", "add_home" to "Ana ekrana ekle", "selected_cities" to "Seçilen şehirler", "theme" to "Tema", "time_format" to "Saat biçimi", "choose_city" to "Şehir seç", "choose_shape" to "Arka plan şekli seç", "font" to "Yazı tipi", "choose_font" to "Yazı tipi seç", "show_time" to "Saati göster", "show_date" to "Tarihi göster", "show_country" to "Ülkeyi göster", "show_solar" to "Güneşi/ayı göster")
        AppLanguage.ARABIC -> english + mapOf(
            "workspace" to "مساحة العمل", "workspace_title" to "مساحة عمل Best Mates", "settings" to "الإعدادات", "editor" to "المحرر", "back" to "رجوع", "save" to "حفظ", "save_auto" to "حفظ · تلقائي", "auto_save" to "الحفظ التلقائي", "undo" to "تراجع", "redo" to "إعادة", "delete" to "حذف", "copy" to "نسخ", "paste" to "لصق", "cut" to "قص", "deselect" to "إلغاء التحديد", "city" to "مدينة", "text" to "نص", "component" to "عنصر", "add_component" to "إضافة عنصر", "add_city" to "إضافة مدينة", "add_text" to "إضافة نص", "apply" to "تطبيق", "cancel" to "إلغاء", "done" to "تم", "close" to "إغلاق", "create_widget" to "إنشاء ويدجت", "rename" to "إعادة تسمية", "widget_name" to "اسم الويدجت", "add_home" to "إضافة إلى الشاشة الرئيسية", "unnamed" to "ويدجت بلا اسم", "empty_workspace" to "لا توجد ويدجت بعد\nاضغط إنشاء ويدجت للبدء", "auto_save_on" to "الحفظ التلقائي مفعّل", "edited_locally" to "تم التعديل محلياً", "selected" to "محدد", "change_city" to "تغيير المدينة", "selected_cities" to "المدن المحددة", "no_cities" to "لا توجد مدن بعد. ستظهر المدن المضافة على اللوحة.", "theme" to "السمة", "time_format" to "تنسيق الوقت", "time_24" to "نظام 24 ساعة (أوقفه لعرض AM/PM)", "add_component" to "إضافة عنصر", "empty_canvas" to "أضف مدينة أو نصاً من أعلى اليمين", "select_hint" to "اضغط على الخلفية أو المدينة أو النص لتحريره. اسحب للتحريك واستخدم المقبض للتكبير.", "drag_shape_hint" to "اسحب العنصر على اللوحة. استخدم المقبض السفلي الأيمن لتغيير العرض والارتفاع.", "drag_city_hint" to "اسحب المدينة على اللوحة، واستخدم المقبض السفلي الأيمن لتغيير حجمها.", "drag_text_hint" to "اسحب النص على اللوحة، واستخدم المقبض السفلي الأيمن لتغيير حجمه.", "shape_library" to "الخلفية المدمجة", "choose" to "اختيار", "choose_shape" to "اختيار شكل الخلفية", "choose_new_shape" to "اختيار شكل عنصر جديد", "palette" to "ألوان النهار والليل", "custom" to "مخصص", "choose_palette" to "اختيار ألوان النهار والليل", "day_color" to "لون النهار", "night_color" to "لون الليل", "font" to "الخط", "choose_font" to "اختيار الخط", "text_content" to "محتوى النص", "text_color" to "لون النص", "name_country" to "اسم المدينة والدولة", "time" to "الوقت", "date" to "التاريخ", "sun_moon" to "الشمس والقمر", "sun_library" to "مكتبة رموز الشمس", "moon_library" to "مكتبة رموز القمر", "browse_icons" to "اضغط لتصفح الرموز", "city_name" to "اسم المدينة", "show_time" to "إظهار الوقت", "show_date" to "إظهار التاريخ", "show_country" to "إظهار الدولة", "show_solar" to "إظهار الشمس والقمر", "tap_edit" to "اضغط للتحرير", "outline_light" to "الإطار والإضاءة", "outline_width" to "عرض الإطار", "outline_opacity" to "شفافية الإطار", "ambient_glow" to "التوهج المحيط", "glow_strength" to "قوة التوهج", "depth" to "العمق", "shadow_spread" to "انتشار الظل", "shadow_opacity" to "شفافية الظل", "shadow_offset" to "إزاحة الظل", "choose_city" to "اختيار مدينة", "search_city" to "اكتب اسم المدينة بالإنجليزية", "continent" to "القارة", "country" to "الدولة", "please_choose" to "يرجى الاختيار", "choose_continent" to "اختيار القارة", "choose_country" to "اختيار الدولة", "choose_or_search_city" to "اختيار أو البحث عن مدينة", "choose_country_first" to "اختر الدولة أولاً", "choose_continent_first" to "اختر القارة أولاً", "search_results" to "نتائج البحث: %d", "popular_city_note" to "تظهر المدن الأشهر أولاً. اكتب بالإنجليزية للتصفية."
        )
        AppLanguage.FRENCH -> english + mapOf("workspace" to "Espace de travail", "workspace_title" to "Espace Best Mates", "settings" to "Réglages", "editor" to "Éditeur", "back" to "Retour", "save" to "Enregistrer", "save_auto" to "Enregistrer · automatique", "auto_save" to "Enregistrement automatique", "undo" to "Annuler", "redo" to "Rétablir", "delete" to "Supprimer", "copy" to "Copier", "paste" to "Coller", "cut" to "Couper", "deselect" to "Désélectionner", "city" to "Ville", "text" to "Texte", "component" to "Élément", "add_component" to "Ajouter un élément", "add_city" to "Ajouter une ville", "add_text" to "Ajouter du texte", "apply" to "Appliquer", "cancel" to "Annuler", "done" to "Terminé", "create_widget" to "Créer un widget", "rename" to "Renommer", "widget_name" to "Nom du widget", "add_home" to "Ajouter à l’accueil", "selected_cities" to "Villes sélectionnées", "theme" to "Thème", "time_format" to "Format de l’heure", "choose_city" to "Choisir une ville", "choose_shape" to "Choisir une forme d’arrière-plan", "font" to "Police", "choose_font" to "Choisir une police", "show_time" to "Afficher l’heure", "show_date" to "Afficher la date", "show_country" to "Afficher le pays", "show_solar" to "Afficher le soleil/la lune")
        else -> english
    }
    return editorLanguageOverride(language, key) ?: completeLanguageOverride(language, key) ?: translated[key] ?: english[key] ?: key
}

/* These are the short, frequently-visible editor strings.  Keeping them in one
 * override prevents a partially translated screen when a new UI control is
 * added to the canvas editor. */
private fun editorLanguageOverride(language: AppLanguage, key: String): String? {
    val labels = when (language) {
        AppLanguage.GERMAN -> mapOf(
            "editor" to "Bearbeiten", "follow_system" to "System verwenden",
            "select_hint" to "Tippe auf einen Hintergrund, eine Stadt oder einen Text auf der Leinwand, um ihn zu bearbeiten. Ziehe zum Verschieben und nutze den Eckgriff zum Skalieren.",
            "drag_shape_hint" to "Ziehe direkt auf der Leinwand. Der feine Eckgriff passt Breite und Höhe an.",
            "drag_city_hint" to "Ziehe die Stadt auf der Leinwand; der Griff unten rechts ändert ihre Größe.",
            "drag_text_hint" to "Ziehe den Text auf der Leinwand; der Griff unten rechts ändert seine Größe.",
            "time_format" to "Zeitformat", "time_24" to "24-Stunden-Zeit (ausschalten für AM/PM)",
            "theme" to "Erscheinungsbild", "shape_library" to "Hintergrundform",
            "choose" to "Auswählen", "palette" to "Tag-/Nachtpalette", "custom" to "Benutzerdefiniert",
            "day_color" to "Tagesfarbe", "night_color" to "Nachtfarbe", "text_content" to "Textinhalt",
            "text_color" to "Textfarbe", "sun_moon" to "Sonne und Mond", "tap_edit" to "Zum Bearbeiten tippen",
            "version" to "Best Mates · v0.3", "theme_cloud_description" to "Warmes Weiß · Lavendel",
            "theme_dream_description" to "Rosiger Dunst · Blauviolett", "theme_midnight_description" to "Blaugrau · weiches Violett",
            "theme_mono_description" to "Ruhiges Monochrom"
        )
        AppLanguage.RUSSIAN -> mapOf(
            "editor" to "Редактор", "follow_system" to "Как в системе",
            "select_hint" to "Нажмите фон, город или текст на холсте для редактирования. Перетаскивайте для перемещения и используйте угловой маркер для изменения размера.",
            "drag_shape_hint" to "Перетаскивайте прямо на холсте. Угловой маркер меняет ширину и высоту.",
            "drag_city_hint" to "Перетаскивайте город на холсте; нижний правый маркер меняет размер.",
            "drag_text_hint" to "Перетаскивайте текст на холсте; нижний правый маркер меняет размер.",
            "time_format" to "Формат времени", "time_24" to "24-часовой формат (выключите для AM/PM)",
            "theme" to "Оформление", "shape_library" to "Форма фона", "choose" to "Выбрать",
            "palette" to "Палитра дня и ночи", "custom" to "Свой вариант", "day_color" to "Цвет дня",
            "night_color" to "Цвет ночи", "text_content" to "Текст", "text_color" to "Цвет текста",
            "sun_moon" to "Солнце и луна", "tap_edit" to "Нажмите для настройки", "version" to "Best Mates · v0.3"
        )
        AppLanguage.TURKISH -> mapOf(
            "editor" to "Düzenleyici", "follow_system" to "Sistemi izle",
            "select_hint" to "Düzenlemek için tuvalde bir arka plana, şehre veya metne dokunun. Taşımak için sürükleyin, boyutlandırmak için köşe tutamacını kullanın.",
            "drag_shape_hint" to "Doğrudan tuval üzerinde sürükleyin. Köşe tutamacı genişliği ve yüksekliği değiştirir.",
            "drag_city_hint" to "Şehri tuvalde sürükleyin; sağ alt tutamaç boyutu değiştirir.",
            "drag_text_hint" to "Metni tuvalde sürükleyin; sağ alt tutamaç boyutu değiştirir.",
            "time_format" to "Saat biçimi", "time_24" to "24 saat biçimi (AM/PM için kapatın)",
            "theme" to "Görünüm", "shape_library" to "Arka plan şekli", "choose" to "Seç",
            "palette" to "Gündüz/gece paleti", "custom" to "Özel", "day_color" to "Gündüz rengi",
            "night_color" to "Gece rengi", "text_content" to "Metin içeriği", "text_color" to "Metin rengi",
            "sun_moon" to "Güneş ve ay", "tap_edit" to "Düzenlemek için dokunun", "version" to "Best Mates · v0.3"
        )
        AppLanguage.ARABIC -> mapOf(
            "editor" to "المحرر", "follow_system" to "اتباع النظام",
            "select_hint" to "اضغط على الخلفية أو المدينة أو النص في اللوحة لتحريره. اسحب للنقل واستخدم مقبض الزاوية لتغيير الحجم.",
            "drag_shape_hint" to "اسحب مباشرة على اللوحة. يغيّر مقبض الزاوية العرض والارتفاع.",
            "drag_city_hint" to "اسحب المدينة على اللوحة؛ يغيّر المقبض السفلي الأيمن حجمها.",
            "drag_text_hint" to "اسحب النص على اللوحة؛ يغيّر المقبض السفلي الأيمن حجمه.",
            "time_format" to "تنسيق الوقت", "time_24" to "تنسيق 24 ساعة (أوقفه لعرض AM/PM)",
            "theme" to "المظهر", "shape_library" to "شكل الخلفية", "choose" to "اختيار",
            "palette" to "ألوان النهار والليل", "custom" to "مخصص", "day_color" to "لون النهار",
            "night_color" to "لون الليل", "text_content" to "محتوى النص", "text_color" to "لون النص",
            "sun_moon" to "الشمس والقمر", "tap_edit" to "اضغط للتحرير", "version" to "Best Mates · v0.3"
        )
        AppLanguage.FRENCH -> mapOf(
            "editor" to "Éditeur", "follow_system" to "Suivre le système",
            "select_hint" to "Touchez un fond, une ville ou un texte sur la toile pour le modifier. Faites glisser pour déplacer et utilisez la poignée d’angle pour redimensionner.",
            "drag_shape_hint" to "Faites glisser directement sur la toile. La poignée d’angle ajuste largeur et hauteur.",
            "drag_city_hint" to "Faites glisser la ville sur la toile ; la poignée inférieure droite change sa taille.",
            "drag_text_hint" to "Faites glisser le texte sur la toile ; la poignée inférieure droite change sa taille.",
            "time_format" to "Format de l’heure", "time_24" to "Format 24 heures (désactivez pour AM/PM)",
            "theme" to "Apparence", "shape_library" to "Forme d’arrière-plan", "choose" to "Choisir",
            "palette" to "Palette jour / nuit", "custom" to "Personnalisé", "day_color" to "Couleur du jour",
            "night_color" to "Couleur de nuit", "text_content" to "Contenu du texte", "text_color" to "Couleur du texte",
            "sun_moon" to "Soleil et lune", "tap_edit" to "Touchez pour modifier", "version" to "Best Mates · v0.3"
        )
        else -> emptyMap()
    }
    return labels[key]
}

private fun AppLanguage.dateLocale(): Locale = when (this) {
    AppLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
    AppLanguage.GERMAN -> Locale.GERMAN
    AppLanguage.RUSSIAN -> Locale("ru")
    AppLanguage.TURKISH -> Locale("tr")
    AppLanguage.ARABIC -> Locale("ar")
    AppLanguage.FRENCH -> Locale.FRENCH
    AppLanguage.ENGLISH -> Locale.ENGLISH
}

/** Keeps the persistent editor chrome translated even when a screen uses a newer string key. */
private fun completeLanguageOverride(language: AppLanguage, key: String): String? = when (language) {
    AppLanguage.ENGLISH -> mapOf(
        "light" to "Light",
        "dark" to "Dark",
        "choose_country_first" to "Choose a country first",
        "choose_continent_first" to "Choose a continent first",
    )[key]
    AppLanguage.CHINESE -> mapOf("light" to "浅色", "dark" to "深色")[key]
    AppLanguage.GERMAN -> mapOf(
        "selected_count" to "%d ausgewählt", "app_theme" to "App-Design", "display_language" to "Anzeigesprache",
        "neutral_bg" to "Neutraler Hintergrund", "canvas" to "Leinwand", "snap" to "Einrasten", "guides" to "Hilfslinien",
        "light" to "Hell", "dark" to "Dunkel", "pin" to "Anheften", "unpin" to "Lösen", "close" to "Schließen",
        "new_widget" to "Neues Widget", "unnamed" to "Unbenanntes Widget", "empty_workspace" to "Noch keine Widgets\nTippe auf „Widget erstellen“, um zu beginnen",
        "backgrounds_cities" to "%d Hintergründe · %d Städte", "auto_save_on" to "Automatisches Speichern aktiv",
        "edited_locally" to "Lokal bearbeitet", "selected" to "Ausgewählt", "change_city" to "Stadt ändern",
        "no_cities" to "Noch keine Städte. Hinzugefügte Städte erscheinen auf der Leinwand.",
        "resource_import" to "Ressourcen importieren", "not_imported" to "Nicht importiert", "import_note" to "Importierte Dateien bleiben auf diesem Gerät und erscheinen in den Schrift- und Symbolbibliotheken."
    )[key]
    AppLanguage.RUSSIAN -> mapOf(
        "selected_count" to "Выбрано: %d", "app_theme" to "Тема приложения", "display_language" to "Язык интерфейса",
        "neutral_bg" to "Нейтральный фон", "canvas" to "Холст", "snap" to "Привязка", "guides" to "Направляющие",
        "light" to "Светлая", "dark" to "Тёмная", "pin" to "Закрепить", "unpin" to "Открепить", "close" to "Закрыть",
        "new_widget" to "Новый виджет", "unnamed" to "Безымянный виджет", "empty_workspace" to "Виджетов пока нет\nНажмите «Создать виджет», чтобы начать",
        "backgrounds_cities" to "%d фонов · %d городов", "auto_save_on" to "Автосохранение включено",
        "edited_locally" to "Изменено локально", "selected" to "Выбрано", "change_city" to "Изменить город",
        "no_cities" to "Городов пока нет. Добавленные города появятся на холсте.",
        "resource_import" to "Импорт ресурсов", "not_imported" to "Не импортировано", "import_note" to "Импортированные файлы остаются на этом устройстве и появляются в библиотеках шрифтов и значков."
    )[key]
    AppLanguage.TURKISH -> mapOf(
        "selected_count" to "%d seçildi", "app_theme" to "Uygulama teması", "display_language" to "Görüntüleme dili",
        "neutral_bg" to "Nötr arka plan", "canvas" to "Tuval", "snap" to "Hizala", "guides" to "Kılavuzlar",
        "light" to "Açık", "dark" to "Koyu", "pin" to "Sabitle", "unpin" to "Sabitlemeyi kaldır", "close" to "Kapat",
        "new_widget" to "Yeni widget", "unnamed" to "Adsız widget", "empty_workspace" to "Henüz widget yok\nBaşlamak için Widget oluştur'a dokunun",
        "backgrounds_cities" to "%d arka plan · %d şehir", "auto_save_on" to "Otomatik kaydetme açık",
        "edited_locally" to "Yerelde düzenlendi", "selected" to "Seçildi", "change_city" to "Şehri değiştir",
        "no_cities" to "Henüz şehir yok. Eklenen şehirler tuvalde görünür.",
        "resource_import" to "Kaynak içe aktarma", "not_imported" to "İçe aktarılmadı", "import_note" to "İçe aktarılan dosyalar bu cihazda kalır ve yazı tipi ile simge kitaplıklarında görünür."
    )[key]
    AppLanguage.ARABIC -> mapOf(
        "selected_count" to "تم تحديد %d", "app_theme" to "مظهر التطبيق", "display_language" to "لغة العرض",
        "neutral_bg" to "خلفية محايدة", "canvas" to "لوحة الرسم", "snap" to "محاذاة", "guides" to "خطوط إرشاد",
        "light" to "فاتح", "dark" to "داكن", "pin" to "تثبيت", "unpin" to "إلغاء التثبيت", "close" to "إغلاق",
        "new_widget" to "أداة جديدة", "unnamed" to "أداة بلا اسم", "empty_workspace" to "لا توجد أدوات بعد\nاضغط إنشاء أداة للبدء",
        "backgrounds_cities" to "%d خلفيات · %d مدن", "auto_save_on" to "الحفظ التلقائي مفعّل",
        "edited_locally" to "تم التعديل محليًا", "selected" to "محدد", "change_city" to "تغيير المدينة",
        "no_cities" to "لا توجد مدن بعد. ستظهر المدن المضافة على لوحة الرسم.",
        "resource_import" to "استيراد الموارد", "not_imported" to "لم يتم الاستيراد", "import_note" to "تبقى الملفات المستوردة على هذا الجهاز وتظهر في مكتبات الخطوط والرموز."
    )[key]
    AppLanguage.FRENCH -> mapOf(
        "selected_count" to "%d sélectionnés", "app_theme" to "Thème de l’application", "display_language" to "Langue d’affichage",
        "neutral_bg" to "Fond neutre", "canvas" to "Toile", "snap" to "Magnétisme", "guides" to "Repères",
        "light" to "Clair", "dark" to "Sombre", "pin" to "Épingler", "unpin" to "Désépingler", "close" to "Fermer",
        "new_widget" to "Nouveau widget", "unnamed" to "Widget sans nom", "empty_workspace" to "Aucun widget pour le moment\nTouchez Créer un widget pour commencer",
        "backgrounds_cities" to "%d arrière-plans · %d villes", "auto_save_on" to "Enregistrement automatique activé",
        "edited_locally" to "Modifié localement", "selected" to "Sélectionné", "change_city" to "Changer de ville",
        "no_cities" to "Aucune ville pour le moment. Les villes ajoutées apparaîtront sur la toile.",
        "resource_import" to "Importation de ressources", "not_imported" to "Non importé", "import_note" to "Les fichiers importés restent sur cet appareil et apparaissent dans les bibliothèques de polices et d’icônes."
    )[key]
    else -> null
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) setResult(Activity.RESULT_CANCELED)
        setContent { BestMatesApp(widgetId) }
    }
}

@Composable private fun BestMatesApp(widgetId: Int) {
    val activity = androidx.compose.ui.platform.LocalContext.current as Activity
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

@Suppress("DEPRECATION")
private fun updateSystemBars(activity: Activity, backgroundColor: Int, isDark: Boolean) {
    activity.window.statusBarColor = backgroundColor
    activity.window.navigationBarColor = backgroundColor
    activity.window.decorView.systemUiVisibility = if (isDark) {
        0
    } else {
        android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
            android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
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

private sealed interface CanvasSelection {
    data class ComponentItem(val index: Int) : CanvasSelection
    data class CityItem(val index: Int) : CanvasSelection
    data class TextItem(val index: Int) : CanvasSelection
}
private sealed interface EditorClipboard {
    data class Layer(val value: WidgetLayer) : EditorClipboard
    data class CityValue(val value: City) : EditorClipboard
    data class TextValue(val value: CustomText) : EditorClipboard
}
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
                selection = selection, onSelect = { selection = it }, snapEnabled = snapEnabled, guidesVisible = guidesVisible)

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

private data class FontChoice(val id: String, val label: String, val family: String, val style: Int = android.graphics.Typeface.NORMAL)
private val FONT_CHOICES: List<FontChoice> = buildList {
    addAll(listOf(
        FontChoice("SANS_ROUNDED", "Rounded Sans", "sans-serif-rounded"), FontChoice("SANS", "Clean Sans", "sans-serif"),
        FontChoice("SANS_LIGHT", "Airy Light", "sans-serif-light"), FontChoice("SANS_THIN", "Silk Thin", "sans-serif-thin"),
        FontChoice("SANS_MEDIUM", "Modern Medium", "sans-serif-medium"), FontChoice("SANS_BLACK", "Bold Display", "sans-serif-black"),
        FontChoice("SANS_CONDENSED", "Narrow Sans", "sans-serif-condensed"), FontChoice("CONDENSED_LIGHT", "Narrow Light", "sans-serif-condensed-light"),
        FontChoice("CONDENSED_MEDIUM", "Narrow Medium", "sans-serif-condensed-medium"), FontChoice("SERIF", "Classic Serif", "serif"),
        FontChoice("SERIF_MONO", "Editorial Mono", "serif-monospace"), FontChoice("MONO", "Modern Mono", "monospace"),
        FontChoice("CURSIVE", "Cursive Script", "cursive"), FontChoice("CASUAL", "Cute Casual", "casual"),
        FontChoice("SMALL_CAPS", "Elegant Small Caps", "sans-serif-smallcaps")
    ))
    // Keep only visually distinct system families. The old matrix repeated the
    // same Android aliases with five styles and made the picker look padded but
    // not meaningfully diverse.
    listOf(
        "sans-serif" to "Neutral Sans", "sans-serif-rounded" to "Rounded", "sans-serif-light" to "Thin Air",
        "sans-serif-condensed" to "Condensed", "sans-serif-black" to "Poster", "sans-serif-smallcaps" to "Small Caps",
        "serif" to "Book Serif", "serif-monospace" to "Typewriter Serif", "monospace" to "Terminal",
        "cursive" to "Handwritten", "casual" to "Casual", "sans" to "Legacy Sans"
    ).forEachIndexed { index, (family, label) ->
        add(FontChoice("BM|$family|${android.graphics.Typeface.NORMAL}|$index", label, family))
        add(FontChoice("BM|$family|${android.graphics.Typeface.BOLD}|${index}b", "$label Bold", family, android.graphics.Typeface.BOLD))
    }
}

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
@Composable private fun SolarIconSample(isDay: Boolean, sun: SunIconStyle, moon: MoonIconStyle, color: Long, sizeDp: Float) {
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
private fun LayoutCanvas(
    config: WidgetConfig,
    onCityChange: (Int, City) -> Unit,
    onTextChange: (Int, CustomText) -> Unit,
    onLayerChange: (Int, WidgetLayer) -> Unit,
    onGestureStart: () -> Unit,
    onGestureFinished: () -> Unit,
    selection: CanvasSelection?,
    onSelect: (CanvasSelection?) -> Unit,
    snapEnabled: Boolean,
    guidesVisible: Boolean
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
    var guideAngle by remember { mutableStateOf<Float?>(null) }
    var sizeGuide by remember { mutableStateOf<String?>(null) }
    var sizeGuideTarget by remember { mutableStateOf<CanvasSelection?>(null) }
    val hostView = LocalView.current
    var lastSnapFeedback by remember { mutableStateOf("") }
    LaunchedEffect(guideX, guideY, guideAngle, sizeGuide) {
        val key = "${guideX?.roundToInt()}:${guideY?.roundToInt()}:${guideAngle?.roundToInt()}:${sizeGuide.orEmpty()}"
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
                    guideAngle?.let { angle ->
                        val radians = Math.toRadians(angle.toDouble())
                        val radius = boardBounds.width() * .72f
                        val dx = (cos(radians) * radius).toFloat()
                        val dy = (sin(radians) * radius).toFloat()
                        drawLine(guideColor, Offset(boardBounds.centerX()-dx, boardBounds.centerY()-dy), Offset(boardBounds.centerX()+dx, boardBounds.centerY()+dy), 1.3f)
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
                            guideX = right.guide?.let { boardBounds.left + it * boardBounds.width() }
                            guideY = bottom.guide?.let { boardBounds.top + it * boardBounds.height() }
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
                            guideAngle = snapped.guide
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

            if (config.cities.isEmpty() && config.texts.isEmpty()) Text(uiText("empty_canvas"), Modifier.align(Alignment.Center), color = designColors.onCanvasMat.copy(alpha = .65f), fontSize = 11.sp)
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
                            guideAngle = snapped.guide
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
                                guideAngle = snapped.guide
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

private data class SnapResult(val value: Float, val guide: Float?)

/**
 * Returns the signed angle in degrees from [from] to [to], measured counter-clockwise
 * from the positive X axis (same convention as atan2).
 */
private fun angleBetween(from: Offset, to: Offset): Float =
    Math.toDegrees(atan2((to.y - from.y).toDouble(), (to.x - from.x).toDouble())).toFloat()

/**
 * Continuously unwrap a raw angle delta so that a smooth gesture can exceed ±180°.
 * [rawDelta] is the current raw angle difference (degrees). [prevDelta] is the
 * previous unwrapped delta. Returns the new unwrapped delta.
 */
private fun unwrapAngleDelta(rawDelta: Float, prevDelta: Float): Float {
    var d = rawDelta - prevDelta
    // Fold into (-180, 180] to find the shortest arc this frame.
    while (d > 180f) d -= 360f
    while (d <= -180f) d += 360f
    return prevDelta + d
}

private fun snapCoordinate(value: Float, enabled: Boolean, divisions: Int = 16, extras: List<Float> = emptyList()): SnapResult {
    if (!enabled) return SnapResult(value, null)
    val linked = extras.minByOrNull { kotlin.math.abs(it - value) }
    if (linked != null && kotlin.math.abs(linked - value) <= .035f) return SnapResult(linked.coerceIn(-1f, 1f), linked.coerceIn(-1f, 1f))
    // Normalized coordinates span -1..1. A 16-division grid is therefore 1/8 apart.
    val step = 2f / divisions.coerceAtLeast(1)
    val nearest = ((value + 1f) / step).roundToInt() * step - 1f
    return SnapResult(nearest.coerceIn(-1f, 1f), nearest.coerceIn(-1f, 1f))
}

private fun snapCanvasFraction(value: Float, step: Float, enabled: Boolean, extras: List<Float> = emptyList()): SnapResult {
    if (!enabled) return SnapResult(value, null)
    val linked = extras.minByOrNull { kotlin.math.abs(it - value) }
    if (linked != null && kotlin.math.abs(linked - value) <= .025f) return SnapResult(linked.coerceIn(0f, 1f), linked.coerceIn(0f, 1f))
    val snapped = (value / step).roundToInt() * step
    return SnapResult(snapped.coerceIn(0f, 1f), snapped.coerceIn(0f, 1f))
}

private fun snapRotation(value: Float, enabled: Boolean): SnapResult {
    // Keep the raw continuous angle. Clamping here makes a continuous gesture
    // jump from +180 to -180 and breaks multi-turn rotation.
    val raw = value
    if (!enabled) return SnapResult(raw, null)
    val snapped = (raw / 15f).roundToInt() * 15f
    // Rotation remains fluid; only common design angles acquire a magnetic zone.
    return if (kotlin.math.abs(snapped - raw) <= 3.5f) SnapResult(snapped, snapped) else SnapResult(raw, null)
}

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
        // User-approved Future canvas language artwork; the outer 42dp target and geometry stay intact.
        Box(Modifier.size(26.dp).background(Color(0xEEF9F5FA),RoundedCornerShape(13.dp)).border(.7.dp,Color(0x66836A93),RoundedCornerShape(13.dp)),contentAlignment=Alignment.Center) {
            Icon(OrbitIcons.Rotate,null,tint=Color(0xFF7655AF),modifier=Modifier.size(18.dp))
        }
    }
}

private fun fontFamilyName(value: String): String = FONT_CHOICES.firstOrNull { it.id == value }?.family ?: value.split('|').getOrNull(1) ?: "sans-serif"
private fun fontStyle(value: String): Int = FONT_CHOICES.firstOrNull { it.id == value }?.style ?: value.split('|').getOrNull(2)?.toIntOrNull() ?: android.graphics.Typeface.NORMAL
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
            {},{},selection,{selection=it},true,true)
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(12.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text("Soft inspector · local study",style=MaterialTheme.typography.titleMedium)
            FontField("Time typeface",study.cities.first().timeFont,"18:40") { next -> study=study.copy(cities=study.cities.map { it.copy(timeFont=next) }) }
            EditableDisplayRow("Date",study.cities.first().showDate,{next -> study=study.copy(cities=study.cities.map { it.copy(showDate=next) })},{})
            Text("Current production Heart · neutral work mat · no saved project",style=MaterialTheme.typography.bodySmall)
        }
    }
}

private fun textFont(value: String): FontFamily = FontFamily(android.graphics.Typeface.create(fontFamilyName(value), fontStyle(value)))

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun CityPicker(onDismiss: () -> Unit, onPick: (City) -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val language = LocalBestMatesLanguage.current
    val catalog = remember { CityCatalog(context) }
    var continent by remember { mutableStateOf<String?>(null) }; var country by remember { mutableStateOf<Pair<String,String>?>(null) }; var query by remember { mutableStateOf("") }
    var chooser by remember { mutableStateOf("") }
    var countries by remember { mutableStateOf(emptyList<Pair<String,String>>()) }; var results by remember { mutableStateOf(emptyList<City>()) }
    LaunchedEffect(continent) { countries = continent?.let { withContext(Dispatchers.IO) { catalog.countries(it) } }.orEmpty(); country = null }
    LaunchedEffect(query) { delay(180); results = withContext(Dispatchers.IO) { catalog.searchEnglish(query) } }
    BackHandler { if (chooser.isNotBlank()) chooser = "" else onDismiss() }
    CrystalDialog(onDismissRequest = onDismiss, confirmButton = {}, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("close")) } }, title = { Text(uiText("choose_city")) }, text = { Column(Modifier.heightIn(max = 620.dp)) {
        CrystalTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(uiText("search_city")) }, singleLine = true)
        if (query.length >= 2) { LazyColumn(Modifier.heightIn(max = 300.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { items(results, key = { it.geoId }) { city -> PickerResultRow("${city.name} · ${city.country} · ${city.zoneId}") { onPick(city) } } }; Text(uiText("city_search_note"), style = MaterialTheme.typography.bodySmall) }
        else { ChoiceButton(uiText("continent"), continent?.let { continentName(it, language) } ?: uiText("please_choose")) { chooser = "continent" }; ChoiceButton(uiText("country"), country?.second ?: uiText("choose_continent_first"), continent != null) { chooser = "country" }; ChoiceButton(uiText("city"), if (country == null) uiText("choose_country_first") else uiText("choose_or_search_city"), country != null) { chooser = "city" } }
    } })
    if (chooser == "continent") SimpleChoiceDialog(uiText("choose_continent"), uiText("search_continent"), catalog.continents().map { it to continentName(it, language) }, onDismiss = { chooser = "" }) { continent = it; chooser = "" }
    if (chooser == "country") SimpleChoiceDialog(uiText("choose_country"), uiText("search_country"), countries, onDismiss = { chooser = "" }) { code -> country = code to countries.first { it.first == code }.second; chooser = "" }
    if (chooser == "city" && country != null) CityListDialog(catalog, country!!.first, onDismiss = { chooser = "" }) { onPick(it); chooser = "" }
}

@Composable private fun ChoiceButton(label: String, value: String, enabled: Boolean = true, action: () -> Unit) = OrbitButton(label="$label：$value",onClick=action,enabled=enabled,modifier=Modifier.fillMaxWidth(),style=OrbitButtonStyle.Secondary)
@Composable private fun PickerResultRow(label: String, action: () -> Unit) = CrystalPickerRow(onClick=action){Text(label,Modifier.weight(1f),style=MaterialTheme.typography.bodyMedium)}
@Composable private fun SimpleChoiceDialog(title: String, hint: String, values: List<Pair<String,String>>, onDismiss: () -> Unit, pick: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(values, query) { values.filter { it.second.contains(query, ignoreCase = true) || it.first.contains(query, ignoreCase = true) } }
    CrystalDialog(onDismissRequest = onDismiss, confirmButton = {}, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("back")) } }, title = { Text(title) }, text = { Column {
        CrystalTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(hint) }, singleLine = true)
        LazyColumn(Modifier.heightIn(max=420.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { items(filtered, key={it.first}) { item -> PickerResultRow(item.second){pick(item.first)} } }
    } })
}

@Composable private fun CityListDialog(catalog: CityCatalog, countryCode: String, onDismiss: () -> Unit, pick: (City)->Unit) {
    var query by remember { mutableStateOf("") }
    var cities by remember { mutableStateOf(emptyList<City>()) }
    var loading by remember { mutableStateOf(true) }
    LaunchedEffect(countryCode, query) {
        loading = true; delay(160)
        cities = withContext(Dispatchers.IO) { catalog.cities(countryCode, query, 120) }
        loading = false
    }
    CrystalDialog(onDismissRequest=onDismiss, confirmButton={}, dismissButton = { TextButton(onClick = onDismiss) { Text(uiText("back")) } }, title={Text(uiText("choose_city"))}, text={Column {
        CrystalTextField(query, { query = it }, Modifier.fillMaxWidth(), label = { Text(uiText("search_city_english")) }, singleLine = true)
        if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(if (query.isBlank()) uiText("popular_city_note") else uiFormat("search_results", cities.size), style = MaterialTheme.typography.bodySmall)
        LazyColumn(Modifier.heightIn(max=420.dp), verticalArrangement = Arrangement.spacedBy(6.dp)){items(cities,key={it.geoId}){city->PickerResultRow("${city.name} · ${city.zoneId}"){pick(city)}}}
    }})
}

private fun continentName(code: String, language: AppLanguage): String {
    val english = mapOf("AS" to "Asia", "EU" to "Europe", "AF" to "Africa", "NA" to "North America", "SA" to "South America", "OC" to "Oceania", "AN" to "Antarctica")
    val chinese = mapOf("AS" to "亚洲", "EU" to "欧洲", "AF" to "非洲", "NA" to "北美洲", "SA" to "南美洲", "OC" to "大洋洲", "AN" to "南极洲")
    return (if (language == AppLanguage.CHINESE) chinese else english)[code] ?: code
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
