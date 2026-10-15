package com.bestmates.worldclock.data

import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.model.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Pure JSON codec for [WidgetConfig] including the complete migration ladder.
 *
 * Kept free of Android framework types (other than org.json) so the whole ladder runs in
 * plain JVM unit tests against real historical payloads.
 *
 * Migration ladder (stored `layoutVersion` → effect):
 *  - < 2  legacy city auto-arrangement; drop the old "新文字" placeholder texts
 *  - < 3  drop the old hard-coded "BEST MATES" / "Same sky · Different time" titles
 *  - < 7  a project always had a background; materialise it when missing
 *  - < 8  near-full legacy hearts expand to the full board
 *  - < 9  explicit 338dp square design board (4×4) and bundled-font identifiers
 */
object ConfigCodec {

    fun decode(raw: String): WidgetConfig {
        val root = JSONObject(raw)
        val storedVersion = root.optInt("layoutVersion", 1)
        val rawCities = root.optJSONArray("cities")?.toCities().orEmpty()
        val cities = if (storedVersion < 2) arrangeLegacyCities(rawCities) else rawCities
        val legacyShape = enumOr(root.optString("shape"), WidgetShape.ROUNDED)
        val parsedLayers = root.optJSONArray("layers")?.toLayers().orEmpty()
        val compatibleLayers = if (storedVersion < 8) parsedLayers.map { layer ->
            if (layer.shape == WidgetShape.HEART && layer.width >= .90f && layer.height >= .90f) {
                layer.copy(x = 0f, y = 0f, width = 1f, height = 1f)
            } else layer
        } else parsedLayers
        val layers = if (storedVersion >= 7) compatibleLayers else compatibleLayers.ifEmpty { listOf(newWidgetLayer(legacyShape)) }
        val texts = root.optJSONArray("texts")?.toTexts().orEmpty().filterNot { storedVersion < 2 && it.content == "新文字" }

        val legacyBoard = storedVersion < 9
        val columns = if (legacyBoard) 4 else root.optInt("columns", 4).coerceIn(1, 6)
        val rows = if (legacyBoard) 4 else root.optInt("rows", 4).coerceIn(1, 6)
        val boardW = if (legacyBoard) LEGACY_BOARD_DP else root.optDouble("boardWidthDp", LEGACY_BOARD_DP.toDouble()).toFloat().coerceIn(40f, 1200f)
        val boardH = if (legacyBoard) LEGACY_BOARD_DP else root.optDouble("boardHeightDp", LEGACY_BOARD_DP.toDouble()).toFloat().coerceIn(40f, 1200f)

        return WidgetConfig(
            layoutVersion = CURRENT_LAYOUT_VERSION,
            shape = legacyShape,
            columns = columns,
            rows = rows,
            theme = enumOr(root.optString("theme"), ThemeMode.SYSTEM),
            title = root.optString("title", "").let { if (storedVersion < 3 && it.equals("BEST MATES", true)) "" else it },
            subtitle = root.optString("subtitle", "").let { if (storedVersion < 3 && it.equals("Same sky · Different time", true)) "" else it },
            use24Hour = root.optBoolean("use24Hour", true),
            showDate = root.optBoolean("showDate", true),
            showCountry = root.optBoolean("showCountry", false),
            showSolarIcon = root.optBoolean("showSolarIcon", true),
            showTime = root.optBoolean("showTime", true),
            dayColor = root.optLong("dayColor", 0x205D5774), nightColor = root.optLong("nightColor", 0x42473D5D),
            textColor = root.optLong("textColor", 0xFFFFFFFF), borderWidth = root.optFloat("borderWidth", 1f),
            borderOpacity = root.optFloat("borderOpacity", .32f),
            softness = root.optFloat("softness", 16f), glowOpacity = root.optFloat("glowOpacity", .10f),
            shadow = root.optFloat("shadow", 8f), shadowOpacity = root.optFloat("shadowOpacity", .16f),
            shadowYOffset = root.optFloat("shadowYOffset", 2f),
            locked = root.optBoolean("locked", false),
            autoSave = root.optBoolean("autoSave", false),
            layers = layers,
            texts = texts.map { it.copy(font = WidgetFonts.canonicalId(it.font)) },
            cities = cities.map {
                it.copy(
                    nameFont = WidgetFonts.canonicalId(it.nameFont),
                    timeFont = WidgetFonts.canonicalId(it.timeFont),
                    dateFont = WidgetFonts.canonicalId(it.dateFont),
                )
            },
            boardWidthDp = boardW,
            boardHeightDp = boardH,
            createdAt = root.optLong("createdAt", 0L),
            updatedAt = root.optLong("updatedAt", 0L),
            backdrop = root.optString("backdrop", "wallpaper").ifBlank { "wallpaper" },
        )
    }

