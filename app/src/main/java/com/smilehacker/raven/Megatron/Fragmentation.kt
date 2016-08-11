package com.smilehacker.raven.Megatron

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.smilehacker.raven.util.DLog
import java.io.Serializable

/**
 * Created by zhouquan on 16/6/9.
 */
class Fragmentation(var activity: HostActivity) : Serializable {


    object START_TYPE {
        const val ADD = 1
        const val ADD_WITH_RESULT = 2
    }

    object LAUNCH_MODE {
        const val SINGLE_TOP = 1
        const val SINGLE_TASK = 2
        const val STANDARD = 3
    }

    init {
        DLog.i("create fragmentation")
    }

    private val mFragmentStack: FragmentStack by lazy { FragmentStack() }

    fun start(fragmentManager: FragmentManager, from: Fragment?, to: Fragment,
              launchMode: Int = LAUNCH_MODE.STANDARD,
              startType: Int = START_TYPE.ADD,
              requestCode: Int = 0) {

        if (to !is IKitFragmentAction) {
            throw IllegalArgumentException("to fragment should implement IKitFragmentAction")
        }

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
            if (from !is IKitFragmentAction) {
                throw IllegalArgumentException("from fragment should implement IKitFragmentAction")
            }
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


    fun finish(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragments = mFragmentStack.getFragments()
        if (fragment !in fragments || fragments.size <= 1) {
            return
        }
        val index = fragments.indexOf(fragment)
        if (index == fragments.lastIndex) {
            val preFrg = mFragmentStack.getFragments()[index - 1]
            handleFragmentResult(fragment, preFrg)
            val ft = fragmentManager.beginTransaction()
            ft.show(preFrg)
            if (fragment is IKitFragmentAction && fragment.getAnimation() != null) {
                ft.setCustomAnimations(fragment.getAnimation()!!.first, fragment.getAnimation()!!.second)
            }
            ft.remove(fragment)
            ft.commit()
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

    fun popTo(fragmentManager: FragmentManager, fragment: Fragment, includeSelf: Boolean = false) {
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
        var target : Fragment? = null
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

    private fun handleFragmentResult(frg: Fragment, preFrg: Fragment) {
        if (frg is IKitFragmentAction && preFrg is IKitFragmentAction) {
            if (frg.fragmentResult != null) {
                preFrg.onFragmentResult(frg.fragmentResult!!.requestCode,
                        frg.fragmentResult!!.resultCode, frg.fragmentResult!!.data)
            }
        }

    }

    fun getFragments(): MutableList<Fragment> {
        return mFragmentStack.getFragments()
    }

    fun getStackCount(): Int {
        return mFragmentStack.getStackCount()
    }

    fun getTopFragment(): Fragment? {
        return mFragmentStack.getTopFragment()
    }

}