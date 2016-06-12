package com.smilehacker.raven.mvp

import com.smilehacker.raven.mvp.Viewer

/**
 * Created by kleist on 15/12/2.
 */
interface Presenter<V : Viewer> {

    //var mView : Viewer?

    public fun attachView(view : V)

    public fun detachView(retainInstance : Boolean)

}