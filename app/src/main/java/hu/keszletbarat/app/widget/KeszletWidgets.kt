package hu.keszletbarat.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import hu.keszletbarat.app.MainActivity
import hu.keszletbarat.app.R
import hu.keszletbarat.app.model.InventoryItem

object KeszletWidgetManager {
    private const val PREFS_WIDGET = "keszletbarat_widget_cache"
    private const val KEY_LOW_STOCK_COUNT = "low_stock_count"
    private const val KEY_LOW_STOCK_SUMMARY = "low_stock_summary"

    const val EXTRA_WIDGET_ACTION = "extra_widget_action"
    const val ACTION_OPEN_ALL = "action_open_all"
    const val ACTION_OPEN_LOW_STOCK = "action_open_low_stock"
    const val ACTION_OPEN_ADD_ITEM = "action_open_add_item"

    fun syncWidgets(context: Context, allItems: List<InventoryItem>) {
        val appContext = context.applicationContext
        val lowStockItems = allItems.filter { it.isLowStock }
        val count = lowStockItems.size
        val summary = if (lowStockItems.isEmpty()) {
            "Mindenből van elég készleten!"
        } else {
            lowStockItems.take(7).joinToString("\n") { item ->
                val status = if (item.isOutOfStock) "Elfogyott" else "${item.formattedQuantity} ${item.unit}"
                "• ${item.name} ($status)"
            }
        }

        appContext.getSharedPreferences(PREFS_WIDGET, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_LOW_STOCK_COUNT, count)
            .putString(KEY_LOW_STOCK_SUMMARY, summary)
            .apply()

        val manager = AppWidgetManager.getInstance(appContext)

        val lowStockIds = manager.getAppWidgetIds(ComponentName(appContext, LowStockWidgetProvider::class.java))
        if (lowStockIds.isNotEmpty()) {
            LowStockWidgetProvider.updateWidgets(appContext, manager, lowStockIds)
        }

        val quickIds = manager.getAppWidgetIds(ComponentName(appContext, QuickActionWidgetProvider::class.java))
        if (quickIds.isNotEmpty()) {
            QuickActionWidgetProvider.updateWidgets(appContext, manager, quickIds)
        }
    }

    fun getCachedLowStockCount(context: Context): Int {
        return context.applicationContext
            .getSharedPreferences(PREFS_WIDGET, Context.MODE_PRIVATE)
            .getInt(KEY_LOW_STOCK_COUNT, 0)
    }

    fun getCachedSummary(context: Context): String {
        return context.applicationContext
            .getSharedPreferences(PREFS_WIDGET, Context.MODE_PRIVATE)
            .getString(KEY_LOW_STOCK_SUMMARY, "Mindenből van elég készleten!")
            ?: "Mindenből van elég készleten!"
    }

    fun requestPinLowStockWidget(context: Context): Boolean {
        val manager = AppWidgetManager.getInstance(context)
        if (!manager.isRequestPinAppWidgetSupported) return false
        val provider = ComponentName(context, LowStockWidgetProvider::class.java)
        return manager.requestPinAppWidget(provider, null, null)
    }

    fun requestPinQuickActionWidget(context: Context): Boolean {
        val manager = AppWidgetManager.getInstance(context)
        if (!manager.isRequestPinAppWidgetSupported) return false
        val provider = ComponentName(context, QuickActionWidgetProvider::class.java)
        return manager.requestPinAppWidget(provider, null, null)
    }

    fun buildLaunchPendingIntent(context: Context, action: String, requestCode: Int): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(EXTRA_WIDGET_ACTION, action)
        }
        return PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}

class LowStockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    companion object {
        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val count = KeszletWidgetManager.getCachedLowStockCount(context)
            val summary = KeszletWidgetManager.getCachedSummary(context)

            for (widgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_low_stock)
                views.setTextViewText(R.id.widget_title, "🛒 Kell venni ($count)")
                views.setTextViewText(R.id.widget_items_text, summary)

                views.setOnClickPendingIntent(
                    R.id.widget_root,
                    KeszletWidgetManager.buildLaunchPendingIntent(
                        context,
                        KeszletWidgetManager.ACTION_OPEN_LOW_STOCK,
                        101
                    )
                )
                views.setOnClickPendingIntent(
                    R.id.widget_btn_add,
                    KeszletWidgetManager.buildLaunchPendingIntent(
                        context,
                        KeszletWidgetManager.ACTION_OPEN_ADD_ITEM,
                        102
                    )
                )
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}

class QuickActionWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    companion object {
        fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val count = KeszletWidgetManager.getCachedLowStockCount(context)

            for (widgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.widget_quick_actions)
                views.setTextViewText(R.id.widget_quick_low_stock, "🛒 Kell ($count)")

                views.setOnClickPendingIntent(
                    R.id.widget_quick_open,
                    KeszletWidgetManager.buildLaunchPendingIntent(
                        context,
                        KeszletWidgetManager.ACTION_OPEN_ALL,
                        201
                    )
                )
                views.setOnClickPendingIntent(
                    R.id.widget_quick_low_stock,
                    KeszletWidgetManager.buildLaunchPendingIntent(
                        context,
                        KeszletWidgetManager.ACTION_OPEN_LOW_STOCK,
                        202
                    )
                )
                views.setOnClickPendingIntent(
                    R.id.widget_quick_add,
                    KeszletWidgetManager.buildLaunchPendingIntent(
                        context,
                        KeszletWidgetManager.ACTION_OPEN_ADD_ITEM,
                        203
                    )
                )
                appWidgetManager.updateAppWidget(widgetId, views)
            }
        }
    }
}
