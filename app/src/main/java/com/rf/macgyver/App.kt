package com.rf.macgyver

import android.app.Application
import com.rf.macgyver.sdkInit.UtellSDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        UtellSDK.initialize(this)
        isAppRunning = true
    }

    override fun onTerminate() {
        super.onTerminate()
        isAppRunning = false
    }

    fun isAppRunning(): Boolean {
        return isAppRunning
    }

    companion object {
        var isAppRunning = false
    }
}