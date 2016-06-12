package com.smilehacker.raven.mvp

import java.lang.ref.WeakReference

/**
 * Created by kleist on 15/12/2.
 */
abstract class BasePresenter<V : Viewer> : Presenter<V> {
    private var mViewRef : WeakReference<V>? = null

    var view : V? = null
        get() = mViewRef?.get()
        private set

    override fun attachView(view: V) {
        mViewRef = WeakReference<V>(view)
    }

    override fun detachView(retainInstance: Boolean) {
        mViewRef?.clear()
        mViewRef = null
    }

    fun isViewAttached() : Boolean {
        return mViewRef?.get() != null
    }

    open fun onShow() {

    }

    open fun onHidden() {

    }
}