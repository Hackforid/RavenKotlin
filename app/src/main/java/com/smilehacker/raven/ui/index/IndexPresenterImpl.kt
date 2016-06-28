package com.smilehacker.raven.ui.index

import com.smilehacker.raven.AppData
import com.smilehacker.raven.base.App

/**
 * Created by kleist on 16/6/28.
 */
class IndexPresenterImpl : IndexPresenter() {

    override fun loadApps() {
        AppData(App.inst).loadAppsFromSys()
    }

}