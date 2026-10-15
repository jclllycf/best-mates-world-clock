package com.bestmates.worldclock.solar

import com.bestmates.worldclock.model.MoonIconStyle
import com.bestmates.worldclock.model.SunIconStyle

/**
 * Status of every sun and moon style (BM-REAL-01 audit).
 *
 * * [Status.DAYBOOK] — the original Daybook set, the only styles offered for new choices.
 * * [Status.REDRAWN] — an older Best Mates style with a Daybook counterpart. Hidden from the
 *   picker; saved designs keep the identifier, and [daybookSun]/[daybookMoon] name the
 *   counterpart the picker suggests.
 * * [Status.RETIRED] — kept only so saved designs still decode and render (third-party
 *   packs and weather variants without a counterpart). Never offered for new choices.
 */
object SolarCatalog {
    enum class Status { DAYBOOK, REDRAWN, RETIRED }

    private val sunRedraws = mapOf(
        SunIconStyle.BM_SUN_SIMPLE_FILL to SunIconStyle.DB_SUN_DISC,
        SunIconStyle.BM_SUN_SIMPLE_ROUND to SunIconStyle.DB_SUN_RING,
        SunIconStyle.BM_SUN_WAVE_FILL to SunIconStyle.DB_SUN_WAVES,
        SunIconStyle.BM_SUN_WAVE_OUTLINE to SunIconStyle.DB_SUN_WAVES,
        SunIconStyle.BM_SUN_CLOUD_FILL to SunIconStyle.DB_SUN_CLOUD,
        SunIconStyle.BM_SUN_CLOUD_OUTLINE to SunIconStyle.DB_SUN_CLOUD_LINE,
        SunIconStyle.BM_SUN_HORIZON_FILL to SunIconStyle.DB_SUN_HORIZON,
        SunIconStyle.BM_SUN_HORIZON_OUTLINE to SunIconStyle.DB_SUN_HORIZON,
        SunIconStyle.BM_SUNRISE_ARROW to SunIconStyle.DB_SUN_RISE,
        SunIconStyle.BM_SUNSET_ARROW to SunIconStyle.DB_SUN_SET,
        SunIconStyle.BM_SUN_MIST to SunIconStyle.DB_SUN_HAZE,
    )

    private val moonRedraws = mapOf(
        MoonIconStyle.BM_MOON_PLAIN_FILL to MoonIconStyle.DB_MOON_CRESCENT,
        MoonIconStyle.BM_MOON_PLAIN_OUTLINE to MoonIconStyle.DB_MOON_CRESCENT_LINE,
        MoonIconStyle.BM_MOON_CLOUD_FILL to MoonIconStyle.DB_MOON_CLOUD,
        MoonIconStyle.BM_MOON_CLOUD_SOFT to MoonIconStyle.DB_MOON_CLOUD,
        MoonIconStyle.BM_MOON_CLOUD_OUTLINE to MoonIconStyle.DB_MOON_CLOUD_LINE,
        MoonIconStyle.BM_MOON_DOUBLE to MoonIconStyle.DB_MOON_DUO,
        MoonIconStyle.BM_MOON_STARS_FILL to MoonIconStyle.DB_MOON_STARS,
        MoonIconStyle.BM_MOON_STARS_OUTLINE to MoonIconStyle.DB_MOON_STARS_LINE,
        MoonIconStyle.BM_MOON_HORIZON_FILL to MoonIconStyle.DB_MOON_HORIZON,
        MoonIconStyle.BM_MOON_HORIZON_STARS to MoonIconStyle.DB_MOON_HORIZON,
        MoonIconStyle.BM_MOON_SLEEP_FILL to MoonIconStyle.DB_MOON_SLEEP,
        MoonIconStyle.BM_MOON_SLEEP_OUTLINE to MoonIconStyle.DB_MOON_SLEEP,
        MoonIconStyle.BM_CRESCENT_RING to MoonIconStyle.DB_MOON_RING,
        MoonIconStyle.BM_ISLAMIC_CRESCENT_STAR to MoonIconStyle.DB_MOON_STAR5,
    )

    fun status(style: SunIconStyle) = when {
        style.name.startsWith("DB_") -> Status.DAYBOOK
        style in sunRedraws -> Status.REDRAWN
        else -> Status.RETIRED
    }

    fun status(style: MoonIconStyle) = when {
        style.name.startsWith("DB_") -> Status.DAYBOOK
        style in moonRedraws -> Status.REDRAWN
        else -> Status.RETIRED
    }

    fun daybookSun(style: SunIconStyle): SunIconStyle? = if (status(style) == Status.DAYBOOK) style else sunRedraws[style]
    fun daybookMoon(style: MoonIconStyle): MoonIconStyle? = if (status(style) == Status.DAYBOOK) style else moonRedraws[style]

    val pickerSuns: List<SunIconStyle> = SunIconStyle.entries.filter { status(it) == Status.DAYBOOK }
    val pickerMoons: List<MoonIconStyle> = MoonIconStyle.entries.filter { status(it) == Status.DAYBOOK }
}