    fun encode(config: WidgetConfig): String = JSONObject().apply {
        put("layoutVersion", CURRENT_LAYOUT_VERSION); put("shape", config.shape.name)
        put("columns", config.columns); put("rows", config.rows); put("theme", config.theme.name)
        put("title", config.title); put("subtitle", config.subtitle)
        put("use24Hour", config.use24Hour); put("showDate", config.showDate)
        put("showCountry", config.showCountry); put("showSolarIcon", config.showSolarIcon)
        put("showTime", config.showTime); put("dayColor", config.dayColor); put("nightColor", config.nightColor); put("textColor", config.textColor)
        put("borderWidth", config.borderWidth); put("borderOpacity", config.borderOpacity)
        put("softness", config.softness); put("glowOpacity", config.glowOpacity)
        put("shadow", config.shadow); put("shadowOpacity", config.shadowOpacity); put("shadowYOffset", config.shadowYOffset)
        put("locked", config.locked); put("autoSave", config.autoSave)
        put("boardWidthDp", config.boardWidthDp); put("boardHeightDp", config.boardHeightDp)
        put("createdAt", config.createdAt); put("updatedAt", config.updatedAt); put("backdrop", config.backdrop)
        put("layers", JSONArray().apply { config.layers.forEach { put(it.toJson()) } })
        put("texts", JSONArray().apply { config.texts.forEach { put(it.toJson()) } })
        put("cities", JSONArray().apply { config.cities.forEach { put(it.toJson()) } })
    }.toString()

    /* ---------------------------------------------------------------- elements */

    private fun WidgetLayer.toJson() = JSONObject().apply {
        put("id", id); put("shape", shape.name); put("x", x); put("y", y); put("width", width); put("height", height)
        put("dayColor", dayColor); put("nightColor", nightColor); put("textColor", textColor)
        put("borderWidth", borderWidth); put("borderOpacity", borderOpacity)
        put("softness", softness); put("glowOpacity", glowOpacity)
        put("shadow", shadow); put("shadowOpacity", shadowOpacity); put("shadowYOffset", shadowYOffset)
        put("rotation", rotation); put("locked", locked); put("material", material)
    }

