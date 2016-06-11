package com.smilehacker.raven.Megatron

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by kleist on 16/6/6.
 */
abstract class KitFragment : Fragment(), IFragmentAction {

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

    var fragmentResult: FragmentResult? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mFragmentation = (context as HostActivity).mFragmentation
    }


    fun onShow() {

    }

    fun onHide() {

    }

    fun onNewBundle(bundle: Bundle?) {

    }

    override fun startFragment(to: KitFragment, bundle: Bundle?, launchMode: Int) {
        mFragmentation.start(fragmentManager, this, to, bundle, launchMode)
    }

    override fun startFragmentForResult(to: KitFragment, requestCode: Int, launchMode: Int) {
    }


    override fun popFragment() {
    }

    open fun onBackPress() : Boolean {
        return false
    }

    open fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {

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

}
