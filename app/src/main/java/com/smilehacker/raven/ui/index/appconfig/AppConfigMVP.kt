package com.smilehacker.raven.ui.index.appconfig

import com.smilehacker.raven.kit.VoiceMaker
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.BasePresenter
import com.smilehacker.raven.mvp.Viewer

/**
 * Created by zhouquan on 16/7/17.
 */

interface AppConfigViewer : Viewer {
    fun showText(text: String)
}

abstract class AppConfigPresenter : BasePresenter<AppConfigViewer>() {
    abstract fun getText(): String
    abstract fun getVoiceSymbols(): Array<VoiceMaker.VoiceSymbol>
    abstract fun getAppInfo(packageName: String): AppInfo?
    abstract fun saveVoiceFormat(packageName: String, voiceFormat: String)
}



