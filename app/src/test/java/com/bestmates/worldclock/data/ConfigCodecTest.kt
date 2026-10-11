package com.bestmates.worldclock.data

import com.bestmates.worldclock.model.CURRENT_LAYOUT_VERSION
import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.CustomText
import com.bestmates.worldclock.model.LEGACY_BOARD_DP
import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import com.bestmates.worldclock.model.ThemeMode
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Runs the real migration ladder on payloads in the exact shape earlier builds wrote.
 * The v8 sample is byte-for-byte the format produced by ConfigStore up to v0.3.
 */
class ConfigCodecTest {

    private val v8 = """
        {"layoutVersion":8,"shape":"ROUNDED","columns":4,"rows":4,"theme":"SYSTEM","title":"Tokyo & Paris","subtitle":"",
         "use24Hour":false,"showDate":true,"showCountry":true,"showSolarIcon":true,"showTime":true,"dayColor":543512436,
         "nightColor":1112227165,"textColor":4294967295,"borderWidth":1,"borderOpacity":0.32,"softness":16,"glowOpacity":0.1,
         "shadow":8,"shadowOpacity":0.16,"shadowYOffset":2,"locked":false,"autoSave":true,
         "layers":[{"id":1001,"shape":"HEART","x":0,"y":0,"width":0.95,"height":0.93,"dayColor":543512436,"nightColor":1112227165,
           "textColor":4294967295,"borderWidth":1,"borderOpacity":0.32,"softness":16,"glowOpacity":0.1,"shadow":8,"shadowOpacity":0.16,
           "shadowYOffset":2,"rotation":12.5,"locked":true}],
         "texts":[{"content":"Same sky","size":12,"color":4294967295,"alpha":0.8,"x":0,"y":-0.55,"font":"SERIF","componentId":1001,"rotation":-8,"locked":false}],
         "cities":[{"name":"Tokyo","country":"Japan","latitude":35.6895,"longitude":139.6917,"zoneId":"Asia/Tokyo","nickname":"Mum",
           "geoId":1850147,"x":-0.48,"y":0.1,"scale":1.2,"showTime":true,"showDate":false,"showCountry":true,"showSolar":true,
           "nameColor":4294967295,"timeColor":4294967295,"dateColor":3439329279,"solarColor":4293912823,"iconStyle":"LINE",
           "nameFont":"SANS_ROUNDED","timeFont":"SANS_ROUNDED","dateFont":"SANS_LIGHT","sunStyle":"LUCIDE_SUN","moonStyle":"BM_ISLAMIC_CRESCENT_STAR",
           "sunColor":4293451730,"moonColor":4292466154,"componentId":1001,"rotation":0,"locked":false}]}
    """.trimIndent()

    @Test fun `v8 project migrates to the current schema without losing user data`() {
        val c = ConfigCodec.decode(v8)
        assertEquals(CURRENT_LAYOUT_VERSION, c.layoutVersion)
        assertEquals("Tokyo & Paris", c.title)
        assertEquals(false, c.use24Hour)
        assertEquals(true, c.autoSave)
        assertEquals(4, c.columns); assertEquals(4, c.rows)
        assertEquals(LEGACY_BOARD_DP, c.boardWidthDp); assertEquals(LEGACY_BOARD_DP, c.boardHeightDp)
        // v<8 heart migration must not touch a v8 payload.
        val layer = c.layers.single()
        assertEquals(WidgetShape.HEART, layer.shape)
        assertEquals(.95f, layer.width, 1e-6f); assertEquals(.93f, layer.height, 1e-6f)
        assertEquals(12.5f, layer.rotation, 1e-6f); assertTrue(layer.locked)
        val city = c.cities.single()
        assertEquals("Mum", city.nickname); assertEquals(1850147L, city.geoId)
        assertEquals(-.48f, city.x, 1e-6f); assertEquals(.1f, city.y, 1e-6f); assertEquals(1.2f, city.scale, 1e-6f)
        assertEquals(false, city.showDate)
        assertEquals(SunIconStyle.LUCIDE_SUN, city.sunStyle)
        assertEquals(MoonIconStyle.BM_ISLAMIC_CRESCENT_STAR, city.moonStyle)
        val text = c.texts.single()
        assertEquals(-8f, text.rotation, 1e-6f); assertEquals(.8f, text.alpha, 1e-6f)
    }

    @Test fun `legacy font identifiers become current styles that keep their old look`() {
        val c = ConfigCodec.decode(v8)
        assertEquals("sys.sans_bold", c.cities[0].nameFont)
        assertEquals("sys.sans_bold", c.cities[0].timeFont)
        assertEquals("sys.sans_light", c.cities[0].dateFont)
        assertEquals("sys.serif", c.texts[0].font)
    }

