package com.smilehacker.raven.Megatron

import android.support.v4.app.FragmentManager
import com.smilehacker.raven.R

/**
 * Created by zhouquan on 16/6/9.
 */
class Fragmentation(val activity: HostActivity) {


    object START_TYPE {
        const val ADD = 1
        const val ADD_WITH_RESULT = 2
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
            if (to.getAnimation() != null) {
                ft.setCustomAnimations(to.getAnimation()!!.first, to.getAnimation()!!.second)
            }
            ft.add(activity.getContainerID(), to, to.javaClass.name)
            if (from.getAnimation() != null) {
                ft.setCustomAnimations(from.getAnimation()!!.first, from.getAnimation()!!.second)
            }
            ft.hide(from)
        } else {
            if (to.getAnimation() != null) {
                ft.setCustomAnimations(to.getAnimation()!!.first, to.getAnimation()!!.second)
            }
            ft.add(activity.getContainerID(), to, to.javaClass.name)
        }
        ft.commit()
    }


    fun finish(fragmentManager: FragmentManager, fragment: KitFragment) {
        val fragments = mFragmentStack.getFragments()
        if (fragment !in fragments || fragments.size <= 1) {
            return
        }
        val index = fragments.indexOf(fragment)
        if (index == fragments.lastIndex) {
            val preFrg = mFragmentStack.getFragments()[index - 1]
            handleFragmentResult(fragment, preFrg)
            fragmentManager.beginTransaction()
                    .show(preFrg)
                    .setCustomAnimations(0, R.anim.frg_slide_out_from_bottom)
                .remove(fragment)
                .commit()
        } else {
            if (index > 1) {
                val preFrg = mFragmentStack.getFragments()[index - 1]
                handleFragmentResult(fragment, preFrg)
            }
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
        mFragmentStack.remove(fragment)
    }

    fun popTo(fragmentManager: FragmentManager, fragment: KitFragment, includeSelf: Boolean = false) {
        val fragments = getFragments()
        if (fragment !in mFragmentStack.getFragments()) {
            return
        }
        val index = fragments.indexOf(fragment)
        if (index == fragments.lastIndex && includeSelf) {
            finish(fragmentManager, fragment)
        } else {
            return
        }

        val top = fragments.last()
        var target : KitFragment? = null
        if (!includeSelf) {
            target = fragment
        } else {
            if (index == 0) {
                // 都空了pop你妹
                return
            } else {
                target = fragments[index-1]
            }
        }
        val ft = fragmentManager.beginTransaction()
        for (i in (fragments.indexOf(target) + 1)..(fragments.indexOf(top))) {
            ft.remove(fragments[i])
        }
        ft.show(target)
        ft.commit()
    }

    private fun handleFragmentResult(frg: KitFragment, preFrg: KitFragment) {
        if (frg.fragmentResult != null) {
            preFrg.onFragmentResult(frg.fragmentResult!!.requestCode,
                    frg.fragmentResult!!.resultCode, frg.fragmentResult!!.data)
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