package com.bestmates.worldclock.ui.i18n

import com.bestmates.worldclock.ui.settings.AppLanguage

/**
 * Strings for the 0.4 interface. English and Chinese are complete; the other languages
 * cover the most visible surfaces and fall back to English key by key.
 */
internal object Strings04 {
    val en: Map<String, String> = mapOf(
        // Workspace
        "your_widgets" to "Your widgets", "new_widget" to "New widget", "copy_suffix" to "copy", "duplicate" to "Duplicate",
        "card_meta" to "%1\$d cities · %2\$d×%3\$d", "card_meta_one" to "%1\$d city · %2\$d×%3\$d", "on_home" to "On home", "empty_design" to "Empty design",
        "empty_title" to "Keep their time close", "empty_body" to "Make a home-screen widget with the cities of the people you love — their time, their sun and moon, at a glance.",
        "make_first_widget" to "Make your first widget", "name" to "Name", "name_placeholder" to "e.g. Family, Tokyo & Paris",
        "home_screen_size" to "Home-screen size", "cells_format" to "%1\$d × %2\$d", "start_designing" to "Start designing",
        "size_note_calibrated" to "Proportions match your launcher's real cells.",
        "size_note_default" to "Proportions are estimated until your first widget is placed; then Best Mates learns your launcher's cell size.",
        "choose_design" to "Choose a design", "unnamed" to "Untitled", "selected_count" to "%d selected",
        "delete_projects_title" to "Delete these designs?", "delete_projects_message" to "%d design(s) will be removed from this device. Widgets already on your home screen keep their last look.",
        // Editor chrome
        "saved" to "Saved", "saving" to "Saving…", "editing" to "Editing",
        "canvas_empty_hint" to "Start with a shape, a city or some text below.",
        "preview_dark" to "Previewing dark mode", "preview_light" to "Previewing light mode", "snap" to "Snap", "grid" to "Grid",
        "add_shape" to "Add a shape", "shape" to "Shape", "city" to "City", "text" to "Text", "text_placeholder" to "Hello",
        "layers" to "Layers", "widget_settings" to "Widget", "add_home" to "Add to home screen", "no_layers" to "Nothing here yet.",
        "size_change_note" to "Changing size keeps positions relative; text keeps its size.",
        "colour_mode" to "Colours follow", "follow_phone" to "Phone", "colour_mode_note" to "Shapes switch between their light-mode and dark-mode fills with your phone's dark mode, or stay fixed.",
        "time_24" to "24-hour time", "preview_backdrop" to "Preview backdrop",
        "backdrop_note" to "Only for previewing here; your widget sits on your real wallpaper.",
        "backdrop_wallpaper" to "Wallpaper", "backdrop_dusk" to "Dusk", "backdrop_night" to "Night", "backdrop_meadow" to "Meadow", "backdrop_paper" to "Paper", "backdrop_ink" to "Ink",
        "pin_unsupported" to "Your launcher can't add widgets from apps. Long-press the home screen, open Widgets and choose Best Mates.",
        // Inspector
        "lock" to "Lock", "unlock" to "Unlock", "bring_forward" to "Bring forward", "send_backward" to "Send backward", "done" to "Done",
        "tab_shape" to "Shape", "tab_colour" to "Colour", "tab_edge" to "Edge", "tab_depth" to "Depth",
        "fill_light_mode" to "Fill in light mode", "fill_dark_mode" to "Fill in dark mode",
        "previewing_dark" to "The canvas is showing dark mode.", "previewing_light" to "The canvas is showing light mode.",
        "outline_width" to "Outline", "outline_opacity" to "Outline strength", "outline_glow_colour" to "Outline and glow colour",
        "glow_softness" to "Glow softness", "glow_strength" to "Glow strength", "shadow_spread" to "Shadow softness",
        "shadow_opacity" to "Shadow strength", "shadow_offset" to "Shadow drop", "rotation" to "Rotation", "reset_rotation" to "Reset rotation",
        "tab_city" to "City", "tab_type" to "Type", "tab_sky" to "Sky", "change" to "Change", "size" to "Size",
        "show_time" to "Time", "show_date" to "Date", "show_solar" to "Sun & moon", "show_country" to "Country",
        "font_name" to "Name", "font_time" to "Time", "font_date" to "Date", "same_type_all" to "Use this type for every city",
        "colour_name" to "Name", "colour_time" to "Time", "colour_date" to "Date",
        "sun_label" to "Sun — shown while it is day there", "sun_colour" to "Sun colour",
        "moon_label" to "Moon — shown while it is night there", "moon_colour" to "Moon colour",
        "tab_text" to "Text", "tab_font" to "Font", "text_content" to "Write something", "all_fonts" to "All fonts", "text_color" to "Text colour",
        "opacity_label" to "Opacity",
        // Pickers
        "font_cat_sans" to "Sans", "font_cat_display" to "Display", "font_cat_serif" to "Serif", "font_cat_rounded" to "Rounded",
        "font_cat_mono" to "Mono & pixel", "font_cat_hand" to "Handwritten", "font_cat_live" to "Live system styles",
        "live_fonts_note" to "Time and date keep ticking on your home screen, so they use your phone's own typefaces. Names and text can use every Best Mates font.",
        "palette_mist" to "Mist", "palette_paper" to "Paper", "palette_marigold" to "Marigold", "palette_apricot" to "Apricot",
        "palette_rose" to "Rose", "palette_lilac" to "Lilac", "palette_dusk" to "Dusk", "palette_sage" to "Sage", "palette_ink" to "Ink",
        "palette_lavender" to "Lavender",
        "choose_city" to "Add a city", "search_city_any" to "Search any city, in any language", "no_results" to "No matching city.", "browse" to "Browse by region",
        // Settings
        "settings" to "Settings", "back" to "Back", "appearance" to "Appearance", "follow_system" to "System", "light" to "Light", "dark" to "Dark",
        "appearance_note" to "For the app only. Each widget keeps its own colours.",
        "accent" to "Accent", "accent_marigold" to "Marigold", "accent_rose" to "Rose", "accent_iris" to "Iris", "accent_graphite" to "Graphite",
        "display_language" to "Language", "widget_library" to "Library",
        "library_fonts" to "Widget typefaces", "library_fonts_note" to "%d bundled, open-licensed typefaces that render identically on every phone.",
        "library_privacy" to "Private by design", "library_privacy_note" to "Your designs and cities never leave this phone. No account, no tracking.",
        "about" to "About", "version_format" to "Version %s", "about_body" to "A world clock for the people you love: their time, their sun and moon, on your home screen.",
        "licences" to "Open-source licences",
        "rename" to "Rename", "apply" to "Apply", "cancel" to "Cancel", "delete" to "Delete", "close" to "Close", "widget_name" to "Widget name",
        "undo" to "Undo", "redo" to "Redo",
    )

