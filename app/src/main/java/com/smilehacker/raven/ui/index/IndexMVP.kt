package com.smilehacker.raven.ui.index

import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.BasePresenter
import com.smilehacker.raven.mvp.Viewer

/**
 * Created by kleist on 16/6/28.
 */

interface IndexViewer : Viewer {
    fun showApps(apps : MutableList<AppInfo>)
}

abstract class IndexPresenter : BasePresenter<IndexViewer>() {
    abstract fun loadApps()
}