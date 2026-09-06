package com.bestmates.worldclock.model

enum class WidgetShape(val label: String, val designAspect: Float) {
    ROUNDED("柔雾卡片", 1.35f),
    SQUIRCLE("柔角方", 1f),
    PILL("柔和拱窗", 1.35f),
    CAPSULE("胶囊", 1.9f),
    CIRCLE("圆月", 1f),
    CLOUD("云朵", 1.6f),
    DIAMOND("四瓣花", 1f),
    PETAL("花瓣", .88f),
    HEART("爱心", 1f),
    DROP("水滴", .82f),
    TICKET("票券", 1.45f),
    WAVE("旗帜", 1.5f)
}

enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class SolarIconStyle { LINE, CLASSIC, SPARKLE, MINIMAL }
/** Every entry points to a real, bundled open-source SVG. */
enum class SunIconStyle(val asset: String) {
    BM_SUN_WAVE_FILL("bm_sun_wave_fill.svg"),
    BM_SUN_WAVE_OUTLINE("bm_sun_wave_outline.svg"),
    BM_SUN_CLOUD_FILL("bm_sun_cloud_fill.svg"),
    BM_SUN_CLOUD_OUTLINE("bm_sun_cloud_outline.svg"),
    BM_SUN_SIMPLE_FILL("bm_sun_simple_fill.svg"),
    BM_SUN_SIMPLE_ROUND("bm_sun_simple_round.svg"),
    BM_SUN_HORIZON_FILL("bm_sun_horizon_fill.svg"),
    BM_SUN_HORIZON_OUTLINE("bm_sun_horizon_outline.svg"),
    BM_SUNRISE_ARROW("bm_sunrise_arrow.svg"),
    BM_SUNSET_ARROW("bm_sunset_arrow.svg"),
    BM_SUN_MIST("bm_sun_mist.svg"),
    BM_SUN_CLOUD_RAIN("bm_sun_cloud_rain.svg"),
    LUCIDE_SUN("lucide_sun.svg"),
    LUCIDE_SUNRISE("lucide_sunrise.svg"),
    LUCIDE_SUNSET("lucide_sunset.svg"),
    LUCIDE_CLOUD_SUN("lucide_cloud-sun.svg"),
    TABLER_SUN("tabler_sun.svg"),
    TABLER_SUN_HIGH("tabler_sun-high.svg"),
    TABLER_SUNRISE("tabler_sunrise.svg"),
    TABLER_SUNSET("tabler_sunset.svg"),
    PHOSPHOR_SUN("ph_sun.svg"),
    PHOSPHOR_SUN_DIM("ph_sun-dim.svg"),
    PHOSPHOR_SUN_HORIZON("ph_sun-horizon.svg"),
    PHOSPHOR_CLOUD_SUN("ph_cloud-sun.svg"),
    FLUENT_SUN("fluent_weather-sunny-24-regular.svg"),
    FLUENT_SUN_HIGH("fluent_weather-sunny-high-24-regular.svg"),
    FLUENT_SUN_LOW("fluent_weather-sunny-low-24-regular.svg"),
    FLUENT_PARTLY_CLOUDY_DAY("fluent_weather-partly-cloudy-day-24-regular.svg"),
    ICONOIR_SUN_LIGHT("iconoir_sun-light.svg"),
    MDI_SUN("mdi_weather-sunny.svg"),
    MDI_SUNRISE("mdi_weather-sunset-up.svg"),
    MDI_SUNSET("mdi_weather-sunset-down.svg"),
    MDI_PARTLY_CLOUDY("mdi_weather-partly-cloudy.svg"),
    METEO_CLEAR_DAY("meteocons_clear-day.svg"),
    METEO_MOSTLY_CLEAR_DAY("meteocons_mostly-clear-day.svg"),
    METEO_PARTLY_CLOUDY_DAY("meteocons_partly-cloudy-day.svg"),
    METEO_FOG_DAY("meteocons_fog-day.svg"),
    METEO_HAZE_DAY("meteocons_haze-day.svg"),
    METEO_SUN_HOT("meteocons_sun-hot.svg"),
    METEO_SUNRISE("meteocons_sunrise.svg"),
    METEO_SUNSET("meteocons_sunset.svg"),
    METEO_DAY_DRIZZLE("meteocons_partly-cloudy-day-drizzle.svg"),
    METEO_DAY_RAIN("meteocons_partly-cloudy-day-rain.svg"),
    METEO_DAY_SNOW("meteocons_partly-cloudy-day-snow.svg"),
    METEO_DAY_THUNDER("meteocons_thunderstorms-day.svg")
}

