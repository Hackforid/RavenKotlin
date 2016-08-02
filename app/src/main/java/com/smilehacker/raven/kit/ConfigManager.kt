package com.smilehacker.raven.kit

import android.content.Context

/**
 * Created by kleist on 16/7/1.
 */
object ConfigManager {
    const val DEFAULT_CONFIG = "default_config"

    const val CONFIG_ENABLE = "config_enable"

    const val CONFIG_FIRST_LAUNCH = "config_first_launch";

    const val CONFIG_SILENCE_TIME = "config_silence_time"


    private lateinit var mContext : Context;

    private val mPref by lazy { mContext.getSharedPreferences(DEFAULT_CONFIG, 0) }

    private var mIsFirstLaunch : Boolean? = null

    fun init(ctx: Context) {
        mContext = ctx
    }

    var isEnable : Boolean
        get() = mPref.getBoolean(CONFIG_ENABLE, false)
        set(value) {
            mPref.edit().putBoolean(CONFIG_ENABLE, value).commit()}

    private var _isFirstLaunch : Boolean
        get() = mPref.getBoolean(CONFIG_FIRST_LAUNCH, true)
        set(value) { mPref.edit().putBoolean(CONFIG_FIRST_LAUNCH, value).commit()}

    var isFirstLaunch : Boolean = true
        get() {
            if (mIsFirstLaunch == null) {
                mIsFirstLaunch = _isFirstLaunch
                if (mIsFirstLaunch as Boolean) {
                    _isFirstLaunch = false
                }
            }
            return mIsFirstLaunch as Boolean
        }
        private set

    var silenceTime :  IntArray
        get() = mPref.getString(CONFIG_SILENCE_TIME, "23-00-09-00").split("-").map { Integer.parseInt(it) }.toIntArray()
        set(value) {
            if (value.size != 4) {
                throw IllegalArgumentException("must be 4 int")
            }
            mPref.edit().putString(CONFIG_SILENCE_TIME, value.joinToString("-")).commit()
        }

}
