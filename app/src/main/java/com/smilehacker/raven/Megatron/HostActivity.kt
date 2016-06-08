package com.smilehacker.raven.Megatron

import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 16/6/6.
 */
abstract class HostActivity : AppCompatActivity(), IFragmentAction {

    abstract fun getContainerID() : Int

    val mFragmentController by lazy { FragmentController(this) }

    override fun startFragment(to : KitFragment, launchMode : Int) {
        mFragmentController.start(supportFragmentManager, mFragmentController.getTopFragment(supportFragmentManager), to, launchMode)
    }

    override fun startFragmentForResult(to: KitFragment, requestCode: Int, launchMode: Int) {
        mFragmentController.start(supportFragmentManager,
                mFragmentController.getTopFragment(supportFragmentManager), to,
                FragmentController.FRAGMENT.LAUNCH_MODE.STANDARD,
                FragmentController.START_TYPE.ADD_WITH_RESULT, requestCode)
    }

    override fun startFragmentWithFinish(to: KitFragment, launchMode: Int) {
        mFragmentController.start(supportFragmentManager, mFragmentController.getTopFragment(supportFragmentManager), to, launchMode,
                FragmentController.FRAGMENT.LAUNCH_MODE.STANDARD,
                FragmentController.START_TYPE.ADD_AND_POP)
    }

    override fun popFragment() {
        mFragmentController.pop(supportFragmentManager)
    }

    fun getFragmentController() : FragmentController {
        return mFragmentController
    }

    override fun onBackPressed() {
        val topFrg = mFragmentController.getTopFragment(supportFragmentManager)
        if (topFrg != null && topFrg.onBackPress()) {
            return
        }

        if (supportFragmentManager.backStackEntryCount > 1) {
            mFragmentController.pop(supportFragmentManager)
        } else {
            finish()
        }
    }
}