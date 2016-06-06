package com.smilehacker.raven.Megatron

import android.support.v7.app.AppCompatActivity

/**
 * Created by kleist on 16/6/6.
 */
abstract class HostActivity : AppCompatActivity() {

    abstract fun getContainerID() : Int

    fun startFragment(to : KitFragment, launchMode : Int = KitFragment.STANDARD) {

    }
}