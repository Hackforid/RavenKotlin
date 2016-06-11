package com.smilehacker.raven.Megatron

import android.os.Bundle

/**
 * Created by kleist on 16/6/7.
 */
interface IFragmentAction {

    fun startFragment(to : KitFragment, bundle: Bundle? = null, launchMode : Int = FragmentController.FRAGMENT.LAUNCH_MODE.STANDARD)

    fun popFragment()

    fun startFragmentForResult(to : KitFragment, requestCode: Int, launchMode : Int = FragmentController.FRAGMENT.LAUNCH_MODE.STANDARD)
}
