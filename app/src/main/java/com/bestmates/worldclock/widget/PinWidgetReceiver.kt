package com.bestmates.worldclock.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bestmates.worldclock.data.ConfigStore
import com.bestmates.worldclock.data.WidgetBindings

/**
 * Receives the launcher's confirmation that a requested widget was placed. Links the new
 * widget id to its project (so later edits reach it) and stores a snapshot.
 */
class PinWidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        val projectId = intent.getStringExtra(EXTRA_PROJECT_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID || projectId.isNullOrBlank()) return

        val store = ConfigStore(context)
        WidgetBindings(context).bind(widgetId, projectId)
        store.save(widgetId, store.loadProject(projectId))
        WorldClockWidgetProvider.render(context, AppWidgetManager.getInstance(context), widgetId)
        WorldClockWidgetProvider.scheduleSolarRefresh(context)
    }

    companion object {
        const val EXTRA_PROJECT_ID = "projectId"
    }
}
