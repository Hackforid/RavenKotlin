package com.smilehacker.raven.ui.index

import android.content.Context
import com.smilehacker.raven.AppData
import com.smilehacker.raven.ConfigManager
import com.smilehacker.raven.NotificationHelper
import com.smilehacker.raven.base.App
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.util.Callback
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.voice.TTSManager
import java.util.*

/**
 * Created by kleist on 16/6/28.
 */
class IndexPresenterImpl : IndexPresenter() {

    private val mAppData = AppData(App.inst)

    override fun loadApps() {
        val apps = mAppData.loadApps()
        sortApp(apps)
        view?.showApps(apps)
    }

    override fun setAppTTSEnable(packageName: String, enable: Boolean) {
        mAppData.setAppTTSEnable(packageName, enable)
    }

    override fun checkNotificationService(ctx: Context) {
        val notiHelper = NotificationHelper(ctx)
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

    private fun sortApp(apps: MutableList<AppInfo>) {
        val enabledApps = apps.filter { it.enable }
        val disabledApps = apps.filter { !it.enable }
//        enabledApps.sortedBy { it.appName.first() }
//        disabledApps.sortedBy { it.appName.first() }
        apps.clear()
        apps.addAll(enabledApps.sortedWith(Comparator { t1, t2 -> t1.appName.compareTo(t2.appName)  }))
        apps.addAll(disabledApps.sortedWith(Comparator { t1, t2 -> t1.appName.compareTo(t2.appName)  }))
    }
}