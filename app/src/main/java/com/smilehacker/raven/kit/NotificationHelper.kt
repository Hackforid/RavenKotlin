package com.smilehacker.raven.kit

import android.app.Notification
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import com.smilehacker.raven.service.NotificationAccessibilityService

/**
 * Created by zhouquan on 16/7/16.
 */
object NotificationHelper {
    data class NotificationData(var title: String? = null,
                                var titleBig: String? = null,
                                var message: String? = null,
                                var messageBig: String? = null,
                                var info: String? = null,
                                var summary: String? = null,
                                var sub: String? = null,
                                var ticker: String? = null
    )

    lateinit var ctx: Context

    fun init(ctx: Context) {
        this.ctx = ctx
    }

    fun getNotificationData(notification: Notification) : NotificationData {
        val ticker = notification.tickerText?.toString()
        val extra = getNotificationExtra(notification)
        val title = extra?.getString(Notification.EXTRA_TITLE)
        val titleBig = extra?.getString(Notification.EXTRA_TITLE_BIG)
        val message = (extra?.getCharSequence(Notification.EXTRA_TEXT))?.toString()
        val messageBig = (extra?.getCharSequence(Notification.EXTRA_BIG_TEXT))?.toString()
        val info = extra?.getCharSequence(Notification.EXTRA_INFO_TEXT)?.toString()
        val summary = extra?.getCharSequence(Notification.EXTRA_MESSAGES)?.toString()
        val sub = extra?.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString()
        return NotificationData(title, titleBig, message, messageBig, info, summary, sub, ticker)
    }

    fun getNotificationExtra(notification: Notification) : Bundle? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return notification.extras
        } else {
            try {
                val field = notification.javaClass.getDeclaredField("extras")
                field.isAccessible = true
                return field.get(notification) as Bundle?
            } catch (e: Exception) {
                return null
            }
        }
    }


    fun isNotificationServiceEnable() : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return isNotificationListenerServiceEnable()
        } else {
            return isNotificationAccessibilityServiceEnable()
        }
    }

    fun isNotificationAccessibilityServiceEnable(): Boolean {
        val serviceName = String.format("$1%s/$2%s", ctx.packageName, NotificationAccessibilityService::class.java.name)
        return isAccessibilityEnabled(serviceName)
    }

    fun isNotificationListenerServiceEnable(): Boolean {
        return isNotificationListenerServiceEnable(ctx)
    }

    private fun isAccessibilityEnabled(id: String): Boolean {

        val am = ctx.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

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