    private fun CustomText.toJson() = JSONObject().apply {
        put("content", content); put("size", size); put("color", color); put("alpha", alpha)
        put("x", x); put("y", y); put("font", font); put("componentId", componentId)
        put("rotation", rotation); put("locked", locked)
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

    private fun JSONArray.toTexts() = buildList {
        for (i in 0 until length()) {
            val it = optJSONObject(i) ?: continue
            add(CustomText(
                content = it.optString("content", ""), size = it.optFloat("size", 14f), color = it.optLong("color", 0xFFFFFFFF),
                alpha = it.optFloat("alpha", 1f), x = it.optFloat("x", 0f), y = it.optFloat("y", 0f),
                font = it.optString("font", "SANS"), componentId = it.optLong("componentId", 0L),
                rotation = it.optFloat("rotation", 0f), locked = it.optBoolean("locked", false),
            ))
        }
    }

    private fun JSONArray.toLayers() = buildList {
        for (i in 0 until length()) {
            val it = optJSONObject(i) ?: continue
            add(WidgetLayer(
                id = it.optLong("id", 1_000_000L + i), shape = enumOr(it.optString("shape"), WidgetShape.ROUNDED),
                x = it.optFloat("x", 0f), y = it.optFloat("y", 0f),
                width = it.optFloat("width", .86f), height = it.optFloat("height", .64f),
                dayColor = it.optLong("dayColor", 0x205D5774), nightColor = it.optLong("nightColor", 0x42473D5D),
                textColor = it.optLong("textColor", 0xFFFFFFFF), borderWidth = it.optFloat("borderWidth", 1f),
                borderOpacity = it.optFloat("borderOpacity", .32f),
                softness = it.optFloat("softness", 16f), glowOpacity = it.optFloat("glowOpacity", .10f),
                shadow = it.optFloat("shadow", 8f), shadowOpacity = it.optFloat("shadowOpacity", .16f),
                shadowYOffset = it.optFloat("shadowYOffset", 2f),
                rotation = it.optFloat("rotation", 0f), locked = it.optBoolean("locked", false),
                // Layers saved before materials existed are flat fills.
                material = it.optString("material", MATERIAL_SOLID).ifBlank { MATERIAL_SOLID },
            ))
        }
    }

    /** A malformed city is skipped rather than discarding the whole project. */
    private fun JSONArray.toCities() = buildList {
        for (i in 0 until length()) {
            val it = optJSONObject(i) ?: continue
            val name = it.optString("name", "")
            val zone = it.optString("zoneId", "")
            if (name.isBlank() || zone.isBlank() || !it.has("latitude") || !it.has("longitude")) continue
            add(City(
                name = name, country = it.optString("country"), latitude = it.optDouble("latitude"), longitude = it.optDouble("longitude"),
                zoneId = zone, nickname = it.optString("nickname"), geoId = it.optLong("geoId"),
                x = it.optFloat("x", 0f), y = it.optFloat("y", 0f), scale = it.optFloat("scale", 1f),
                showTime = it.optBoolean("showTime", true), showDate = it.optBoolean("showDate", true),
                showCountry = it.optBoolean("showCountry", false), showSolar = it.optBoolean("showSolar", true),
                nameColor = it.optLong("nameColor", 0xFFFFFFFF), timeColor = it.optLong("timeColor", 0xFFFFFFFF),
                dateColor = it.optLong("dateColor", 0xCCFFFFFF), solarColor = it.optLong("solarColor", 0xFFEFE8F7),
                iconStyle = enumOr(it.optString("iconStyle"), SolarIconStyle.LINE),
                nameFont = it.optString("nameFont", "SANS_ROUNDED"), timeFont = it.optString("timeFont", "SANS_ROUNDED"),
                dateFont = it.optString("dateFont", "SANS_LIGHT"),
                sunStyle = enumOr(it.optString("sunStyle"), SunIconStyle.LUCIDE_SUN),
                moonStyle = enumOr(it.optString("moonStyle"), MoonIconStyle.LUCIDE_MOON),
                sunColor = it.optLong("sunColor", 0xFFE8DFD2), moonColor = it.optLong("moonColor", 0xFFD9D5EA),
                componentId = it.optLong("componentId", 0L), rotation = it.optFloat("rotation", 0f), locked = it.optBoolean("locked", false),
            ))
        }
    }

    internal fun arrangeLegacyCities(cities: List<City>): List<City> {
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

    private fun JSONObject.optFloat(key: String, fallback: Float): Float {
        val value = optDouble(key, fallback.toDouble())
        return if (value.isNaN() || value.isInfinite()) fallback else value.toFloat()
    }

    private inline fun <reified T : Enum<T>> enumOr(name: String?, fallback: T): T =
        if (name.isNullOrBlank()) fallback else runCatching { enumValueOf<T>(name) }.getOrDefault(fallback)
}
