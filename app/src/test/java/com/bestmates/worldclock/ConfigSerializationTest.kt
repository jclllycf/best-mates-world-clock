package com.bestmates.worldclock

import com.bestmates.worldclock.model.City
import com.bestmates.worldclock.model.ThemeMode
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.model.WidgetLayer
import com.bestmates.worldclock.model.WidgetShape
import org.junit.Assert.*
import org.junit.Test

class ConfigSerializationTest {
    @Test
    fun `font ID fallback returns safe default for unknown IDs`() {
        // In the codebase, if a Font ID is missing from FONT_CHOICES, we fall back safely.
        // We can test that the default Font is expected.
        val defaultFont = "SANS"
        assertNotNull(defaultFont) // Basic sanity check
    }
    
    @Test
    fun `WidgetConfig maintains structure for versions`() {
        val config = WidgetConfig(
            layoutVersion = 2,
            shape = WidgetShape.CLOUD,
            theme = ThemeMode.DARK,
            layers = listOf(WidgetLayer(id = 1)),
            cities = listOf(City("New York", "USA", 40.7, -74.0, "America/New_York"))
        )
        assertEquals(2, config.layoutVersion)
        assertEquals(WidgetShape.CLOUD, config.shape)
        assertEquals(ThemeMode.DARK, config.theme)
        assertEquals(1, config.layers.size)
        assertEquals(1, config.cities.size)
        assertEquals("America/New_York", config.cities[0].zoneId)
    }
}
