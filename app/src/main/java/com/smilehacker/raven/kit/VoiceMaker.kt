package com.smilehacker.raven.kit

import android.app.Notification
import android.support.annotation.DrawableRes
import com.smilehacker.raven.R
import com.smilehacker.raven.base.App
import com.smilehacker.raven.util.DLog

/**
 * Created by zhouquan on 16/7/17.
 */
object VoiceMaker {
    data class VoiceSymbol(val id: Int, val symbol: String, val text: String, @DrawableRes val icon: Int)

    val voiceSymbols = arrayOf(
            VoiceSymbol(1, "[title]", "title", R.drawable.input_title),
            VoiceSymbol(2, "[message]", "message", R.drawable.input_message),
            VoiceSymbol(3, "[ticker]", "ticker", R.drawable.input_ticker),
            VoiceSymbol(4, "[appname]", "appname", R.drawable.input_name)
    )

    const val default_voice_format = "[appname]消息 [title] [message]"

    fun makeVoice(packageName: String, notification: Notification, voiceFormat: String): String {
        val notificationData = NotificationHelper.getNotificationData(notification)
        var r = voiceFormat
        DLog.i("r = $r data = $notificationData")
        voiceSymbols.forEach {
            val appName = AppData(App.inst.applicationContext).loadAppNameByPackage(packageName)
            val str = when (it.symbol) {
                "[title]" ->  notificationData.title
                "[message]" -> notificationData.message
                "[ticker]" -> notificationData.ticker
                "[appname]" -> appName
                else -> {
                    ""
                }
            }
            r = r.replace(it.symbol, str?:"", false)
        }
        return r
    }
}
