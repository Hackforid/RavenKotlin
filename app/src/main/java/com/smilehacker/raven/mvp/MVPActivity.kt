package com.smilehacker.raven.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 15/12/2.
 */

abstract class MVPActivity<in P : Presenter<V>, in V : Viewer>: AppCompatActivity() {

    private val mPresenter : P by lazy { createPresenter() }
    val presenter = mPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this !is Viewer) {
            throw NotImplementedError("Viewer not implemented")
        }
        presenter.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView(false)
    }

    protected abstract fun createPresenter() : P
}

