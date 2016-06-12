package com.smilehacker.raven.mvp

import android.os.Bundle
import android.view.View
import com.smilehacker.raven.base.BaseFragment

/**
 * Created by kleist on 15/12/2.
 */
abstract class MVPFragment<P : BasePresenter<V>, V : Viewer> : BaseFragment() {
    private val mPresenter : P by lazy { createPresenter() }
    val presenter = mPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this !is Viewer) {
            throw NotImplementedError("Viewer not implemented")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.attachView(this as V)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.detachView(retainInstance)
    }

    protected abstract fun createPresenter() : P

    override fun onVisible() {
        super.onVisible()
        presenter.onShow()
    }

    override fun onInvisible() {
        super.onInvisible()
        presenter.onHidden()
    }

}


