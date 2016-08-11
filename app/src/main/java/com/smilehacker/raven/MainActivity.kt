package com.smilehacker.raven

import android.os.Bundle
import com.smilehacker.raven.Megatron.HostActivity
import com.smilehacker.raven.ui.index.index.IndexFragment

class MainActivity : HostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        if (savedInstanceState == null) {
            init()
        }
    }

    override fun getContainerID(): Int {
        return R.id.container
    }

    private fun init() {
        startFragment(IndexFragment())
    }
}
