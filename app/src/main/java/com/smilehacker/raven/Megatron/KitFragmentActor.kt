package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by kleist on 16/8/2.
 */
class KitFragmentActor(val fragment: Fragment) : IKitFragmentActor {

    private var mNewBundle : Bundle? = null

    override val hostActivity : HostActivity by lazy { fragment.activity as HostActivity }
    private val mFragmentation : Fragmentation by lazy { hostActivity.mFragmentation }

    override var fragmentResult: FragmentResult? = null

    init {
        if (fragment !is IKitFragmentAction) {
            throw IllegalArgumentException("fragment must impl IKitFragmentAction")
        }
    }

    // should call
    fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            (fragment as IKitFragmentAction).onVisible()
        } else {
            (fragment as IKitFragmentAction).onInvisible()
        }
    }

    override fun startFragment(to: Fragment, launchMode: Int) {
        mFragmentation.start(fragment.fragmentManager, fragment, to, launchMode)
    }

    override fun startFragmentForResult(to: Fragment, requestCode: Int, launchMode: Int) {
        mFragmentation.start(fragment.fragmentManager, fragment, to, launchMode, Fragmentation.START_TYPE.ADD_WITH_RESULT, requestCode)
    }


    override fun popFragment() {
        finish()
    }

    override fun popToFragment(fragment: Fragment, includeSelf: Boolean) {
        mFragmentation.popTo(fragment.fragmentManager, fragment, includeSelf)
    }

    override fun finish() {
        mFragmentation.finish(fragment.fragmentManager, fragment)
    }


    override fun setResult(resultCode: Int, data: Bundle?) {
        fragmentResult?.let { it.data = data; it.resultCode = resultCode }
    }

    override fun setNewBundle(bundle: Bundle?) {
        mNewBundle = bundle
    }

    override fun getNewBundle() : Bundle? {
        return mNewBundle
    }

}
