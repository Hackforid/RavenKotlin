package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.Fragment
import java.util.*

/**
 * Created by zhouquan on 16/6/9.
 */
class FragmentStack {
    private val mFragmentStack : MutableList<Fragment> = ArrayList()

    fun getFragments() = mFragmentStack

    fun getStackCount() = mFragmentStack.size

    fun putStandard(fragment : Fragment) {
        mFragmentStack.add(fragment)
    }

    fun putSingleTop(fragment: Fragment) : Boolean {
        if (mFragmentStack.isEmpty() || mFragmentStack.last() != fragment) {
            mFragmentStack.add(fragment)
            return false
        } else {
            return true
        }
    }

    fun putSingleTask(fragment: Fragment, bundle: Bundle? = null) : Boolean {
        if (mFragmentStack.isEmpty() || fragment !in mFragmentStack) {
            mFragmentStack.add(fragment)
            return false
        } else {
            if (fragment == mFragmentStack.last()) {
                return false
            } else {
                mFragmentStack.remove(fragment)
                mFragmentStack.add(fragment)
                return true
            }
        }
    }

    fun pop() {
        mFragmentStack.removeAt(mFragmentStack.lastIndex)
    }

    fun popTo(fragment: Fragment, includeSelf: Boolean = false) {
        if (fragment !in mFragmentStack) {
            return
        }
        if (includeSelf) {
            if (mFragmentStack.indexOf(fragment) > 0) {
                mFragmentStack.subList(0, mFragmentStack.indexOf(fragment)-1)
            } else {
                mFragmentStack.clear()
            }
        } else {
            mFragmentStack.subList(0, mFragmentStack.indexOf(fragment))
        }
    }

    fun remove(fragment: Fragment) {
        mFragmentStack.remove(fragment)
    }

    fun getTopFragment() : Fragment? {
        if (mFragmentStack.isEmpty()) {
            return null
        } else {
            return mFragmentStack.last()
        }
    }

}
