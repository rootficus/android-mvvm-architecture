package com.rf.geolgy

import android.app.Application
import com.rf.geolgy.sdkInit.GeolgySDK

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        GeolgySDK.initialize(this)
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