    val zh: Map<String, String> = mapOf(
        "your_widgets" to "我的组件", "new_widget" to "新建组件", "copy_suffix" to "副本", "duplicate" to "复制一份",
        "card_meta" to "%1\$d 个城市 · %2\$d×%3\$d", "card_meta_one" to "%1\$d 个城市 · %2\$d×%3\$d", "on_home" to "已在桌面", "empty_design" to "空白设计",
        "empty_title" to "把牵挂的时间放在身边", "empty_body" to "为你在意的人做一个桌面组件——他们那里的时间、日出月落，一眼就能看到。",
        "make_first_widget" to "做第一个组件", "name" to "名称", "name_placeholder" to "例如：家人、东京与巴黎",
        "home_screen_size" to "桌面尺寸", "cells_format" to "%1\$d × %2\$d", "start_designing" to "开始设计",
        "size_note_calibrated" to "比例与你的桌面格子一致。",
        "size_note_default" to "放置第一个组件前比例为估算值；之后 Best Mates 会学习你桌面的真实格子尺寸。",
        "choose_design" to "选择一个设计", "unnamed" to "未命名", "selected_count" to "已选 %d 项",
        "delete_projects_title" to "删除这些设计？", "delete_projects_message" to "将从本机删除 %d 个设计。已经放在桌面的组件会保留最后的样子。",
        "saved" to "已保存", "saving" to "保存中…", "editing" to "编辑中",
        "canvas_empty_hint" to "从下方添加形状、城市或文字开始。",
        "preview_dark" to "正在预览深色模式", "preview_light" to "正在预览浅色模式", "snap" to "吸附", "grid" to "网格",
        "add_shape" to "添加形状", "shape" to "形状", "city" to "城市", "text" to "文字", "text_placeholder" to "你好",
        "layers" to "图层", "widget_settings" to "组件设置", "add_home" to "添加到桌面", "no_layers" to "这里还什么都没有。",
        "size_change_note" to "改变尺寸会保持相对位置，文字大小不变。",
        "colour_mode" to "颜色跟随", "follow_phone" to "手机", "colour_mode_note" to "形状会随手机深色模式在浅色与深色填充之间切换，也可以固定为一种。",
        "time_24" to "24 小时制", "preview_backdrop" to "预览背景",
        "backdrop_note" to "仅用于这里的预览；桌面上组件会显示在你真实的壁纸上。",
        "backdrop_wallpaper" to "壁纸", "backdrop_dusk" to "黄昏", "backdrop_night" to "夜空", "backdrop_meadow" to "草地", "backdrop_paper" to "纸白", "backdrop_ink" to "墨色",
        "pin_unsupported" to "你的桌面不支持由应用添加组件。请长按桌面，打开“小组件”，选择 Best Mates。",
        "lock" to "锁定", "unlock" to "解锁", "bring_forward" to "上移一层", "send_backward" to "下移一层", "done" to "完成",
        "tab_shape" to "形状", "tab_colour" to "颜色", "tab_edge" to "轮廓", "tab_depth" to "层次",
        "fill_light_mode" to "浅色模式填充", "fill_dark_mode" to "深色模式填充",
        "previewing_dark" to "画布正在显示深色模式。", "previewing_light" to "画布正在显示浅色模式。",
        "outline_width" to "轮廓粗细", "outline_opacity" to "轮廓浓度", "outline_glow_colour" to "轮廓与光晕颜色",
        "glow_softness" to "光晕柔和度", "glow_strength" to "光晕强度", "shadow_spread" to "阴影柔和度",
        "shadow_opacity" to "阴影浓度", "shadow_offset" to "阴影下沉", "rotation" to "旋转", "reset_rotation" to "重置旋转",
        "tab_city" to "城市", "tab_type" to "字体", "tab_sky" to "日月", "change" to "更换", "size" to "大小",
        "show_time" to "时间", "show_date" to "日期", "show_solar" to "日月图标", "show_country" to "国家",
        "font_name" to "城市名", "font_time" to "时间", "font_date" to "日期", "same_type_all" to "所有城市使用这套字体",
        "colour_name" to "城市名", "colour_time" to "时间", "colour_date" to "日期",
        "sun_label" to "太阳——当地白天时显示", "sun_colour" to "太阳颜色",
        "moon_label" to "月亮——当地夜晚时显示", "moon_colour" to "月亮颜色",
        "tab_text" to "文字", "tab_font" to "字体", "text_content" to "写点什么", "all_fonts" to "全部字体", "text_color" to "文字颜色",
        "opacity_label" to "不透明度",
        "font_cat_sans" to "无衬线", "font_cat_display" to "展示", "font_cat_serif" to "衬线", "font_cat_rounded" to "圆体",
        "font_cat_mono" to "等宽与像素", "font_cat_hand" to "手写", "font_cat_live" to "实时系统字体",
        "live_fonts_note" to "时间和日期会在桌面上实时走动，因此使用手机自带字体；城市名和文字可以使用全部 Best Mates 字体。",
        "palette_mist" to "薄雾", "palette_paper" to "纸白", "palette_marigold" to "金盏", "palette_apricot" to "杏色",
        "palette_rose" to "玫瑰", "palette_lilac" to "丁香", "palette_dusk" to "黄昏", "palette_sage" to "鼠尾草", "palette_ink" to "墨色",
        "palette_lavender" to "薰衣草",
        "choose_city" to "添加城市", "search_city_any" to "搜索城市，支持中文与各种语言", "no_results" to "没有找到匹配的城市。", "browse" to "按地区浏览",
        "settings" to "设置", "back" to "返回", "appearance" to "外观", "follow_system" to "跟随系统", "light" to "浅色", "dark" to "深色",
        "appearance_note" to "只影响应用界面，每个组件保留自己的颜色。",
        "accent" to "强调色", "accent_marigold" to "金盏", "accent_rose" to "玫瑰", "accent_iris" to "鸢尾", "accent_graphite" to "石墨",
        "display_language" to "语言", "widget_library" to "资源库",
        "library_fonts" to "组件字体", "library_fonts_note" to "内置 %d 款开源授权字体，在每台手机上显示一致。",
        "library_privacy" to "隐私优先", "library_privacy_note" to "你的设计和城市只保存在这台手机上。无需账号，没有追踪。",
        "about" to "关于", "version_format" to "版本 %s", "about_body" to "为你牵挂的人做的世界时钟：他们的时间、他们的日月，都在你的桌面上。",
        "licences" to "开源许可",
        "rename" to "重命名", "apply" to "应用", "cancel" to "取消", "delete" to "删除", "close" to "关闭", "widget_name" to "组件名称",
        "undo" to "撤销", "redo" to "重做",
    )

