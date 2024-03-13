package com.rf.tiffinService

import android.app.Application
import com.rf.tiffinService.sdkInit.UtellSDK

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