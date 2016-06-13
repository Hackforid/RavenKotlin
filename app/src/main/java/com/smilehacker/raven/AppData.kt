package com.smilehacker.raven

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * Created by kleist on 16/6/13.
 */
class AppData(val context: Context) {

    private val packageManager = context.packageManager

    fun loadAppsFromSys() {
        val packages = packageManager.getInstalledPackages(PackageManager.PERMISSION_GRANTED)

    }

    fun isLaunchable(packageInfo: PackageInfo)
            =  packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null
}
