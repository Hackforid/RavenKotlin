package com.smilehacker.raven.Megatron

import android.support.v4.app.FragmentManager

/**
 * Created by zhouquan on 16/6/9.
 */
class Fragmentation(val activity: HostActivity) {

    object FRAGMENT {
        const val IS_ROOT = "key_is_root"

    }

    object START_TYPE {
        const val ADD = 1
        const val ADD_WITH_RESULT = 2
        const val ADD_AND_POP = 3
    }

    object LAUNCH_MODE {
        const val SINGLE_TOP = 1
        const val SINGLE_TASK = 2
        const val STANDARD = 3
    }

    private val mFragmentStack: FragmentStack by lazy { FragmentStack() }

    fun start(fragmentManager: FragmentManager, from: KitFragment?, to: KitFragment,
              launchMode: Int = LAUNCH_MODE.STANDARD,
              startType: Int = START_TYPE.ADD,
              requestCode: Int = 0) {

        when (launchMode) {
            LAUNCH_MODE.STANDARD -> {
                mFragmentStack.putStandard(to)
            }
            LAUNCH_MODE.SINGLE_TOP -> {
                if (mFragmentStack.putSingleTop(to)) {
                    to.onNewBundle(to.getNewBundle())
                }
            }
            LAUNCH_MODE.SINGLE_TASK -> {
                if (mFragmentStack.putSingleTask(to)) {
                    to.onNewBundle(to.getNewBundle())
                }
            }
        }

        when (startType) {
            START_TYPE.ADD_WITH_RESULT -> {
                to.fragmentResult = FragmentResult(requestCode)
            }
        }

        val ft = fragmentManager.beginTransaction()

        if (from != null) {
            ft
                    .add(activity.getContainerID(), to, to.javaClass.name)
                    .hide(from)
        } else {
            ft
                    .add(activity.getContainerID(), to, to.javaClass.name)
        }
        ft.commit()
    }

    fun back(fragmentManager: FragmentManager) {
        val count = mFragmentStack.getStackCount()
        if (count >= 2) {
            val frg = mFragmentStack.getTopFragment()
            val preFrg = mFragmentStack.getFragments()[count - 2]
            if (frg!!.fragmentResult != null) {
                preFrg.onFragmentResult(frg.fragmentResult!!.requestCode,
                        frg.fragmentResult!!.resultCode, frg.fragmentResult!!.data)
            }

            mFragmentStack.pop()
            val ft = fragmentManager.beginTransaction()
            ft
                    .remove(frg)
                    .show(preFrg)
            ft.commit()
        }
    }

    fun finish(fragmentManager: FragmentManager, fragment: KitFragment) {
        val fragments = mFragmentStack.getFragments()
        if (fragment !in fragments) {
            return
        }
        if (fragments.size == 1) {
            activity.finish()
        }
        val index = fragments.indexOf(fragment)
        if (index > 1) {
            val preFragment = fragments[index - 1]
            if (fragment.fragmentResult != null) {
                preFragment.onFragmentResult(fragment.fragmentResult!!.requestCode,
                        fragment.fragmentResult!!.resultCode, fragment.fragmentResult!!.data)
            }
        }
    }

    fun getFragments(): MutableList<KitFragment> {
        return mFragmentStack.getFragments()
    }

    fun getStackCount(): Int {
        return mFragmentStack.getStackCount()
    }

    fun getTopFragment(): KitFragment? {
        return mFragmentStack.getTopFragment()
    }

}