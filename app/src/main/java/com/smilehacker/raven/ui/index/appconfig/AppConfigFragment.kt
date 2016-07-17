package com.smilehacker.raven.ui.index.appconfig

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import butterknife.bindView
import com.smilehacker.raven.R
import com.smilehacker.raven.kit.VoiceMaker
import com.smilehacker.raven.mvp.MVPFragment
import com.smilehacker.raven.util.DLog
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import org.jetbrains.anko.textView

/**
 * Created by zhouquan on 16/7/17.
 */
class AppConfigFragment : MVPFragment<AppConfigPresenter, AppConfigViewer>(), AppConfigViewer {


    val mEtText by bindView<EditText>(R.id.et_text)
    val mVgSymbols by bindView<LinearLayout>(R.id.vg_symbols)
    val mSymbols by lazy { presenter.getVoiceSymbols() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.frg_app_config, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        mSymbols.forEach {
            val view = getSymbolView(it)
            mVgSymbols.addView(view)
            view.setTag(R.string.tag_key_symbol, it)
            view.setOnClickListener(mOnSymbolClickListener)
        }
        mEtText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val cursorStart = mEtText.selectionStart
                mEtText.removeTextChangedListener(this)
                mEtText.setText(replaceSymbolByImage(mEtText.text.toString()))
                mEtText.setSelection(cursorStart)
                mEtText.addTextChangedListener(this)
            }

        })
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
            DLog.d("onclik")
            if (symbol != null) {
                insetImageSpan(symbol as VoiceMaker.VoiceSymbol)
            }
    }

    private fun getImageSpan(symbol: VoiceMaker.VoiceSymbol) : Spannable {
        val drawable = resources.getDrawable(symbol.icon, context.theme)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val spannable = SpannableString(symbol.symbol)
        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        spannable.setSpan(imageSpan, 0, symbol.symbol.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannable
    }

    private fun replaceSymbolByImage(text: String): SpannableString {
        val spannable = SpannableString(text)
        mSymbols.forEach {


            var pos = text.indexOf(it.symbol)
            while (pos >= 0) {
                val drawable = resources.getDrawable(it.icon, context.theme)
                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
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

    private fun getSymbolView(symbol: VoiceMaker.VoiceSymbol): View {
        val view = UI {
            linearLayout {
                bottomPadding = dip(12)
                orientation = LinearLayout.HORIZONTAL
                imageView {
                    setImageResource(symbol.icon)
                }
                textView {
                    text = symbol.text
                }
            }
        }.view

        return view
    }

}
