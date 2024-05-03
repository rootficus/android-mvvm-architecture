package com.rf.accessAli

import android.app.Application
import com.rf.accessAli.sdkInit.AccessAliSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AccessAliSDK.initialize(this)
        isAppRunning = true
    }

    override fun onTerminate() {
        super.onTerminate()
        isAppRunning = false
    }
    companion object {
        var isAppRunning = false
    }
}