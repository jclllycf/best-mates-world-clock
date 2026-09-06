package com.bestmates.worldclock.widget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bestmates.worldclock.data.ConfigStore

/** Copies the edited project into the widget id assigned by the launcher. */
class PinWidgetReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        val projectId = intent.getStringExtra(EXTRA_PROJECT_ID)
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID || projectId.isNullOrBlank()) return

        val store = ConfigStore(context)
        store.save(widgetId, store.loadProject(projectId))
        WorldClockWidgetProvider.render(context, AppWidgetManager.getInstance(context), widgetId)
    }

    companion object {
        const val EXTRA_PROJECT_ID = "projectId"
    }
}
