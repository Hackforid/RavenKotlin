package com.smilehacker.raven.ui.index.appconfig

import com.smilehacker.raven.base.App
import com.smilehacker.raven.kit.AppData
import com.smilehacker.raven.kit.VoiceMaker
import com.smilehacker.raven.model.AppInfo

/**
 * Created by zhouquan on 16/7/17.
 */
class AppConfigPresenterImpl: AppConfigPresenter() {

    private val mAppData by lazy { AppData(App.inst) }

    override fun getText(): String {
        return ""
    }

    override fun getVoiceSymbols(): Array<VoiceMaker.VoiceSymbol> {
        return VoiceMaker.voiceSymbols
    }


    override fun getAppInfo(packageName: String): AppInfo? {
        return mAppData.getAppByPackage(packageName)
    }

    override fun saveVoiceFormat(packageName: String, voiceFormat: String) {
        mAppData.saveVoiceFormat(packageName, voiceFormat)
    }
}
