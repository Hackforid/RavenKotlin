package com.smilehacker.raven.mvp

/**
 * Created by kleist on 15/12/2.
 */
interface Presenter<in V : Viewer> {

    //var mView : Viewer?

    fun attachView(view : V)

    fun detachView(retainInstance : Boolean)

}