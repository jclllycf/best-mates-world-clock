package com.bestmates.worldclock.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.bestmates.worldclock.model.WidgetConfig

object WidgetPinning {
    fun supported(context: Context): Boolean = AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported

    /**
     * Asks the launcher to place this project as a widget of its own cell size. The real
     * design is passed as the confirmation-dialog preview where the launcher supports it.
     */
    fun request(context: Context, projectId: String, config: WidgetConfig): Boolean {
        val manager = AppWidgetManager.getInstance(context)
        if (!manager.isRequestPinAppWidgetSupported) return false
        val provider = WorldClockWidgetProvider.providerFor(context, config.columns, config.rows)
        val preview = runCatching { WidgetViews.buildPinPreview(context, config) }.getOrNull()
        val extras = Bundle().apply { if (preview != null) putParcelable(AppWidgetManager.EXTRA_APPWIDGET_PREVIEW, preview) }
        val callback = PendingIntent.getBroadcast(
            context,
            projectId.hashCode(),
            Intent(context, PinWidgetReceiver::class.java).putExtra(PinWidgetReceiver.EXTRA_PROJECT_ID, projectId),
            // Mutable: the launcher fills in EXTRA_APPWIDGET_ID.
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )
        return manager.requestPinAppWidget(provider, extras, callback)
    }
}
