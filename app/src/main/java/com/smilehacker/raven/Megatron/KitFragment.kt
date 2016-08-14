package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.smilehacker.raven.R
import com.smilehacker.raven.util.DLog

/**
 * Created by kleist on 16/6/6.
 */
abstract class KitFragment : Fragment(), IKitFragmentAction {

    companion object {
        const val KEY_IS_HIDDEN = "key_is_hidden"
    }

    val mFragmentActor by lazy { KitFragmentActor(this) }

    override var fragmentResult: FragmentResult? = mFragmentActor.fragmentResult

    override val hostActivity: HostActivity
        get() = mFragmentActor.hostActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DLog.i("===stack===" + hostActivity.mFragmentation.hashCode())
        hostActivity.mFragmentation.getFragments(fragmentManager).forEach {
            DLog.i(it.javaClass.name)
        }
        if (savedInstanceState != null) {
            val isHidden = savedInstanceState.getBoolean(KEY_IS_HIDDEN, false)
            val ft = fragmentManager.beginTransaction()
            if (isHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onInvisible()
        } else {
            onVisible()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
    }

    override fun startFragment(to: Fragment, launchMode: Int) {
        mFragmentActor.startFragment(to, launchMode)
    }

    override fun startFragmentForResult(to: Fragment, requestCode: Int, launchMode: Int) {
        mFragmentActor.startFragmentForResult(to, requestCode, launchMode)
    }


    override fun popFragment() {
        mFragmentActor.popFragment()
    }

    override fun popToFragment(fragment: Fragment, includeSelf: Boolean) {
        mFragmentActor.popToFragment(fragment, includeSelf)
    }

    override fun finish() {
        mFragmentActor.finish()
    }

    override fun setResult(resultCode: Int, data: Bundle?) {
        mFragmentActor.setResult(resultCode, data)

    }

    override fun onNewBundle(bundle: Bundle?) {
    }

    override fun onBackPress(): Boolean {
        return false
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
    }

    override fun getAnimation(): Pair<Int, Int>? {
        val anim = Pair(R.anim.default_frg_in, R.anim.default_frg_out)
        return anim
    }

    override fun onVisible() {
    }

    override fun onInvisible() {
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(KEY_IS_HIDDEN, isHidden)
    }


}
