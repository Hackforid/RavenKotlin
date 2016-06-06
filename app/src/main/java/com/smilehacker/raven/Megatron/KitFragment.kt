package com.smilehacker.raven.Megatron

import android.support.v4.app.Fragment

/**
 * Created by kleist on 16/6/6.
 */
abstract class KitFragment : Fragment() {

    companion object {
        // LaunchMode
        val STANDARD = 0
        val SINGLETOP = 1
        val SINGLETASK = 2

        val RESULT_CANCELED = 0
        val RESULT_OK = 1

        val STATE_SAVE_ENTER = "state_save_enter"
        val STATE_SAVE_EXIT = "state_save_exit"
        val STATE_SAVE_POP_ENTER = "state_save_pop_enter"
        val STATE_SAVE_POP_EXIT = "state_save_pop_exit"
    }



    fun onShow() {

    }

    fun onHide() {

    }
}
