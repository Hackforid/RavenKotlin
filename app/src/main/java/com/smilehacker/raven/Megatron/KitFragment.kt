package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by kleist on 16/6/6.
 */
abstract class KitFragment : Fragment(), IKitFragmentAction {

    val mFragmentActor by lazy { KitFragmentActor(this) }

    override var fragmentResult: FragmentResult? = mFragmentActor.fragmentResult

    override val hostActivity: HostActivity
        get() = mFragmentActor.hostActivity



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mFragmentActor.setUserVisibleHint(isVisibleToUser)
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

    override fun setNewBundle(bundle: Bundle?) {
        mFragmentActor.setNewBundle(bundle)
    }

    override fun getNewBundle() : Bundle? {
        return mFragmentActor.getNewBundle()
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
