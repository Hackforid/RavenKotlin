package com.smilehacker.raven.Megatron

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.smilehacker.raven.util.DLog
import java.io.Serializable
import java.util.*

/**
 * Created by zhouquan on 16/6/9.
 */
class Fragmentation() : Serializable {


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

    private var mContainerID: Int = 0

    fun init(activity: HostActivity) {
        mContainerID = activity.getContainerID()
    }


    fun start(fragmentManager: FragmentManager, from: Fragment?, to: Fragment,
              launchMode: Int = LAUNCH_MODE.STANDARD,
              startType: Int = START_TYPE.ADD,
              requestCode: Int = 0) {

        if (to !is IKitFragmentAction) {
            throw IllegalArgumentException("to fragment should implement IKitFragmentAction")
        }

        var fragmentTag : String? = to.tag
        DLog.i("frag tag = " + fragmentTag)

        when (launchMode) {
            LAUNCH_MODE.STANDARD -> {
                if (!fragmentTag.isNullOrEmpty()) {
                    throw IllegalArgumentException("new fragment should not has tag")
                }
                fragmentTag = mFragmentStack.getNewFragmentName(to)
                mFragmentStack.putStandard(fragmentTag)
            }
            LAUNCH_MODE.SINGLE_TOP -> {
                if (fragmentTag.isNullOrEmpty()) {
                    fragmentTag = mFragmentStack.getNewFragmentName(to)
                }
                if (mFragmentStack.putSingleTop(fragmentTag!!)) {
                    to.onNewBundle(to.getNewBundle())
                }
            }
            LAUNCH_MODE.SINGLE_TASK -> {
                if (fragmentTag.isNullOrEmpty()) {
                    fragmentTag = mFragmentStack.getNewFragmentName(to)
                }
                if (mFragmentStack.putSingleTask(fragmentTag!!)) {
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
            ft.add(mContainerID, to, fragmentTag)
            if (from.getAnimation() != null) {
                ft.setCustomAnimations(from.getAnimation()!!.first, from.getAnimation()!!.second)
            }
            ft.hide(from)
        } else {
            if (to.getAnimation() != null) {
                ft.setCustomAnimations(to.getAnimation()!!.first, to.getAnimation()!!.second)
            }
            ft.add(mContainerID, to, fragmentTag)
        }
        ft.commit()
    }


    fun finish(fragmentManager: FragmentManager, fragment: Fragment) {
        val fragments = getFragments(fragmentManager)
        if (fragment !in fragments || fragments.size <= 1) {
            return
        }
        val index = fragments.indexOf(fragment)
        if (index == fragments.lastIndex) {
            val preFrg = fragments[index - 1]
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
                val preFrg = fragments[index - 1]
                handleFragmentResult(fragment, preFrg)
            }
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
        }
        mFragmentStack.remove(fragment.tag)
    }

    fun popTo(fragmentManager: FragmentManager, fragment: Fragment, includeSelf: Boolean = false) {
        val fragments = getFragments(fragmentManager)
        if (fragment !in fragments) {
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

    fun getFragments(fragmentManager: FragmentManager): MutableList<Fragment> {
        val frgTags = mFragmentStack.getFragments()
        val frgs = ArrayList<Fragment>(frgTags.size)
        frgTags.forEach {
            val frg = fragmentManager.findFragmentByTag(it)
            frg?.let { frgs.add(it) }
        }
        return frgs
    }

    fun getStackCount(): Int {
        return mFragmentStack.getStackCount()
    }

    fun getTopFragment(fragmentManager: FragmentManager): Fragment? {
        val frgTags = mFragmentStack.getFragments()
        if (frgTags.size > 0) {
            return fragmentManager.findFragmentByTag(frgTags.last())
        } else {
            return null
        }
    }
}