    val de: Map<String, String> = mapOf(
        "your_widgets" to "Deine Widgets", "new_widget" to "Neues Widget", "duplicate" to "Duplizieren", "on_home" to "Auf dem Startbildschirm",
        "empty_title" to "Ihre Zeit immer nah", "make_first_widget" to "Erstes Widget gestalten", "name" to "Name",
        "home_screen_size" to "Größe auf dem Startbildschirm", "start_designing" to "Gestalten", "choose_design" to "Design wählen",
        "saved" to "Gespeichert", "saving" to "Speichert…", "editing" to "Bearbeitung", "shape" to "Form", "city" to "Stadt", "text" to "Text",
        "layers" to "Ebenen", "widget_settings" to "Widget", "add_home" to "Zum Startbildschirm", "done" to "Fertig",
        "tab_shape" to "Form", "tab_colour" to "Farbe", "tab_edge" to "Kante", "tab_depth" to "Tiefe", "tab_city" to "Stadt", "tab_type" to "Schrift", "tab_sky" to "Himmel",
        "tab_text" to "Text", "tab_font" to "Schrift", "size" to "Größe", "rotation" to "Drehung",
        "settings" to "Einstellungen", "appearance" to "Erscheinungsbild", "follow_system" to "System", "light" to "Hell", "dark" to "Dunkel",
        "accent" to "Akzent", "display_language" to "Sprache", "about" to "Über", "licences" to "Open-Source-Lizenzen",
        "rename" to "Umbenennen", "apply" to "Anwenden", "cancel" to "Abbrechen", "delete" to "Löschen", "close" to "Schließen", "back" to "Zurück",
        "undo" to "Rückgängig", "redo" to "Wiederholen", "choose_city" to "Stadt hinzufügen", "search_city_any" to "Stadt suchen, in jeder Sprache",
    )

