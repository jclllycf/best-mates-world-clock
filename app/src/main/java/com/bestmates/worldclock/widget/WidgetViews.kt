package com.bestmates.worldclock.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import com.bestmates.worldclock.MainActivity
import com.bestmates.worldclock.R
import com.bestmates.worldclock.fonts.WidgetFonts
import com.bestmates.worldclock.model.ThemeMode
import com.bestmates.worldclock.model.WidgetConfig
import com.bestmates.worldclock.scene.DrawOptions
import com.bestmates.worldclock.scene.LineKind
import com.bestmates.worldclock.scene.SceneBuilder
import com.bestmates.worldclock.scene.SceneGeometry
import com.bestmates.worldclock.ui.common.SceneServices
import kotlin.math.ceil
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Builds the launcher RemoteViews for one widget size.
 *
 * Everything static (shapes, sun/moon, city names, custom text) is drawn into one bitmap
 * by the same [SceneRenderer] the editor uses. Time and date stay live as TextClocks set
 * in the same bundled typeface, sized and positioned from the same scene layout, so the
 * launcher shows what the editor showed.
 */
object WidgetViews {
    const val EXTRA_OPEN_PROJECT = "com.bestmates.worldclock.OPEN_PROJECT"
    private const val SLOT_COUNT = 20

