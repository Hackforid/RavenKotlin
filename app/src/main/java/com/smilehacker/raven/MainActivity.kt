package com.smilehacker.raven

import android.os.Bundle

import com.smilehacker.raven.Megatron.HostActivity

class MainActivity : HostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        init()
    }

    override fun getContainerID(): Int {
        return R.id.container
    }

    private fun init() {
        startFragment(AFragment())
    }
}
