package com.smilehacker.raven.Megatron

import android.os.Bundle
import android.support.v4.app.FragmentManager

/**
 * Created by kleist on 16/6/6.
 */
class FragmentController(val activity: HostActivity) {

    object FRAGMENT {
        const val IS_ROOT = "key_is_root"

        object LAUNCH_MODE {
            const val SINGLE_TOP = 1
            const val SINGLE_TASK = 2
            const val STANDARD = 3
        }
    }

    fun start(fragmentManager: FragmentManager, from: KitFragment?, to: KitFragment, launchMode: Int) {
        val ft = fragmentManager.beginTransaction()
        val toName = to.javaClass.name

        if (handleLaunchMode(fragmentManager, to, launchMode)) {
            return
        }

        if (from == null) {
            val bundle = to.arguments ?: Bundle()

            bundle.putBoolean(FRAGMENT.IS_ROOT, true)
            ft.add(activity.getContainerID(), to, toName)
        } else {
            ft.add(activity.getContainerID(), to, toName)
            ft.hide(from)
        }

        ft.addToBackStack(toName)
        ft.commit()
    }

    private fun handleLaunchMode(fragmentManager: FragmentManager, to: KitFragment, launchMode: Int) : Boolean {
        when(launchMode) {
            FRAGMENT.LAUNCH_MODE.SINGLE_TOP -> {
                val fragments = fragmentManager.fragments
                val index = fragments.indexOf(to)
                if (index == fragmentManager.backStackEntryCount - 1) {
                    // TODO:new bundle
                    handleNewBundle(to, null)
                    return true
                }
            }
            FRAGMENT.LAUNCH_MODE.SINGLE_TASK -> {
                // TODO: support singleTask
                handleNewBundle(to, null)
                return true
            }
        }
        return false
    }

    private fun handleNewBundle(to: KitFragment, bundle : Bundle?) {
        to.onNewBundle(bundle)
    }

    fun pop(fragmentManager: FragmentManager) {
        if (fragmentManager.backStackEntryCount > 1) {
            handleBack(fragmentManager)
        }
    }

    private fun handleBack(fragmentManager: FragmentManager) {
        fragmentManager.popBackStackImmediate()
    }

    fun getTopFragment(fragmentManager: FragmentManager) : KitFragment? {
        val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (i in (fragments.size - 1) downTo  0) {
                val frg = fragments[i]
                if (frg != null && frg is KitFragment) {
                    return frg
                }
            }
        }
        return null
    }

}
