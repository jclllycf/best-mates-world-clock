package com.bestmates.worldclock.data

import android.content.Context
import com.bestmates.worldclock.model.WidgetConfig
import java.util.UUID

class ProjectStore(context: Context) {
    private val prefs = context.getSharedPreferences("projects", Context.MODE_PRIVATE)
    private val configs = ConfigStore(context)
    fun ids(): List<String> = prefs.getStringSet("ids", emptySet()).orEmpty().toList()
    fun load(id: String) = configs.loadProject(id)
    fun create(config: WidgetConfig = WidgetConfig()): String = UUID.randomUUID().toString().also { save(it, config) }
    fun save(id: String, config: WidgetConfig) {
        val ids = prefs.getStringSet("ids", emptySet()).orEmpty().toMutableSet().apply { add(id) }
        prefs.edit().putStringSet("ids", ids).apply(); configs.saveProject(id, config)
    }
    fun copy(id: String): String = create(load(id).copy(title = load(id).title + " 副本"))
    fun delete(id: String) {
        val ids = prefs.getStringSet("ids", emptySet()).orEmpty().toMutableSet().apply { remove(id) }
        prefs.edit().putStringSet("ids", ids).apply(); configs.deleteProject(id)
    }
}
