package com.smilehacker.raven

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import com.activeandroid.query.Select
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

    fun mergeAppTTSEnable(apps: MutableList<AppInfo>) {
        val _apps = Select().from(AppInfo::class.java).execute<AppInfo>()
        apps.forEach {
            val packageName = it.packageName
            val _app = _apps.find { it.packageName.equals(packageName) }
            if (_app != null) {
                it.enable = _app.enable
            }
        }
    }

    fun loadApps() : MutableList<AppInfo> {
        val apps = loadAppsFromSys()
        mergeAppTTSEnable(apps)
        return apps
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

    fun setAppTTSEnable(packageName: String, enable: Boolean) {
        var app : AppInfo? = Select().from(AppInfo::class.java)
            .where("${AppInfo.DB.PACKAGE_NAME} = \"$packageName\"")
            .executeSingle()
        if (app == null) {
            app = AppInfo()
            app.packageName = packageName
        }
        app.enable = enable
        app.save()
    }

    fun isAppTTSEnable(packageName: String) : Boolean {
        val app : AppInfo? = Select().from(AppInfo::class.java)
                .where("${AppInfo.DB.PACKAGE_NAME} = \"$packageName\"")
                .executeSingle()
        return app != null && app.enable
    }
}
