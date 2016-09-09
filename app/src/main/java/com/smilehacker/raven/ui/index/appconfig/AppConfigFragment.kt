package com.smilehacker.raven.ui.index.appconfig

import android.annotation.TargetApi
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.bindView
import com.smilehacker.raven.Constants
import com.smilehacker.raven.R
import com.smilehacker.raven.kit.VoiceMaker
import com.smilehacker.raven.model.AppInfo
import com.smilehacker.raven.mvp.MVPFragment
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.util.nullOr
import org.jetbrains.anko.onClick
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.support.v4.toast

/**
 * Created by zhouquan on 16/7/17.
 */
class AppConfigFragment : MVPFragment<AppConfigPresenter, AppConfigViewer>(), AppConfigViewer {


    val mEtText by bindView<EditText>(R.id.et_text)
    val mToolbar by bindView<Toolbar>(R.id.toolbar)
    val mBtnDefault by bindView<TextView>(R.id.btn_default)
    val mBtnConfirm by bindView<FloatingActionButton>(R.id.btn_confirm)
    val mTagName by bindView<ImageView>(R.id.tag_name)
    val mTagTitle by bindView<ImageView>(R.id.tag_title)
    val mTagMsg by bindView<ImageView>(R.id.tag_msg)
    val mTvAppName by bindView<TextView>(R.id.tv_app_name)
    val mContainer by bindView<RelativeLayout>(R.id.container)

    val mSymbols by lazy { presenter.getVoiceSymbols() }

    lateinit var mAppInfo :AppInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLog.i("onCreate")
        if (arguments == null) {
            DLog.e("argument is null")
            finish()
            return
        }
        val packageName = arguments.getString(Constants.KEY_PACKAGE_NAME)
        if (packageName == null) {
            DLog.e("packageName is null")
            finish()
            return
        }
        val appInfo = presenter.getAppInfo(packageName)
        if (appInfo == null) {
            toast("应用不存在")
            finish()
            return
        }
        mAppInfo = appInfo

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.frg_app_config, container, false)
        return view
    }

    override fun onVisible() {
        super.onVisible()
        DLog.i("onVisible")
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onInvisible() {
        super.onInvisible()
        DLog.i("onInVisible")
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        mContainer.post {
            doStartAnim()
        }
    }

    private fun initUI() {
        mTagTitle.setOnClickListener(mOnSymbolClickListener)
        mTagTitle.setTag(R.string.tag_key_symbol, mSymbols[0])
        mTagName.setOnClickListener(mOnSymbolClickListener)
        mTagName.setTag(R.string.tag_key_symbol, mSymbols[3])
        mTagMsg.setOnClickListener(mOnSymbolClickListener)
        mTagMsg.setTag(R.string.tag_key_symbol, mSymbols[1])

        mEtText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cursorStart = mEtText.selectionStart
                mEtText.removeTextChangedListener(this)
                mEtText.setText(replaceSymbolByImage(mEtText.text.toString()))
                mEtText.setSelection(cursorStart)
                mEtText.addTextChangedListener(this)
            }
        })

        mEtText.background.mutate().setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)

        if (mAppInfo.voiceFormat == null) {
            mAppInfo.voiceFormat = VoiceMaker.default_voice_format
        }
        mEtText.setText(mAppInfo.voiceFormat)

        mBtnConfirm.onClick {
            val text = mEtText.text.toString()
            presenter.saveVoiceFormat(mAppInfo.packageName, text)
            finish()
        }

        mBtnDefault.onClick {
            mEtText.setText(VoiceMaker.default_voice_format)
            mEtText.setSelection(VoiceMaker.default_voice_format.length)
        }

        mTvAppName.text = "${mAppInfo.appName}消息语音定制"

        mContainer.visibility = View.INVISIBLE
        initToolbar()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun doStartAnim() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        mContainer.visibility = View.VISIBLE
        val x = arguments?.getInt(Constants.KEY_X, 0).nullOr(mContainer.width / 2)
        val y = arguments?.getInt(Constants.KEY_Y, 0).nullOr(mContainer.height / 2)
        val anim = ViewAnimationUtils.createCircularReveal(mContainer, x, y, 0f, Math.hypot((mContainer.width / 2).toDouble(), (mContainer.height / 2).toDouble()).toFloat())
        anim.duration = 200
        anim.interpolator = AccelerateInterpolator()
        anim.start()
    }

    private fun initToolbar() {
        hostActivity.setSupportActionBar(mToolbar)
        //mToolbar.title = "${mAppInfo.appName} 消息定制"
        mToolbar.title = ""
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener {
            finish()
        }
        hostActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun insetImageSpan(symbol: VoiceMaker.VoiceSymbol) {
        val builder = StringBuilder()
        val cursorPos = mEtText.selectionStart
        val text = mEtText.text.toString()
        if (cursorPos > 0) {
            builder.append(text.substring(0, cursorPos))
        }
        builder.append(symbol.symbol)
        builder.append(text.substring(cursorPos))
//        mEtText.setText(replaceSymbolByImage(builder.toString()))
        mEtText.setText(builder.toString())
        mEtText.setSelection(cursorPos + symbol.symbol.length)
    }


    private val mOnSymbolClickListener = View.OnClickListener {
        view ->
            val symbol = view.getTag(R.string.tag_key_symbol)
            if (symbol != null) {
                insetImageSpan(symbol as VoiceMaker.VoiceSymbol)
            }
    }

    private fun getImageSpan(symbol: VoiceMaker.VoiceSymbol) : Spannable {
        val drawable = resources.getDrawable(symbol.icon, context.theme)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val spannable = SpannableString(symbol.symbol)
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
        spannable.setSpan(imageSpan, 0, symbol.symbol.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannable
    }

    private fun replaceSymbolByImage(text: String): SpannableString {
        val spannable = SpannableString(text)
        mSymbols.forEach {


            var pos = text.indexOf(it.symbol)
            while (pos >= 0) {
                val drawable = resources.getDrawable(it.icon, context.theme)
                drawable.setBounds(0, 0, dip(50), dip(25))
                val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM)
                spannable.setSpan(imageSpan, pos, pos + it.symbol.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                pos = text.indexOf(it.symbol, pos + 1)
            }
        }
        return spannable
    }

    override fun createPresenter(): AppConfigPresenter {
        return AppConfigPresenterImpl()
    }

    override fun showText(text: String) {
        mEtText.setText(text)
    }

    override fun getAnimation(): kotlin.Pair<Int, Int>? {
        return null
    }

}
