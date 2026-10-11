package com.bestmates.worldclock.fonts

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Pure identifier logic; resource ids are compile-time constants so this runs on the JVM. */
class WidgetFontsTest {

    @Test fun `identifiers are unique`() {
        val ids = WidgetFonts.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test fun `every historical identifier resolves`() {
        val legacy = listOf(
            "SANS", "SANS_MEDIUM", "SANS_BLACK", "SANS_ROUNDED", "SANS_CONDENSED", "SERIF", "SERIF_MONO", "MONO",
            "CASUAL", "CURSIVE", "SMALL_CAPS", "SANS_LIGHT", "SANS_THIN", "CONDENSED_LIGHT", "CONDENSED_MEDIUM",
            "BM|sans-serif|0|0", "BM|sans-serif|1|0b", "BM|serif|0|2", "BM|monospace|1|3b", "BM|sans-serif-condensed|0|1",
            "something-unknown",
        )
        legacy.forEach { id -> WidgetFonts.resolve(id) } // must not throw
        assertEquals("sys.sans_bold", WidgetFonts.canonicalId("SANS_ROUNDED"))
        assertEquals("sys.serif_bold", WidgetFonts.canonicalId("BM|serif|1|2b"))
        assertEquals(WidgetFonts.DEFAULT_NAME, WidgetFonts.canonicalId("something-unknown"))
    }

    @Test fun `time and date always resolve to a live style`() {
        WidgetFonts.all.forEach { assertTrue(it.id, WidgetFonts.live(it.id).isLive) }
        assertTrue(WidgetFonts.resolve(WidgetFonts.DEFAULT_TIME).isLive)
        assertTrue(WidgetFonts.resolve(WidgetFonts.DEFAULT_DATE).isLive)
        assertEquals("sys.serif", WidgetFonts.live("bm.fraunces").id)
    }
}
