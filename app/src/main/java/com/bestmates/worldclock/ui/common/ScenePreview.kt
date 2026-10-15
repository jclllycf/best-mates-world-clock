package com.bestmates.worldclock.ui.common

import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.scene.DrawOptions
import com.bestmates.worldclock.scene.Scene
import com.bestmates.worldclock.scene.SceneBuilder
import com.bestmates.worldclock.scene.SceneGeometry
import com.bestmates.worldclock.scene.SceneRenderer
import kotlinx.coroutines.delay
import java.time.Instant

/** App-wide scene services; the renderer owns bitmap caches that should be shared. */
object SceneServices {
    @Volatile private var renderer: SceneRenderer? = null
    @Volatile private var builder: SceneBuilder? = null
    fun renderer(context: Context): SceneRenderer = renderer ?: synchronized(this) {
        renderer ?: SceneRenderer(context.applicationContext).also { renderer = it }
    }
    @Volatile private var widgetRenderer: SceneRenderer? = null
    /** Separate instance for background widget rendering so it never contends with UI drawing. */
    fun widgetRenderer(context: Context): SceneRenderer = widgetRenderer ?: synchronized(this) {
        widgetRenderer ?: SceneRenderer(context.applicationContext).also { widgetRenderer = it }
    }
    fun builder(context: Context): SceneBuilder = builder ?: synchronized(this) {
        builder ?: SceneBuilder(context.applicationContext).also { builder = it }
    }
}

/** Minute-aligned clock for live previews. */
@Composable
fun rememberMinuteTick(): Long {
    var now by remember { mutableLongStateOf(System.currentTimeMillis() / 60_000L) }
    LaunchedEffect(Unit) {
        while (true) {
            val ms = System.currentTimeMillis()
            delay(60_000L - ms % 60_000L + 50L)
            now = System.currentTimeMillis() / 60_000L
        }
    }
    return now
}

/** Non-interactive, live rendering of a widget design, identical to the launcher bitmap. */
@Composable
fun ScenePreview(config: WidgetConfig, night: Boolean, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val minute = rememberMinuteTick()
    val scene: Scene = remember(config, minute) { SceneServices.builder(context).build(config, Instant.ofEpochMilli(minute * 60_000L)) }
    val renderer = remember { SceneServices.renderer(context) }
    Canvas(modifier) {
        val fit = SceneGeometry.fit(scene.boardW, scene.boardH, 0f, 0f, size.width, size.height)
        drawIntoCanvas { renderer.draw(it.nativeCanvas, scene, fit, DrawOptions(night = night)) }
    }
}

/* -------------------------------------------------------------------- Backdrops */

/** Editor/preview backdrops. They stand in for a home-screen wallpaper and are never rendered on the widget. */
enum class Backdrop(val id: String) { WALLPAPER("wallpaper"), DUSK("dusk"), NIGHT("night"), MEADOW("meadow"), PAPER("paper"), INK("ink");
    companion object { fun from(id: String?) = entries.firstOrNull { it.id == id } ?: WALLPAPER }
}

data class WallpaperTones(val top: Color, val bottom: Color)

/** Reads the system wallpaper's dominant colours (API 27+, no permission needed). */
@Composable
fun rememberWallpaperTones(): WallpaperTones? {
    val context = LocalContext.current
    return remember {
        if (Build.VERSION.SDK_INT < 27) return@remember null
        runCatching {
            val colors = WallpaperManager.getInstance(context).getWallpaperColors(WallpaperManager.FLAG_SYSTEM) ?: return@runCatching null
            val primary = Color(colors.primaryColor.toArgb())
            val secondary = colors.secondaryColor?.let { Color(it.toArgb()) } ?: primary
            WallpaperTones(secondary, primary)
        }.getOrNull()
    }
}

fun backdropBrush(backdrop: Backdrop, wallpaper: WallpaperTones?): Brush = when (backdrop) {
    Backdrop.WALLPAPER -> wallpaper?.let { Brush.verticalGradient(listOf(it.top, it.bottom)) } ?: backdropBrush(Backdrop.DUSK, null)
    Backdrop.DUSK -> Brush.verticalGradient(listOf(Color(0xFFEFB27C), Color(0xFFC77F8C), Color(0xFF55507F)))
    Backdrop.NIGHT -> Brush.verticalGradient(listOf(Color(0xFF2A2C52), Color(0xFF12132A)))
    Backdrop.MEADOW -> Brush.verticalGradient(listOf(Color(0xFFD6E3C6), Color(0xFF7FA38D), Color(0xFF4C6F64)))
    Backdrop.PAPER -> Brush.verticalGradient(listOf(Color(0xFFF3EEE5), Color(0xFFE4DCCF)))
    Backdrop.INK -> Brush.verticalGradient(listOf(Color(0xFF26242D), Color(0xFF141318)))
}

/** Swatch colours for the backdrop picker. */
fun backdropSwatch(backdrop: Backdrop, wallpaper: WallpaperTones?): Brush = backdropBrush(backdrop, wallpaper)

fun Modifier.backdrop(backdrop: Backdrop, wallpaper: WallpaperTones?): Modifier = this.background(backdropBrush(backdrop, wallpaper))
