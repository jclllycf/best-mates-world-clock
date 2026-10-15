package com.bestmates.worldclock.solar

import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/** BM-REAL-01: every saved identifier still resolves; the picker offers only the Daybook set. */
class SolarCatalogTest {
    private val assets = listOf(File("src/main/assets/solar_icons"), File("app/src/main/assets/solar_icons")).first { it.isDirectory }

    @Test
    fun everyStyle_hasItsArtwork() {
        (SunIconStyle.entries.map { it.asset } + MoonIconStyle.entries.map { it.asset }).forEach {
            assertTrue("missing $it", File(assets, it).isFile)
        }
    }

    @Test
    fun picker_offersOnlyDaybook() {
        assertEquals(15, SolarCatalog.pickerSuns.size)
        assertEquals(14, SolarCatalog.pickerMoons.size)
        assertTrue(SolarCatalog.pickerSuns.all { it.asset.startsWith("db_") })
        assertTrue(SolarCatalog.pickerMoons.all { it.asset.startsWith("db_") })
    }

    @Test
    fun redraws_pointAtDaybook_andKeepTheirIdentifier() {
        SunIconStyle.entries.filter { SolarCatalog.status(it) == SolarCatalog.Status.REDRAWN }.forEach {
            assertEquals(SolarCatalog.Status.DAYBOOK, SolarCatalog.status(SolarCatalog.daybookSun(it)!!))
            assertEquals(it, SunIconStyle.valueOf(it.name))
        }
        MoonIconStyle.entries.filter { SolarCatalog.status(it) == SolarCatalog.Status.REDRAWN }.forEach {
            assertEquals(SolarCatalog.Status.DAYBOOK, SolarCatalog.status(SolarCatalog.daybookMoon(it)!!))
        }
        assertEquals(SunIconStyle.DB_SUN_DISC, SolarCatalog.daybookSun(SunIconStyle.BM_SUN_SIMPLE_FILL))
        assertEquals(MoonIconStyle.DB_MOON_CRESCENT, SolarCatalog.daybookMoon(MoonIconStyle.BM_MOON_PLAIN_FILL))
    }

    @Test
    fun daybookArtwork_usesTheSharedGrid() {
        assets.listFiles { f -> f.name.startsWith("db_") }!!.forEach {
            val svg = it.readText()
            assertTrue("${it.name} must use the 48 grid", svg.contains("viewBox=\"0 0 48 48\""))
        }
    }
}
