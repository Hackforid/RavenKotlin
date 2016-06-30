package com.smilehacker.raven.service

import android.annotation.TargetApi
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.voice.TTSManager

/**
 * Created by kleist on 16/6/30.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class NLService : NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.apply {
            if (notification.tickerText != null) {
                DLog.d(notification.tickerText.toString())
                TTSManager.readText(notification.tickerText.toString())
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
