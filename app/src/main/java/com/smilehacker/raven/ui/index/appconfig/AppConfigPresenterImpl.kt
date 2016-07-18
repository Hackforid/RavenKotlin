package com.smilehacker.raven.ui.index.appconfig

import com.smilehacker.raven.kit.VoiceMaker

/**
 * Created by zhouquan on 16/7/17.
 */
class AppConfigPresenterImpl: AppConfigPresenter() {

    override fun getText(): String {
        return ""
    }

    override fun getVoiceSymbols(): Array<VoiceMaker.VoiceSymbol> {
        return VoiceMaker.voiceSymbols
    }


}
