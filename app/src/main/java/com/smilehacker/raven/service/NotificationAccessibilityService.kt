package com.smilehacker.raven.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Created by kleist on 16/6/30.
 */
class NotificationAccessibilityService : AccessibilityService() {
    override fun onInterrupt() {
        throw UnsupportedOperationException()
    }

    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        throw UnsupportedOperationException()
    }

}