enum class MoonIconStyle(val asset: String) {
    BM_MOON_CLOUD_FILL("bm_moon_cloud_fill.svg"),
    BM_MOON_CLOUD_SOFT("bm_moon_cloud_soft.svg"),
    BM_MOON_CLOUD_OUTLINE("bm_moon_cloud_outline.svg"),
    BM_MOON_PLAIN_FILL("bm_moon_plain_fill.svg"),
    BM_MOON_PLAIN_OUTLINE("bm_moon_plain_outline.svg"),
    BM_MOON_DOUBLE("bm_moon_double.svg"),
    BM_MOON_STARS_FILL("bm_moon_stars_fill.svg"),
    BM_MOON_STARS_OUTLINE("bm_moon_stars_outline.svg"),
    BM_MOON_HORIZON_FILL("bm_moon_horizon_fill.svg"),
    BM_MOON_HORIZON_STARS("bm_moon_horizon_stars.svg"),
    BM_MOON_SLEEP_FILL("bm_moon_sleep_fill.svg"),
    BM_MOON_SLEEP_OUTLINE("bm_moon_sleep_outline.svg"),
    BM_CRESCENT_RING("bm_crescent_ring.svg"),
    BM_ISLAMIC_CRESCENT_STAR("bm_islamic_crescent_star.svg"),
    BM_MOON_CLOUD_RAIN("bm_moon_cloud_rain.svg"),
    BM_MOON_CLOUD_SNOW("bm_moon_cloud_snow.svg"),
    LUCIDE_MOON("lucide_moon.svg"),
    LUCIDE_MOON_STAR("lucide_moon-star.svg"),
    LUCIDE_CLOUD_MOON("lucide_cloud-moon.svg"),
    LUCIDE_ECLIPSE("lucide_eclipse.svg"),
    TABLER_MOON("tabler_moon.svg"),
    TABLER_MOON_STARS("tabler_moon-stars.svg"),
    PHOSPHOR_MOON("ph_moon.svg"),
    PHOSPHOR_MOON_STARS("ph_moon-stars.svg"),
    PHOSPHOR_CLOUD_MOON("ph_cloud-moon.svg"),
    PHOSPHOR_STAR_CRESCENT("ph_star-and-crescent.svg"),
    FLUENT_MOON("fluent_weather-moon-24-regular.svg"),
    FLUENT_PARTLY_CLOUDY_NIGHT("fluent_weather-partly-cloudy-night-24-regular.svg"),
    ICONOIR_HALF_MOON("iconoir_half-moon.svg"),
    MDI_WAXING_CRESCENT("mdi_moon-waxing-crescent.svg"),
    MDI_WANING_CRESCENT("mdi_moon-waning-crescent.svg"),
    MDI_WEATHER_NIGHT("mdi_weather-night.svg"),
    MDI_STAR_CRESCENT("mdi_star-crescent.svg"),
    METEO_CLEAR_NIGHT("meteocons_clear-night.svg"),
    METEO_STARRY_NIGHT("meteocons_starry-night.svg"),
    METEO_MOSTLY_CLEAR_NIGHT("meteocons_mostly-clear-night.svg"),
    METEO_PARTLY_CLOUDY_NIGHT("meteocons_partly-cloudy-night.svg"),
    METEO_FOG_NIGHT("meteocons_fog-night.svg"),
    METEO_HAZE_NIGHT("meteocons_haze-night.svg"),
    METEO_WAXING_CRESCENT("meteocons_moon-waxing-crescent.svg"),
    METEO_WANING_CRESCENT("meteocons_moon-waning-crescent.svg"),
    METEO_MOONRISE("meteocons_moonrise.svg"),
    METEO_MOONSET("meteocons_moonset.svg"),
    METEO_NIGHT_DRIZZLE("meteocons_partly-cloudy-night-drizzle.svg"),
    METEO_NIGHT_RAIN("meteocons_partly-cloudy-night-rain.svg"),
    METEO_NIGHT_SNOW("meteocons_partly-cloudy-night-snow.svg"),
    METEO_NIGHT_THUNDER("meteocons_thunderstorms-night.svg")
}

