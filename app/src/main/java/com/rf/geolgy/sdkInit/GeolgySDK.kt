package com.rf.geolgy.sdkInit

import android.app.Application
import com.rf.geolgy.ui.base.BaseSdk
import com.rf.geolgy.sdkInit.di.AppComponent
import com.rf.geolgy.sdkInit.di.AppModule
import com.rf.geolgy.sdkInit.di.DaggerAppComponent

object GeolgySDK : BaseSdk() {
    private var mApplication: Application? = null
    lateinit var appComponent: AppComponent

    fun initialize(mApplication: Application): GeolgySDK {
        GeolgySDK.mApplication = mApplication
        appComponent = DaggerAppComponent.builder().appModule(AppModule(mApplication)).build()
        appComponent.inject(this)
        return this
    }

}