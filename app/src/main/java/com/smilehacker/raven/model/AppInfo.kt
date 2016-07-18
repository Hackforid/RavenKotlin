package com.smilehacker.raven.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

/**
 * Created by kleist on 16/6/13.
 */
@Table(name = AppInfo.DB.TABLENAME)
class AppInfo : Model() {

    object DB {
        const val TABLENAME = "app_info"
        const val PACKAGE_NAME = "package_name"
        const val ENABLE = "enable"
        const val APP_NAME = "app_name"
        const val VOICE_FORMAT = "voice_format"
    }

    //@Column(name = DB.APP_NAME)
    var appName : String = ""

    @Column(name = DB.PACKAGE_NAME, index = true, unique = true)
    var packageName : String = ""

    @Column(name = DB.ENABLE)
    var enable : Boolean = false

    @Column(name = DB.VOICE_FORMAT)
    var voiceFormat : String? = null

}
