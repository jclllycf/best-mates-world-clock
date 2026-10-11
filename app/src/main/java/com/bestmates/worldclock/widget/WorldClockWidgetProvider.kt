package com.bestmates.worldclock.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SizeF
import android.widget.RemoteViews
import com.bestmates.worldclock.data.ConfigStore
import com.bestmates.worldclock.data.LauncherMetrics
import com.bestmates.worldclock.data.WidgetBindings
import com.bestmates.worldclock.solar.SolarSchedule

open class WorldClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { render(context, manager, it) }
        scheduleSolarRefresh(context)
    }

    override fun onAppWidgetOptionsChanged(context: Context, manager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle) {
        render(context, manager, appWidgetId)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action in REFRESH_ACTIONS) refreshAll(context)
    }

    override fun onDeleted(context: Context, ids: IntArray) {
        val bindings = WidgetBindings(context)
        val store = ConfigStore(context)
        ids.forEach { store.delete(it); bindings.unbind(it) }
    }

    companion object {
        private const val TAG = "BestMatesWidget"
        const val ACTION_REFRESH = "com.bestmates.worldclock.action.REFRESH_WIDGETS"
        private val REFRESH_ACTIONS = setOf(
            Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_TIME_CHANGED, Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_LOCALE_CHANGED, Intent.ACTION_CONFIGURATION_CHANGED, ACTION_REFRESH,
        )

        /** Every widget instance of this app, across the generic and all fixed-size providers. */
        fun allWidgetIds(context: Context): List<Int> {
            val manager = AppWidgetManager.getInstance(context)
            return manager.getInstalledProvidersForPackage(context.packageName, null)
                .flatMap { manager.getAppWidgetIds(it.provider).toList() }
                .distinct()
        }

        fun refreshAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            allWidgetIds(context).forEach { render(context, manager, it) }
            scheduleSolarRefresh(context)
        }

        /** Re-renders every widget linked to [projectId] (after the project is saved). */
        fun refreshProject(context: Context, projectId: String) {
            val manager = AppWidgetManager.getInstance(context)
            val bound = WidgetBindings(context).widgetsFor(projectId).toSet()
            allWidgetIds(context)
                .filter { it in bound || manager.getAppWidgetOptions(it).getString("projectId") == projectId }
                .forEach { render(context, manager, it) }
            scheduleSolarRefresh(context)
        }

        fun projectFor(context: Context, widgetId: Int): String? =
            WidgetBindings(context).projectFor(widgetId)
                ?: AppWidgetManager.getInstance(context).getAppWidgetOptions(widgetId).getString("projectId")

        fun render(context: Context, manager: AppWidgetManager, id: Int) {
            runCatching {
                val store = ConfigStore(context)
                val projectId = projectFor(context, id)
                val config = if (projectId != null && store.hasProject(projectId)) {
                    // Keep a snapshot so the widget survives if the project is ever deleted.
                    store.loadProject(projectId).also { store.save(id, it) }
                } else store.load(id)

                val options = manager.getAppWidgetOptions(id)
                val sizes = reportedSizes(options)
                val portrait = sizes.minByOrNull { it.width } ?: SizeF(300f, 300f)
                cellSpan(manager, id)?.let { (c, r) -> LauncherMetrics(context).record(c, r, portrait.width, portrait.height) }

                val views = if (Build.VERSION.SDK_INT >= 31 && sizes.size > 1) {
                    val budget = 2_400_000 / sizes.size
                    RemoteViews(sizes.associateWith { WidgetViews.build(context, config, it.width, it.height, projectId, id, budget) })
                } else {
                    WidgetViews.build(context, config, portrait.width, portrait.height, projectId, id)
                }
                manager.updateAppWidget(id, views)
            }.onFailure { Log.e(TAG, "Widget $id failed to render", it) }
        }

        /** Exact launcher sizes on API 31+; otherwise the portrait/landscape min–max pair. */
        private fun reportedSizes(options: Bundle): List<SizeF> {
            if (Build.VERSION.SDK_INT >= 31) {
                @Suppress("DEPRECATION")
                val exact = options.getParcelableArrayList<SizeF>(AppWidgetManager.OPTION_APPWIDGET_SIZES)
                    ?.filter { it.width > 20f && it.height > 20f }
                    ?.distinctBy { "${it.width.toInt()}x${it.height.toInt()}" }
                    ?.take(3)
                if (!exact.isNullOrEmpty()) return exact
            }
            val minW = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 0)
            val maxH = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, 0)
            val maxW = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, 0)
            val minH = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 0)
            return listOfNotNull(
                if (minW > 20 && maxH > 20) SizeF(minW.toFloat(), maxH.toFloat()) else null,
                if (maxW > 20 && minH > 20) SizeF(maxW.toFloat(), minH.toFloat()) else null,
            ).ifEmpty { listOf(SizeF(300f, 300f)) }
        }

        /** Cell span is encoded in the fixed-size provider class name, e.g. SizedWidgetProvider_4x2. */
        private fun cellSpan(manager: AppWidgetManager, id: Int): Pair<Int, Int>? {
            val name = manager.getAppWidgetInfo(id)?.provider?.className ?: return null
            val match = Regex("_(\\d)x(\\d)$").find(name) ?: return null
            return match.groupValues[1].toInt() to match.groupValues[2].toInt()
        }

        /**
         * Sun/moon icons are part of the static bitmap; refresh shortly after the next
         * sunrise or sunset of any city on any widget. Inexact alarms need no permission.
         */
        fun scheduleSolarRefresh(context: Context) {
            runCatching {
                val store = ConfigStore(context)
                val cities = allWidgetIds(context).flatMap { id ->
                    val projectId = projectFor(context, id)
                    (if (projectId != null && store.hasProject(projectId)) store.loadProject(projectId) else store.load(id)).cities
                }.distinctBy { "${it.latitude},${it.longitude},${it.zoneId}" }
                val alarm = context.getSystemService(AlarmManager::class.java) ?: return
                val intent = Intent(context, WorldClockWidgetProvider::class.java).setAction(ACTION_REFRESH)
                val pending = PendingIntent.getBroadcast(context, 7101, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                val next = SolarSchedule.nextTransition(cities, System.currentTimeMillis())
                if (next == null) alarm.cancel(pending) else alarm.set(AlarmManager.RTC, next + 60_000L, pending)
            }.onFailure { Log.w(TAG, "Could not schedule solar refresh", it) }
        }


        fun providerFor(context: Context, columns: Int, rows: Int): ComponentName =
            ComponentName(context.packageName, "${context.packageName}.widget.SizedWidgetProvider_${columns.coerceIn(1, 4)}x${rows.coerceIn(1, 6)}")
    }
}
