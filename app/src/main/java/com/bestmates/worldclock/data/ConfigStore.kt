package com.bestmates.worldclock.data

import android.content.Context
import com.bestmates.worldclock.model.*
import org.json.JSONArray
import org.json.JSONObject

class ConfigStore(context: Context) {
    private val prefs = context.getSharedPreferences("widget_configs", Context.MODE_PRIVATE)

    fun load(widgetId: Int): WidgetConfig = loadKey("widget_$widgetId")
    fun loadProject(projectId: String): WidgetConfig = loadKey("project_$projectId")
    private fun loadKey(key: String): WidgetConfig {
        val raw = prefs.getString(key, null) ?: return WidgetConfig()
        return runCatching {
            val root = JSONObject(raw)
            val storedVersion = root.optInt("layoutVersion", 1)
            val rawCities = root.getJSONArray("cities").toCities()
            val cities = if (storedVersion < 2) arrangeLegacyCities(rawCities) else rawCities
            val legacyShape = runCatching { WidgetShape.valueOf(root.optString("shape", WidgetShape.ROUNDED.name)) }.getOrDefault(WidgetShape.ROUNDED)
            // Version 7+ intentionally allows an empty scene. Older presets did not,
            // so only migrate their missing layer to a friendly default background.
            val parsedLayers = root.optJSONArray("layers")?.toLayers().orEmpty()
            val compatibleLayers = if (storedVersion < 8) {
                parsedLayers.map { layer ->
                    // Older builds left an artificial margin around a nearly full-size
                    // heart. Preserve deliberately small hearts, but migrate the old
                    // default so existing projects immediately use the whole 4x4 canvas.
                    if (layer.shape == WidgetShape.HEART && layer.width >= .90f && layer.height >= .90f) {
                        layer.copy(x = 0f, y = 0f, width = 1f, height = 1f)
                    } else layer
                }
            } else parsedLayers
            val layers = if (storedVersion >= 7) compatibleLayers else compatibleLayers.ifEmpty { listOf(newWidgetLayer(legacyShape)) }
            WidgetConfig(
                layoutVersion = 8,
                shape = legacyShape,
                columns = 4, rows = 4,
                theme = ThemeMode.valueOf(root.optString("theme", ThemeMode.SYSTEM.name)),
                title = root.optString("title", "").let { if (storedVersion < 3 && it.equals("BEST MATES", true)) "" else it },
                subtitle = root.optString("subtitle", "").let { if (storedVersion < 3 && it.equals("Same sky · Different time", true)) "" else it },
                use24Hour = root.optBoolean("use24Hour", true),
                showDate = root.optBoolean("showDate", true),
                showCountry = root.optBoolean("showCountry", false),
                showSolarIcon = root.optBoolean("showSolarIcon", true),
                showTime = root.optBoolean("showTime", true),
                dayColor = root.optLong("dayColor", 0x385A526D), nightColor = root.optLong("nightColor", 0x55433D59),
                textColor = root.optLong("textColor", 0xFFFFFFFF), borderWidth = root.optDouble("borderWidth", 1.0).toFloat(),
                borderOpacity = root.optDouble("borderOpacity", .32).toFloat(),
                softness = root.optDouble("softness", 24.0).toFloat(), glowOpacity = root.optDouble("glowOpacity", .10).toFloat(),
                shadow = root.optDouble("shadow", 12.0).toFloat(), shadowOpacity = root.optDouble("shadowOpacity", .16).toFloat(),
                shadowYOffset = root.optDouble("shadowYOffset", 2.0).toFloat(),
                locked = root.optBoolean("locked", false),
                autoSave = root.optBoolean("autoSave", false),
                layers = layers,
                texts = root.optJSONArray("texts")?.toTexts().orEmpty().filterNot { storedVersion < 2 && it.content == "新文字" },
                cities = cities
            )
        }.getOrDefault(WidgetConfig())
    }

