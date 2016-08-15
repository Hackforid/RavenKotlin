package com.smilehacker.raven.Megatron

import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.smilehacker.raven.util.DLog
import com.smilehacker.raven.util.createParcel
import java.util.*

/**
 * Created by zhouquan on 16/6/9.
 */
class Fragmentation : Parcelable {

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeParcelable(mFragmentStack, 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField @Suppress("unused") val CREATOR = createParcel { Fragmentation(it) }
    }

    constructor(parcel: Parcel) : this() {
        mFragmentStack = parcel.readParcelable(FragmentStack.javaClass.classLoader)
    }

    constructor() {
        mFragmentStack = FragmentStack()
    }


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


    private lateinit var mFragmentStack: FragmentStack

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
                startStandard(fragmentManager, from, to, fragmentTag, startType, requestCode)
            }
            LAUNCH_MODE.SINGLE_TOP -> {
                val topFrg = getTopFragment(fragmentManager) as IKitFragmentAction
                if (topFrg != null && topFrg.javaClass == to.javaClass) {
                    topFrg.onNewBundle(to.arguments)
                } else {
                    fragmentTag = mFragmentStack.getNewFragmentName(to)
                    mFragmentStack.putStandard(fragmentTag)
                    startStandard(fragmentManager, from, to, fragmentTag, startType, requestCode)
                }
            }
            LAUNCH_MODE.SINGLE_TASK -> {

                val instanceIndex = mFragmentStack.getSingleTaskInstancePos(to)
                if (instanceIndex == -1) {
                    fragmentTag = mFragmentStack.getNewFragmentName(to)
                    mFragmentStack.putStandard(fragmentTag)
                    startStandard(fragmentManager, from, to, fragmentTag, startType, requestCode)
                } else {
                    startSingleTask(fragmentManager, to)
                }
            }
        }
    }

    private fun startSingleTask(fragmentManager: FragmentManager, to: Fragment) {
        popTo(fragmentManager, to, false)
    }

    private fun startStandard(fragmentManager: FragmentManager, from: Fragment?, to: Fragment,
                              toTag: String,
              startType: Int = START_TYPE.ADD,
              requestCode: Int = 0) {
        if (to !is IKitFragmentAction) {
            throw IllegalArgumentException("to fragment should implement IKitFragmentAction")
        }
        when (startType) {
            START_TYPE.ADD_WITH_RESULT -> {
                to.fragmentResult = FragmentResult(requestCode)
            }
        }

        val ft = fragmentManager.beginTransaction()
        if (to.getAnimation() != null) {
            ft.setCustomAnimations(to.getAnimation()!!.first, to.getAnimation()!!.second)
        }

        if (from != null) {
            ft.add(mContainerID, to, toTag)
            ft.hide(from)
        } else {
            ft.add(mContainerID, to, toTag)
        }
        ft.commitNow()
    }


    fun finish(fragmentManager: FragmentManager, fragment: Fragment) {
        popTo(fragmentManager, fragment, true)
    }

    fun popTo(fragmentManager: FragmentManager, fragment: Fragment, includeSelf: Boolean = false) {
        if (fragment !is IKitFragmentAction) {
            throw IllegalArgumentException("to fragment should implement IKitFragmentAction")
        }
        val fragments = getFragments(fragmentManager)
        if (fragments.isEmpty()) {
            return
        }

        val instanceIndex = mFragmentStack.getSingleTaskInstancePos(fragment)
        val target : Fragment?
        val top = fragments.last()
        top as IKitFragmentAction

        if (instanceIndex == -1) {
            return
        }

        if (instanceIndex == fragments.lastIndex) {
            if (includeSelf) {
            } else {
                top.onNewBundle(fragment.arguments)
                return
            }
        }


        if (!includeSelf) {
            target = fragments[instanceIndex]
        } else {
            if (instanceIndex == 0) {
                popAll(fragmentManager)
                return
            } else {
                target = fragments[instanceIndex - 1]
            }
        }
        target as IKitFragmentAction

        val ft = fragmentManager.beginTransaction()
        if (top.getAnimation() != null) {
            ft.setCustomAnimations(top.getAnimation()!!.first, top.getAnimation()!!.second)
        }
        for (i in (fragments.indexOf(target) + 1)..(fragments.lastIndex)) {
            if (i > 0) {
                handleFragmentResult(fragments[i], fragments[i-1])
            }
            ft.remove(fragments[i])
        }
        ft.show(target)
        ft.commitNow()
        mFragmentStack.popTo(target.tag, false)
    }

    fun popAll(fragmentManager: FragmentManager) {
        val fragments = getFragments(fragmentManager)
        if (fragments.isEmpty()) {
            return
        }
        val top = fragments.last()
        top as IKitFragmentAction
        val ft = fragmentManager.beginTransaction()
        ft.remove(top)
        if (top.getAnimation() != null) {
            ft.setCustomAnimations(top.getAnimation()!!.first, top.getAnimation()!!.second)
        }
        for (fragment in fragments) {
            if (fragment != top) {
                ft.remove(fragment)
            }
        }
        ft.commitNow()
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