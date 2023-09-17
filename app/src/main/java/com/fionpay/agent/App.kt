package com.fionpay.agent

import android.app.Application
import com.fionpay.agent.sdkInit.FionSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FionSDK.initialize(this)
    }
}