data class CustomText(
    val content: String = "",
    val size: Float = 14f,
    val color: Long = 0xFFFFFFFF,
    val alpha: Float = 1f,
    val x: Float = 0f,
    val y: Float = 0f,
    val font: String = "SANS",
    val componentId: Long = 0L,
    val rotation: Float = 0f,
    val locked: Boolean = false
)

data class City(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val zoneId: String,
    val nickname: String = "",
    val geoId: Long = 0,
    val x: Float = 0f,
    val y: Float = 0f,
    val scale: Float = 1f,
    val showTime: Boolean = true,
    val showDate: Boolean = true,
    val showCountry: Boolean = false,
    val showSolar: Boolean = true,
    val nameColor: Long = 0xFFFFFFFF,
    val timeColor: Long = 0xFFFFFFFF,
    val dateColor: Long = 0xCCFFFFFF,
    val solarColor: Long = 0xFFEFE8F7,
    val iconStyle: SolarIconStyle = SolarIconStyle.LINE,
    val nameFont: String = "SANS_ROUNDED",
    val timeFont: String = "SANS_ROUNDED",
    val dateFont: String = "SANS_LIGHT",
    val sunStyle: SunIconStyle = SunIconStyle.LUCIDE_SUN,
    val moonStyle: MoonIconStyle = MoonIconStyle.LUCIDE_MOON,
    val sunColor: Long = 0xFFE8DFD2,
    val moonColor: Long = 0xFFD9D5EA,
    val componentId: Long = 0L,
    val rotation: Float = 0f,
    val locked: Boolean = false
)

data class WidgetLayer(
    val id: Long = System.nanoTime(),
    val shape: WidgetShape = WidgetShape.ROUNDED,
    val x: Float = 0f,
    val y: Float = 0f,
    val width: Float = .86f,
    val height: Float = .64f,
    val dayColor: Long = 0x205D5774,
    val nightColor: Long = 0x42473D5D,
    val textColor: Long = 0xFFFFFFFF,
    val borderWidth: Float = 1f,
    val borderOpacity: Float = .32f,
    val softness: Float = 16f,
    val glowOpacity: Float = .10f,
    val shadow: Float = 8f,
    val shadowOpacity: Float = .16f,
    val shadowYOffset: Float = 2f,
    val rotation: Float = 0f,
    val locked: Boolean = false
)

fun newWidgetLayer(shape: WidgetShape = WidgetShape.ROUNDED, index: Int = 0): WidgetLayer {
    // A new shape occupies the largest natural rectangle available on the 4x4 board.
    // The user can then reshape it freely in whole-cell steps.
    val maxSide = 1f
    val width: Float
    val height: Float
    if (shape.designAspect >= 1f) {
        width = maxSide
        height = maxSide / shape.designAspect
    } else {
        height = maxSide
        width = maxSide * shape.designAspect
    }
    return WidgetLayer(shape = shape, width = width, height = height)
}

data class WidgetConfig(
    val layoutVersion: Int = 8,
    val shape: WidgetShape = WidgetShape.ROUNDED,
    val columns: Int = 4,
    val rows: Int = 4,
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val title: String = "",
    val subtitle: String = "",
    val use24Hour: Boolean = true,
    val showDate: Boolean = true,
    // Overall display gates stay enabled. Each city owns its own visibility switch.
    val showCountry: Boolean = true,
    val showSolarIcon: Boolean = true,
    val showTime: Boolean = true,
    val dayColor: Long = 0x205D5774,
    val nightColor: Long = 0x42473D5D,
    val textColor: Long = 0xFFFFFFFF,
    val borderWidth: Float = 1f,
    val borderOpacity: Float = .32f,
    val softness: Float = 16f,
    val glowOpacity: Float = .10f,
    val shadow: Float = 8f,
    val shadowOpacity: Float = .16f,
    val shadowYOffset: Float = 2f,
    val locked: Boolean = false,
    val autoSave: Boolean = false,
    val layers: List<WidgetLayer> = emptyList(),
    val texts: List<CustomText> = emptyList(),
    val cities: List<City> = emptyList()
)