    val fr: Map<String, String> = mapOf(
        "your_widgets" to "Vos widgets", "new_widget" to "Nouveau widget", "duplicate" to "Dupliquer", "on_home" to "Sur l’accueil",
        "empty_title" to "Gardez leur heure tout près", "make_first_widget" to "Créer mon premier widget", "name" to "Nom",
        "home_screen_size" to "Taille sur l’écran d’accueil", "start_designing" to "Commencer", "choose_design" to "Choisir un design",
        "saved" to "Enregistré", "saving" to "Enregistrement…", "editing" to "Modification", "shape" to "Forme", "city" to "Ville", "text" to "Texte",
        "layers" to "Calques", "widget_settings" to "Widget", "add_home" to "Ajouter à l’accueil", "done" to "Terminé",
        "tab_shape" to "Forme", "tab_colour" to "Couleur", "tab_edge" to "Contour", "tab_depth" to "Relief", "tab_city" to "Ville", "tab_type" to "Police", "tab_sky" to "Ciel",
        "tab_text" to "Texte", "tab_font" to "Police", "size" to "Taille", "rotation" to "Rotation",
        "settings" to "Réglages", "appearance" to "Apparence", "follow_system" to "Système", "light" to "Clair", "dark" to "Sombre",
        "accent" to "Accent", "display_language" to "Langue", "about" to "À propos", "licences" to "Licences libres",
        "rename" to "Renommer", "apply" to "Appliquer", "cancel" to "Annuler", "delete" to "Supprimer", "close" to "Fermer", "back" to "Retour",
        "undo" to "Annuler", "redo" to "Rétablir", "choose_city" to "Ajouter une ville", "search_city_any" to "Chercher une ville, dans toute langue",
    )

