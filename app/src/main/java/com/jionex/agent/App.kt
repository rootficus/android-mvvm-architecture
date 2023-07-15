package com.jionex.agent

import android.app.Application
import com.jionex.agent.sdkInit.JionexSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        JionexSDK.initialize(this)
    }
}