    @Test fun `v1 payload arranges cities, drops placeholder text and gains a background`() {
        val v1 = """
            {"shape":"CLOUD","title":"BEST MATES","subtitle":"Same sky · Different time",
             "texts":[{"content":"新文字","x":0,"y":0},{"content":"keep","x":0.1,"y":0.2}],
             "cities":[{"name":"A","latitude":1,"longitude":2,"zoneId":"UTC"},{"name":"B","latitude":1,"longitude":2,"zoneId":"UTC"},{"name":"C","latitude":1,"longitude":2,"zoneId":"UTC"}]}
        """.trimIndent()
        val c = ConfigCodec.decode(v1)
        assertEquals("", c.title); assertEquals("", c.subtitle)
        assertEquals(listOf("keep"), c.texts.map { it.content })
        assertEquals(1, c.layers.size); assertEquals(WidgetShape.CLOUD, c.layers[0].shape)
        assertEquals(listOf(0f, -.48f, .48f), c.cities.map { it.x })
        assertEquals(listOf(-.28f, .46f, .46f), c.cities.map { it.y })
    }

    @Test fun `v7 near-full heart expands to the board while small hearts are preserved`() {
        val v7 = """{"layoutVersion":7,"layers":[{"id":1,"shape":"HEART","width":0.92,"height":0.91,"x":0.1,"y":0.1},{"id":2,"shape":"HEART","width":0.4,"height":0.4,"x":0.3,"y":0.3}],"cities":[]}"""
        val c = ConfigCodec.decode(v7)
        assertEquals(1f, c.layers[0].width); assertEquals(0f, c.layers[0].x)
        assertEquals(.4f, c.layers[1].width, 1e-6f); assertEquals(.3f, c.layers[1].x, 1e-6f)
    }

    @Test fun `v7 and later keep an intentionally empty scene`() {
        val c = ConfigCodec.decode("""{"layoutVersion":7,"layers":[],"cities":[]}""")
        assertTrue(c.layers.isEmpty())
    }

    @Test fun `a malformed city is skipped instead of discarding the project`() {
        val raw = """{"layoutVersion":8,"title":"x","cities":[{"name":"Broken"},{"name":"Oslo","latitude":59.9,"longitude":10.7,"zoneId":"Europe/Oslo"}]}"""
        val c = ConfigCodec.decode(raw)
        assertEquals("x", c.title)
        assertEquals(listOf("Oslo"), c.cities.map { it.name })
    }

    @Test fun `unknown enum values fall back safely`() {
        val raw = """{"layoutVersion":9,"theme":"SEPIA","layers":[{"shape":"STAR"}],"cities":[{"name":"Oslo","latitude":59.9,"longitude":10.7,"zoneId":"Europe/Oslo","sunStyle":"NOPE","moonStyle":"NOPE"}]}"""
        val c = ConfigCodec.decode(raw)
        assertEquals(ThemeMode.SYSTEM, c.theme)
        assertEquals(WidgetShape.ROUNDED, c.layers[0].shape)
        assertEquals(SunIconStyle.LUCIDE_SUN, c.cities[0].sunStyle)
        assertEquals(MoonIconStyle.LUCIDE_MOON, c.cities[0].moonStyle)
    }

    @Test fun `current schema round-trips exactly`() {
        val original = WidgetConfig(
            title = "Duo", columns = 4, rows = 2, boardWidthDp = 344f, boardHeightDp = 196f, theme = ThemeMode.DARK,
            createdAt = 1_700_000_000_000L, updatedAt = 1_700_000_500_000L, backdrop = "dusk", use24Hour = false,
            layers = listOf(WidgetLayer(id = 7, shape = WidgetShape.TICKET, x = -.2f, y = .1f, width = .7f, height = .5f, rotation = 33f, locked = true)),
            texts = listOf(CustomText(content = "hi\nthere", size = 18f, font = "bm.caveat", rotation = 4f)),
            cities = listOf(City("Lima", "Peru", -12.04, -77.03, "America/Lima", nickname = "Ana", geoId = 3936456, x = .4f, y = -.3f, scale = .8f, timeFont = "sys.serif")),
        )
        val decoded = ConfigCodec.decode(ConfigCodec.encode(original))
        assertEquals(original, decoded)
    }

    @Test fun `schema 9 keeps the chosen widget size and board`() {
        val c = ConfigCodec.decode("""{"layoutVersion":9,"columns":2,"rows":1,"boardWidthDp":172,"boardHeightDp":98,"cities":[]}""")
        assertEquals(2, c.columns); assertEquals(1, c.rows)
        assertEquals(172f, c.boardWidthDp); assertEquals(98f, c.boardHeightDp)
    }
}
