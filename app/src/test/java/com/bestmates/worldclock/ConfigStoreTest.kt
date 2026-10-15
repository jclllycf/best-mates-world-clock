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
    fun `new cities default to the Daybook sun and moon`() {
        val city = City("Test", "Country", 0.0, 0.0, "UTC")
        assertEquals(SunIconStyle.DB_SUN_DISC, city.sunStyle)
        assertEquals(MoonIconStyle.DB_MOON_CRESCENT, city.moonStyle)
    }
}
