package com.smilehacker.raven.ui.index

import com.smilehacker.raven.AppData
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
}