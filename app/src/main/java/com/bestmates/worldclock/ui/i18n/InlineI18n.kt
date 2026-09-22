package com.bestmates.worldclock.ui.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.bestmates.worldclock.ui.settings.AppLanguage
import java.util.Locale

internal val LocalBestMatesLanguage = staticCompositionLocalOf { AppLanguage.ENGLISH }

@Composable internal fun uiText(key: String): String = translated(LocalBestMatesLanguage.current, key)
@Composable internal fun uiFormat(key: String, vararg values: Any): String = String.format(uiText(key), *values)
internal fun translated(language: AppLanguage, key: String): String {
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
        AppLanguage.TURKISH -> english + mapOf("workspace" to "Çalışma alanı", "workspace_title" to "Best Mates Çalışma Alanı", "settings" to "Ayarlar", "editor" to "Düzenleyici", "back" to "Geri", "save" to "Kaydet", "save_auto" to "Kaydet · otomatik", "auto_save" to "Otomatik kaydet", "undo" to "Geri al", "redo" to "Yinele", "delete" to "Sil", "copy" to "Kopyala", "paste" to "Yapıştır", "cut" to "Kes", "deselect" to "Seçimi kaldır", "city" to "Şehir", "text" to "Metin", "component" to "Bileşen", "add_component" to "Bileşen ekle", "add_city" to "Şehir ekle", "add_text" to "Metin ekle", "apply" to "Uygula", "cancel" to "İptal", "done" to "Bitti", "create_widget" to "Widget oluştur", "rename" to "Yeniden adlandır", "widget_name" to "Widget adı", "add_home" to "Ana ekrana ekle", "selected_cities" to "Seçilen şehirler", "theme" to "Tema", "time_format" to "Saat biçimi", "choose_city" to "Şehir seç", "choose_shape" to "Arka plan şekli seç", "font" to "Yazı tipi", "choose_font" to "Yazı tipi seç", "show_time" to "Saati göster", "show_date" to "Tarihi göster", "show_country" to "Ülkeyi göster", "show_solar" to "Güneşi/ayı göster", "classic" to "Klasik", "natural" to "Doğal", "playful" to "Eğlenceli")
        AppLanguage.ARABIC -> english + mapOf(
            "workspace" to "مساحة العمل", "workspace_title" to "مساحة عمل Best Mates", "settings" to "الإعدادات", "editor" to "المحرر", "back" to "رجوع", "save" to "حفظ", "save_auto" to "حفظ · تلقائي", "auto_save" to "الحفظ التلقائي", "undo" to "تراجع", "redo" to "إعادة", "delete" to "حذف", "copy" to "نسخ", "paste" to "لصق", "cut" to "قص", "deselect" to "إلغاء التحديد", "city" to "مدينة", "text" to "نص", "component" to "عنصر", "add_component" to "إضافة عنصر", "add_city" to "إضافة مدينة", "add_text" to "إضافة نص", "apply" to "تطبيق", "cancel" to "إلغاء", "done" to "تم", "close" to "إغلاق", "create_widget" to "إنشاء ويدجت", "rename" to "إعادة تسمية", "widget_name" to "اسم الويدجت", "add_home" to "إضافة إلى الشاشة الرئيسية", "unnamed" to "ويدجت بلا اسم", "empty_workspace" to "لا توجد ويدجت بعد\nاضغط إنشاء ويدجت للبدء", "auto_save_on" to "الحفظ التلقائي مفعّل", "edited_locally" to "تم التعديل محلياً", "selected" to "محدد", "change_city" to "تغيير المدينة", "selected_cities" to "المدن المحددة", "no_cities" to "لا توجد مدن بعد. ستظهر المدن المضافة على اللوحة.", "theme" to "السمة", "time_format" to "تنسيق الوقت", "time_24" to "نظام 24 ساعة (أوقفه لعرض AM/PM)", "add_component" to "إضافة عنصر", "empty_canvas" to "أضف مدينة أو نصاً من أعلى اليمين", "select_hint" to "اضغط على الخلفية أو المدينة أو النص لتحريره. اسحب للتحريك واستخدم المقبض للتكبير.", "drag_shape_hint" to "اسحب العنصر على اللوحة. استخدم المقبض السفلي الأيمن لتغيير العرض والارتفاع.", "drag_city_hint" to "اسحب المدينة على اللوحة، واستخدم المقبض السفلي الأيمن لتغيير حجمها.", "drag_text_hint" to "اسحب النص على اللوحة، واستخدم المقبض السفلي الأيمن لتغيير حجمه.", "shape_library" to "الخلفية المدمجة", "choose" to "اختيار", "choose_shape" to "اختيار شكل الخلفية", "choose_new_shape" to "اختيار شكل عنصر جديد", "palette" to "ألوان النهار والليل", "custom" to "مخصص", "choose_palette" to "اختيار ألوان النهار والليل", "day_color" to "لون النهار", "night_color" to "لون الليل", "font" to "الخط", "choose_font" to "اختيار الخط", "text_content" to "محتوى النص", "text_color" to "لون النص", "name_country" to "اسم المدينة والدولة", "time" to "الوقت", "date" to "التاريخ", "sun_moon" to "الشمس والقمر", "sun_library" to "مكتبة رموز الشمس", "moon_library" to "مكتبة رموز القمر", "browse_icons" to "اضغط لتصفح الرموز", "city_name" to "اسم المدينة", "show_time" to "إظهار الوقت", "show_date" to "إظهار التاريخ", "show_country" to "إظهار الدولة", "show_solar" to "إظهار الشمس والقمر", "tap_edit" to "اضغط للتحرير", "outline_light" to "الإطار والإضاءة", "outline_width" to "عرض الإطار", "outline_opacity" to "شفافية الإطار", "ambient_glow" to "التوهج المحيط", "glow_strength" to "قوة التوهج", "depth" to "العمق", "shadow_spread" to "انتشار الظل", "shadow_opacity" to "شفافية الظل", "shadow_offset" to "إزاحة الظل", "choose_city" to "اختيار مدينة", "search_city" to "اكتب اسم المدينة بالإنجليزية", "continent" to "القارة", "country" to "الدولة", "please_choose" to "يرجى الاختيار", "choose_continent" to "اختيار القارة", "choose_country" to "اختيار الدولة", "choose_or_search_city" to "اختيار أو البحث عن مدينة", "choose_country_first" to "اختر الدولة أولاً", "choose_continent_first" to "اختر القارة أولاً", "search_results" to "نتائج البحث: %d", "popular_city_note" to "تظهر المدن الأشهر أولاً. اكتب بالإنجليزية للتصفية.", "classic" to "كلاسيكي", "natural" to "طبيعي", "playful" to "مرح")
        AppLanguage.FRENCH -> english + mapOf("workspace" to "Espace de travail", "workspace_title" to "Espace Best Mates", "settings" to "Réglages", "editor" to "Éditeur", "back" to "Retour", "save" to "Enregistrer", "save_auto" to "Enregistrer · automatique", "auto_save" to "Enregistrement automatique", "undo" to "Annuler", "redo" to "Rétablir", "delete" to "Supprimer", "copy" to "Copier", "paste" to "Coller", "cut" to "Couper", "deselect" to "Désélectionner", "city" to "Ville", "text" to "Texte", "component" to "Élément", "add_component" to "Ajouter un élément", "add_city" to "Ajouter une ville", "add_text" to "Ajouter du texte", "apply" to "Appliquer", "cancel" to "Annuler", "done" to "Terminé", "create_widget" to "Créer un widget", "rename" to "Renommer", "widget_name" to "Nom du widget", "add_home" to "Ajouter à l’accueil", "selected_cities" to "Villes sélectionnées", "theme" to "Thème", "time_format" to "Format de l’heure", "choose_city" to "Choisir une ville", "choose_shape" to "Choisir une forme d’arrière-plan", "font" to "Police", "choose_font" to "Choisir une police", "show_time" to "Afficher l’heure", "show_date" to "Afficher la date", "show_country" to "Afficher le pays", "show_solar" to "Afficher le soleil/la lune", "classic" to "Classique", "natural" to "Naturel", "playful" to "Ludique")
        else -> english
    }
    return editorLanguageOverride(language, key) ?: completeLanguageOverride(language, key) ?: translated[key] ?: english[key] ?: key
}

/* These are the short, frequently-visible editor strings.  Keeping them in one
 * override prevents a partially translated screen when a new UI control is
 * added to the canvas editor. */
internal fun editorLanguageOverride(language: AppLanguage, key: String): String? {
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

internal fun AppLanguage.dateLocale(): Locale = when (this) {
    AppLanguage.CHINESE -> Locale.SIMPLIFIED_CHINESE
    AppLanguage.GERMAN -> Locale.GERMAN
    AppLanguage.RUSSIAN -> Locale("ru")
    AppLanguage.TURKISH -> Locale("tr")
    AppLanguage.ARABIC -> Locale("ar")
    AppLanguage.FRENCH -> Locale.FRENCH
    AppLanguage.ENGLISH -> Locale.ENGLISH
}

/** Keeps the persistent editor chrome translated even when a screen uses a newer string key. */
internal fun completeLanguageOverride(language: AppLanguage, key: String): String? = when (language) {
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
