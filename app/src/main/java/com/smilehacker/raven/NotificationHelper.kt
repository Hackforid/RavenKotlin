package com.smilehacker.raven

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import com.smilehacker.raven.service.NotificationAccessibilityService

/**
 * Created by kleist on 16/7/15.
 */
class NotificationHelper(val ctx: Context) {

    fun isNotificationServiceEnable() : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return isNotificationListenerServiceEnable()
        } else {
            return isNotificationAccessibilityServiceEnable()
        }
    }

    fun isNotificationAccessibilityServiceEnable(): Boolean {
        val serviceName = String.format("$1%s/$2%s", ctx.packageName, NotificationAccessibilityService::class.java.name)
        return isAccessibilityEnabled(ctx, serviceName)
    }

    fun isNotificationListenerServiceEnable(): Boolean {
        return isNotificationListenerServiceEnable(ctx)
    }

    private fun isAccessibilityEnabled(context: Context, id: String): Boolean {

        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

        val runningServices = am.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK)
        for (service in runningServices) {
            if (id == service.id) {
                return true
            }
        }

        return false
    }

    private fun isNotificationListenerServiceEnable(context: Context): Boolean {
        val contentResolver = context.contentResolver
        val enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val packageName = context.packageName

        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName)
    }
}
