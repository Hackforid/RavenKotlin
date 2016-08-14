package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.smilehacker.raven.util.createParcel
import java.util.*

/**
 * Created by zhouquan on 16/6/9.
 */
class FragmentStack() : Parcelable {

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeStringList(mFragmentStack)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = createParcel { FragmentStack(it) }
    }

    constructor(parcel: Parcel) : this() {
        parcel.readStringList(mFragmentStack)
    }

    private var mFragmentStack : MutableList<String> = ArrayList()

    fun getFragments() = mFragmentStack

    fun getStackCount() = mFragmentStack.size

    fun putStandard(fragmentTag : String) {
        mFragmentStack.add(fragmentTag)
    }

    fun putSingleTop(fragmentTag: String) : Boolean {
        if (mFragmentStack.isEmpty() || mFragmentStack.last() != fragmentTag) {
            mFragmentStack.add(fragmentTag)
            return false
        } else {
            return true
        }
    }

    fun putSingleTask(fragmentTag: String, bundle: Bundle? = null) : Boolean {
        if (mFragmentStack.isEmpty() || fragmentTag !in mFragmentStack) {
            mFragmentStack.add(fragmentTag)
            return false
        } else {
            if (fragmentTag == mFragmentStack.last()) {
                return false
            } else {
                mFragmentStack.remove(fragmentTag)
                mFragmentStack.add(fragmentTag)
                return true
            }
        }
    }

    fun pop() {
        mFragmentStack.removeAt(mFragmentStack.lastIndex)
    }

    fun popTo(fragmentTag: String, includeSelf: Boolean = false) {
        if (fragmentTag !in mFragmentStack) {
            return
        }
        if (includeSelf) {
            if (mFragmentStack.indexOf(fragmentTag) > 0) {
                mFragmentStack.subList(0, mFragmentStack.indexOf(fragmentTag)-1)
            } else {
                mFragmentStack.clear()
            }
        } else {
            mFragmentStack.subList(0, mFragmentStack.indexOf(fragmentTag))
        }
    }

    fun remove(fragment: String) {
        mFragmentStack.remove(fragment)
    }

    fun getTopFragment() : String? {
        if (mFragmentStack.isEmpty()) {
            return null
        } else {
            return mFragmentStack.last()
        }
    }

    fun getNewFragmentName(fragment : Fragment) : String {
        val className = fragment.javaClass.name
        var index = 0
        for (tag in mFragmentStack.asReversed()) {
            if (tag.startsWith(className)) {
                index = tag.substring(className.length).toInt()
                break
            }
        }

        return "$className${index+1}"
    }

}
