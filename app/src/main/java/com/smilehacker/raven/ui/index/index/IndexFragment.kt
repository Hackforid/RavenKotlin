package com.smilehacker.raven.ui.index.index

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.*
import android.view.*
import android.widget.RelativeLayout
import butterknife.bindView
import com.smilehacker.raven.Constants
import com.smilehacker.raven.R
import com.smilehacker.raven.kit.ConfigManager
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.MVPFragment
import com.smilehacker.raven.ui.index.appconfig.AppConfigFragment
import com.smilehacker.raven.ui.index.preference.ConfigFragment
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.util.ViewUtils
import com.smilehacker.raven.widget.IndexSideBar

/**
 * Created by kleist on 16/6/28.
 */
class IndexFragment : MVPFragment<IndexPresenter, IndexViewer>(), IndexViewer, AppAdapter.AppCallback {

    private val mRvApps by bindView<RecyclerView>(R.id.rv_apps)
    private val mToolbar by bindView<Toolbar>(R.id.toolbar)
    private val mRoot by bindView<RelativeLayout>(R.id.root)
    private val mSwitch by bindView<SwitchCompat>(R.id.v_switch)
    private val mIndexSider by bindView<IndexSideBar>(R.id.indexsider)

    private lateinit var mSearchView : SearchView

    private val mAppAdapter by lazy { AppAdapter(context, this) }

    private val REQUEST_CODE_SET_NOTIFICATION_SERVICE = 1231
    private val REQUEST_CODE_SET_TTS = 1232
    private val REQUEST_CODE_SEARCH_TTS = 1233

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
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

        mRvApps.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                DLog.i("scroll le = " + mAppAdapter.getAppByPos(layoutManager.findFirstVisibleItemPosition()).sortName.first())
                mIndexSider.selectIndex = mAppAdapter.getAppByPos(layoutManager.findFirstVisibleItemPosition()).sortName.first().toLowerCase()
            }
        })

        mIndexSider.setOnIndexListener(object : IndexSideBar.OnIndexListener {
            override fun onSelect(pos: Int, index: String) {
                val pos = mAppAdapter.getPositionByFirstLetter(index.first())
                DLog.i("index $index pos = $pos")
                if (pos != -1) {
                    mRvApps.smoothScrollToPosition(pos)
                }
            }

            override fun onTouchDown() {
            }

            override fun onTouchUp() {
            }

        })

        mSwitch.isChecked = ConfigManager.isEnable
        mSwitch.setOnCheckedChangeListener { compoundButton, checked -> ConfigManager.isEnable = checked }

        initToolbar()
    }

    private fun initToolbar() {
        hostActivity.setSupportActionBar(mToolbar)
        mToolbar.title = "Raven"
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)

        mSearchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        mSearchView.isSubmitButtonEnabled = false
        mSearchView.setOnCloseListener {
            presenter.queryByName(null); true }
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.queryByName(newText)
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_settings -> {
                startFragment(ConfigFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter.loadApps()
        presenter.checkNotificationService(context)
    }

    override fun onAppEnableChange(packageName: String, enable: Boolean) {
        presenter.setAppTTSEnable(packageName, enable)
    }

    override fun showSetNotificationDialog() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("通知服务未开启")
        builder.setMessage("Raven需要您开启通知服务以正常运行,请手动开启.")
        builder.setPositiveButton("设置通知服务",
                { dialog, i -> gotoSetNotificationService(); dialog.dismiss() }
        )
        builder.setNegativeButton("以后设置", {dialog, i -> showSetNotificationSnackbar(); dialog.dismiss(); presenter.checkTTS()})
        builder.setCancelable(false)
        builder.create().show()
    }

    override fun showSetNotificationSnackbar() {
        val snackbar = Snackbar.make(mRvApps, "Raven需要您开启通知服务以正常运行,请手动开启.", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("设置", {view -> gotoSetNotificationService(); snackbar.dismiss()})
        snackbar.show()
    }

    private fun gotoSetNotificationService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startActivityForResult(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"),
                    REQUEST_CODE_SET_NOTIFICATION_SERVICE)
        } else {
            startActivityForResult(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                    REQUEST_CODE_SET_NOTIFICATION_SERVICE)
        }
    }

    private fun gotoMarketSearchTTS() {
        val viewIntent = Intent("android.intent.action.VIEW",
                Uri.parse("market://search?q=TTS"))
        startActivityForResult(viewIntent, REQUEST_CODE_SEARCH_TTS)
    }

    private fun gotoSetTTS() {
        val intent = Intent()
        intent.action = "com.android.settings.TTS_SETTINGS"
        startActivityForResult(intent, REQUEST_CODE_SET_TTS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SET_NOTIFICATION_SERVICE) {
            presenter.checkNotificationService(context)
        }
        when(resultCode) {
            REQUEST_CODE_SET_NOTIFICATION_SERVICE -> presenter.checkNotificationService(context)
            REQUEST_CODE_SET_TTS -> presenter.checkTTS()
            REQUEST_CODE_SEARCH_TTS -> presenter.checkTTS()
        }
    }

    override fun showSetTTSDialog(msg: String) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("TTS服务")
        builder.setMessage("$msg,请手动设置.如果未安装文字转语音服务,您可以到各大市场搜索TTS来安装,推荐使用讯飞语音.")
        builder.setPositiveButton("去设置",
                { dialog, i -> gotoSetTTS(); dialog.dismiss() }
        )
        builder.setNeutralButton("去下载", {dialog, i -> gotoMarketSearchTTS(); showSetTTSSnackbar(msg); dialog.dismiss() })
        builder.setNegativeButton("以后再说", {dialog, i -> showSetTTSSnackbar(msg); dialog.dismiss()})
        builder.setCancelable(false)
        builder.create().show()
    }

    override fun showSetTTSSnackbar(msg: String) {
        val snackbar = Snackbar.make(mRoot, "文字转语音服务不可用,请手动设置", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("查看", {view -> showSetTTSDialog(msg); snackbar.dismiss()})
        snackbar.show()
    }


    override fun onOpenAppConfig(app: AppInfo) {
        val frg = AppConfigFragment()
        val bundle = Bundle()
        bundle.putString(Constants.KEY_PACKAGE_NAME, app.packageName)
        DLog.i("set args")
        frg.arguments = bundle
        startFragment(frg)
    }

    override fun onBackPress(): Boolean {
        mSearchView.onActionViewCollapsed()
        return super.onBackPress()
    }
}
