package com.smilehacker.raven.ui.index

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import butterknife.bindView
import com.smilehacker.raven.R
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.MVPFragment
import com.smilehacker.raven.util.ViewUtils

/**
 * Created by kleist on 16/6/28.
 */
class IndexFragment : MVPFragment<IndexPresenter, IndexViewer>(), IndexViewer, AppAdapter.AppCallback {

    private val mRvApps by bindView<RecyclerView>(R.id.rv_apps)
    private val mToolbar by bindView<Toolbar>(R.id.toolbar)

    private val mAppAdapter by lazy { AppAdapter(context, this)}

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.frg_index, container, false)
        return view
    }

    override fun createPresenter(): IndexPresenter {
        return IndexPresenterImpl()
    }

    override fun showApps(apps: MutableList<AppInfo>) {
        mAppAdapter.setApps(apps)
    }

    private fun initView() {
        val columnCount = 2
        val spaceOut = ViewUtils.dpToPx(12)
        val spaceInner = ViewUtils.dpToPx(4)
        val layoutManager = GridLayoutManager(context, columnCount, GridLayoutManager.VERTICAL, false)
        mRvApps.layoutManager = layoutManager
        mRvApps.adapter = mAppAdapter

        mRvApps.addItemDecoration(object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                val pos = parent.getChildAdapterPosition(view)
                val total = parent.adapter.itemCount
                if (pos % columnCount == 0) {
                    outRect.left = spaceOut
                    outRect.right = spaceInner
                } else {
                    outRect.left = spaceInner
                    outRect.right = spaceOut
                }

                if (pos < columnCount) {
                    outRect.top = spaceOut
                    outRect.bottom = spaceInner
                } else if (pos >= total - columnCount) {
                    outRect.top = spaceInner
                    outRect.bottom = spaceOut
                } else {
                    outRect.top = spaceInner
                    outRect.bottom = spaceInner
                }

            }
        })

        initToolbar()
    }

    private fun initToolbar() {
        hostActivity.setSupportActionBar(mToolbar)

        mToolbar.setOnMenuItemClickListener { item ->
            val id = item.itemId
            when (id) {
                R.id.action_settings -> {
                    val intent = Intent(this@MainActivity, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }



        mToolbar.title = "Raven"
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter.loadApps()
    }

    override fun onAppEnableChange(packageName: String, enable: Boolean) {
        presenter.setAppTTSEnable(packageName, enable)
    }

}
