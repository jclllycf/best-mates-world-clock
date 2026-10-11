package com.bestmates.worldclock.data

import android.content.Context

/**
 * Links a home-screen widget instance to the project it was created from, so edits to
 * the project reach the widget. Before v0.4 the pin flow copied a snapshot but never
 * recorded the link, so a pinned widget could never receive later edits.
 *
 * The widget keeps its own snapshot (`widget_<id>` in [ConfigStore]); if the project is
 * deleted the widget simply keeps showing its last design.
 */
class WidgetBindings(context: Context) {
    private val prefs = context.getSharedPreferences("widget_bindings", Context.MODE_PRIVATE)

    fun projectFor(widgetId: Int): String? = prefs.getString("w$widgetId", null)
    fun bind(widgetId: Int, projectId: String) = prefs.edit().putString("w$widgetId", projectId).apply()
    fun unbind(widgetId: Int) = prefs.edit().remove("w$widgetId").apply()
    fun widgetsFor(projectId: String): List<Int> = prefs.all.mapNotNull { (key, value) ->
        if (value == projectId && key.startsWith("w")) key.drop(1).toIntOrNull() else null
    }
    fun widgetCount(projectId: String): Int = widgetsFor(projectId).size
}

/**
 * Real launcher cell size learned from placed widgets. Launchers differ (a Pixel 4×4 is
 * tall and narrow, a Samsung 4×4 nearly square), so new projects take their design
 * proportions from what this phone's launcher actually reported.
 */
class LauncherMetrics(context: Context) {
    private val prefs = context.getSharedPreferences("launcher_metrics", Context.MODE_PRIVATE)

    val cellWidthDp: Float get() = prefs.getFloat("cellW", DEFAULT_CELL_W)
    val cellHeightDp: Float get() = prefs.getFloat("cellH", DEFAULT_CELL_H)
    val calibrated: Boolean get() = prefs.contains("cellW")

    fun record(columns: Int, rows: Int, widthDp: Float, heightDp: Float) {
        if (columns <= 0 || rows <= 0 || widthDp < 30f || heightDp < 30f) return
        val w = widthDp / columns
        val h = heightDp / rows
        if (w !in 30f..260f || h !in 30f..260f) return
        prefs.edit().putFloat("cellW", w).putFloat("cellH", h).apply()
    }

    fun boardFor(columns: Int, rows: Int): Pair<Float, Float> = (cellWidthDp * columns) to (cellHeightDp * rows)

    companion object {
        // A typical 4-column phone grid in portrait (about 360dp usable width).
        const val DEFAULT_CELL_W = 86f
        const val DEFAULT_CELL_H = 98f
    }
}
