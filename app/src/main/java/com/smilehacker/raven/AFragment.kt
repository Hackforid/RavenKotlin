package com.smilehacker.raven

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.raven.Megatron.KitFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by kleist on 16/6/7.
 */
class AFragment : KitFragment() {

    private var mNum : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments ?: Bundle()
        mNum = bundle.getInt("num", 0)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            verticalLayout {
                backgroundColor = Color.WHITE
                padding = dip(30)
                textView {
                    text = "AFragment $mNum"
                }
                button {
                    text = "next"
                    onClick { showNext() }
                }
            }
        }.view
//        val view = inflater?.inflate(R.layout.frg_a, container, false)
//        view?.find<Button>(R.id.btn)?.onClick { showNext() }
//        return view
    }

    private fun showNext() {
        val frg = AFragment()
        val bundle = Bundle()
        bundle.putInt("num", mNum+1)
        frg.arguments = bundle
        startFragment(frg)
    }
}
