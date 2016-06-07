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

    private lateinit var mFragmentController : FragmentController

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mFragmentController = (context as HostActivity).getFragmentController()
    }


    fun onShow() {

    }

    fun onHide() {

    }

    fun onNewBundle(bundle: Bundle?) {

    }

    override fun startFragment(to: KitFragment, launchMode: Int) {
        mFragmentController.start(fragmentManager, null, to, launchMode)
    }

    override fun popFragment() {
        mFragmentController.pop(fragmentManager)
    }
}
