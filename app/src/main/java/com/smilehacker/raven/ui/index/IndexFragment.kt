package com.smilehacker.raven.ui.index

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.smilehacker.raven.R
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.MVPFragment

/**
 * Created by kleist on 16/6/28.
 */
class IndexFragment : MVPFragment<IndexPresenter, IndexViewer>(), IndexViewer {

    private val mRvApps by bindView<RecyclerView>(R.id.rv_apps)
    private val mAppAdpater by lazy { AppAdapter(context) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.frg_index, container, false)
        initView()
        return view
    }

    override fun createPresenter(): IndexPresenter {
        return IndexPresenterImpl()
    }

    override fun showApps(apps: MutableList<AppInfo>) {
    }

    private fun initView() {
        val layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        mRvApps.adapter = mAppAdpater
        mRvApps.layoutManager = layoutManager
    }
}
