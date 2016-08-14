package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View

/**
 * Created by kleist on 16/8/2.
 */
abstract class KitPreferenceFragment: PreferenceFragmentCompat(), IKitFragmentAction {
    val mFragmentActor by lazy { KitFragmentActor(this) }

    override var fragmentResult: FragmentResult? = mFragmentActor.fragmentResult

    override val hostActivity: HostActivity
        get() = mFragmentActor.hostActivity


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
        return null
    }

    override fun onVisible() {
    }

    override fun onInvisible() {
    }
}
