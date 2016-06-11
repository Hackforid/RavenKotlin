package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 16/6/6.
 */
abstract class HostActivity : AppCompatActivity(), IFragmentAction {

    abstract fun getContainerID() : Int

    val mFragmentation by lazy { Fragmentation(this) }

    override fun startFragment(to: KitFragment, bundle: Bundle?, launchMode: Int) {
        mFragmentation.start(supportFragmentManager, mFragmentation.getTopFragment(), to, bundle, launchMode, Fragmentation.START_TYPE.ADD)
    }

    override fun startFragmentForResult(to: KitFragment, requestCode: Int, launchMode: Int) {
        mFragmentation.start(supportFragmentManager, mFragmentation.getTopFragment(), to, bundle, launchMode, Fragmentation.START_TYPE.ADD)
    }

    override fun popFragment() {
        onBackPressed()
    }


    override fun onBackPressed() {
        val top = mFragmentation.getTopFragment()
        if (top != null && top.onBackPress()) {
            return
        }

        if (mFragmentation.getStackCount() > 1) {
            mFragmentation.back(supportFragmentManager)
        } else {
            finish()
        }
    }
}