package com.smilehacker.raven.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.view.accessibility.AccessibilityEvent
import com.smilehacker.raven.voice.TTSManager

/**
 * Created by kleist on 16/6/30.
 */
class NotificationAccessibilityService : AccessibilityService() {

    override fun onInterrupt() {
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_AUDIBLE
        info.notificationTimeout = 100
        serviceInfo = info
    }

    override fun onAccessibilityEvent(e: AccessibilityEvent?) {
        if (e == null) {
            return
        }
        if (e.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            val data = e.parcelableData
            if (data is Notification) {
                TTSManager.readText(e.packageName.toString(), data.tickerText.toString())
            }
        }
    }

}
