package com.smilehacker.raven

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.smilehacker.raven.model.AppInfo
import java.util.*

/**
 * Created by kleist on 16/6/13.
 */
class AppData(val context: Context) {

    private val packageManager = context.packageManager

    fun loadAppsFromSys() : MutableList<AppInfo> {
        val appList = ArrayList<AppInfo>()
        val packages = packageManager.getInstalledPackages(PackageManager.PERMISSION_GRANTED)
        for (pkg in packages) {
            if (!isLaunchable(pkg.packageName)) {
                continue
            }

            val appInfo = AppInfo()
            appInfo.appName = packageManager.getApplicationLabel(pkg.applicationInfo).toString()
            appInfo.packageName = pkg.packageName
            appList.add(appInfo)
        }

        return appList
    }

    fun isLaunchable(packageName: String)
            =  packageManager.getLaunchIntentForPackage(packageName) != null

    companion object {
        fun getIcon(ctx: Context, packageName: String) : Drawable? {
            val pm = ctx.packageManager
            try {
                return pm.getApplicationIcon(packageName)
            } catch (e : PackageManager.NameNotFoundException) {
                return null
            }
        }
    }
}
