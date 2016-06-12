package com.smilehacker.raven.Megatron

/**
 * Created by kleist on 16/6/7.
 */
interface IFragmentAction {

    fun startFragment(to : KitFragment, launchMode : Int = FragmentController.FRAGMENT.LAUNCH_MODE.STANDARD)


    fun startFragmentForResult(to : KitFragment, requestCode: Int, launchMode : Int = FragmentController.FRAGMENT.LAUNCH_MODE.STANDARD)

    fun popFragment()

    fun popToFragment(fragment: KitFragment, includeSelf: Boolean = false)
}
