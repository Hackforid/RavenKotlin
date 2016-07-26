package com.smilehacker.raven.Megatron

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

/**
 * Created by kleist on 16/7/26.
 */
class PreferenceKitFragment: PreferenceFragmentCompat(), IFragmentAction {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
    }


    companion object {

        val RESULT_CANCELED = 0
        val RESULT_OK = 1

        val STATE_SAVE_ENTER = "state_save_enter"
        val STATE_SAVE_EXIT = "state_save_exit"
        val STATE_SAVE_POP_ENTER = "state_save_pop_enter"
        val STATE_SAVE_POP_EXIT = "state_save_pop_exit"

    }

    private lateinit var mFragmentation : Fragmentation
    private var mNewBundle : Bundle? = null

    var hostActivity : HostActivity? = null
        get() = activity as HostActivity
        private  set

    var fragmentResult: FragmentResult? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mFragmentation = (context as HostActivity).mFragmentation
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onVisible()
        } else {
            onInvisible()
        }
    }

    open fun onVisible() {

    }

    open fun onInvisible() {

    }


    open fun onNewBundle(bundle: Bundle?) {

    }

    override fun startFragment(to: KitFragment, launchMode: Int) {
        mFragmentation.start(fragmentManager, this, to, launchMode)
    }

    override fun startFragmentForResult(to: KitFragment, requestCode: Int, launchMode: Int) {
        mFragmentation.start(fragmentManager, this, to, launchMode, Fragmentation.START_TYPE.ADD_WITH_RESULT, requestCode)
    }


    override fun popFragment() {
        finish()
    }

    override fun popToFragment(fragment: KitFragment, includeSelf: Boolean) {
        mFragmentation.popTo(fragmentManager, fragment, includeSelf)
    }

    open fun onBackPress() : Boolean {
        return false
    }

    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {

    }

    fun finish() {
        mFragmentation.finish(fragmentManager, this)
    }

    fun setResult(resultCode: Int, data: Bundle? = null) {
        fragmentResult?.let { it.data = data; it.resultCode = resultCode }
    }

    fun setNewBundle(bundle: Bundle?) {
        mNewBundle = bundle
    }

    fun getNewBundle() : Bundle? {
        return mNewBundle
    }

    open fun getAnimation() : Pair<Int, Int>? {
        return null
    }
}
