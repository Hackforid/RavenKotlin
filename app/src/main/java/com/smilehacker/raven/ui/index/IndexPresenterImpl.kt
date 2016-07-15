package com.smilehacker.raven.ui.index

import android.content.Context
import com.smilehacker.raven.AppData
import com.smilehacker.raven.ConfigManager
import com.smilehacker.raven.NotificationHelper
import com.smilehacker.raven.base.App

/**
 * Created by kleist on 16/6/28.
 */
class IndexPresenterImpl : IndexPresenter() {

    private val mAppData = AppData(App.inst)

    override fun loadApps() {
        val apps = mAppData.loadApps()
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
        }
    }
}