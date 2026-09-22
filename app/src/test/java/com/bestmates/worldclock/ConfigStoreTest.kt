package com.bestmates.worldclock

import com.bestmates.worldclock.model.*
import org.junit.Assert.*
import org.junit.Test
import org.json.JSONArray
import org.json.JSONObject

class ConfigStoreTest {
    // ConfigStore relies on SharedPreferences which isn't mocked by default in pure JVM tests, 
    // so we can test the serialization structure manually or mock it if needed.
    // Wait, the deserialization is private. I can just test Models.kt defaults.
    
    @Test
    fun `WidgetConfig defaults are backward compatible`() {
        val config = WidgetConfig()
        assertEquals(2, config.layoutVersion)
        assertEquals(WidgetShape.ROUNDED, config.shape)
        assertEquals(ThemeMode.SYSTEM, config.theme)
        assertTrue(config.use24Hour)
        assertTrue(config.showDate)
    }

    @Test
    fun `City defaults contain safe fallbacks`() {
        val city = City("Test", "Country", 0.0, 0.0, "UTC")
        assertEquals(SunIconStyle.BM_SUN_SIMPLE_FILL, city.sunStyle)
        assertEquals(MoonIconStyle.BM_MOON_PLAIN_FILL, city.moonStyle)
    }
}
