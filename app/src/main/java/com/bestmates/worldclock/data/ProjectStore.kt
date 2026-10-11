package com.bestmates.worldclock.data

import android.content.Context
import com.bestmates.worldclock.model.WidgetConfig
import java.util.UUID

data class ProjectEntry(val id: String, val config: WidgetConfig)

/**
 * Project collection. The id set (`ids`) is the historical index and stays authoritative
 * for membership; ordering comes from each project's `updatedAt` (most recent first),
 * with never-edited legacy projects after them in title order. The old StringSet had
 * no order at all, which made the Workspace reshuffle between launches.
 */
class ProjectStore(context: Context) {
    private val prefs = context.getSharedPreferences("projects", Context.MODE_PRIVATE)
    private val configs = ConfigStore(context)

    fun ids(): List<String> = prefs.getStringSet("ids", emptySet()).orEmpty().toList()

    fun all(): List<ProjectEntry> = ids()
        .map { ProjectEntry(it, configs.loadProject(it)) }
        .sortedWith(compareByDescending<ProjectEntry> { it.config.updatedAt }.thenBy { it.config.title.lowercase() }.thenBy { it.id })

    fun load(id: String) = configs.loadProject(id)
    fun exists(id: String) = id in ids() && configs.hasProject(id)

    fun create(config: WidgetConfig = WidgetConfig()): String {
        val now = System.currentTimeMillis()
        return UUID.randomUUID().toString().also { save(it, config.copy(createdAt = now, updatedAt = now), touch = false) }
    }

    /** Persists a project. [touch] marks it as edited now, which moves it to the top. */
    fun save(id: String, config: WidgetConfig, touch: Boolean = true) {
        val ids = prefs.getStringSet("ids", emptySet()).orEmpty().toMutableSet().apply { add(id) }
        prefs.edit().putStringSet("ids", ids).apply()
        configs.saveProject(id, if (touch) config.copy(updatedAt = System.currentTimeMillis()) else config)
    }

    fun duplicate(id: String, suffix: String): String {
        val source = load(id)
        return create(source.copy(title = (source.title + " " + suffix).trim()))
    }

    fun delete(id: String) {
        val ids = prefs.getStringSet("ids", emptySet()).orEmpty().toMutableSet().apply { remove(id) }
        prefs.edit().putStringSet("ids", ids).apply(); configs.deleteProject(id)
    }
}