    fun isNight(context: Context, config: WidgetConfig): Boolean =
        config.theme == ThemeMode.DARK || (config.theme == ThemeMode.SYSTEM &&
            (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)

    /**
     * @param pixelBudget upper bound on bitmap pixels so a set of responsive layouts stays
     * well inside the launcher's RemoteViews bitmap memory limit.
     */
    fun build(
        context: Context,
        config: WidgetConfig,
        widthDp: Float,
        heightDp: Float,
        projectId: String?,
        requestCode: Int,
        pixelBudget: Int = 2_400_000,
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.widget_scene)
        val density = context.resources.displayMetrics.density
        val viewW = widthDp * density
        val viewH = heightDp * density

        // Bitmap resolution: native density unless the pixel budget requires less.
        val bitmapScale = min(1f, sqrt(pixelBudget / (viewW * viewH).coerceAtLeast(1f)))
        val bw = (viewW * bitmapScale).roundToInt().coerceAtLeast(1)
        val bh = (viewH * bitmapScale).roundToInt().coerceAtLeast(1)
        val scene = SceneServices.builder(context).build(config)
        val bitmap = Bitmap.createBitmap(bw, bh, Bitmap.Config.ARGB_8888)
        val bitmapFit = SceneGeometry.fit(scene.boardW, scene.boardH, 0f, 0f, bw.toFloat(), bh.toFloat())
        SceneServices.widgetRenderer(context).draw(Canvas(bitmap), scene, bitmapFit, DrawOptions(night = isNight(context, config), liveText = false))
        views.setImageViewBitmap(R.id.widget_scene, bitmap)

        // Live text slots in real view pixels.
        val fit = SceneGeometry.fit(scene.boardW, scene.boardH, 0f, 0f, viewW, viewH)
        var slot = 0
        val builder = SceneServices.builder(context)
        for (city in scene.cities) {
            val f = city.frame
            for (line in city.lines) {
                if (line.kind != LineKind.TIME && line.kind != LineKind.DATE) continue
                if (slot >= SLOT_COUNT) break
                val slotId = slotIds[slot++]
                // Line centre: horizontally centred in the block, rotated with the block.
                val (bx, by) = f.toBoard(0f, f.top + line.cy - f.cy)
                val cx = fit.x(bx)
                val cy = fit.y(by)
                var slotW = line.slotWidth * fit.scale
                var slotH = ceil(line.box * fit.scale) + 2f
                if (f.rotation % 360f != 0f) {
                    // A rotated TextClock turns about its own centre inside the slot; a square
                    // as wide as the text box's diagonal keeps every rotation unclipped.
                    val diagonal = hypot(slotW, slotH)
                    slotW = diagonal; slotH = diagonal
                }
                val halfW = min(slotW / 2f, min(cx, viewW - cx)).coerceAtLeast(1f)
                val halfH = min(slotH / 2f, min(cy, viewH - cy)).coerceAtLeast(1f)
                val left = (cx - halfW).roundToInt().coerceAtLeast(0)
                val top = (cy - halfH).roundToInt().coerceAtLeast(0)
                val right = (viewW - (cx + halfW)).roundToInt().coerceAtLeast(0)
                val bottom = (viewH - (cy + halfH)).roundToInt().coerceAtLeast(0)
                views.setViewPadding(slotId, left, top, right, bottom)
                views.setViewVisibility(slotId, View.VISIBLE)
                val pattern = if (line.kind == LineKind.TIME) builder.timePattern(config.use24Hour) else builder.datePattern
                val clock = RemoteViews(context.packageName, WidgetFonts.resolve(line.fontId).clockLayout).apply {
                    setTextViewTextSize(R.id.clock, TypedValue.COMPLEX_UNIT_PX, line.size * fit.scale)
                    setTextColor(R.id.clock, line.color)
                    setCharSequence(R.id.clock, "setFormat12Hour", pattern)
                    setCharSequence(R.id.clock, "setFormat24Hour", pattern)
                    setString(R.id.clock, "setTimeZone", city.city.zoneId)
                    // View.setRotation is remotable (verified on Pixel Launcher, API 35).
                    if (f.rotation % 360f != 0f) setFloat(R.id.clock, "setRotation", f.rotation)
                }
                views.removeAllViews(slotId)
                views.addView(slotId, clock)
            }
        }
        for (i in slot until SLOT_COUNT) views.setViewVisibility(slotIds[i], View.GONE)

        val open = Intent(context, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .apply { if (projectId != null) putExtra(EXTRA_OPEN_PROJECT, projectId) }
        views.setOnClickPendingIntent(
            R.id.widget_root,
            PendingIntent.getActivity(context, requestCode, open, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE),
        )
        return views
    }

    /**
     * Static preview for the launcher's pin-confirmation dialog. The dialog's frame size is
     * unknown, so the complete design (including the current time) is one fit-centred image.
     */
    fun buildPinPreview(context: Context, config: WidgetConfig): RemoteViews {
        val density = context.resources.displayMetrics.density
        val scale = min(density, sqrt(900_000f / (config.boardWidthDp * config.boardHeightDp)))
        val w = (config.boardWidthDp * scale).roundToInt().coerceAtLeast(1)
        val h = (config.boardHeightDp * scale).roundToInt().coerceAtLeast(1)
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val scene = SceneServices.builder(context).build(config)
        SceneServices.widgetRenderer(context).draw(
            Canvas(bitmap), scene, SceneGeometry.fit(scene.boardW, scene.boardH, 0f, 0f, w.toFloat(), h.toFloat()),
            DrawOptions(night = isNight(context, config), liveText = true),
        )
        return RemoteViews(context.packageName, R.layout.widget_pin_preview).apply {
            setImageViewBitmap(R.id.pin_preview_image, bitmap)
        }
    }

    private val slotIds = intArrayOf(
        R.id.clock_slot_1, R.id.clock_slot_2, R.id.clock_slot_3, R.id.clock_slot_4, R.id.clock_slot_5,
        R.id.clock_slot_6, R.id.clock_slot_7, R.id.clock_slot_8, R.id.clock_slot_9, R.id.clock_slot_10,
        R.id.clock_slot_11, R.id.clock_slot_12, R.id.clock_slot_13, R.id.clock_slot_14, R.id.clock_slot_15,
        R.id.clock_slot_16, R.id.clock_slot_17, R.id.clock_slot_18, R.id.clock_slot_19, R.id.clock_slot_20,
    )
}
