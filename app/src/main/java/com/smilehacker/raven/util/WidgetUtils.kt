package com.smilehacker.raven.util

import android.support.design.widget.Snackbar
import android.view.ViewGroup
import android.view.ViewParent

/**
 * Created by zhouquan on 16/8/15.
 */

/**
 * 解决Snackbar只能使用RootView的问题 防止Fragment切换后Snackbar依旧显示
 */
fun makeSnackbar(parent: ViewParent, text: CharSequence, duration: Int) : Snackbar {
    val constructor = Snackbar::class.java.getDeclaredConstructor(ViewGroup::class.java)
    constructor.isAccessible = true
    val snackbar = constructor.newInstance(parent)
    snackbar.setText(text)
    snackbar.duration = duration
    return snackbar
}
