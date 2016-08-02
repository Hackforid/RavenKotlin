package com.smilehacker.raven.Megatron

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 16/6/6.
 */
abstract class HostActivity : AppCompatActivity(), IFragmentAction {

    abstract fun getContainerID() : Int

    val mFragmentation by lazy { Fragmentation(this) }

    override fun startFragment(to: Fragment, launchMode: Int) {
        mFragmentation.start(supportFragmentManager, mFragmentation.getTopFragment(), to, launchMode, Fragmentation.START_TYPE.ADD)
    }

    override fun startFragmentForResult(to: Fragment, requestCode: Int, launchMode: Int) {
        mFragmentation.start(supportFragmentManager, mFragmentation.getTopFragment(), to, launchMode, Fragmentation.START_TYPE.ADD_WITH_RESULT, requestCode)
    }

    override fun popFragment() {
        onBackPressed()
    }

    override fun popToFragment(fragment: Fragment, includeSelf: Boolean) {
        mFragmentation.popTo(supportFragmentManager, fragment, includeSelf)
    }


    override fun onBackPressed() {
        val top = mFragmentation.getTopFragment()
        if (top == null || top !is IKitFragmentAction) {
            return
        }
        if (top.onBackPress()) {
            return
        }

        if (mFragmentation.getStackCount() > 1) {
            mFragmentation.finish(supportFragmentManager, top!!)
        } else {
            finish()
        }
    }
}