package com.smilehacker.raven.Megatron

import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 16/6/6.
 */
abstract class HostActivity : AppCompatActivity(), IFragmentAction {

    abstract fun getContainerID() : Int

    val mFragmentController by lazy { FragmentController(this) }

    override fun startFragment(to : KitFragment, launchMode : Int) {
        mFragmentController.start(supportFragmentManager, null, to, launchMode)
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