package com.smilehacker.raven.ui.index.index

import android.content.Context
import com.smilehacker.raven.base.App
import com.smilehacker.raven.kit.AppData
import com.smilehacker.raven.kit.ConfigManager
import com.smilehacker.raven.kit.NotificationHelper
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.util.Callback
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.util.isChinese
import com.smilehacker.raven.util.isNullOrEmpty
import com.smilehacker.raven.util.pinyin.Hz2Py
import com.smilehacker.raven.voice.TTSManager
import java.util.*

/**
 * Created by kleist on 16/6/28.
 */
class IndexPresenterImpl : IndexPresenter() {

    private val mAppData = AppData(App.Companion.inst)
    private val mAppInfos by lazy { ArrayList<AppInfo>() }

    override fun loadApps() {
        mAppData.loadAppAsync {
            mAppInfos.clear()
            mAppInfos.addAll(it)
            sortApp(mAppInfos)
            view?.showApps(mAppInfos)
        }
    }

    override fun setAppTTSEnable(packageName: String, enable: Boolean) {
        mAppData.setAppTTSEnable(packageName, enable)
    }

    override fun checkNotificationService(ctx: Context) {
        val notiHelper = NotificationHelper
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
        addSortName(apps)
        val enabledApps = apps.filter { it.enable }
        val disabledApps = apps.filter { !it.enable }

        apps.clear()
        apps.addAll(enabledApps.sortedWith(Comparator { t1, t2 -> t1.sortName.compareTo(t2.sortName)  }))
        apps.addAll(disabledApps.sortedWith(Comparator { t1, t2 -> t1.sortName.compareTo(t2.sortName)  }))
    }

    private fun addSortName(apps: MutableList<AppInfo>) {
        val pinyin = Hz2Py()
        apps.forEach {
            if (it.appName.isChinese()) {
                val pin = pinyin.getAllPy(it.appName.first())
                if (pin.isNullOrEmpty()) {
                    it.sortName = it.appName
                } else {
                    it.sortName = pin.first()
                }
            } else {
                it.sortName = it.appName
            }
            it.sortName = it.sortName.toLowerCase()
        }
        pinyin.free()
    }

    override fun queryByName(name: String?) {
        if (name.isNullOrBlank()) {
            view?.showApps(mAppInfos)
            return
        }
        val str = name!!.trim()
        val queriedApps = mAppInfos.filter { it.appName.toLowerCase().contains(str.toLowerCase()) }
        view?.showApps(queriedApps as MutableList<AppInfo>)
    }
}