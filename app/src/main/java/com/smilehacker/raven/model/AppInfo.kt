package com.smilehacker.raven.model

import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/**
 * Created by kleist on 16/6/13.
 */
@Table(name = AppInfo.DB.TABLENAME)
class AppInfo {

    object DB {
        const val TABLENAME = "app_info"
        const val PACKAGE_NAME = "package_name"
        const val ENABLE = "enable"
    }

    @Column(name = DB.PACKAGE_NAME, index = true, unique = true)
    var packageName : String? = null

    @Column(name = DB.ENABLE)
    var enable : Boolean = false
}