    fun save(widgetId: Int, config: WidgetConfig) = saveKey("widget_$widgetId", config)
    fun saveProject(projectId: String, config: WidgetConfig) = saveKey("project_$projectId", config)
    private fun saveKey(key: String, config: WidgetConfig) {
        val root = JSONObject().apply {
            put("layoutVersion", config.layoutVersion); put("shape", config.shape.name); put("columns", config.columns); put("rows", config.rows); put("theme", config.theme.name)
            put("title", config.title); put("subtitle", config.subtitle)
            put("use24Hour", config.use24Hour); put("showDate", config.showDate)
            put("showCountry", config.showCountry); put("showSolarIcon", config.showSolarIcon)
            put("showTime", config.showTime); put("dayColor", config.dayColor); put("nightColor", config.nightColor); put("textColor", config.textColor)
            put("borderWidth", config.borderWidth); put("borderOpacity", config.borderOpacity)
            put("softness", config.softness); put("glowOpacity", config.glowOpacity)
            put("shadow", config.shadow); put("shadowOpacity", config.shadowOpacity); put("shadowYOffset", config.shadowYOffset)
            put("locked", config.locked); put("autoSave", config.autoSave)
            put("layers", JSONArray().apply { config.layers.forEach { layer -> put(JSONObject().apply {
                put("id", layer.id); put("shape", layer.shape.name); put("x", layer.x); put("y", layer.y); put("width", layer.width); put("height", layer.height)
                put("dayColor", layer.dayColor); put("nightColor", layer.nightColor); put("textColor", layer.textColor)
                put("borderWidth", layer.borderWidth); put("borderOpacity", layer.borderOpacity)
                put("softness", layer.softness); put("glowOpacity", layer.glowOpacity)
                put("shadow", layer.shadow); put("shadowOpacity", layer.shadowOpacity); put("shadowYOffset", layer.shadowYOffset)
                put("rotation", layer.rotation); put("locked", layer.locked)
            }) } })
            put("texts", JSONArray().apply { config.texts.forEach { text -> put(JSONObject().apply { put("content", text.content); put("size", text.size); put("color", text.color); put("alpha", text.alpha); put("x", text.x); put("y", text.y); put("font", text.font); put("componentId", text.componentId); put("rotation", text.rotation); put("locked", text.locked) }) } })
            put("cities", JSONArray().apply { config.cities.forEach { put(it.toJson()) } })
        }
        prefs.edit().putString(key, root.toString()).apply()
    }

    fun delete(widgetId: Int) = prefs.edit().remove("widget_$widgetId").apply()
    fun deleteProject(projectId: String) = prefs.edit().remove("project_$projectId").apply()
}
private fun JSONArray.toTexts() = buildList { for (i in 0 until length()) getJSONObject(i).let { add(CustomText(it.optString("content", ""), it.optDouble("size", 14.0).toFloat(), it.optLong("color", 0xFFFFFFFF), it.optDouble("alpha", 1.0).toFloat(), it.optDouble("x").toFloat(), it.optDouble("y").toFloat(), it.optString("font", "SANS"), it.optLong("componentId", 0L), it.optDouble("rotation", 0.0).toFloat(), it.optBoolean("locked", false))) } }

private fun JSONArray.toLayers() = buildList {
    for (i in 0 until length()) getJSONObject(i).let {
        val shape = runCatching { WidgetShape.valueOf(it.optString("shape", WidgetShape.ROUNDED.name)) }.getOrDefault(WidgetShape.ROUNDED)
        add(WidgetLayer(
            id = it.optLong("id", System.nanoTime() + i), shape = shape,
            x = it.optDouble("x", 0.0).toFloat(), y = it.optDouble("y", 0.0).toFloat(),
            width = it.optDouble("width", .86).toFloat(), height = it.optDouble("height", .64).toFloat(),
            dayColor = it.optLong("dayColor", 0x385A526D), nightColor = it.optLong("nightColor", 0x55433D59),
            textColor = it.optLong("textColor", 0xFFFFFFFF), borderWidth = it.optDouble("borderWidth", 1.0).toFloat(),
            borderOpacity = it.optDouble("borderOpacity", .32).toFloat(),
            softness = it.optDouble("softness", 24.0).toFloat(), glowOpacity = it.optDouble("glowOpacity", .10).toFloat(),
            shadow = it.optDouble("shadow", 12.0).toFloat(), shadowOpacity = it.optDouble("shadowOpacity", .16).toFloat(),
            shadowYOffset = it.optDouble("shadowYOffset", 2.0).toFloat(),
            rotation = it.optDouble("rotation", 0.0).toFloat(), locked = it.optBoolean("locked", false)
        ))
    }
}

