package com.smilehacker.raven

import android.content.Context

/**
 * Created by kleist on 16/7/1.
 */
object ConfigManager {
    const val DEFAULT_CONFIG = "default_config"

    const val CONFIG_ENABLE = "config_enable"


    private lateinit var mContext : Context;

    private val mPref by lazy { mContext.getSharedPreferences(DEFAULT_CONFIG, 0) }

    fun init(ctx: Context) {
        mContext = ctx
    }

    var isEnable : Boolean
        get() = mPref.getBoolean(CONFIG_ENABLE, false)
        set(value) {mPref.edit().putBoolean(CONFIG_ENABLE, value).commit()}
}
