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
    fun unbind(widgetId: Int) = prefs.edit().remove("w$widgetId").remove("sw$widgetId").remove("sh$widgetId").apply()
    fun widgetsFor(projectId: String): List<Int> = prefs.all.mapNotNull { (key, value) ->
        if (value == projectId && key.startsWith("w") && !key.startsWith("sw")) key.drop(1).toIntOrNull() else null
    }
    fun widgetCount(projectId: String): Int = widgetsFor(projectId).size

    /** The portrait size (dp) the launcher last reported for [widgetId]. */
    fun reportedSize(widgetId: Int): Pair<Float, Float>? {
        val w = prefs.getFloat("sw$widgetId", 0f); val h = prefs.getFloat("sh$widgetId", 0f)
        return if (w > 0f && h > 0f) w to h else null
    }

    fun recordSize(widgetId: Int, widthDp: Float, heightDp: Float) {
        if (widthDp < 20f || heightDp < 20f) return
        if (reportedSize(widgetId) == (widthDp to heightDp)) return
        prefs.edit().putFloat("sw$widgetId", widthDp).putFloat("sh$widgetId", heightDp).apply()
    }

    /** The real size of a home-screen widget showing [projectId], if one is placed. */
    fun placedSizeFor(projectId: String): Pair<Float, Float>? = widgetsFor(projectId).sortedDescending().firstNotNullOfOrNull { reportedSize(it) }
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
        // Cells are not additive (launchers put gaps and padding between them), so the exact
        // size of every span seen is kept as well as the average cell.
        prefs.edit().putFloat("cellW", w).putFloat("cellH", h)
            .putFloat("span${columns}x${rows}w", widthDp).putFloat("span${columns}x${rows}h", heightDp).apply()
    }

    fun boardFor(columns: Int, rows: Int): Pair<Float, Float> {
        val w = prefs.getFloat("span${columns}x${rows}w", 0f); val h = prefs.getFloat("span${columns}x${rows}h", 0f)
        return if (w > 0f && h > 0f) w to h else (cellWidthDp * columns) to (cellHeightDp * rows)
    }

    companion object {
        // A typical 4-column phone grid in portrait (about 360dp usable width).
        const val DEFAULT_CELL_W = 86f
        const val DEFAULT_CELL_H = 98f
    }
}