private fun arrangeLegacyCities(cities: List<City>): List<City> {
    if (cities.size == 1) return listOf(cities.first().copy(x = 0f, y = 0f, scale = 1f))
    if (cities.size == 3) return cities.mapIndexed { index, city ->
        when (index) {
            0 -> city.copy(x = 0f, y = -.28f, scale = 1f)
            1 -> city.copy(x = -.48f, y = .46f, scale = 1f)
            else -> city.copy(x = .48f, y = .46f, scale = 1f)
        }
    }
    val rows = (cities.size + 1) / 2
    return cities.mapIndexed { index, city ->
        val row = index / 2
        val x = if (index == cities.lastIndex && cities.size % 2 == 1) 0f else if (index % 2 == 0) -.48f else .48f
        val y = if (rows == 1) 0f else (-.56f + row * (1.24f / (rows - 1))).coerceIn(-.56f, .68f)
        city.copy(x = x, y = y, scale = 1f)
    }
}

private fun City.toJson() = JSONObject().apply {
    put("name", name); put("country", country); put("latitude", latitude); put("longitude", longitude); put("zoneId", zoneId); put("nickname", nickname)
    put("geoId", geoId); put("x", x); put("y", y); put("scale", scale)
    put("showTime", showTime); put("showDate", showDate); put("showCountry", showCountry); put("showSolar", showSolar)
    put("nameColor", nameColor); put("timeColor", timeColor); put("dateColor", dateColor); put("solarColor", solarColor); put("iconStyle", iconStyle.name)
    put("nameFont", nameFont); put("timeFont", timeFont); put("dateFont", dateFont)
    put("sunStyle", sunStyle.name); put("moonStyle", moonStyle.name); put("sunColor", sunColor); put("moonColor", moonColor)
    put("componentId", componentId); put("rotation", rotation); put("locked", locked)
}
private fun JSONArray.toCities() = buildList {
    for (i in 0 until length()) getJSONObject(i).let {
        add(City(it.getString("name"), it.optString("country"), it.getDouble("latitude"), it.getDouble("longitude"), it.getString("zoneId"), it.optString("nickname"), it.optLong("geoId"), it.optDouble("x").toFloat(), it.optDouble("y").toFloat(), it.optDouble("scale", 1.0).toFloat(), it.optBoolean("showTime", true), it.optBoolean("showDate", true), it.optBoolean("showCountry", false), it.optBoolean("showSolar", true), it.optLong("nameColor", 0xFFFFFFFF), it.optLong("timeColor", 0xFFFFFFFF), it.optLong("dateColor", 0xCCFFFFFF), it.optLong("solarColor", 0xFFEFE8F7), runCatching { SolarIconStyle.valueOf(it.optString("iconStyle", "LINE")) }.getOrDefault(SolarIconStyle.LINE), it.optString("nameFont", "SANS_ROUNDED"), it.optString("timeFont", "SANS_ROUNDED"), it.optString("dateFont", "SANS_LIGHT"), runCatching { SunIconStyle.valueOf(it.optString("sunStyle", "LUCIDE_SUN")) }.getOrDefault(SunIconStyle.LUCIDE_SUN), runCatching { MoonIconStyle.valueOf(it.optString("moonStyle", "LUCIDE_MOON")) }.getOrDefault(MoonIconStyle.LUCIDE_MOON), it.optLong("sunColor", 0xFFE8DFD2), it.optLong("moonColor", 0xFFD9D5EA), it.optLong("componentId", 0L), it.optDouble("rotation", 0.0).toFloat(), it.optBoolean("locked", false)))
    }
}
