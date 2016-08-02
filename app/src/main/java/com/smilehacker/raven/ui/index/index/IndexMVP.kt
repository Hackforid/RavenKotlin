package com.smilehacker.raven.ui.index.index

import android.content.Context
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.BasePresenter
import com.smilehacker.raven.mvp.Viewer

/**
 * Created by kleist on 16/6/28.
 */

interface IndexViewer : Viewer {
    fun showApps(apps : MutableList<AppInfo>)
    fun showSetNotificationDialog()
    fun showSetNotificationSnackbar()
    fun showSetTTSDialog(msg: String)
    fun showSetTTSSnackbar(msg: String)
}

abstract class IndexPresenter : BasePresenter<IndexViewer>() {
    abstract fun loadApps()
    abstract fun setAppTTSEnable(packageName: String, enable: Boolean)
    abstract fun checkNotificationService(ctx : Context)
    abstract fun checkTTS()
    abstract fun queryByName(name: String)
}
