package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 16/6/6.
 */
abstract class HostActivity : AppCompatActivity(), IFragmentAction {

    companion object {
        const val KEY_FRAGMENTATION = "key_fragmentation"
    }

    abstract fun getContainerID() : Int

    lateinit var mFragmentation : Fragmentation

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mFragmentation = savedInstanceState.getSerializable(KEY_FRAGMENTATION) as Fragmentation
            mFragmentation.init(this)
        } else {
            mFragmentation = Fragmentation()
            mFragmentation.init(this)
        }
        super.onCreate(savedInstanceState)
    }

    override fun startFragment(to: Fragment, launchMode: Int) {
        mFragmentation.start(supportFragmentManager, mFragmentation.getTopFragment(supportFragmentManager), to, launchMode, Fragmentation.START_TYPE.ADD)
    }

    override fun startFragmentForResult(to: Fragment, requestCode: Int, launchMode: Int) {
        mFragmentation.start(supportFragmentManager, mFragmentation.getTopFragment(supportFragmentManager), to, launchMode, Fragmentation.START_TYPE.ADD_WITH_RESULT, requestCode)
    }

    override fun popFragment() {
        onBackPressed()
    }

    override fun popToFragment(fragment: Fragment, includeSelf: Boolean) {
        mFragmentation.popTo(supportFragmentManager, fragment, includeSelf)
    }


    override fun onBackPressed() {
        val top = mFragmentation.getTopFragment(supportFragmentManager)
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(KEY_FRAGMENTATION, mFragmentation)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
    }
}