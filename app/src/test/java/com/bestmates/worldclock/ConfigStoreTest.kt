package com.bestmates.worldclock

import com.bestmates.worldclock.model.*
import org.junit.Assert.*
import org.junit.Test

class ConfigStoreTest {
    
    @Test
    fun `WidgetConfig defaults are backward compatible`() {
        val config = WidgetConfig()
        assertEquals(8, config.layoutVersion)
        assertEquals(WidgetShape.ROUNDED, config.shape)
        assertEquals(ThemeMode.SYSTEM, config.theme)
        assertTrue(config.use24Hour)
        assertTrue(config.showDate)
    }

    @Test
    fun `City defaults contain safe fallbacks`() {
        val city = City("Test", "Country", 0.0, 0.0, "UTC")
        assertEquals(SunIconStyle.LUCIDE_SUN, city.sunStyle)
        assertEquals(MoonIconStyle.LUCIDE_MOON, city.moonStyle)
    }
}