    val ru: Map<String, String> = mapOf(
        "your_widgets" to "Ваши виджеты", "new_widget" to "Новый виджет", "duplicate" to "Дублировать", "on_home" to "На экране",
        "empty_title" to "Их время всегда рядом", "make_first_widget" to "Создать первый виджет", "name" to "Название",
        "home_screen_size" to "Размер на экране", "start_designing" to "Начать", "choose_design" to "Выберите дизайн",
        "saved" to "Сохранено", "saving" to "Сохранение…", "editing" to "Изменение", "shape" to "Фигура", "city" to "Город", "text" to "Текст",
        "layers" to "Слои", "widget_settings" to "Виджет", "add_home" to "На главный экран", "done" to "Готово",
        "tab_shape" to "Фигура", "tab_colour" to "Цвет", "tab_edge" to "Контур", "tab_depth" to "Глубина", "tab_city" to "Город", "tab_type" to "Шрифт", "tab_sky" to "Небо",
        "tab_text" to "Текст", "tab_font" to "Шрифт", "size" to "Размер", "rotation" to "Поворот",
        "settings" to "Настройки", "appearance" to "Оформление", "follow_system" to "Система", "light" to "Светлая", "dark" to "Тёмная",
        "accent" to "Акцент", "display_language" to "Язык", "about" to "О приложении", "licences" to "Лицензии",
        "rename" to "Переименовать", "apply" to "Применить", "cancel" to "Отмена", "delete" to "Удалить", "close" to "Закрыть", "back" to "Назад",
        "undo" to "Отменить", "redo" to "Повторить", "choose_city" to "Добавить город", "search_city_any" to "Поиск города на любом языке",
    )

    val tr: Map<String, String> = mapOf(
        "your_widgets" to "Widget’ların", "new_widget" to "Yeni widget", "duplicate" to "Çoğalt", "on_home" to "Ana ekranda",
        "empty_title" to "Onların saati hep yanında", "make_first_widget" to "İlk widget’ını yap", "name" to "Ad",
        "home_screen_size" to "Ana ekran boyutu", "start_designing" to "Tasarlamaya başla", "choose_design" to "Bir tasarım seç",
        "saved" to "Kaydedildi", "saving" to "Kaydediliyor…", "editing" to "Düzenleniyor", "shape" to "Şekil", "city" to "Şehir", "text" to "Metin",
        "layers" to "Katmanlar", "widget_settings" to "Widget", "add_home" to "Ana ekrana ekle", "done" to "Bitti",
        "settings" to "Ayarlar", "appearance" to "Görünüm", "follow_system" to "Sistem", "light" to "Açık", "dark" to "Koyu",
        "accent" to "Vurgu", "display_language" to "Dil", "about" to "Hakkında", "licences" to "Açık kaynak lisansları",
        "rename" to "Yeniden adlandır", "apply" to "Uygula", "cancel" to "İptal", "delete" to "Sil", "close" to "Kapat", "back" to "Geri",
        "undo" to "Geri al", "redo" to "Yinele", "choose_city" to "Şehir ekle",
    )

    val ar: Map<String, String> = mapOf(
        "your_widgets" to "أدواتك", "new_widget" to "أداة جديدة", "duplicate" to "تكرار", "on_home" to "على الشاشة",
        "empty_title" to "اجعل وقتهم قريبًا منك", "make_first_widget" to "أنشئ أداتك الأولى", "name" to "الاسم",
        "home_screen_size" to "الحجم على الشاشة الرئيسية", "start_designing" to "ابدأ التصميم", "choose_design" to "اختر تصميمًا",
        "saved" to "تم الحفظ", "saving" to "جارٍ الحفظ…", "editing" to "تحرير", "shape" to "شكل", "city" to "مدينة", "text" to "نص",
        "layers" to "الطبقات", "widget_settings" to "الأداة", "add_home" to "إضافة إلى الشاشة الرئيسية", "done" to "تم",
        "settings" to "الإعدادات", "appearance" to "المظهر", "follow_system" to "النظام", "light" to "فاتح", "dark" to "داكن",
        "accent" to "لون التمييز", "display_language" to "اللغة", "about" to "حول", "licences" to "تراخيص المصادر المفتوحة",
        "rename" to "إعادة تسمية", "apply" to "تطبيق", "cancel" to "إلغاء", "delete" to "حذف", "close" to "إغلاق", "back" to "رجوع",
        "undo" to "تراجع", "redo" to "إعادة", "choose_city" to "إضافة مدينة",
    )

    fun lookup(language: AppLanguage, key: String): String? {
        val local = when (language) {
            AppLanguage.ENGLISH -> en
            AppLanguage.CHINESE -> zh
            AppLanguage.GERMAN -> de
            AppLanguage.FRENCH -> fr
            AppLanguage.RUSSIAN -> ru
            AppLanguage.TURKISH -> tr
            AppLanguage.ARABIC -> ar
        }
        return local[key]
    }
}
