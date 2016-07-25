package com.smilehacker.raven.ui.index.preference

import android.os.Bundle
import android.preference.PreferenceActivity
import com.smilehacker.raven.R

/**
 * Created by zhouquan on 16/7/25.
 */
class ConfigActivity: PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)
    }

}
