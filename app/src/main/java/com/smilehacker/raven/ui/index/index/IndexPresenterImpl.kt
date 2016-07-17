package com.smilehacker.raven.ui.index.index

import android.content.Context
import com.smilehacker.raven.base.App
import com.smilehacker.raven.kit.AppData
import com.smilehacker.raven.kit.ConfigManager
import com.smilehacker.raven.kit.NotificationHelper
import com.smilehacker.raven.util.Callback
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.voice.TTSManager

/**
 * Created by kleist on 16/6/28.
 */
class IndexPresenterImpl : IndexPresenter() {

    private val mAppData = AppData(App.Companion.inst)

    override fun loadApps() {
        val apps = mAppData.loadApps()
        view?.showApps(apps)
    }

    override fun setAppTTSEnable(packageName: String, enable: Boolean) {
        mAppData.setAppTTSEnable(packageName, enable)
    }

    override fun checkNotificationService(ctx: Context) {
        val notiHelper = NotificationHelper
        if (!notiHelper.isNotificationServiceEnable()) {
            if (ConfigManager.isFirstLaunch) {
                view?.showSetNotificationDialog()
            } else {
                view?.showSetNotificationSnackbar()
            }
        } else {
            checkTTS()
        }
    }

    override fun checkTTS() {
        TTSManager.checkTTS(object : Callback<Void> {
            override fun onResult(result: Void?) {
                DLog.d("check TTS success")
            }

            override fun onError(e: Exception) {
                if (ConfigManager.isFirstLaunch) {
                    view?.showSetTTSDialog(e.message.toString())
                } else {
                    view?.showSetTTSSnackbar(e.message.toString())
                }
            }

        })
    }
}