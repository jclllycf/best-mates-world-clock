package com.bestmates.worldclock.data

import android.content.Context
import android.util.Log
import com.bestmates.worldclock.model.CURRENT_LAYOUT_VERSION
import com.bestmates.worldclock.model.WidgetConfig
import org.json.JSONObject

/**
 * SharedPreferences persistence for project and widget-instance configurations.
 *
 * Keys are unchanged since v0.1 (`project_<uuid>`, `widget_<appWidgetId>`). Before a
 * payload from an older schema is migrated, its raw JSON is copied once to
 * `backup_v<old>_<key>`; nothing ever deletes those backups, so a migration can be
 * reversed by hand if a regression is ever found.
 */
class ConfigStore(context: Context) {
    private val prefs = context.getSharedPreferences("widget_configs", Context.MODE_PRIVATE)

    fun load(widgetId: Int): WidgetConfig = loadKey("widget_$widgetId") ?: WidgetConfig()
    fun loadProject(projectId: String): WidgetConfig = loadKey("project_$projectId") ?: WidgetConfig()
    fun hasProject(projectId: String): Boolean = prefs.contains("project_$projectId")
    fun hasWidget(widgetId: Int): Boolean = prefs.contains("widget_$widgetId")

    private fun loadKey(key: String): WidgetConfig? {
        val raw = prefs.getString(key, null) ?: return null
        return runCatching {
            val stored = runCatching { JSONObject(raw).optInt("layoutVersion", 1) }.getOrDefault(1)
            if (stored < CURRENT_LAYOUT_VERSION) backupOnce(key, stored, raw)
            ConfigCodec.decode(raw)
        }.onFailure {
            // Never silently replace an unreadable project: keep the raw text for recovery.
            Log.e(TAG, "Unreadable config $key", it)
            backupOnce(key, -1, raw)
        }.getOrNull()
    }

    private fun backupOnce(key: String, version: Int, raw: String) {
        val backupKey = "backup_v${version}_$key"
        if (!prefs.contains(backupKey)) prefs.edit().putString(backupKey, raw).apply()
    }

    fun save(widgetId: Int, config: WidgetConfig) = saveKey("widget_$widgetId", config)
    fun saveProject(projectId: String, config: WidgetConfig) = saveKey("project_$projectId", config)
    private fun saveKey(key: String, config: WidgetConfig) {
        val encoded = ConfigCodec.encode(config)
        if (prefs.getString(key, null) == encoded) return
        prefs.edit().putString(key, encoded).apply()
    }

    fun delete(widgetId: Int) = prefs.edit().remove("widget_$widgetId").apply()
    fun deleteProject(projectId: String) = prefs.edit().remove("project_$projectId").apply()

    companion object { private const val TAG = "BestMatesConfig" }
}
