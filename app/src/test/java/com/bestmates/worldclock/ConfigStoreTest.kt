package com.bestmates.worldclock

import com.bestmates.worldclock.model.*
import org.junit.Assert.*
import org.junit.Test

class ConfigStoreTest {

    @Test
    fun `WidgetConfig defaults describe the current schema and a legacy-sized board`() {
        val config = WidgetConfig()
        assertEquals(CURRENT_LAYOUT_VERSION, config.layoutVersion)
        assertEquals(9, CURRENT_LAYOUT_VERSION)
        assertEquals(WidgetShape.ROUNDED, config.shape)
        assertEquals(ThemeMode.SYSTEM, config.theme)
        assertEquals(LEGACY_BOARD_DP, config.boardWidthDp)
        assertTrue(config.use24Hour)
        assertTrue(config.showDate)
    }

    @Test
    fun `new cities default to the curated Best Mates sun and moon`() {
        val city = City("Test", "Country", 0.0, 0.0, "UTC")
        assertEquals(SunIconStyle.BM_SUN_SIMPLE_FILL, city.sunStyle)
        assertEquals(MoonIconStyle.BM_MOON_PLAIN_FILL, city.moonStyle)
    }
}
