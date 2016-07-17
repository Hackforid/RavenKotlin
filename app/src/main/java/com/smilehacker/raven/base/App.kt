package com.smilehacker.raven.base

import android.app.Application
import com.activeandroid.ActiveAndroid
import com.smilehacker.raven.kit.ConfigManager
import com.smilehacker.raven.kit.NotificationHelper
import com.smilehacker.raven.voice.TTSManager

/**
 * Created by kleist on 16/6/28.
 */

class App : Application() {

    companion object {
        lateinit  var inst : App
    }

    override fun onCreate() {
        super.onCreate()
        inst = this
        ActiveAndroid.initialize(this)
        TTSManager.init(this)
        ConfigManager.init(this)
        NotificationHelper.init(this)
    }
}
